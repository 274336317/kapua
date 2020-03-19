/*******************************************************************************
 * Copyright (c) 2018, 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.service.device.call.message.app.request;

import org.eclipse.kapua.service.device.call.message.DeviceMetrics;

/**
 * {@link DeviceRequestMetrics} definition.
 *
 * @since 1.0.0
 */
public interface DeviceRequestMetrics extends DeviceMetrics {

    /**
     * Gets the value of this {@link DeviceRequestMetrics}.
     *
     * @return The value of this {@link DeviceRequestMetrics}.
     * @since 1.0.0
     * @deprecated Since 1.2.0. Renamed to {@link #getName()}.
     */
    @Deprecated
    String getValue();
}
