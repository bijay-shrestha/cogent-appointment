# eappointments-be

#For development: application.yml->
spring:
  profiles.active: dev
  
#For production: application.yml->
spring:
  profiles.active: prod  
  
#Required files:
application.yml
application-dev.yml
application-prod.yml  

#Copy the properties from this URL:
https://gist.github.com/smreeti/a608116cca9e783adb4c80c29f62996e


#SPRINT-7:
1. Create table 'AdminFeature':
- To provide admin the flexibility of either collapse or un-collapse sidebar.
- By default, isSideBarCollapse = ‘Y’ while creating admin

2. Add email-scheduler for Admin Add/Update
- For client module, admin-module needs to be deployed to execute email-scheduler