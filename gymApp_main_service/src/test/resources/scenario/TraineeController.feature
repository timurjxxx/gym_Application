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



  Scenario: Delete trainee profile
    Given invalid trainee username for deleting
    When the delete ivalid trainee profile request is sent
    Then the trainee profile should return bad response



  Scenario: Update trainee profile
    Given a trainee username and updated trainer details
    When the update trainee profile request is sent
    Then the trainee profile should be updated

  Scenario: Update invalid trainee profile
    Given a trainee invalid username and updated trainer details
    When the update invalid trainee profile request is sent
    Then the trainee invalid profile should not found