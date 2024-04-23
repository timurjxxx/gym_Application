Feature: Trainer Workload Consumer

  Scenario: Receive valid message
    Given a valid JSON request
    When the message is received
    Then the workload should be updated


  Scenario: Receive invalid message
    Given an invalid JSON request
    When the invalid message is received
    Then the message should be sent to the dead-letter queue
