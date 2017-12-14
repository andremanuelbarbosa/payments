package com.andremanuelbarbosa.payments.domain;

import com.andremanuelbarbosa.payments.resources.Resource;
import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.google.common.collect.Lists;
import org.apache.commons.lang3.builder.EqualsBuilder;
import org.apache.commons.lang3.builder.HashCodeBuilder;
import org.apache.commons.lang3.builder.ToStringBuilder;
import org.joda.time.DateTime;

import java.util.List;
import java.util.UUID;

@JsonInclude(JsonInclude.Include.NON_NULL)
public class Payment implements Resource {

    // TODO use enum or at least make static
    private final String type = "Payment";

    private final UUID id;
    private final int version;
    private final UUID organisationId;
    private final Attributes attributes;

    public Payment(UUID id, int version, UUID organisationId, Attributes attributes) {

        this.id = id;
        this.version = version;
        this.organisationId = organisationId;
        this.attributes = attributes;
    }

    public String getType() {

        return type;
    }

    public UUID getId() {

        return id;
    }

    public int getVersion() {

        return version;
    }

    public UUID getOrganisationId() {

        return organisationId;
    }

    public Attributes getAttributes() {

        return attributes;
    }

    public static class Attributes {

        private final double amount;
        private final Party beneficiaryParty;
        private final ChargesInformation chargesInformation;
        private final String currency;
        private final Party debtorParty;
        private final String endToEndReference;
        private final Fx fx;
        private final long numericReference;
        private final String paymentId;
        private final String paymentPurpose;
        private final String paymentScheme;
        private final String paymentType;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
        private final DateTime processingDate;
        private final String reference;
        private final String schemePaymentSubType;
        private final String schemePaymentType;
        private final Party sponsorParty;

        public Attributes(double amount, Party beneficiaryParty, ChargesInformation chargesInformation, String currency, Party debtorParty, String endToEndReference, Fx fx, long numericReference, String paymentId, String paymentPurpose, String paymentScheme, String paymentType, DateTime processingDate, String reference, String schemePaymentSubType, String schemePaymentType, Party sponsorParty) {

            this.amount = amount;
            this.beneficiaryParty = beneficiaryParty;
            this.chargesInformation = chargesInformation;
            this.currency = currency;
            this.debtorParty = debtorParty;
            this.endToEndReference = endToEndReference;
            this.fx = fx;
            this.numericReference = numericReference;
            this.paymentId = paymentId;
            this.paymentPurpose = paymentPurpose;
            this.paymentScheme = paymentScheme;
            this.paymentType = paymentType;
            this.processingDate = processingDate;
            this.reference = reference;
            this.schemePaymentSubType = schemePaymentSubType;
            this.schemePaymentType = schemePaymentType;
            this.sponsorParty = sponsorParty;
        }

        public double getAmount() {

            return amount;
        }

        public Party getBeneficiaryParty() {

            return beneficiaryParty;
        }

        public ChargesInformation getChargesInformation() {

            return chargesInformation;
        }

        public String getCurrency() {

            return currency;
        }

        public Party getDebtorParty() {

            return debtorParty;
        }

        public String getEndToEndReference() {

            return endToEndReference;
        }

        public Fx getFx() {

            return fx;
        }

        public long getNumericReference() {

            return numericReference;
        }

        public String getPaymentId() {

            return paymentId;
        }

        public String getPaymentPurpose() {

            return paymentPurpose;
        }

        public String getPaymentScheme() {

            return paymentScheme;
        }

        public String getPaymentType() {

            return paymentType;
        }

        public DateTime getProcessingDate() {

            return processingDate;
        }

        public String getReference() {

            return reference;
        }

        public String getSchemePaymentSubType() {

            return schemePaymentSubType;
        }

        public String getSchemePaymentType() {

            return schemePaymentType;
        }

        public Party getSponsorParty() {

            return sponsorParty;
        }

        public static class Party {

            private final String accountName;
            private final String accountNumber;
            private final String accountNumberCode;
            private final Integer accountType;
            private final String address;
            private final long bankId;
            private final String bankIdCode;
            private final String name;

            public Party(String accountName, String accountNumber, String accountNumberCode, Integer accountType, String address, long bankId, String bankIdCode, String name) {

                this.accountName = accountName;
                this.accountNumber = accountNumber;
                this.accountNumberCode = accountNumberCode;
                this.accountType = accountType;
                this.address = address;
                this.bankId = bankId;
                this.bankIdCode = bankIdCode;
                this.name = name;
            }

            public String getAccountName() {

                return accountName;
            }

            public String getAccountNumber() {

                return accountNumber;
            }

            public String getAccountNumberCode() {

                return accountNumberCode;
            }

            public Integer getAccountType() {

                return accountType;
            }

            public String getAddress() {

                return address;
            }

            public long getBankId() {

                return bankId;
            }

            public String getBankIdCode() {

                return bankIdCode;
            }

            public String getName() {

                return name;
            }

            @Override
            public String toString() {

                return ToStringBuilder.reflectionToString(this);
            }
        }

        public static class ChargesInformation {

            private final String bearerCode;
            private final List<SenderCharge> senderCharges;
            private final double receiverChargesAmount;
            private final String receiverChargesCurrency;

            public ChargesInformation(String bearerCode, double receiverChargesAmount, String receiverChargesCurrency) {

                this(bearerCode, Lists.newArrayList(), receiverChargesAmount, receiverChargesCurrency);
            }

            @JsonCreator
            public ChargesInformation(String bearerCode, List<SenderCharge> senderCharges, double receiverChargesAmount, String receiverChargesCurrency) {

                this.bearerCode = bearerCode;
                this.senderCharges = senderCharges;
                this.receiverChargesAmount = receiverChargesAmount;
                this.receiverChargesCurrency = receiverChargesCurrency;
            }

            public String getBearerCode() {

                return bearerCode;
            }

            public List<SenderCharge> getSenderCharges() {

                return senderCharges;
            }

            public double getReceiverChargesAmount() {

                return receiverChargesAmount;
            }

            public String getReceiverChargesCurrency() {

                return receiverChargesCurrency;
            }

            public static class SenderCharge {

                private final double amount;
                private final String currency;

                public SenderCharge(double amount, String currency) {

                    this.amount = amount;
                    this.currency = currency;
                }

                public double getAmount() {

                    return amount;
                }

                public String getCurrency() {

                    return currency;
                }

                @Override
                public String toString() {

                    return ToStringBuilder.reflectionToString(this);
                }
            }
        }

        public static class Fx {

            private final String contractReference;
            private final double exchangeRate;
            private final double originalAmount;
            private final String originalCurrency;

            public Fx(String contractReference, double exchangeRate, double originalAmount, String originalCurrency) {

                this.contractReference = contractReference;
                this.exchangeRate = exchangeRate;
                this.originalAmount = originalAmount;
                this.originalCurrency = originalCurrency;
            }

            public String getContractReference() {

                return contractReference;
            }

            public double getExchangeRate() {

                return exchangeRate;
            }

            public double getOriginalAmount() {

                return originalAmount;
            }

            public String getOriginalCurrency() {

                return originalCurrency;
            }

            @Override
            public String toString() {

                return ToStringBuilder.reflectionToString(this);
            }
        }

        @Override
        public String toString() {

            return ToStringBuilder.reflectionToString(this);
        }
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString(this);
    }
}
