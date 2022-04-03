![University of Limerick Logo](https://gamma1.ul.ie/LabStats/Images/Image-UniversityOfLimerick.png)
# Hotel Reservation API
**Table of Contents**
- [Hotel Reservation API](#Hotel-Reservation-API)
    - [About](#about)
	- [Deploying Application](#deploying-application)
	- [API](#api)
		- [GraphQL References](#graphql-references)
		- [SpringBoot References](#springboot-references)
	- [Database](#database)
		- [Schema](#schema)
		- [SQLite References](#sqlite-references)
    - [Contibutors](#contibutors)
	- [Links](#links)

## About
Hotel reservation system is a Java project designed to show effective and efficient use of design patterns and architectural patterns. The applications allows users to book and reserve different rooms. The user specifies a check-in and check-out date and the system then checks if a room is available. A room can accommodate multiple guests specified by the user. The system can be used by multiple types of users including a customer and staff members.

## Deploying Application
The application can be ran in two main ways:
1. By running the application as an executable by using the `gradlew bootRun` command in the terminal within the project directory. This command makes gradle install the project and execute it.
2. By running the application as a service. A great guide to achieve this can be found [here](https://docs.spring.io/spring-boot/docs/current/reference/html/deployment.html#deployment.installing), but to summerise this can be achieved on unix based systems by doing the following:
	- Executing `gradlew bootJar` to compile an executable jar file in the `CS4227-Client/server/build/libs directory`
	- Create a script named `{name}.service` and place it in `/etc/systemd/system` directory following example below
	```
	[Unit]
	Description="Hotel Reservation API"

	[Service]
	User={username}
	ExecStart={project directory}/server/build/libs/{jar name}

	TimeoutStopSec=10
	Restart=on-failure
	RestartSec=5

	[Install]
	WantedBy=multi-user.target
	```
	- After the above service script is created the application can be started on the system using `systemctl start {name}.service` and can be registered to start automatically on system boot using `systemctl enable {name}.service`

## API
For the setup of the API two major frameworks were used. SpringBoot was used for the creation and managment of the webserver using Java. As well as GraphQL was chosen to support request handling using the `/graphql` endpoint to host all the data and services available in the application.
### GraphQL References
| Description | Link |
| :-----: | :-----: |
| Official Page | https://graphql.org/ |
| Quickstart | https://www.graphql-java.com/documentation/getting-started/ |
| Tutorial / Reference Sheet | https://www.tutorialspoint.com/graphql/index.htm |
### SpringBoot References
| Description | Link |
| :-----: | :-----: |
| Official Page | https://spring.io/projects/spring-boot|
| Quickstart | https://spring.io/quickstart |
| Tutorial / Reference Sheet | https://www.tutorialspoint.com/spring_boot/index.htm |

## Database
### Schema
![Database schema for server](/server-schema.png)
### SQLite References
| Description | Link |
| :-----: | :-----: |
| Official Page | https://sqlite.org/index.html |
| Quickstart | https://sqlite.org/quickstart.html |
| Tutorial / Reference Sheet | https://www.tutorialspoint.com/sqlite/index.htm |

## Contibutors
- Marcin Sęk -            18254187         
- Jordan Marshall -       18256716    
- Aleksandr Jakusevs -    18257038  
- Jakub Pažej -           18260179         
- Eoin McDonough -        18241646      

## Links
- Overall Repository: https://github.com/Crystal4B/CS4227-Software-Design-And-Architecture
- Client Repository:  https://github.com/Crystal4B/CS4227-Client/
- Server Repository:  https://github.com/Crystal4B/CS4227-Server/