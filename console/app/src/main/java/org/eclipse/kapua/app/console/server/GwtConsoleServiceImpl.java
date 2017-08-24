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
package org.eclipse.kapua.app.console.server;

import org.eclipse.kapua.app.console.commons.client.views.ViewDescriptor;
import org.eclipse.kapua.app.console.commons.server.KapuaRemoteServiceServlet;
import org.eclipse.kapua.app.console.module.about.client.about.AboutViewDescriptor;
import org.eclipse.kapua.app.console.module.authorization.client.role.RoleViewDescriptor;
import org.eclipse.kapua.app.console.module.user.client.UserViewDescriptor;
import org.eclipse.kapua.app.console.shared.service.GwtConsoleService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class GwtConsoleServiceImpl extends KapuaRemoteServiceServlet implements GwtConsoleService {

     private static final String ABOUT_CLASSNAME = "org.eclipse.kapua.app.console.module.about.client.about.AboutViewDescriptor";
     private static final String ROLES_CLASSNAME = "org.eclipse.kapua.app.console.module.authorization.client.role.RoleViewDescriptor";
     private static final String USER_CLASSNAME = "org.eclipse.kapua.app.console.module.user.client.UserViewDescriptor";

    @Override
    public List<ViewDescriptor> getCustomEntityViews() {
        List<ViewDescriptor> views = new ArrayList<ViewDescriptor>();
        try {
            UserViewDescriptor userView = (UserViewDescriptor)Class.forName(USER_CLASSNAME).newInstance();
            views.add(userView);
            AboutViewDescriptor aboutView = (AboutViewDescriptor)Class.forName(ABOUT_CLASSNAME).newInstance();
            views.add(aboutView);
            RoleViewDescriptor roleView = (RoleViewDescriptor)Class.forName(ROLES_CLASSNAME).newInstance();
            views.add(roleView);
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }
        Collections.sort(views);
        return views;
    }
}
