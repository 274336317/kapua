/*******************************************************************************
 * Copyright (c) 2016, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.kura.app;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppChannel;
import org.eclipse.kapua.service.device.call.message.kura.KuraChannel;

/**
 * {@link DeviceAppChannel} {@link org.eclipse.kapua.service.device.call.kura.Kura} implementation.
 *
 * @since 1.0.0
 */
public abstract class KuraAppChannel extends KuraChannel implements DeviceAppChannel {

    /**
     * The applicatin identifier.
     *
     * @since 1.0.0
     */
    protected String appId;

    /**
     * Constructor.
     *
     * @since 1.0.0
     */
    public KuraAppChannel() {
        super();
    }

    /**
     * Constructor.
     *
     * @param messageClassification The message classification.
     * @param scopeNamespace        The scope namespace.
     * @param clientId              The clientId
     * @see org.eclipse.kapua.service.device.call.message.DeviceChannel
     * @since 1.0.0
     */
    public KuraAppChannel(String messageClassification, String scopeNamespace, String clientId) {
        super(messageClassification, scopeNamespace, clientId);
    }

    @Override
    public String getAppId() {
        return appId;
    }

    @Override
    public void setAppId(String appId) {
        this.appId = appId;
    }
}
