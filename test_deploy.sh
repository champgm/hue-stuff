#!/usr/bin/env bash

#!/usr/bin/env bash
export JAVA_HOME=`/usr/libexec/java_home -v '1.8*'`
sh /usr/local/Cellar/tomcat/8.*/libexec/bin/catalina.sh stop || true
mvn clean install
rm -rf /usr/local/Cellar/tomcat/8.*/libexec/webapps/hue-web-1.0-SNAPSHOT
cp hue-web/target/hue-web-1.0-SNAPSHOT.war /usr/local/Cellar/tomcat/8.*/libexec/webapps/
sh /usr/local/Cellar/tomcat/8.*/libexec/bin/catalina.sh start

while [ ! -f /usr/local/Cellar/tomcat/8.*/libexec/webapps/hue-web-1.0-SNAPSHOT/index_bs.jsp ]
do
  echo "Waiting for Tomcat to deploy WAR"
  sleep 2
done

#mv /usr/local/Cellar/tomcat/8.0.28/libexec/webapps/hue-web-1.0-SNAPSHOT/index_bs.jsp /usr/local/Cellar/tomcat/8.*/libexec/webapps/hue-web-1.0-SNAPSHOT/index_bs.jsp.bak
#ln hue-web/src/main/webapp/index_bs.jsp /usr/local/Cellar/tomcat/8.*/libexec/webapps/hue-web-1.0-SNAPSHOT/index_bs.jsp
touch /usr/local/Cellar/tomcat/8.*/libexec/webapps/hue-web-1.0-SNAPSHOT/deploy_done
