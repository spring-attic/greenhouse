# Greenhouse Reference Application

## Overview

Greenhouse is a Java web application built for the Spring community that has two primary goals:

1. Serve as an open-source, public-facing reference and driver for Spring technology,
   including Spring MVC, Security, Integration, Social and Mobile projects.      
2. Help foster and support our passionate application developer community through the development of useful community services.

## Features

* Extensive use of Spring Framework, Spring MVC, Spring Social, Spring Mobile, Spring Security, and Spring Integration projects.
* An OAuth-based App Catalog that allows Developers to develop new client apps for which users may establish Account->App connections.
* The ability to connect your local Greenhouse Account to Twitter, Facebook, LinkedIn, and TripIt ServiceProviders.
* Member Sign Up, Sign In, and Reset Password Modules.
* A Member Invite Module allowing you to invite your Facebook friends and email contacts.
* A Member Badge (Award) System that rewards members for community participation.
* A mobile web version of the app that can be used across multiple smartphone platforms.
* A Group Event Management Module for powering Spring events, such as conferences and user group meetings.
* Numerous Spring Framework contributions in the areas of:
 * Data (S3 File Storage and RDMS Migrations)
 * Web (Email Templating, Flash Map, Comet)
* An agile Continuous Deployment CI Process

## Check Out and Build from Source

1. Clone the repository from GitHub:

		$ git clone git://github.com/SpringSource/greenhouse.git

2. Navigate into the cloned repository directory:

		$ cd greenhouse

3. The project uses [Maven](http://maven.apache.org/) to build:

		$ mvn clean install

## Running from the Command Line

Deploy the .war to any Servlet 2.5 >, such as Tomcat. By default, the app will run in 'embedded' mode which does not require any external setup. The Tomcat 7 Maven plugin is configured for you in the POM file.

1. Launch Tomcat from the command line:

		$ mvn t7:run

2. Access the deployed webapp at 

		http://localhost:8080/greenhouse

## IDE Support

If you would like to build and run from a Maven/Java Dynamic Web-project-capable IDE, such as Eclipse/SpringSource Tool Suite, you may simply import "as a Maven Project" into your IDE and deploy the project to your IDE's embedded servlet container.
