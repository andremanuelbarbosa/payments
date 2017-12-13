package com.andremanuelbarbosa.payments.dao;

import com.andremanuelbarbosa.payments.domain.Payment;

import java.util.List;

public interface PaymentsDao extends Dao {

    List<Payment> getPayments();
}
