@ignore
Feature: Create a Payment Resource

  Scenario: Payment Resource is created and returned in the Response

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" does NOT exist in the DB
    And the Request content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    When the client makes a "POST" request to "/payments"
    Then the client should receive a "201" status code in the Response
    And the Response content contains the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
