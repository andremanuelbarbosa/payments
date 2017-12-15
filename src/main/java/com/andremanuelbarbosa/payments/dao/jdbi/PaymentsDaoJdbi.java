package com.andremanuelbarbosa.payments.dao.jdbi;

import com.andremanuelbarbosa.payments.dao.PaymentsDao;
import com.andremanuelbarbosa.payments.dao.jdbi.binder.BindCompositeBean;
import com.andremanuelbarbosa.payments.dao.mapper.PaymentSetMapper;
import com.andremanuelbarbosa.payments.domain.Payment;
import org.skife.jdbi.v2.sqlobject.Bind;
import org.skife.jdbi.v2.sqlobject.BindBean;
import org.skife.jdbi.v2.sqlobject.SqlQuery;
import org.skife.jdbi.v2.sqlobject.SqlUpdate;
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
    @SqlUpdate("DELETE FROM payments WHERE id = :id")
    void deletePayment(@Bind("id") UUID id);

    @Override
    @SqlUpdate("DELETE FROM payments_sender_charges WHERE payment_id = :paymentId")
    void deletePaymentSenderCharges(@Bind("paymentId") UUID paymentId);

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

    @Override
    @SqlUpdate("INSERT INTO payments ( " + PAYMENT_COLUMNS + " ) VALUES ( " +
            ":id, :version, :organisationId, :attributes.amount, :attributes.beneficiaryParty.accountName, :attributes.beneficiaryParty.accountNumber, " +
            ":attributes.beneficiaryParty.accountNumberCode, :attributes.beneficiaryParty.accountType, :attributes.beneficiaryParty.address, :attributes.beneficiaryParty.bankId, " +
            ":attributes.beneficiaryParty.bankIdCode, :attributes.beneficiaryParty.name, :attributes.chargesInformation.bearerCode, :attributes.chargesInformation.receiverChargesAmount, :attributes.chargesInformation.receiverChargesCurrency, " +
            ":attributes.currency, :attributes.debtorParty.accountName, :attributes.debtorParty.accountNumber, :attributes.debtorParty.accountNumberCode, :attributes.debtorParty.accountType, " +
            ":attributes.debtorParty.address, :attributes.debtorParty.bankId, :attributes.debtorParty.bankIdCode, :attributes.debtorParty.name, :attributes.endToEndReference, " +
            ":attributes.fx.contractReference, :attributes.fx.exchangeRate, :attributes.fx.originalAmount, :attributes.fx.originalCurrency, :attributes.numericReference, :attributes.paymentId, " +
            ":attributes.paymentPurpose, :attributes.paymentScheme, :attributes.paymentType, :attributes.processingDate, :attributes.reference, :attributes.schemePaymentSubType, :attributes.schemePaymentType, " +
            ":attributes.sponsorParty.accountName, :attributes.sponsorParty.accountNumber, :attributes.sponsorParty.accountNumberCode, :attributes.sponsorParty.accountType, " +
            ":attributes.sponsorParty.address, :attributes.sponsorParty.bankId, :attributes.sponsorParty.bankIdCode, :attributes.sponsorParty.name )")
    void insertPayment(@BindCompositeBean Payment payment);

    @Override
    @SqlUpdate("INSERT INTO payments_sender_charges ( " + PAYMENT_SENDER_CHARGE_COLUMNS + " ) VALUES ( :paymentId, :amount, :currency )")
    void insertPaymentSenderCharges(@Bind("paymentId") UUID paymentId, @BindBean Payment.Attributes.ChargesInformation.SenderCharge senderCharge);
}
