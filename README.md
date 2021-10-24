# Alert

Provide data to emergency services.

## Installation

-Verify all the properties in "alert/src/main/resources/application.properties" are to your liking.
-Execute the line "mvn install" at the root of the project.
-Move the generated jar file in alert/target in the location you desire.
-Move the data file according to path defined in application.properties by default the path is "resources/data.json" relative to the jar file.
-In case no data file is available create a json file with a text editor containing the 2 following characters "{}".

## Usage

The following endpoints accept POST, PUT and DELETE request :
/person
/medicalRecord
/firestation
The data need to be contained in the body of the request in the form of a json string.

/!\in case of "DELETE /firestation" if an address is provided only the mapping of that address will be deleted, if the address is null ALL mapping related to the provided station will be deleted.

The following endpoints accept GET request argument must be provided in the url:




## Credits

Educationnal project for OpenClassrooms made by Kafeinedev with the help of Spring Boot.
