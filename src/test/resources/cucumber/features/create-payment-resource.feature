Feature: Create a Payment Resource

  Scenario: Response is 201 and contains the Payment Resource created when invoking the POST /payments endpoint and the Payment does NOT exist in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" does NOT exist in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    When the client makes a "POST" request to "/payments"
    Then the client should receive a "201" status code in the Response
    And the Response content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"

  Scenario: Response is 400 when invoking the POST /payments endpoint and the Payment Resource is malformed

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" does NOT exist in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43-malformed"
    When the client makes a "POST" request to "/payments"
    Then the client should receive a "400" status code in the Response

  Scenario: Response is 400 when invoking POST /payments endpoint and the Payment exists in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    When the client makes a "POST" request to "/payments"
    Then the client should receive a "400" status code in the Response
