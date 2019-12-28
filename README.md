# portal-housagotchi
[![Build Status](https://server.stijnhooft.be/jenkins/buildStatus/icon?job=portal-housagotchi/master)](https://server.stijnhooft.be/jenkins/job/portal-housagotchi/job/master/)

A module for my personal portal, which turns my household into a Tamagotchi. Gamification of keeping the house clean, you know...

## Published events
This application publishes the following events:

* **Reminder**: a reminder that a task is due
    * **flowId**: Housagotchi-[task id], for example Housagotchi-1001
    * **data**
        * **type**: *"reminder"*
        * **urgent**: true/false
        * **task**: string
        * **lastExecution**: local date time
* **Execution**: the task has been executed
    * **flowId**: Housagotchi-[task id], for example Housagotchi-1001
    * **data**
        * **type**: *"execution"*
* **Cancellation**: the task has been cancelled
    * **flowId**: Housagotchi-[task id], for example Housagotchi-1001
        * **data**
            * **type**: *"cancellation"*

## Environment variables
| Name | Example value | Description | Required? |
| ---- | ------------- | ----------- | -------- |
| POSTGRES_PASSWORD | secret | Password to log in to the database | required
| JAVA_OPTS_HOUSAGOTCHI | -Xmx400m -Xms400m | Java opts you want to pass to the JVM | optional

### Release
#### Maven release
To release a module, this project makes use of the JGitflow plugin.
More information can be found [here](https://gist.github.com/lemiorhan/97b4f827c08aed58a9d8).

At the moment, releases are made on a local machine. No Jenkins job has been made (yet).

#### Docker release
A Docker release is made, by running `mvn clean deploy` on the Maven release branch.