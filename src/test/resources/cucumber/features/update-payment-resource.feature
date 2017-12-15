Feature: Update a Payment Resource

  Scenario: Response is 200 and contains the Payment Resource updated when invoking the PUT /payments/{id} endpoint and the Payment exists in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43-updated"
    When the client makes a "PUT" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "200" status code in the Response
    And the Response content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43-updated"

  Scenario: Response is 400 when invoking the PUT /payments/{id} endpoint and the Payment Resource is malformed

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43-malformed"
    When the client makes a "PUT" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "400" status code in the Response

  Scenario: Response is 400 when invoking the PUT /payments/{id} endpoint and the new Payment Resource ID is NOT the same as the one in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    And the Request content contains the Payment "216d4da9-e59a-4cc6-8df3-3da6e7580b77"
    When the client makes a "PUT" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "400" status code in the Response

  Scenario: Response is 404 when invoking the PUT /payments/{id} endpoint and the Payment does NOT exist in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" does NOT exist in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43-updated"
    When the client makes a "PUT" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "404" status code in the Response
