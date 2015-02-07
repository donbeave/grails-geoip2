grails.project.class.dir = 'target/classes'
grails.project.test.class.dir = 'target/test-classes'
grails.project.test.reports.dir = 'target/test-reports'

grails.project.dependency.resolver = 'maven'
grails.project.dependency.resolution = {
    inherits('global') {

    }
    log 'warn'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        mavenLocal()
        mavenCentral()
    }
    dependencies {
        runtime 'com.maxmind.geoip2:geoip2:2.1.0', {
            excludes 'junit'
        }
    }

    plugins {
        build(':release:3.0.1', ':rest-client-builder:2.0.3') {
            export = false
        }
    }
}
