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

    public List<Payment> getPayments() {

        return paymentsDao.getPayments();
    }
}
