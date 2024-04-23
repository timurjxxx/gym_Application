Feature: Trainer Workload Producer Tests

  Scenario: Updating Workload with Training
    Given a training and an action type
    When the update workload request is sent to the ActiveMQ queue
    Then the request should be successfully sent

  Scenario: Creating Request for Training
    Given a training and an action type
    When the request is created
    Then the request should contain the correct details

  Scenario: Handling Bad Request
    Given an invalid training or action type
    When the update workload request is sent to the ActiveMQ queue
    Then the system should handle the error appropriately