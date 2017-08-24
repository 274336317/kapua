/*******************************************************************************
 * Copyright (c) 2011, 2017 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *     Red Hat Inc
 *******************************************************************************/
package org.eclipse.kapua.app.console.shared.util;

import com.extjs.gxt.ui.client.data.BaseModel;
import com.extjs.gxt.ui.client.data.PagingLoadConfig;
import org.eclipse.kapua.app.console.commons.shared.model.GwtConfigComponent;
import org.eclipse.kapua.app.console.commons.shared.model.GwtConfigParameter;
import org.eclipse.kapua.app.console.commons.shared.model.GwtEntityModel;
import org.eclipse.kapua.app.console.commons.shared.model.GwtUpdatableEntityModel;
import org.eclipse.kapua.app.console.module.account.shared.model.GwtAccountQuery;
import org.eclipse.kapua.commons.model.id.KapuaEid;
import org.eclipse.kapua.commons.model.query.predicate.AndPredicate;
import org.eclipse.kapua.commons.model.query.predicate.AttributePredicate;
import org.eclipse.kapua.locator.KapuaLocator;
import org.eclipse.kapua.model.KapuaEntity;
import org.eclipse.kapua.model.KapuaUpdatableEntity;
import org.eclipse.kapua.model.id.KapuaId;
import org.eclipse.kapua.model.query.predicate.KapuaAttributePredicate.Operator;
import org.eclipse.kapua.service.account.AccountFactory;
import org.eclipse.kapua.service.account.AccountQuery;

import java.util.HashMap;
import java.util.Map;

/**
 * Utility class for convertKapuaId {@link BaseModel}s to {@link KapuaEntity}ies and other Kapua models
 */
public class GwtKapuaModelConverter {

    private GwtKapuaModelConverter() {
    }

    public static AccountQuery convertAccountQuery(PagingLoadConfig loadConfig, GwtAccountQuery gwtAccountQuery) {
        KapuaLocator locator = KapuaLocator.getInstance();
        AccountFactory factory = locator.getFactory(AccountFactory.class);
        AccountQuery query = factory.newQuery(convertKapuaId(gwtAccountQuery.getScopeId()));
        AndPredicate predicate = new AndPredicate();

        if (gwtAccountQuery.getName() != null && !gwtAccountQuery.getName().trim().isEmpty()) {
            predicate.and(new AttributePredicate<String>("name", gwtAccountQuery.getName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationName() != null && !gwtAccountQuery.getOrganizationName().isEmpty()) {
            predicate.and(new AttributePredicate<String>("organization.name", gwtAccountQuery.getOrganizationName(), Operator.LIKE));
        }

        if (gwtAccountQuery.getOrganizationEmail() != null && !gwtAccountQuery.getOrganizationEmail().isEmpty()) {
            predicate.and(new AttributePredicate<String>("organization.email", gwtAccountQuery.getOrganizationEmail(), Operator.LIKE));
        }

        query.setPredicate(predicate);

        return query;
    }

    /**
     * Utility method to convertKapuaId commons properties of {@link GwtUpdatableEntityModel} object to the matching {@link KapuaUpdatableEntity} object
     *
     * @param gwtEntity   The {@link GwtUpdatableEntityModel} from which copy values
     * @param kapuaEntity The {@link KapuaUpdatableEntity} into which to copy values
     * @since 1.0.0
     */
    private static void convertEntity(GwtUpdatableEntityModel gwtEntity, KapuaUpdatableEntity kapuaEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        convertEntity((GwtEntityModel) gwtEntity, (KapuaEntity) kapuaEntity);

        kapuaEntity.setOptlock(gwtEntity.getOptlock());
    }

    /**
     * Utility method to convertKapuaId commons properties of {@link GwtEntityModel} object to the matching {@link KapuaEntity} object
     *
     * @param gwtEntity   The {@link GwtEntityModel} from which copy values
     * @param kapuaEntity The {@link KapuaEntity} into which to copy values
     * @since 1.0.0
     */
    private static void convertEntity(GwtEntityModel gwtEntity, KapuaEntity kapuaEntity) {
        if (kapuaEntity == null || gwtEntity == null) {
            return;
        }

        kapuaEntity.setId(convertKapuaId(gwtEntity.getId()));
    }

    /**
     * Converts a {@link KapuaId} form the short form to the actual object.
     * <p>
     * Example: AQ =&gt; 1
     * </p>
     *
     * @param shortKapuaId the {@link KapuaId} in the short form
     * @return The converted {@link KapuaId}
     * @since 1.0.0
     */
    public static KapuaId convertKapuaId(String shortKapuaId) {
        if (shortKapuaId == null) {
            return null;
        }
        return KapuaEid.parseCompactId(shortKapuaId);
    }

    public static Map<String, Object> convert(GwtConfigComponent configComponent) {
        Map<String, Object> parameters = new HashMap<String, Object>();
        for (GwtConfigParameter gwtConfigParameter : configComponent.getParameters()) {
            switch (gwtConfigParameter.getType()) {
            case BOOLEAN:
                parameters.put(gwtConfigParameter.getId(), Boolean.parseBoolean(gwtConfigParameter.getValue()));
                break;
            case BYTE:
                parameters.put(gwtConfigParameter.getId(), Byte.parseByte(gwtConfigParameter.getValue()));
                break;
            case CHAR:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue().toCharArray());
                break;
            case DOUBLE:
                parameters.put(gwtConfigParameter.getId(), Double.parseDouble(gwtConfigParameter.getValue()));
                break;
            case FLOAT:
                parameters.put(gwtConfigParameter.getId(), Float.parseFloat(gwtConfigParameter.getValue()));
                break;
            case INTEGER:
                parameters.put(gwtConfigParameter.getId(), Integer.parseInt(gwtConfigParameter.getValue()));
                break;
            case LONG:
                parameters.put(gwtConfigParameter.getId(), Long.parseLong(gwtConfigParameter.getValue()));
                break;
            case PASSWORD:
            case STRING:
            default:
                parameters.put(gwtConfigParameter.getId(), gwtConfigParameter.getValue());
                break;
            case SHORT:
                parameters.put(gwtConfigParameter.getId(), Short.parseShort(gwtConfigParameter.getValue()));
                break;
            }
        }
        return parameters;
    }

}
