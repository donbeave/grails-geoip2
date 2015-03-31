/*
 * Copyright 2015 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.plugin.geoip2

import com.maxmind.geoip2.DatabaseReader
import com.maxmind.geoip2.WebServiceClient
import com.maxmind.geoip2.model.*

import javax.annotation.PostConstruct

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class GeoIp2Service {

    def grailsApplication

    WebServiceClient webClient
    DatabaseReader cityDb
    DatabaseReader anonymousIpDb
    DatabaseReader connectionTypeDb
    DatabaseReader domainDb
    DatabaseReader ispDb

    CountryResponse getCountry(String addr) {
        if (webClient) {
            return webClient.country(toInetAddr(addr))
        } else {
            return cityDb.country(toInetAddr(addr))
        }
    }

    InsightsResponse getInsights(String addr) {
        if (webClient) {
            return webClient.insights(toInetAddr(addr))
        } else {
            // TODO
        }
    }

    CityResponse getCity(String addr) {
        if (webClient) {
            return webClient.city(toInetAddr(addr))
        } else {
            return cityDb.city(toInetAddr(addr))
        }
    }

    AnonymousIpResponse getAnonymousIp(String addr) {
        if (webClient) {
            // TODO
        } else {
            return anonymousIpDb.anonymousIp(toInetAddr(addr))
        }
    }

    ConnectionTypeResponse getConnectionType(String addr) {
        if (webClient) {
            // TODO
        } else {
            return connectionTypeDb.connectionType(toInetAddr(addr))
        }
    }

    DomainResponse getDomain(String addr) {
        if (webClient) {
            // TODO
        } else {
            return domainDb.domain(toInetAddr(addr))
        }
    }

    IspResponse getIsp(String addr) {
        if (webClient) {
            // TODO
        } else {
            return ispDb.isp(toInetAddr(addr))
        }
    }

    @PostConstruct
    def init() {
        initWebClient()
        initDb()
    }

    private void initWebClient() {
        if (config.webService.userId && config.webService.licenseKey) {
            webClient = new WebServiceClient.Builder(config.webService.userId, config.webService.licenseKey)
                    .host(config.webService.host).locales(config.webService.locales)
                    .connectTimeout(config.webService.connectTimeout)
                    .readTimeout(config.webService.readTimeout).build()
        }
    }

    private void initDb() {
        if (config.db.city) {
            File database = new File(config.db.city)

            cityDb = new DatabaseReader.Builder(database).locales(config.db.locales).build()
        }
        if (config.db.anonymousIp) {
            File database = new File(config.db.anonymousIp)

            anonymousIpDb = new DatabaseReader.Builder(database).locales(config.db.locales).build()
        }
        if (config.db.connectionType) {
            File database = new File(config.db.connectionType)

            connectionTypeDb = new DatabaseReader.Builder(database).locales(config.db.locales).build()
        }
        if (config.db.domain) {
            File database = new File(config.db.domain)

            domainDb = new DatabaseReader.Builder(database).locales(config.db.locales).build()
        }
        if (config.db.isp) {
            File database = new File(config.db.isp)

            ispDb = new DatabaseReader.Builder(database).locales(config.db.locales).build()
        }
    }

    // TODO
    private InetAddress toInetAddr(String addr) {
        Inet4Address.getByName(addr)
    }

    def getConfig() {
        grailsApplication.config.grails.plugin.geoip2
    }

}
