/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.steps;

import static org.eclipse.kapua.locator.KapuaLocator.getInstance;

import org.eclipse.kapua.service.device.registry.Device;
import org.eclipse.kapua.service.device.registry.DeviceRegistryService;
import org.eclipse.kapua.service.user.User;
import org.eclipse.kapua.service.user.UserService;
import org.junit.Assert;

public final class With {

    private With() {
    }

    public static void withUserAccount(final String accountName, final ThrowingConsumer<User> consumer) throws Exception {
        final UserService userService = getInstance().getService(UserService.class);
        final User account = userService.findByName(accountName);

        if (account == null) {
            Assert.fail("Unable to find account: " + accountName);
            return;
        }

        consumer.accept(account);
    }

    public static void withDevice(final User account, final String clientId, final ThrowingConsumer<Device> consumer) throws Exception {
        DeviceRegistryService service = getInstance().getService(DeviceRegistryService.class);
        Device device = service.findByClientId(account.getId(), clientId);
        if (device == null) {
            throw new IllegalStateException(String.format("Unable to find device '%s' for account '%s'", clientId, account.getId()));
        }

        consumer.accept(device);
    }
}
