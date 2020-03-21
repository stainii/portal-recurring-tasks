# portal-recurring-tasks
[![Build Status](https://server.stijnhooft.be/jenkins/buildStatus/icon?job=portal-recurring-tasks/master)](https://server.stijnhooft.be/jenkins/job/portal-recurring-tasks/job/master/)

A generic module which allows you to manage recurring tasks.

Can be deployed multiple times, for each specific purpose: a deployment to manage household tasks, a deployment to keep an eye on my sportivity, ...

If you build a different front-end for each deployment, each deployment looks like a separate app. Like:

* Housagotchi: a module for my personal portal, which turns my household into a Tamagotchi. Gamification of keeping the house clean, you know...
* Health: warns you if you aren't behaving like a sporty-spice
* Social: reminds you to keep in touch with friends and collegues
* ...

## How to deploy this generic module as a specific module
### Running directly in the IDE / as a jar
You'll need to override several (Spring) properties:

| Name | Example value | Description | Required? |
| ---- | ------------- | ----------- | -------- |
| deployment-name | Housagotchi | Name you want to give to the deployment of this application | required
| server.port | 2002 | Port which this application can run on | required 
| spring.datasource.url | jdbc:postgresql://localhost:5433/portal-housagotchi | JDBC url to connect to the database | required
| spring.datasource.username | my-username | Username to log in to the database | required
| spring.datasource.password | secret | Password to log in to the database | required
| spring.rabbitmq.host | rabbitmq | Host where RabbitMQ is located | required
| spring.rabbitmq.port | 5672 | Port of RabbitMQ| required
| spring.rabbitmq.username | my-username | Username to log in to RabbitMQ | required
| spring.rabbitmq.password | secret | Password to log in to RabbitMQ | required

### Running with Docker
When running with Docker, this is done by providing the required Docker environment variables.

| Name | Example value | Description | Required? |
| ---- | ------------- | ----------- | -------- |
| DEPLOYMENT_NAME | Housagotchi | Name you want to give to the deployment of this application | required 
| POSTGRES_URL | jdbc:postgresql://localhost:5433/portal-housagotchi | JDBC url to connect to the database | required
| POSTGRES_USERNAME | my-username | Username to log in to the database | required
| POSTGRES_PASSWORD | secret | Password to log in to the database | required
| RABBITMQ_HOST | rabbitmq | Host where RabbitMQ is located | required
| RABBITMQ_PORT | 5672 | Port of RabbitMQ| required
| RABBITMQ_USERNAME | my-username | Username to log in to RabbitMQ | required
| RABBITMQ_PASSWORD | secret | Password to log in to RabbitMQ | required
| EUREKA_SERVICE_URL | http://portal-eureka:8761/eureka | Url of Eureka | required
| JAVA_OPTS_RECURRING_TASKS | -Xmx400m -Xms400m | Java opts you want to pass to the JVM | optional


## Published events
This application publishes the following events:

* **Reminder**: a reminder that a task is due
    * **flowId**: [deployment-name]-[task id], for example Housagotchi-1001
    * **flowAction**: START
    * **data**
        * **type**: *"reminder"*
        * **urgent**: true/false
        * **task**: string
        * **lastExecution**: local date time
        * **minDueDate**: expected due date
        * **maxDueDate**: maximum due date, if this due date is not met things get really nasty
* **Execution**: the task has been executed
    * **flowId**: [deployment-name]-[task id], for example Housagotchi-1001
    * **flowAction**: END
    * **data**
        * **type**: *"execution"*
* **Cancellation**: the task has been cancelled
    * **flowId**: [deployment-name]-[task id], for example Housagotchi-1001
    * **flowAction**: END
    * **data**
        * **type**: *"cancellation"*

When a task becomes **urgent**, a **cancel event** is sent, **followed by a new reminder event**.

### Release
#### How to release
To release a module, this project makes use of the JGitflow plugin and the Dockerfile-maven-plugin.

1. Make sure all changes have been committed and pushed to Github.
1. Switch to the dev branch.
1. Make sure that the dev branch has at least all commits that were made to the master branch
1. Make sure that your Maven has been set up correctly (see below)
1. Run `mvn jgitflow:release-start -Pproduction`.
1. Run `mvn jgitflow:release-finish -Pproduction`.
1. In Github, mark the release as latest release.
1. Congratulations, you have released both a Maven and a Docker build!

More information about the JGitflow plugin can be found [here](https://gist.github.com/lemiorhan/97b4f827c08aed58a9d8).

##### Maven configuration
At the moment, releases are made on a local machine. No Jenkins job has been made (yet).
Therefore, make sure you have the following config in your Maven `settings.xml`;

````$xml
<servers>
		<server>
			<id>docker.io</id>
			<username>your_username</username>
			<password>*************</password>
		</server>
		<server>
			<id>portal-nexus-releases</id>
			<username>your_username</username>
            <password>*************</password>
		</server>
	</servers>
````
* docker.io points to the Docker Hub.
* portal-nexus-releases points to my personal Nexus (see `<distributionManagement>` in the project's `pom.xml`)
