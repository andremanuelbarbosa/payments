Feature: List a Collection of Payment Resources

  Scenario: No Payment Resources are returned in the Payment Entities data attribute when there are no Payments

    Given there are no Payments in the DB
    When the client makes a "GET" request to "/payments"
    Then the client should receive a "200" status code in the Response
    And the "data" attribute in the Response content contains no elements
    And the "links" attribute in the Response content contains the "self" attribute as "{{baseUrl}}/payments"

  Scenario: Payment Resource is returned in the Payment Entities data attribute when Payment exists in DB

    Given the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43" exists in the DB
    When the client makes a "GET" request to "/payments"
    Then the client should receive a "200" status code in the Response
    And the "data" attribute in the Response content contains one element with the Payment "4ee3a8d8-ca7b-4290-a52c-dd5b6165ec43"
    And the "links" attribute in the Response content contains the "self" attribute as "{{baseUrl}}/payments"
