package com.andremanuelbarbosa.payments.manager;

import com.andremanuelbarbosa.payments.dao.PaymentsDao;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class PaymentsManager {

    private final PaymentsDao paymentsDao;

    @Inject
    public PaymentsManager(PaymentsDao paymentsDao) {

        this.paymentsDao = paymentsDao;
    }
}
