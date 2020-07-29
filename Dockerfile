FROM tomcat

COPY eappointments-be-admin/target/*.war /usr/local/tomcat/webapps/

EXPOSE 8080
CMD ["catalina.sh", "run"]
