/*******************************************************************************
 * Copyright (c) 2017, 2020 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *     Eurotech
 *******************************************************************************/
package org.eclipse.kapua.commons.liquibase;

import com.google.common.base.Strings;
import liquibase.Liquibase;
import liquibase.database.Database;
import liquibase.database.DatabaseFactory;
import liquibase.database.jvm.JdbcConnection;
import liquibase.exception.LiquibaseException;
import liquibase.resource.FileSystemResourceAccessor;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.SystemUtils;
import org.eclipse.kapua.commons.liquibase.settings.LiquibaseClientSettingKeys;
import org.eclipse.kapua.commons.liquibase.settings.LiquibaseClientSettings;
import org.eclipse.kapua.commons.util.SemanticVersion;
import org.reflections.Reflections;
import org.reflections.scanners.ResourcesScanner;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.List;
import java.util.Set;
import java.util.regex.Pattern;

public class KapuaLiquibaseClient {

    private static final Logger LOG = LoggerFactory.getLogger(KapuaLiquibaseClient.class);

    private static final SemanticVersion LIQUIBASE_TIMESTAMP_FIX_VERSION = new SemanticVersion("3.3.3"); // https://liquibase.jira.com/browse/CORE-1958

    private static final LiquibaseClientSettings LIQUIBASE_CLIENT_SETTINGS = LiquibaseClientSettings.getInstance();

    private final String jdbcUrl;
    private final String username;
    private final String password;
    private final String schema;
    private final boolean runTimestampsFix;

    public KapuaLiquibaseClient(String jdbcUrl, String username, String password, String schema) {
        this.jdbcUrl = jdbcUrl;
        this.username = username;
        this.password = password;
        this.schema = schema;

        // Check wether or not fix the timestamp based on Liquibase version
        boolean forceTimestampFix = LIQUIBASE_CLIENT_SETTINGS.getBoolean(LiquibaseClientSettingKeys.FORCE_TIMESTAMPS_FIX);
        String currentLiquibaseVersionString = LIQUIBASE_CLIENT_SETTINGS.getString(LiquibaseClientSettingKeys.LIQUIBASE_VERSION);
        SemanticVersion currentLiquibaseVersion = new SemanticVersion(currentLiquibaseVersionString);

        runTimestampsFix = (currentLiquibaseVersion.afterOrMatches(LIQUIBASE_TIMESTAMP_FIX_VERSION) || forceTimestampFix);
    }

    public KapuaLiquibaseClient(String jdbcUrl, String username, String password) {
        this(jdbcUrl, username, password, null);
    }

    public void update() {
        try {
            if (Boolean.parseBoolean(System.getProperty("LIQUIBASE_ENABLED", "true")) || Boolean.parseBoolean(System.getenv("LIQUIBASE_ENABLED"))) {
                LOG.info("Running Liquibase update with schema: {}", schema);
                try (Connection connection = DriverManager.getConnection(jdbcUrl, username, password)) {
                    File changelogDir = loadChangelogs();

                    List<String> contexts = new ArrayList<>();
                    if (!runTimestampsFix) {
                        contexts.add("!fixTimestamps");
                }

                    executeMasters(connection, schema, changelogDir, contexts);
                }
            }
        } catch (LiquibaseException | SQLException | IOException e) {
            LOG.error("Error while running Liquibase scripts: {}", e.getMessage(), e);
            throw new RuntimeException(e);
        }
    }

    protected static synchronized File loadChangelogs() throws IOException {
        String tmpDirectory = SystemUtils.getJavaIoTmpDir().getAbsolutePath();

        File changelogTempDirectory = new File(tmpDirectory, "kapua-liquibase");

        if (changelogTempDirectory.exists()) {
            FileUtils.deleteDirectory(changelogTempDirectory);
        }

        boolean createdTmp = changelogTempDirectory.mkdirs();
        LOG.trace("{} Tmp dir: {}", createdTmp ? "Created" : "Using", changelogTempDirectory.getAbsolutePath());

        Reflections reflections = new Reflections("liquibase", new ResourcesScanner());
        Set<String> changeLogs = reflections.getResources(Pattern.compile(".*\\.xml|.*\\.sql"));
        for (String script : changeLogs) {
            URL scriptUrl = KapuaLiquibaseClient.class.getResource("/" + script);
            File changelogFile = new File(changelogTempDirectory, script.replaceFirst("liquibase/", ""));
            if (changelogFile.getParentFile() != null && !changelogFile.getParentFile().exists()) {
                boolean createdParent = changelogFile.getParentFile().mkdirs();
                LOG.trace("{} parent dir: {}", createdParent ? "Created" : "Using", changelogFile.getParentFile().getAbsolutePath());
            }
            try (FileOutputStream tmpStream = new FileOutputStream(changelogFile)) {
                IOUtils.write(IOUtils.toString(scriptUrl), tmpStream);
            }
            LOG.trace("Copied file: {}", changelogFile.getAbsolutePath());
        }

        return changelogTempDirectory;
    }

    protected static void executeMasters(Connection connection, String schema, File changelogDir, List<String> contexts) throws LiquibaseException {
        //
        // Find and execute all master scripts
        LOG.info("Executing pre master files...");
        executeMasters(connection, schema, changelogDir, "-master.pre.xml", contexts);
        LOG.info("Executing pre master files... DONE!");

        LOG.info("Executing master files...");
        executeMasters(connection, schema, changelogDir, "-master.xml", contexts);
        LOG.info("Executing master files... DONE!");

        LOG.info("Executing post master files...");
        executeMasters(connection, schema, changelogDir, "-master.post.xml", contexts);
        LOG.info("Executing post master files... DONE!");
    }

    protected static void executeMasters(Connection connection, String schema, File changelogTempDirectory, String preMaster, List<String> contexts) throws LiquibaseException {
        List<File> masterChangelogs = Arrays.asList(changelogTempDirectory.listFiles((dir, name) -> name.endsWith(preMaster)));

        LOG.info("\tMaster Liquibase files found: {}", masterChangelogs.size());

        LOG.trace("\tSorting master Liquibase files found.");
        masterChangelogs.sort(Comparator.comparing(File::getAbsolutePath));

        String ctx = contexts.isEmpty() ? null : String.join(",", contexts);
        for (File masterChangelog : masterChangelogs) {
            LOG.info("\t\tExecuting liquibase script: {}...", masterChangelog.getAbsolutePath());
            Database database = DatabaseFactory.getInstance().findCorrectDatabaseImplementation(new JdbcConnection(connection));
            if (!Strings.isNullOrEmpty(schema)) {
                database.setDefaultSchemaName(schema);
            }
            Liquibase liquibase = new Liquibase(masterChangelog.getAbsolutePath(), new FileSystemResourceAccessor(), database);
            liquibase.update(ctx);

            LOG.debug("\t\tExecuting liquibase script: {}... DONE!", masterChangelog.getAbsolutePath());
        }
    }

}
