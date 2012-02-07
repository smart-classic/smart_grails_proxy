grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

def externalConfigFileLocation
environments{
	production{
		externalConfigFileLocation="specify me"
	}
	development{
		externalConfigFileLocation="C:\\grails-config\\smart\\"
	}
	test{
		externalConfigFileLocation="${userHome}/grails-config/"
	}
}
grails.config.locations = [ ]

// External config settings derived from Mick Knutson's blog: http://www.baselogic.com/blog/
def PROPERTY_ENV_NAME = "${appName}.config.location"
def SYSTEM_ENV_NAME = "${appName}_config_location"
println "--------------------------------------------------------"

// 1: A command line option overrides everything
// Test by running:
// grails -Dsmartproxy.config.location=C:\temp\divr-config.groovy run-app
if (System.getProperty(PROPERTY_ENV_NAME) && new File(System.getProperty(PROPERTY_ENV_NAME)).exists()) {
    println "Including configuration file specified on command line: " + System.getProperty(PROPERTY_ENV_NAME)
    grails.config.locations << "file:" + System.getProperty(PROPERTY_ENV_NAME)
}
// 2: If no command line optins, check in ~/grails-config
else if (new File(externalConfigFileLocation+"${appName}-config.groovy").exists()) {
    println "*** User defined config: file:${externalConfigFileLocation}${appName}-config.groovy. ***"
    grails.config.locations = ["file:${externalConfigFileLocation}${appName}-config.groovy"]
}
// 3: Finally, check for a System Environment variable
//    that will define where we should look.
else if (System.getenv(SYSTEM_ENV_NAME) && new File(System.getenv(SYSTEM_ENV_NAME)).exists()) {
    println("Including System Environment configuration file: " + System.getenv(SYSTEM_ENV_NAME))
    grails.config.locations << "file:" + System.getenv(SYSTEM_ENV_NAME)
}
println "(*) grails.config.locations = ${grails.config.locations}"
println "--------------------------------------------------------"


println grails.config.locations

// log4j configuration
log4j = {

    appenders {
       'console' name:'stdout'
       'null' name: 'stacktrace'
    }

}


tomcat.deploy.username="manager"
tomcat.deploy.password="secret"
tomcat.deploy.url="http://10.36.142.250:8980/manager"

/* Use this as a template for `~/grails-config/smartproxy-config.groovy` */ 
/*
oauth {
    smartEmr {
        token = 'setme'
        secret = 'setme'
        apiBase = 'setme'
    }
}

cas{
    chb {
        skipValidation = false
        casValidationUrl = 'setme'
        casClientId = 'smartmpage'
        casServiceId = 'SmartWebApp'
    }
}

*/