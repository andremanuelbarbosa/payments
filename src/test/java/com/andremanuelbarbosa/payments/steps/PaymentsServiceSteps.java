package com.andremanuelbarbosa.payments.steps;

import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import cucumber.api.Scenario;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.ws.rs.core.Response;
import java.util.HashMap;

import static com.andremanuelbarbosa.payments.PaymentsServiceAcceptanceTest.*;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

public class PaymentsServiceSteps {

    private static final String SCENARIO_PARAM_RESPONSE = "RESPONSE";
    private static final String SCENARIO_PARAM_RESPONSE_CONTENT = "RESPONSE_CONTENT";

    private final HashMap<String, Object> scenarioParams = Maps.newHashMap();

    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {

        this.scenario = scenario;

        scenarioParams.clear();
    }

    @Given("^there are no Payments$")
    public void there_are_no_Payments() throws Exception {

        assertEquals(0, (long) handle.select("SELECT COUNT(*) AS count FROM payments").get(0).get("count"));
    }

    @When("^the client makes a \"(GET|POST|PUT|DELETE)\" request to \"([^\"]*)\"$")
    public void the_client_makes_a_request_to(String method, String endpoint) throws Exception {

        scenario.write(method + " " + endpoint);

        final Response response = DROPWIZARD_APP_RULE.client().target(baseUrl + endpoint).request().build(method).invoke();

        final String responseContent = response.readEntity(String.class);

        scenarioParams.put(SCENARIO_PARAM_RESPONSE, response);
        scenarioParams.put(SCENARIO_PARAM_RESPONSE_CONTENT, responseContent);

        scenario.write("Response: " + response.toString());
    }

    @Then("^the client should receive a \"([0-9]{3})\" status code in the Response$")
    public void the_client_should_receive_a_status_code_in_the_Response(int statusCode) throws Exception {

        assertEquals(statusCode, ((Response) scenarioParams.get(SCENARIO_PARAM_RESPONSE)).getStatus());
    }

    private HashMap<String, Object> getResponseHashMap() throws Exception {

        final String responseContent = (String) scenarioParams.get(SCENARIO_PARAM_RESPONSE_CONTENT);

        return DROPWIZARD_APP_RULE.getObjectMapper().readValue(responseContent, new TypeReference<HashMap<String, String>>() {

        });
    }

    @Then("^the \"([^\"]*)\" attribute in the Response content contains no elements$")
    public void the_attribute_in_the_Response_content_contains_no_elements(String attributeName) throws Exception {

        final HashMap<String, Object> responseHashMap = getResponseHashMap();

        assertTrue(responseHashMap.containsKey(attributeName));

        final HashMap elementHashMap = DROPWIZARD_APP_RULE.getObjectMapper().convertValue(responseHashMap.get(attributeName), HashMap.class);

        assertEquals(0, elementHashMap.size());
    }

    @Then("^the \"([^\"]*)\" attribute in the Response content contains the \"([^\"]*)\" attribute as \"([^\"]*)\"$")
    public void the_attribute_in_the_Response_content_contains_the_attribute_as(String attributeName, String subAttributeName, String subAttributeValue) throws Exception {

        final HashMap<String, Object> responseHashMap = getResponseHashMap();

        assertTrue(responseHashMap.containsKey(attributeName));

        final HashMap elementHashMap = DROPWIZARD_APP_RULE.getObjectMapper().convertValue(responseHashMap.get(attributeName), HashMap.class);

        assertTrue(elementHashMap.containsKey(subAttributeName));

        assertEquals(subAttributeValue, elementHashMap.get(subAttributeName));
    }
}
