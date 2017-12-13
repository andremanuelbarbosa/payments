# Payments Service


createdb -U postgres payments-service


QUESTIONS

* Which fields/attributes of the Payment are required and which ones are optional?
* Should the service perform data validation when creating/updating a Payment?
* Are the Payment attributes structured (static) or is this an unstructured map of names/values?
* Is the information about the Parties (e.g. beneficiary_party) global and can be re-used in other Payments or should these be stored against the Payment only? Or both?  
