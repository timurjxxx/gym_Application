Feature: Update Trainer Workload

  Scenario: Trainer workload is updated successfully
    Given a trainer workload request is received
    When the trainer workload is updated
    Then the trainer workload should be successfully updated

  Scenario: Invalid trainer workload request
    Given an invalid trainer workload request is received
    When the trainer workload is updated
    Then the update should fail with a bad request response



  Scenario: NUll username trainer workload request
    Given an nullpoint username trainer workload request is received
    When the null point trainer send endpoint
    Then the update should fail