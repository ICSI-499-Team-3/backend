# Backend of Mobile App for Health Tracking
This codebase services requests from our [React Native health tracking app](https://github.com/ICSI-499-Team-3/frontend). It handles queries to the MongoDB databse, performs logic checks to ensure data integrity, and facilitates the password reset feature of the app.

## Features
- Supports all GraphQL queries to the database
- Enforces the data models stored in the MongoDB database
- Emails users to facilitate the in-app password reset sequence

## Getting Started
This project was created and run in IntelliJ Idea, so it is the reccomended (although not required) IDE to run the application.

## Installation and Setup
- When using IntelliJ Idea, all dependancies will be recognized from the Gradle files and downloaded automatically.
- One file which must be configured to enable the emailing functionality is the [`email.config.json`](backend/src/main/java/com/team3/backend/helpers/email.config.json) file. All properties in the template JSON file must be properly configured to allow the application to email users. **If it is not configured, the rest of the application will run properly**, but an error will be thrown if a user requests a password reset code.
  - The following fields must be set:
  - `name`: The name of the sender which will appear when the user receives an email
  - `email`: The email address being used to send the password recovery emails
  - `password`: password to aforementioned account
  - `host`: SMTP (Simple Mail Transfer Protocol) hostname of the email service
  - `port`: SMTP port of the email service
  - For more information about SMTP for your particular mail service, Google `~you_mail_service~ SMTP settings`; Also refer to the [Simple Java Mail configuration page](https://www.simplejavamail.org/configuration.html#section-programmatic-api-common)

## Running
Run [`BackendApplication.java`](backend/src/main/java/com/team3/backend/BackendApplication.java) to start the project.
