# Getting Started

### Description

This application provides a RESTful API for user to compute the price of their basket.

It follows the description provided in https://stephane-genicot.github.io/DevelopmentBooks.html

### Technologies

* Java 21
* Maven 3.*
* Spring Boot 3.3.5
* Swagger UI with OpenAPI 2.6.0
* Junit Jupiter 5.10

### How to run project

* Install Java 21 (or use IDE with Java embedded) → https://www.oracle.com/europe/java/technologies/downloads/#java21
* Install Maven (or use IDE with Maven embedded) → https://maven.apache.org/install.html
* Clone repository from https://github.com/ntonon/2025-CCE-E-DEV-035-DevelopmentBooks
* To compile application, run "mvn clean -U install"
* To launch application (on port 8080 by default), run "mvn spring-boot:run"

### Swagger

You can access the swagger from http://localhost:8080/swagger-ui/index.html. This page provides the contracts for APIs made available externally. It also gives user a quick tool to send HTTP request to the API and see the response.