package com.andremanuelbarbosa.payments.dao.mapper;

import com.andremanuelbarbosa.payments.resources.Payment;
import org.joda.time.DateTime;
import org.skife.jdbi.v2.StatementContext;
import org.skife.jdbi.v2.tweak.ResultSetMapper;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.UUID;

public class PaymentSetMapper implements ResultSetMapper<Payment> {

    @Override
    public Payment map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {

        final Payment.Attributes.Party beneficiaryParty = new Payment.Attributes.Party(
                resultSet.getString("beneficiary_party_account_name"), resultSet.getString("beneficiary_party_account_number"),
                resultSet.getString("beneficiary_party_account_number_code"), resultSet.getObject("beneficiary_party_account_type") != null ? resultSet.getInt("beneficiary_party_account_type") : null,
                resultSet.getString("beneficiary_party_address"), resultSet.getLong("beneficiary_party_bank_id"),
                resultSet.getString("beneficiary_party_bank_id_code"), resultSet.getString("beneficiary_party_name"));

        final Payment.Attributes.ChargesInformation chargesInformation = new Payment.Attributes.ChargesInformation(
                resultSet.getString("bearer_code"), resultSet.getDouble("receiver_charges_amount"),
                resultSet.getString("receiver_charges_currency"));

        final Payment.Attributes.Party debtorParty = new Payment.Attributes.Party(
                resultSet.getString("debtor_party_account_name"), resultSet.getString("debtor_party_account_number"),
                resultSet.getString("debtor_party_account_number_code"), resultSet.getObject("debtor_party_account_type") != null ? resultSet.getInt("debtor_party_account_type") : null,
                resultSet.getString("debtor_party_address"), resultSet.getLong("debtor_party_bank_id"),
                resultSet.getString("debtor_party_bank_id_code"), resultSet.getString("debtor_party_name"));

        final Payment.Attributes.Fx fx = new Payment.Attributes.Fx(
                resultSet.getString("fx_contract_reference"), resultSet.getDouble("fx_exchange_rate"),
                resultSet.getDouble("fx_original_amount"), resultSet.getString("fx_original_currency"));

        final Payment.Attributes.Party sponsorParty = new Payment.Attributes.Party(
                resultSet.getString("sponsor_party_account_name"), resultSet.getString("sponsor_party_account_number"),
                resultSet.getString("sponsor_party_account_number_code"), resultSet.getObject("sponsor_party_account_type") != null ? resultSet.getInt("sponsor_party_account_type") : null,
                resultSet.getString("sponsor_party_address"), resultSet.getLong("sponsor_party_bank_id"),
                resultSet.getString("sponsor_party_bank_id_code"), resultSet.getString("sponsor_party_name"));

        final Payment.Attributes attributes = new Payment.Attributes(resultSet.getDouble("amount"), beneficiaryParty, chargesInformation,
                resultSet.getString("currency"), debtorParty, resultSet.getString("end_to_end_reference"),
                fx, resultSet.getLong("numeric_reference"), resultSet.getString("payment_id"),
                resultSet.getString("payment_purpose"), resultSet.getString("payment_scheme"),
                resultSet.getString("payment_type"), new DateTime(resultSet.getTimestamp("processing_date")),
                resultSet.getString("reference"), resultSet.getString("scheme_payment_sub_type"),
                resultSet.getString("scheme_payment_type"), sponsorParty);

        return new Payment(UUID.fromString(resultSet.getString("id")), resultSet.getInt("version"),
                UUID.fromString(resultSet.getString("organisation_id")), attributes);
    }

    public static class SenderChargeSetMapper implements ResultSetMapper<Payment.Attributes.ChargesInformation.SenderCharge> {

        @Override
        public Payment.Attributes.ChargesInformation.SenderCharge map(int i, ResultSet resultSet, StatementContext statementContext) throws SQLException {

            return new Payment.Attributes.ChargesInformation.SenderCharge(resultSet.getDouble("amount"), resultSet.getString("currency"));
        }
    }
}
