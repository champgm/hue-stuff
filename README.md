# Hue-Stuff

"Hue Stuff" is a project to interface directly with the Phillips Hue bridge. The bridge has a fairly open and well documented API that is accessed via a RESTful calls. Our main goals for this project are as follows:

  - Provide a command line interface for bridge functions
  - Learn how to use some of the functional aspects of Java 8
  - Provide a web interface for bridge functions
  - Learn how to use various web technologies
  - Learn about the infrastructure required to accomplish the above


### Tech

Hue-Stuff uses a number of open source projects to work properly:

* [Java 8] - Java.
* [Apache Tomcat] - A server used to host [War Files]
* [Apache Avro] - To create models of various Hue concepts
* [Jackson] - To (sometimes with some fudging) directly parse the Hue Bridge's output into the Avro models
* [Apache Maven] - For dependency and plugin management

### Network Diagram
Here's a diagram explaining how the Hue Bridge connects to the lights, your network, and how the server running hue-stuff fits in.

![Network Graph](/network-graph.png?raw=true)

### Installation

The first step is to build this project with Maven. A simple "mvn clean install" should suffice. Once that's done, there are two files you might want to consume.

The first is the console jar located here:
```
hue-console/target/hue-console-1.0-SNAPSHOT.jar
```
The console jar can be run from the command line like so:
```
java -jar hue-console/target/hue-console-1.0-SNAPSHOT.jar ...
```
Running it without parameters will display some usage help and each available command will display its own help as well.

You also might want to consume the WAR file. To do so, you will first need to get an instance of Tomcat installed and running. This is not something we can help with especially since it may work with almost any operating system. You should be running at least Version 8. Once you are, you will need to put this file into the webapps folder of Tomcat.
```
hue-web/target/hue-web-1.0-SNAPSHOT.war
```
You will also need to place a "bridge.properties" file into the classpath. Either by injecting it into the WAR file or placing it in a library or resource folder of Tomcat's. An example file used for testing can be found here: https://github.com/champgm/hue-stuff/blob/master/hue-utilities/src/test/resources/bridge.properties

This file should contain the local IP address of your bridge and a token specific to this application. To create the token, you will need physical access to the bridge and you will need to follow the instructions here: http://www.developers.meethue.com/documentation/getting-started


[//]: # (STOLEN FROM DILLINGER.IO : These are reference links used in the body of this note and get stripped out when the markdown processor does its job. There is no need to format nicely because it shouldn't be seen. Thanks SO - http://stackoverflow.com/questions/4823468/store-comments-in-markdown-syntax)


   [Java 8]: <http://www.oracle.com/technetwork/java/javase/documentation/index.html>
   [Apache Tomcat]: <https://tomcat.apache.org/>
   [War Files]: <https://en.wikipedia.org/wiki/WAR_%28file_format%29>
   [Apache Avro]: <https://avro.apache.org/>
   [Jackson]: <https://github.com/FasterXML/jackson>
   [Apache Maven]: <https://maven.apache.org/>
