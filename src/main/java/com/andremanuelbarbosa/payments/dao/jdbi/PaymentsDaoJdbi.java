package com.andremanuelbarbosa.payments.dao.jdbi;

import com.andremanuelbarbosa.payments.dao.PaymentsDao;
import com.andremanuelbarbosa.payments.dao.mapper.PaymentSetMapper;
import com.andremanuelbarbosa.payments.domain.Payment;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.customizers.RegisterMapper;

import java.util.List;
import java.util.UUID;

@RegisterMapper(PaymentSetMapper.class)
public interface PaymentsDaoJdbi extends PaymentsDao {

    String PAYMENT_COLUMNS = "id, version, organisation_id, amount, beneficiary_party_account_name, beneficiary_party_account_number, " +
            "beneficiary_party_account_number_code, beneficiary_party_account_type, beneficiary_party_address, beneficiary_party_bank_id, " +
            "beneficiary_party_bank_id_code, beneficiary_party_name, bearer_code, receiver_charges_amount, receiver_charges_currency, " +
            "currency, debtor_party_account_name, debtor_party_account_number, debtor_party_account_number_code, debtor_party_account_type, " +
            "debtor_party_address, debtor_party_bank_id, debtor_party_bank_id_code, debtor_party_name, end_to_end_reference, " +
            "fx_contract_reference, fx_exchange_rate, fx_original_amount, fx_original_currency, numeric_reference, payment_id, " +
            "payment_purpose, payment_scheme, payment_type, processing_date, reference, scheme_payment_sub_type, scheme_payment_type, " +
            "sponsor_party_account_name, sponsor_party_account_number, sponsor_party_account_number_code, sponsor_party_account_type, " +
            "sponsor_party_address, sponsor_party_bank_id, sponsor_party_bank_id_code, sponsor_party_name";

    String PAYMENT_SENDER_CHARGE_COLUMNS = "payment_id, amount, currency";

    @Override
    @SqlQuery("SELECT " + PAYMENT_COLUMNS + " FROM payments WHERE id = :id")
    Payment getPayment(@Bind("id") UUID id);

    @Override
    @SqlQuery("SELECT " + PAYMENT_COLUMNS + " FROM payments")
    List<Payment> getPayments();

    @Override
    @RegisterMapper(PaymentSetMapper.SenderChargeSetMapper.class)
    @SqlQuery("SELECT " + PAYMENT_SENDER_CHARGE_COLUMNS + " FROM payments_sender_charges WHERE payment_id = :paymentId")
    List<Payment.Attributes.ChargesInformation.SenderCharge> getPaymentSenderCharges(@Bind("paymentId") UUID paymentId);
}
