package com.andremanuelbarbosa.payments.steps;

import com.andremanuelbarbosa.payments.dao.jdbi.PaymentsDaoJdbi;
import com.andremanuelbarbosa.payments.domain.Payment;
import com.fasterxml.jackson.core.type.TypeReference;
import com.google.common.collect.Maps;
import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;

import javax.ws.rs.core.Response;
import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static com.andremanuelbarbosa.payments.PaymentsServiceAcceptanceTest.*;
import static com.andremanuelbarbosa.payments.dao.jdbi.PaymentsDaoJdbi.PAYMENT_COLUMNS;
import static com.andremanuelbarbosa.payments.dao.jdbi.PaymentsDaoJdbi.PAYMENT_SENDER_CHARGE_COLUMNS;
import static org.junit.Assert.*;

public class PaymentsServiceSteps {

    private static final String SCENARIO_PARAM_RESPONSE = "response";
    private static final String SCENARIO_PARAM_RESPONSE_CONTENT = "response.content";

    private final HashMap<String, Object> scenarioParams = Maps.newHashMap();

    private Scenario scenario;

    @Before
    public void setUp(Scenario scenario) {

        this.scenario = scenario;

        scenarioParams.clear();
    }

    @After
    public void tearDown(Scenario scenario) {

        handle.update("DELETE FROM payments_sender_charges");
        handle.update("DELETE FROM payments");
    }

    @Given("^there are no Payments in the DB$")
    public void there_are_no_Payments_in_the_DB() throws Exception {

        assertEquals(0, (long) handle.select("SELECT COUNT(*) AS count FROM payments").get(0).get("count"));
    }

    private long getPaymentsCount(String id) {

        return (long) handle.select("SELECT COUNT(*) AS count FROM payments WHERE id = ?", UUID.fromString(id)).get(0).get("count");
    }

    private Payment loadPaymentFromJson(String id) throws Exception {

        final Payment payment = DROPWIZARD_APP_RULE.getObjectMapper().readValue(new File("src/test/resources/cucumber/resources/" + id + ".json"), Payment.class);

        scenario.write(payment.toString());

        return payment;
    }

    @Given("^the Payment \"([^\"]*)\" exists in the DB$")
    public void the_Payment_exists_in_the_DB(String paymentId) throws Exception {

        if (getPaymentsCount(paymentId) == 0) {

            final Payment payment = loadPaymentFromJson(paymentId);
            final Payment.Attributes attributes = payment.getAttributes();
            final Payment.Attributes.Party beneficiaryParty = attributes.getBeneficiaryParty();
            final Payment.Attributes.ChargesInformation chargesInformation = attributes.getChargesInformation();
            final Payment.Attributes.Party debtorParty = attributes.getDebtorParty();
            final Payment.Attributes.Fx fx = attributes.getFx();
            final Payment.Attributes.Party sponsorParty = attributes.getSponsorParty();

            handle.update("INSERT INTO payments ( " + PAYMENT_COLUMNS + " ) VALUES ( ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ? )",
                    payment.getId(), payment.getVersion(), payment.getOrganisationId(), attributes.getAmount(),
                    beneficiaryParty.getAccountName(), beneficiaryParty.getAccountNumber(), beneficiaryParty.getAccountNumberCode(),
                    beneficiaryParty.getAccountType(), beneficiaryParty.getAddress(), beneficiaryParty.getBankId(),
                    beneficiaryParty.getBankIdCode(), beneficiaryParty.getName(), chargesInformation.getBearerCode(),
                    chargesInformation.getReceiverChargesAmount(), chargesInformation.getReceiverChargesCurrency(),
                    attributes.getCurrency(), debtorParty.getAccountName(), debtorParty.getAccountNumber(),
                    debtorParty.getAccountNumberCode(), debtorParty.getAccountType(), debtorParty.getAddress(),
                    debtorParty.getBankId(), debtorParty.getBankIdCode(), debtorParty.getName(),
                    attributes.getEndToEndReference(), fx.getContractReference(), fx.getExchangeRate(),
                    fx.getOriginalAmount(), fx.getOriginalCurrency(), attributes.getNumericReference(),
                    attributes.getPaymentId(), attributes.getPaymentPurpose(), attributes.getPaymentScheme(),
                    attributes.getPaymentType(), attributes.getProcessingDate(), attributes.getReference(),
                    attributes.getSchemePaymentSubType(), attributes.getSchemePaymentType(), sponsorParty.getAccountName(),
                    sponsorParty.getAccountNumber(), sponsorParty.getAccountNumberCode(), sponsorParty.getAccountType(),
                    sponsorParty.getAddress(), sponsorParty.getBankId(), sponsorParty.getBankIdCode(), sponsorParty.getName());

            chargesInformation.getSenderCharges().forEach(senderCharge -> {

                handle.update("INSERT INTO payments_sender_charges ( " + PAYMENT_SENDER_CHARGE_COLUMNS + " ) VALUES ( ?, ?, ? )", payment.getId(), senderCharge.getAmount(), senderCharge.getCurrency());
            });
        }

        assertEquals(1, getPaymentsCount(paymentId));
    }

    @When("^the client makes a \"(GET|POST|PUT|DELETE)\" request to \"([^\"]*)\"$")
    public void the_client_makes_a_request_to(String method, String endpoint) throws Exception {

        final Response response = DROPWIZARD_APP_RULE.client().target(baseUrl + endpoint).request().build(method).invoke();

        final String responseContent = response.readEntity(String.class);

        scenarioParams.put(SCENARIO_PARAM_RESPONSE, response);
        scenarioParams.put(SCENARIO_PARAM_RESPONSE_CONTENT, responseContent);

        scenario.write(method + " " + baseUrl + endpoint + "\n" +
                "Status Code: " + response.getStatus() + "\n\n" +
                responseContent);
    }

    @Then("^the client should receive a \"([0-9]{3})\" status code in the Response$")
    public void the_client_should_receive_a_status_code_in_the_Response(int statusCode) throws Exception {

        assertEquals(statusCode, ((Response) scenarioParams.get(SCENARIO_PARAM_RESPONSE)).getStatus());
    }

    private HashMap<String, Object> getResponseHashMap() throws Exception {

        final String responseContent = (String) scenarioParams.get(SCENARIO_PARAM_RESPONSE_CONTENT);

        return DROPWIZARD_APP_RULE.getObjectMapper().readValue(responseContent, new TypeReference<HashMap<String, Object>>() {

        });
    }

    @Then("^the \"([^\"]*)\" attribute in the Response content contains no elements$")
    public void the_attribute_in_the_Response_content_contains_no_elements(String attributeName) throws Exception {

        final HashMap<String, Object> responseHashMap = getResponseHashMap();

        assertTrue(responseHashMap.containsKey(attributeName));

        assertEquals(0, ((List) responseHashMap.get(attributeName)).size());
    }

    @Then("^the \"([^\"]*)\" attribute in the Response content contains one element with the Payment \"([^\"]*)\"$")
    public void the_attribute_in_the_Response_content_contains_one_element_with_the_Payment(String attributeName, String paymentId) throws Exception {

        final HashMap<String, Object> responseHashMap = getResponseHashMap();

        assertTrue(responseHashMap.containsKey(attributeName));

        final List payments = (List) responseHashMap.get(attributeName);

        final Map expectedPaymentMap = DROPWIZARD_APP_RULE.getObjectMapper().convertValue(loadPaymentFromJson(paymentId), Map.class);

        final AtomicBoolean expectedPaymentFound = new AtomicBoolean(false);

        payments.forEach(payment -> {

            if (payment.toString().equals(expectedPaymentMap.toString())) {

                expectedPaymentFound.set(true);

                return;
            }
        });

        assertTrue(expectedPaymentFound.get());
    }

    private String getValueWithReplacedParams(String value) {

        final Matcher matcher = Pattern.compile("\\{\\{([^}]+)\\}\\}").matcher(value);

        final StringBuffer stringBuffer = new StringBuffer(value.length());

        while (matcher.find()) {

            final String paramName = matcher.group(1);

            matcher.appendReplacement(stringBuffer,
                    Matcher.quoteReplacement(GLOBAL_PARAMS.containsKey(paramName) ? GLOBAL_PARAMS.get(paramName) : ""));
        }

        matcher.appendTail(stringBuffer);

        return stringBuffer.toString();
    }

    @Then("^the \"([^\"]*)\" attribute in the Response content contains the \"([^\"]*)\" attribute as \"([^\"]*)\"$")
    public void the_attribute_in_the_Response_content_contains_the_attribute_as(String attributeName, String subAttributeName, String subAttributeValue) throws Exception {

        final HashMap<String, Object> responseHashMap = getResponseHashMap();

        assertTrue(responseHashMap.containsKey(attributeName));

        final HashMap elementHashMap = DROPWIZARD_APP_RULE.getObjectMapper().convertValue(responseHashMap.get(attributeName), HashMap.class);

        assertTrue(elementHashMap.containsKey(subAttributeName));

        assertEquals(getValueWithReplacedParams(subAttributeValue), elementHashMap.get(subAttributeName));
    }
}
