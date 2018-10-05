package com.qaprosoft.selenium.grid

import groovy.json.JsonOutput
import groovy.json.JsonSlurper;

class ProxyInfo {

    private def dslFactory
    private String proxyInfoUrl
    private def platformDeviceListMap = ["android":[], "ios":[]]
    private def baseDeviceList = ["DefaultPool", "ANY"]

    ProxyInfo(dslFactory) {
        this.dslFactory = dslFactory
        this.proxyInfoUrl = dslFactory.binding.variables.QPS_HUB + "/grid/admin/ProxyInfo"
    }

    //TODO: reused grid/admin/ProxyInfo to get atual list of iOS/Android devices
    public def getDevicesList(String platform) {
        def deviceList = platformDeviceListMap.get(platform.toLowerCase())
        try {
            if (deviceList.size() == 0) {
                def json = new JsonSlurper().parse(proxyInfoUrl.toURL())
                json.each {
                    if (platform.equalsIgnoreCase(it.configuration.capabilities.platform)) {
                        dslFactory.println "platform: " + it.configuration.capabilities.platform[0] + "; device: " + it.configuration.capabilities.browserName[0]
                        deviceList.add(it.configuration.capabilities.browserName[0]);
                    }
                }
                platformDeviceListMap.put(platform.toLowerCase(), deviceList)
            }
        } catch (Exception e) {
            dslFactory.println e.getMessage()
        }
        return baseDeviceList + deviceList.sort()
    }

    public def getGridConsoleInfo(String platform) {
        String consoleUrl = dslFactory.binding.variables.QPS_HUB + "/grid/console"
        try {
            def response = consoleUrl.toURL()
            //def json = new JsonSlurper().parse(consoleUrl.toURL())
            dslFactory.println "CONSOLE:\n"
            dslFactory.println response
//            dslFactory.println JsonOutput.prettyPrint(json.toString())
        } catch (Exception e) {
            dslFactory.println e.getMessage()
        }
        //return baseDeviceList + deviceList.sort()
    }
}