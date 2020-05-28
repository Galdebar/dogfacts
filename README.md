#DogFacts

A REST-full app that provides random facts about dogs.
Consumes remote API to get the facts: https://alexwohlbruck.github.io/cat-facts/docs/

##Pre-requisites
-JDK 11
-Maven

##Quickstart
`$./mvn clean install` - clean build with all tests. All tests are integration tests, so will fail if the system is dosconnected from the internet.

Default app port is `8080`. Can be changed in application.properties.

##Fact
Example of the default JSON object used in all endpoints:
````
{
  "text": "In total there is said to be around 400 million dogs in the world.",
  "_id": "5b12d7cccf4b960d5eb02ec7"
}
````

##Endpoints
- `/facts` - Produces a list of user-submited facts waiting to be verified.
- `facts/:factid` - Fetches specific fact. 
Example request `GET /facts/5b12d7cccf4b960d5eb02ec7`

- `/facts/random` - Fetches a random fact. 
    - `?amount=` -  Optional query parameter. Returns a list of random facts not larger than the specified amount. Will not return more than 500 facts.
    Example request `GET/facts/random?amount=25`
    
##Design & Decisions
### RestTemplate
It comes pre-packaged with Spring Started Web, so it integrates into the rest of the app automatically and already has error handling when consuming remote API out-of-the-box.
It also allows easy object mapping.

##Fact
Since the requirement was to have easy-to-read facts, the Fact object is a heavily simplified version of the similar objects from the remote API. There's always room to expand if needed.

##QueuedFact
Certain requests to the remote API produce specific response objects. To make object mapping easier, QueuedFact mimics that response object.
An alternative way would be to parse the response into JsonNode object and then map it to a list of Fact objects, but it makes the code harder to read.

##Integration Tests
Since the app is very simple, even integration tests run fairly fast, so I've decided to forego unit testing.

