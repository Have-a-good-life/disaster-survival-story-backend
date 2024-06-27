#!/bin/bash

# Change directory to where the application is deployed
cd /home/ec2-user/app

# Assuming your application is a Spring Boot application deployed as a WAR file
# You may need to adjust this command based on your specific deployment requirements
java -jar *.war > /dev/null 2> /dev/null < /dev/null &
