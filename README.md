## Money Transfer Service 
[![Build Status](https://travis-ci.org/manojshevate/moneytransferservice.svg?branch=master)](https://travis-ci.org/manojshevate/moneytransferservice)

## Running tests
`./gradlew clean test`

## Running the app
```
./gradlew clean build
java -jar ./build/libs/moneytransferservice-1.0-SNAPSHOT-all.jar
```

## Postman script
Use `bank.postman_collection.json` for manual api testing. This can also be used in blue/green deployment. 
We can run this script against blue instance and once all tests passed we can switch it with green instance.

