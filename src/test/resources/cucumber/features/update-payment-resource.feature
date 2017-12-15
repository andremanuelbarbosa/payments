Feature: Update a Payment Resource

  Scenario: Payment Resource should be updated and returned when exists in the DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43-updated"
    When the client makes a "PUT" request to "/payments/4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    Then the client should receive a "200" status code in the Response
    And the Response content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43-updated"
