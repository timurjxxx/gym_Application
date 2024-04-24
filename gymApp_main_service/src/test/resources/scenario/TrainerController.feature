Feature: Trainer Controller

  Scenario: Get trainer profile
    Given a trainer username
    When the get trainer profile request is sent
    Then the trainer profile should be returned

  Scenario: Update trainer profile
    Given a trainer username and updated trainer details
    When the update trainer profile request is sent
    Then the trainer profile should be updated

  Scenario: Get not assigned active trainers
    Given a username
    When the get not assigned active trainers request is sent
    Then the list of not assigned active trainers should be returned



  Scenario: Get trainer profile with invalid username
    Given an invalid username
    When the get trainer profile request with invalid username is sent
    Then the API should return a not found response