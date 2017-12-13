package com.andremanuelbarbosa.payments.dao.jdbi;

import com.andremanuelbarbosa.payments.dao.PaymentsDao;
import com.andremanuelbarbosa.payments.dao.mapper.PaymentSetMapper;
import com.andremanuelbarbosa.payments.domain.Payment;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;

@RegisterMapper(PaymentSetMapper.class)
public interface PaymentsDaoJdbi extends PaymentsDao {

    @Override
    @SqlQuery("SELECT id, version, organisation_id FROM payments")
    List<Payment> getPayments();
}
