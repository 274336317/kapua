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
package org.eclipse.kapua.app.console.module.authorization.client.role;

import org.eclipse.kapua.app.console.commons.client.views.AbstractTabDescriptor;
import org.eclipse.kapua.app.console.commons.shared.model.GwtSession;
import org.eclipse.kapua.app.console.module.authorization.shared.model.GwtRole;

public class RoleTabPermissionGridDescriptor extends AbstractTabDescriptor<GwtRole, RoleTabPermissionGrid, RoleView> {

    @Override
    public RoleTabPermissionGrid getTabViewInstance(RoleView view, GwtSession currentSession) {
        return new RoleTabPermissionGrid(null, currentSession);
    }

    @Override
    public String getViewId() {
        return "role.permissions";
    }

    @Override
    public Integer getOrder() {
        return 200;
    }

    @Override
    public Boolean isEnabled(GwtSession currentSession) {
        return true;
    }
}
