package com.andremanuelbarbosa.payments.dao.mapper;

import com.andremanuelbarbosa.payments.domain.Payment;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PaymentSetMapper implements ResultSetMapper<Payment> {

    @Override
    public Payment map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {

        return new Payment(UUID.fromString(resultSet.getString("id")), resultSet.getInt("version"),
                UUID.fromString(resultSet.getString("organisation_id")), null);
    }
}
