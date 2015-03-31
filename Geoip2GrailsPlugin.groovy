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

import grails.util.Environment
import org.codehaus.groovy.grails.commons.GrailsApplication

/**
 * @author <a href='mailto:donbeave@gmail.com'>Alexey Zhokhov</a>
 */
class Geoip2GrailsPlugin {

    def version = '0.1'
    def grailsVersion = '2.0 > *'
    def dependsOn = [:]

    def title = 'Grails GeoIP2 Plugin'
    def author = 'Alexey Zhokhov'
    def authorEmail = 'donbeave@gmail.com'
    def description = '''\\
This plugin facilitates grails integration with the opensource GeoIP2 framework offered by MaxMind.
Using its straightforward API one can find out the country, area, city, geographical coordinates and
others based on an IP.

This product includes GeoLite data created by MaxMind, available from
[www.maxmind.com|http://www.maxmind.com].
'''

    def documentation = 'http://grails.org/plugin/geoip2'

    def license = "APACHE"

    def developers = [
            [name: 'Alexey Zhokhov', email: 'donbeave@gmail.com']
    ]
    def organization = [name: 'Polusharie', url: 'http://www.polusharie.com']

    def issueManagement = [system: 'GitHub', url: 'https://github.com/donbeave/grails-geoip2/issues']
    def scm = [url: 'https://github.com/donbeave/grails-geoip2/']

    // Get a configuration instance
    def getConfiguration(GrailsApplication application) {
        def config = application.config

        // try to load it from class file and merge into GrailsApplication#config
        // Config.groovy properties override the default one
        try {
            Class dataSourceClass = application.getClassLoader().loadClass('DefaultGeoIP2Config')
            ConfigSlurper configSlurper = new ConfigSlurper(Environment.current.name)
            Map binding = [:]
            binding.userHome = System.properties['user.home']
            binding.grailsEnv = application.metadata['grails.env']
            binding.appName = application.metadata['app.name']
            binding.appVersion = application.metadata['app.version']
            configSlurper.binding = binding

            ConfigObject defaultConfig = configSlurper.parse(dataSourceClass)

            ConfigObject newGeoConfig = new ConfigObject()
            newGeoConfig.putAll(defaultConfig.geoip2.merge(config.grails.plugin.geoip2))

            config.grails.plugin.geoip2 = newGeoConfig
            application.configChanged()
            return config.grails.plugin.geoip2
        } catch (ClassNotFoundException e) {
            log.debug("GeoConfig default configuration file not found: ${e.message}")
        }

        // Here the default configuration file was not found, so we
        // try to get it from GrailsApplication#config and add some mandatory default values
        if (config.grails.plugin.containsKey('geoip2')) {
            if (config.grails.plugin.geoip.active == [:]) {
                config.grails.plugin.geoip.active = true
            }
            if (config.grails.plugin.geoip.printStatusMessages == [:]) {
                config.grails.plugin.geoip.printStatusMessages = true
            }
            application.configChanged()
            return config.grails.plugin.geoip
        }

        // No config found, add some default and obligatory properties
        config.grails.plugin.geoip.active = true
        config.grails.plugin.geoip.printStatusMessages = true
        application.configChanged()
        return config
    }

}
