Feature: List a Collection of Payment Resources

  Scenario: No Payment Resources are returned in the Payment Entities data attribute when there are no Payments

    Given there are no Payments
    When the client makes a "GET" request to "/payments"
    Then the client should receive a "200" status code in the Response
    And the "data" attribute in the Response content contains no elements
    And the "links" attribute in the Response content contains the "self" attribute as "{{baseUrl}}/payments"
