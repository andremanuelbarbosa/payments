Feature: Delete a Payment Resource

  Scenario: Response is 204 and the Payment Resource should be removed when invoking the DEL /payments/{id} endpoint and the Payment exists in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    When the client makes a "DELETE" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "204" status code in the Response
    And the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" does NOT exist in the DB

  Scenario: Response is 404 and no action should be performed when invoking the DEL /payments/{id} endpoint and the Payment Resource does NOT exist in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" does NOT exist in the DB
    When the client makes a "DELETE" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "404" status code in the Response
