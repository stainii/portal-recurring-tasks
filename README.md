# portal-housagotchi
[![Build Status](https://server.stijnhooft.be/jenkins/buildStatus/icon?job=portal-housagotchi/master)](https://server.stijnhooft.be/jenkins/job/portal-housagotchi/job/master/)

A module for my personal portal, which turns my household into a Tamagotchi. Gamification of keeping the house clean, you know...

## Environment variables
| Name | Example value | Description | Required? |
| ---- | ------------- | ----------- | -------- |
| POSTGRES_PASSWORD | secret | Password to log in to the database | required
| JAVA_OPTS_PORTAL_HOUSAGOTCHI | -Xmx400m -Xms400m | Java opts you want to pass to the JVM | optional
