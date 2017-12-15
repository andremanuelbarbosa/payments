Feature: Fetch a Payment Resource

  Scenario: Response is 200 and contains the Payment Resource when invoking the GET /payments/{id} endpoint and the Payment exists in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    When the client makes a "GET" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "200" status code in the Response
    And the Response content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"

  Scenario: Response is 404 when invoking the GET /payments/{id} endpoint and the Payment does NOT exist in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" does NOT exist in the DB
    When the client makes a "GET" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "404" status code in the Response
