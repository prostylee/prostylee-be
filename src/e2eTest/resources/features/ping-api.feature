Feature: Ping service is running

  Scenario Outline: client makes call to GET /ping
    When the client calls the ping endpoint
    Then the client receives status code of <status>
    And the client receives a text is <message>
    Examples:
      | status | message                |
      | 200    | "Server is running"    |