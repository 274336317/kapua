/*******************************************************************************
 * Copyright (c) 2017 Red Hat Inc and others.
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Red Hat Inc - initial API and implementation
 *******************************************************************************/
package org.eclipse.kapua.security.registration;

import java.util.Optional;

import org.eclipse.kapua.service.user.User;
import org.jose4j.jwt.consumer.JwtContext;

public interface RegistrationProcessor extends AutoCloseable {

    public Optional<User> createUser(JwtContext context) throws Exception;
}
