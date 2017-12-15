package com.andremanuelbarbosa.payments.dao.jdbi;

import com.andremanuelbarbosa.payments.PaymentsServiceIntegrationTest;
import com.andremanuelbarbosa.payments.PaymentsServiceTest;
import com.andremanuelbarbosa.payments.domain.Payment;
import org.junit.Before;
import org.junit.Ignore;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

@Ignore
public class PaymentsDaoJdbiIntegrationTest extends PaymentsServiceIntegrationTest {

    private PaymentsDaoJdbi paymentsDaoJdbi;

    @Before
    public void setUp() {

        super.setUp();

        paymentsDaoJdbi = dbi.onDemand(PaymentsDaoJdbi.class);
    }

    @Test
    public void shouldInsertAndReturnPaymentOnInsertPaymentWhenPaymentIsValid() {

        final Payment expectedPayment = PaymentsServiceTest.createPayment();

        paymentsDaoJdbi.insertPayment(expectedPayment);

        final Payment payment = paymentsDaoJdbi.getPayment(expectedPayment.getId());

        assertEquals(expectedPayment, payment);
    }
}
