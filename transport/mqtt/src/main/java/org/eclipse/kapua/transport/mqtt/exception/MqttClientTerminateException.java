/*******************************************************************************
 * Copyright (c) 2020 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.transport.mqtt.exception;

import org.eclipse.kapua.transport.mqtt.MqttClient;

/**
 * {@link Exception} to {@code throw} when the {@link MqttClient} is not terminated properly.
 *
 * @since 1.2.0
 */
public class MqttClientTerminateException extends MqttClientException {

    /**
     * Constructor.
     *
     * @param cause    The root {@link Throwable} that caused the error.
     * @param clientId The clientId of the {@link org.eclipse.kapua.transport.mqtt.MqttClient} that produced this {@link MqttClientTerminateException}.
     * @since 1.2.0
     */
    public MqttClientTerminateException(Throwable cause, String clientId) {
        super(MqttClientErrorCodes.TERMINATE_ERROR, cause, clientId);
    }
}