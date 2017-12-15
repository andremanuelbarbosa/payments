package com.andremanuelbarbosa.payments.manager;

import com.andremanuelbarbosa.payments.dao.PaymentsDao;
import com.andremanuelbarbosa.payments.domain.Payment;
import com.google.inject.Inject;
import com.google.inject.Singleton;

import java.util.List;
import java.util.UUID;

@Singleton
public class PaymentsManager {

    private final PaymentsDao paymentsDao;

    @Inject
    public PaymentsManager(PaymentsDao paymentsDao) {

        this.paymentsDao = paymentsDao;
    }

    public void deletePayment(UUID id) {

        paymentsDao.deletePaymentSenderCharges(id);

        paymentsDao.deletePayment(id);
    }

    private Payment loadPayment(Payment payment) {

        payment.getAttributes().getChargesInformation().getSenderCharges().addAll(
                paymentsDao.getPaymentSenderCharges(payment.getId()));

        return payment;
    }

    public Payment getPayment(UUID id) {

        return loadPayment(paymentsDao.getPayment(id));
    }

    public List<Payment> getPayments() {

        final List<Payment> payments = paymentsDao.getPayments();

        payments.forEach(payment -> {

            loadPayment(payment);
        });

        return payments;
    }

    public Payment createPayment(Payment payment) {

        paymentsDao.insertPayment(payment);

        payment.getAttributes().getChargesInformation().getSenderCharges().forEach(senderCharge -> {

            paymentsDao.insertPaymentSenderCharges(payment.getId(), senderCharge);
        });

        return getPayment(payment.getId());
    }

    public Payment updatePayment(Payment payment) {

        paymentsDao.deletePaymentSenderCharges(payment.getId());

        paymentsDao.updatePayment(payment);

        payment.getAttributes().getChargesInformation().getSenderCharges().forEach(senderCharge -> {

            paymentsDao.insertPaymentSenderCharges(payment.getId(), senderCharge);
        });

        return getPayment(payment.getId());
    }
}
