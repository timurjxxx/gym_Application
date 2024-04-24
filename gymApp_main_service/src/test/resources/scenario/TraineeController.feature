Feature: Trainee Controller

  Scenario: Get trainee profile
    Given a trainee username
    When the get trainee profile request is sent
    Then the trainee profile should be returned


  Scenario: Delete trainee profile
    Given a trainee username for deleting
    When the delete trainee profile request is sent
    Then the trainee profile should be deleted and return 200


  Scenario: Get trainee profile with invalid username
    Given an invalid trainee username
    When the get trainee profile request with invalid username is sent
    Then the API should return a not found response for trainee