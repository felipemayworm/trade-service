# Trade Service

The trade validation service is based on Spring Boot 1.4.7, Java 8, Maven and REST.

The services controllers call the responsible to check and to validate the rules. </br>
The result is the original JSON plus the list of issues.

To check a single trade:</br>
localhost:8080/api/validateTrade

To check a N trades:</br>
localhost:8080/api/validateTrades
