
# MARVEL_CHARACTER_API


# Description
This is a code challenge 

Use Marvel API to build a characters API

Serve an endpoint __/characters__ that returns all the Marvel character ids only, in a JSON array of numbers.

Request should be like this

GET /characters
```json
[1009718,1017100]
````

Serve an endpoint /characters/{characterId} that contains the real-time data from
the Marvel API /v1/public/characters/{characterId}, but containing only the following information
about each character: id, name, description, thumbnail

>
> Enable a translated version to another language of the character’s “description”
>
> Accept a query parameter with the language ISO-639-1 code: /characters/ {characterId}?language={languageCode}
>
> Use any translation service of your choice ( the goal is to execute the translations in real-time)

Request should be like this

GET /characters/{id}?language={languageCode}

```json
{
  "id": 1009718,
  "name":"Wolverine",
  "description": "Born with super-human senses and the power to ...",
  "thumbnail": {
    "path": "http://i.annihil.us/u/prod/marvel/i/mg/2/60/537bcaef0f6cf",
    "extension": "jpg"
  }
}
```



## Prerequisites
- Git
- Java 17+
- Maven 3.8.4+
- API keys from marvel ( http://developer.marvel.com/)

In order for this project to be executed, it is necessary to set up this environment variables:

Linux or Mac
```shell
export MARVEL_PUBLIC_KEY=ZZZ-ZZZZ-ZZZZ
export MARVEL_PRIVATE_KEY=XXX-XXX-XXXX
```

Windows
```shell
$env:MARVEL_PUBLIC_KEY='ZZZ-ZZZZ-ZZZZ'
$env:MARVEL_PRIVATE_KEY='XXX-XXX-XXXX'
```


Run the project after set up the environment:

- Build and execute tests of the project (first time will take longer than usual because will download all dependencies)
```bash
mvn clean package
```

- Execution
```bash
java -jar target/marvel.api-0.0.1-SNAPSHOT.jar
```