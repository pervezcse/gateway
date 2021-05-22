Technologies
============
Language: Java 8 (prerequisite to build)\
Backend: Spring Boot\
DB: h2\
Automated Build Tool: maven (prerequisite to build)\
UI: React\
Unit test: JUnit\
IDE: IntelliJ IDEA

How to perform Unit Tests and build the Jar
===========================================
go to the project folder run following command:\
mvn clean package

How to perform Integration Tests
================================
go to the project folder run following command:\
mvn failsafe:integration-test

How to run the Jar
==================
java -jar target/gateway-0.0.1-SNAPSHOT.jar

Website URL
===========
http://localhost:8080

API URL for Gateway resource
============================
http://localhost:8080/api/gateways

Automatic Test Data Import
==========================
data.sql: This file can be found in src/main/resources \
It contains test data which'll be automatically populated in db when the jar is executed

Sample Requests
===============
sample_requests.txt: It contains sample requests to run in Postman

Additional Notes
================
Design Pattern used: Immutable Object, Dependency Injection, Singleton, Optional
