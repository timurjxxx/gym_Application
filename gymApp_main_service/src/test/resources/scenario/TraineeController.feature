Feature: Trainee Controller

  Scenario: Get trainee profile
    Given a trainee username
    When the get trainee profile request is sent
    Then the trainee profile should be returned

  Scenario: Update trainee profile
    Given a trainee username and updated trainee details
    When the update trainee profile request is sent
    Then the trainee profile should be updated

  Scenario: Delete trainee profile
    Given a trainee username for deleting
    When the delete trainee profile request is sent
    Then the trainee profile should be deleted and return 200
