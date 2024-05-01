Feature: Authentication API Tests

  Scenario: Logging in
    Given a valid login request
    When the login request is sent to the API
    Then the API should return a successful response

  Scenario: Creating a trainee
    Given a valid trainee creation request
    When the trainee creation request is sent to the API
    Then the API should return a successful response

  Scenario: Creating a trainer
    Given a valid trainer creation request
    When the trainer creation request is sent to the API
    Then the API should return a successful response

  Scenario: Changing password
    Given a valid change password request
    When the change password request is sent to the API
    Then the API should return a successful response



  Scenario: Changing password
    Given a invalid change password request
    When the invalid change password request is sent to the API
    Then the API should return a bad response


  Scenario: Creating a invalid  trainer
    Given a invalid trainer creation request
    When the invalid trainer creation request is sent to the API
    Then the API should return bad response

  Scenario: Creating a invalid  trainee
    Given a invalid trainee creation request
    When the invalid trainee creation request is sent to the API
    Then  API should return bad response