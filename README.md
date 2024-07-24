# PACs Demo Guide

## Requirements
1. Java 17
2. Orthanc

## Installation of Orthanc
1. Prerequisite: make sure to update your package definition before installing:
     ```bash
      sudo apt update
     ```
2. To install Orthanc and its plugins:

     ```bash
      $ sudo apt install orthanc
      $ sudo apt install orthanc-dicomweb
      $ sudo apt install orthanc-gdcm
      $ sudo apt install orthanc-imagej
      $ sudo apt install orthanc-mysql
      $ sudo apt install orthanc-postgresql
      $ sudo apt install orthanc-python
      $ sudo apt install orthanc-webviewer
      $ sudo apt install orthanc-wsi
     ```
3.  Run the command below to ensure Orthanc is running:
     ```bash
      $ sudo systemctl status orthanc.service
     ```
## Clone and run application
1. Clone this repository into your local machine :
2. Install maven dependencies. In the root directory, run the command
     ```bash
       mvn clean install
      ```
3. To start the application, run the command:
      ```bash
       mvn clean package spring-boot:run
      ```
4. The application will start on port 8408



# Points to note
1. Upon initialization, the application will generate a default superuser with the credentials\
      Username: admin@pacs.demo.com\
      Password: 12345678
2. A token is generate upon successful login.
3. Append the token on the header of all your requests as all API endpoints require authentication.