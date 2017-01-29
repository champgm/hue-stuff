FROM tomcat:jre8
MAINTAINER github.com/champgm

ADD hue-web/target/hue-web-1.0-SNAPSHOT.war /usr/local/tomcat/webapps/

CMD ["catalina.sh", "run"]

EXPOSE 1981:8888