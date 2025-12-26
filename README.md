# client-profile-api

Key features:
1. Asynchronous service that mocks obtaining the beneficiaries for a particular account.  The service takes 2 * primary id seconds to return a result.
2. Error messages formatted by the ControllerExceptionHandler.  The services and controllers create a `UserProfileClientException` and the handler uses the embedded `ClientErrorType` to format the message and/or log an error message.
3. Separation of concerns - the REST controller creates a `UserProfileRequest` to send to the service.  The service uses a mapper to transform the async service response and repository response into a `UserProfileResponse`.  The separation allows the database and service to change implementation without affecting the existing REST clients.
