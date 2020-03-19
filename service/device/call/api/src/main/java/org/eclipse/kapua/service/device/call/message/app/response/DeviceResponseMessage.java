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
package org.eclipse.kapua.service.device.call.message.app.response;

import org.eclipse.kapua.service.device.call.message.app.DeviceAppMessage;

/**
 * {@link DeviceResponseMessage} definition.
 *
 * @param <C> The {@link DeviceResponseChannel} type.
 * @param <P> The {@link DeviceResponsePayload} type.
 * @since 1.0.0
 */
public interface DeviceResponseMessage<C extends DeviceResponseChannel, P extends DeviceResponsePayload> extends DeviceAppMessage<C, P> {

}
