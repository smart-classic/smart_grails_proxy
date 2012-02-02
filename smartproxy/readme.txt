------ A quick resolution to "java.lang.OutOfMemoryError: PermGen space" when deploying the smartproxy app on Tomcat------

In "CATALINA_HOME"/bin/catalina.sh edit the line where "JAVA_OPTS" is set, put
 
	JAVA_OPTS="-Xms512m -Xmx1024m -XX:MaxPermSize=128m"

A brief explanation on the issue: http://www.unidata.ucar.edu/projects/THREDDS/tech/tds4.2/faq.html#permGenOutOfMemoryError

What the java options stand for:
-Xmx sets the maximum amount of memory that can be allocated to the JVM heap; here it is being set to 1024 megabytes.
-Xms sets the initial amount of memory allocated to the JVM heap; here it is being set to 256 megabytes.
-XX:MaxPermSize set the maximum amount of memory that can be used for PermGen.

------------