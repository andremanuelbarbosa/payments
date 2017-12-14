package com.andremanuelbarbosa.payments.dao;

import com.andremanuelbarbosa.payments.domain.Payment;

import java.util.List;
import java.util.UUID;

public interface PaymentsDao extends Dao {

    List<Payment> getPayments();

    List<Payment.Attributes.ChargesInformation.SenderCharge> getPaymentSenderCharges(UUID paymentId);
}
