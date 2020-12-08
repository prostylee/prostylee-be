Feature: Ping service is running
  Scenario: client makes call to GET /ping
    When the client calls /ping
    Then the client receives status code of 200
    And the client receives a text is "Server is running"