package com.andremanuelbarbosa.payments.domain;

import com.andremanuelbarbosa.payments.PaymentsServiceTest;
import org.junit.Ignore;
import org.junit.Test;

import java.util.UUID;

import static org.junit.Assert.assertEquals;

@Ignore
public class PaymentTest extends PaymentsServiceTest {

    @Test
    public void shouldReturnTrueOnEqualsWhenPaymentObjectsHaveSameValues() {

        final UUID id = UUID.randomUUID();
        final UUID organisationId = UUID.randomUUID();

        assertEquals(createPayment(id, organisationId), createPayment(id, organisationId));
    }
}
