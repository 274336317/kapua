/*******************************************************************************
 * Copyright (c) 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.app.console.module.authentication.client.tabs.credentials;

import org.eclipse.kapua.app.console.commons.client.resources.icons.IconSet;
import org.eclipse.kapua.app.console.commons.client.resources.icons.KapuaIcon;
import org.eclipse.kapua.app.console.commons.client.ui.tab.KapuaTabItem;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;

import com.google.gwt.core.client.GWT;
import com.google.gwt.user.client.Element;
import org.eclipse.kapua.app.console.module.authentication.client.messages.ConsoleCredentialMessages;
import org.eclipse.kapua.app.console.module.authentication.shared.model.GwtCredential;

public class UserTabItemCredentials extends KapuaTabItem<GwtCredential> {

    private static final ConsoleCredentialMessages MSGS = GWT.create(ConsoleCredentialMessages.class);

    private CredentialGrid credentialsGrid;

    public UserTabItemCredentials(GwtSession currentSession) {
        super(MSGS.gridTabCredentialsLabel(), new KapuaIcon(IconSet.KEY));
        credentialsGrid = new CredentialGrid(null, currentSession);
    }

    @Override
    protected void onRender(Element parent, int index) {
        super.onRender(parent, index);
        add(credentialsGrid);
    }

    public CredentialGrid getCredentialsGrid() {
        return credentialsGrid;
    }

    @Override
    public void setEntity(GwtCredential credential) {
        super.setEntity(credential);
        credentialsGrid.setSelectedUserId(credential.getUserId());
    }

    @Override
    protected void doRefresh() {
        credentialsGrid.refresh();
        credentialsGrid.getToolbar().getAddEntityButton().setEnabled(selectedEntity != null);
        credentialsGrid.getToolbar().getRefreshEntityButton().setEnabled(selectedEntity != null);
    }

}
