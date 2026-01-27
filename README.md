# client-profile-api

## Key features:

1. Asynchronous service that mocks obtaining the beneficiaries for a particular account. The service takes 2 * primary
   id seconds to return a result.
2. Error messages formatted by the ControllerExceptionHandler. The services and controllers create a
   `UserProfileClientException` and the handler uses the embedded `ClientErrorType` to format the message and/or log an
   error message.
3. Separation of concerns -
    * The REST controller creates a `UserProfileRequest` to send to the service which allows the controller the ability
      to prevent SQL injection attacks.
    * The service uses a mapper to transform the async service response and repository response into a
      `UserProfileResponse`. The separation allows the database and service to change implementation without affecting
      the existing REST clients.
        * For example - the database is too slow so the database is flattened and moved to a Dynamo NoSql database
          schema which allows massive scalability. If Separation of Concerns was not implemented, then the change would
          have affected the existing REST clients.
    * The REST controller passes CreateUserProfileRequest to the service. The service validates the request and uses
      several data mappers to create the database entity. Again, the calling program does not have to deal with any
      database schema changes.
4. Optimistic locking - the database verifies another thread has not modified the database row. This required addition
   of a new column which resulted in no changes to the REST API.
5. Spring Retry and Recover - Spring retries the method several times and then calls the recover method.
    * The method also immediately calls a different recover method when a validation error occurs. There is no reason to
      retry a method that will fail every time.

## Tech Stack

* PostgreSQL free database
* Java 21
* Spring Boot 3.5.9 - the final release of 3.5. Expires 30 June 2026.
* JUnit
* Mockito

## How to Run

* `mvn clean install ` runs all tests and adds two rows to the `user_profile` table
* `mvn clean spring-boot:run` starts the application and allows curl or Postman queries to retrieve/create test data.