Feature: Training Controller

  Scenario: Create a new training
    Given a valid training
    When the create training request is sent
    Then the training should be successfully created



  Scenario: Delete a training
    Given an existing training name
    When the delete training request is sent
    Then the training should be successfully deleted



  Scenario: Get trainee trainings by criteria
    Given a trainee username "testTrainee"
    And a criteria for trainee trainings
    When the get trainee trainings by criteria request is sent
    Then the trainee trainings should be returned



  Scenario: Get trainer trainings by criteria
    Given a trainer username "testTrainer"
    And a criteria for trainer trainings
    When the get trainer trainings by criteria request is sent
    Then the trainer trainings should be returned



  Scenario: Get invalid trainer trainings by criteria
    Given invalid trainer username "testTrainer"
    And a criteria for invalid trainer trainings
    When the get invalid trainer trainings by criteria request is sent
    Then the API should return a not found training response



  Scenario: Get invalid trainee trainings by criteria
    Given invalid trainee username "testTrainer"
    And a criteria for invalid trainee trainings
    When the get invalid trainee trainings by criteria request is sent
    Then the API should return a not found trainee training response



  Scenario: Delete a invalid training
    Given an invalid training name
    When the delete invalid training request is sent
    Then the training should return not found
