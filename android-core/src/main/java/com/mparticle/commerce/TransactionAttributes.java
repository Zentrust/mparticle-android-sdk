package com.mparticle.commerce;


import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

/**
 * Class encaspulating the parameters of a given Product CommerceEvent
 *
 */
public class TransactionAttributes {
    private String mAffiliation = null;
    private Double mRevenue;
    private Double mShipping;
    private Double mTax;
    private String mCouponCode = null;
    private String mId = null;

    public TransactionAttributes(@NonNull TransactionAttributes transactionAttributes) {
        mAffiliation = transactionAttributes.mAffiliation;
        mRevenue = transactionAttributes.mRevenue;
        mShipping = transactionAttributes.mShipping;
        mTax = transactionAttributes.mTax;
        mCouponCode = transactionAttributes.mCouponCode;
        mId = transactionAttributes.mId;
    }

    @NonNull
    public String getId() {
        return mId;
    }

    /**
     * Create a TransactionAttributes object to be associated with a {@link CommerceEvent}.
     *
     * Note that transaction ID is required for {@link Product#PURCHASE} and {@link Product#REFUND} events.
     *
     */
    public TransactionAttributes() {
        super();
    }

    /**
     * Create a TransactionAttributes object to be associated with a {@link CommerceEvent}.
     *
     * Note that transaction ID is required for {@link Product#PURCHASE} and {@link Product#REFUND} events.
     *
     * @param transactionId a unique ID for this transaction
     */
    public TransactionAttributes(@NonNull String transactionId) {
        super();
        setId(transactionId);
    }

    @NonNull
    public TransactionAttributes setId(@NonNull String id) {
        mId = id;
        return this;
    }

    @Nullable
    public String getCouponCode() {
        return mCouponCode;
    }

    @NonNull
    public TransactionAttributes setCouponCode(@Nullable String couponCode) {
        this.mCouponCode = couponCode;
        return this;
    }

    @Nullable
    public Double getTax() {
        return mTax;
    }

    @NonNull
    public TransactionAttributes setTax(@Nullable Double tax) {
        this.mTax = tax;
        return this;
    }

    @Nullable
    public Double getShipping() {
        return mShipping;
    }

    @NonNull
    public TransactionAttributes setShipping(@Nullable Double shipping) {
        this.mShipping = shipping;
        return this;
    }

    @Nullable
    public Double getRevenue() {
        return mRevenue;
    }

    @NonNull
    public TransactionAttributes setRevenue(@Nullable Double revenue) {
        this.mRevenue = revenue;
        return this;
    }

    @Nullable
    public String getAffiliation() {
        return mAffiliation;
    }

    @NonNull
    public TransactionAttributes setAffiliation(@Nullable String affiliation) {
        this.mAffiliation = affiliation;
        return this;
    }
}