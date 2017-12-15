package com.andremanuelbarbosa.payments;

import com.andremanuelbarbosa.payments.domain.Payment;
import org.assertj.core.util.Lists;
import org.joda.time.DateTime;
import org.junit.runner.RunWith;
import org.mockito.junit.MockitoJUnitRunner;

import java.util.UUID;

@RunWith(MockitoJUnitRunner.class)
public abstract class PaymentsServiceTest {

    public static Payment createPayment() {

        return createPayment(UUID.randomUUID(), UUID.randomUUID());
    }

    public static Payment createPayment(UUID id, UUID organisationId) {

        final Payment.Attributes.Party beneficiaryParty = new Payment.Attributes.Party("accountNameB",
                "accountNumberB", "accountNumbCodeB", 0, "addressB",
                1, "bankIdCodeB", "nameB");
        final Payment.Attributes.ChargesInformation chargesInformation = new Payment.Attributes.ChargesInformation(
                "bearerCode",5.0, "USD");
        final Payment.Attributes.Party debtorParty = new Payment.Attributes.Party("accountNameD",
                "accountNumberD", "accountNumbCodeD", 1, "addressD",
                2, "bankIdCodeD", "nameD");
        final Payment.Attributes.Fx fx = new Payment.Attributes.Fx("contractReference", 1.1,
                100.0, "GBP");
        final Payment.Attributes.Party sponsorParty = new Payment.Attributes.Party("accountNameS",
                "accountNumberS", "accountNumbCodeS", 2, "addressS",
                3, "bankIdCodeS", "nameS");

        final Payment.Attributes attributes = new Payment.Attributes(100.0, beneficiaryParty, chargesInformation,
                "GBP", debtorParty, "endToEndReference", fx, 123, "paymentId",
                "paymentPurpose", "PYS", "paymentType", DateTime.parse("2018-01-01"),
                "reference", "schemePaymentSubType", "schemePaymentType", sponsorParty);

        return new Payment(id, 0, organisationId, attributes);
    }
}
