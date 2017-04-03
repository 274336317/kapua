/*******************************************************************************
* Copyright (c) 2016, 2017 Eurotech and/or its affiliates                       
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
export default class DevicesService implements IDevicesService {

    constructor(private $http: ng.IHttpService) {
    }

    getDevices(): ng.IHttpPromise<ListResult<Device>> {
        return this.$http.get("api/_/devices?fetchAttributes=connection");
    }

    getDeviceById(deviceID: string): ng.IHttpPromise<Device> {
        return this.$http.get("/api/_/devices/" + deviceID);
    }

    getBundlesByDeviceId(deviceID: string): ng.IHttpPromise<DeviceBundles> {
        return this.$http.get("/api/_/devices/" + deviceID + "/bundles");
    };

    getPackagesByDeviceId(deviceID: string): ng.IHttpPromise<DevicePackages> {
        return this.$http.get("/api/_/devices/" + deviceID + "/packages");
    };

    startDeviceBundle(deviceID: string, bundleID: number): ng.IHttpPromise<DeviceBundles> {
        return this.$http.post("/api/_/devices/" + deviceID + "/bundles/" + bundleID + "/_start", {});

    };

    stopDeviceBundle(deviceID: string, bundleID: number): ng.IHttpPromise<DeviceBundles> {
        return this.$http.post("/api/_/devices/" + deviceID + "/bundles/" + bundleID + "/_stop", {});

    };

}