# restassured
#### restassured API automation framework

This functional testing automation framework for RESTful API webservices has been built using:

1. Java8 (OpenJDK8) for programming language

2. Cucumber for BDD approach

3. JUnit for RunnerClass

4. restassured framework for firing requests & getting responses

5. Maven for build configuration & dependencies

6. Serenity for reporting


##### Steps to get this project:

1. git clone https://github.com/Bhala1987/restassured.git

2. Once cloned locally in your system, open the project in IntelliJ IDEA or your preferred interface (Eclipse for example)

3. Install the Lombok plugin in File --> Settings -> Plugins --> Search for Lombok - This is optional else you will see some errors in the code in RED as Lombok's Getter & Setter have been used instead of Java native ones.

4. Run the whole project as Maven as it is using the command '```mvn clean verify "-Dcucumber.options=src/test/resources/features --tags @api"```"

5. Once run is over without any issues, take a look at the Serenity report under target/site/serenity/index.html


NOTE: This whole project is built using java8 version as newer versions are having some problems, hence highly recommended to use Java8 open source JDK.


##### Commits:
1. Don't commit the following files/directories (also .gitignore file will take care of):

.idea

target

*.iml