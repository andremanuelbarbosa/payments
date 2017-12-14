package com.andremanuelbarbosa.payments.manager;

import com.andremanuelbarbosa.payments.dao.PaymentsDao;
import com.andremanuelbarbosa.payments.domain.Payment;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;

@Singleton
public class PaymentsManager {

    private final PaymentsDao paymentsDao;

    @Inject
    public PaymentsManager(PaymentsDao paymentsDao) {

        this.paymentsDao = paymentsDao;
    }

    private void loadPayment(Payment payment) {

        payment.getAttributes().getChargesInformation().getSenderCharges().addAll(
                paymentsDao.getPaymentSenderCharges(payment.getId()));
    }

    public List<Payment> getPayments() {

        final List<Payment> payments = paymentsDao.getPayments();

        payments.forEach(payment -> {

            loadPayment(payment);
        });

        return payments;
    }
}
