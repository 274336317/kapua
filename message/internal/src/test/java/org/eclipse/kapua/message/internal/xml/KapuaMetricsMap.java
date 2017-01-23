/*******************************************************************************
 * Copyright (c) 2011, 2016 Eurotech and/or its affiliates and others
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *     Eurotech - initial API and implementation
 *
 *******************************************************************************/
package org.eclipse.kapua.message.internal.xml;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.adapters.XmlJavaTypeAdapter;
import java.util.Map;

@XmlRootElement(name = "metricsmap")
@XmlAccessorType(XmlAccessType.FIELD)
public class KapuaMetricsMap {

    @XmlElement(name = "metrics")
    @XmlJavaTypeAdapter(KapuaMetricsMapAdapter.class)
    private Map<String, Object> metrics;

    public Map<String, Object> getMetrics()
    {
        return metrics;
    }

    public void setMetrics(Map<String, Object> metrics)
    {
        this.metrics = metrics;
    }
}
