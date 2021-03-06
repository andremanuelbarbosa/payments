package com.andremanuelbarbosa.payments.dao;

import com.andremanuelbarbosa.payments.resources.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentsDao extends Dao {

    void deletePayment(UUID id);

    void deletePaymentSenderCharges(UUID paymentId);

    Payment getPayment(UUID id);

    List<Payment> getPayments();

    List<Payment.Attributes.ChargesInformation.SenderCharge> getPaymentSenderCharges(UUID paymentId);

    void insertPayment(Payment payment);

    void insertPaymentSenderCharges(UUID paymentId, Payment.Attributes.ChargesInformation.SenderCharge senderCharge);

    void updatePayment(Payment payment);
}
