package com.example.ourmoney.Models;

import android.os.Parcel;
import android.os.Parcelable;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

public class MoneyTransaction implements Parcelable {
    private int transactionAmount;
    private String transactionType;
    private Category transactionCategory;
    private Date transactionDate;

    public MoneyTransaction(int transactionAmount, String transactionType, Category transactionCategory) {
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
        this.transactionDate = new Date();
    }

    protected MoneyTransaction(Parcel in) {
        transactionAmount = in.readInt();
        transactionType = in.readString();
        transactionCategory = in.readParcelable(Category.class.getClassLoader());
    }

    public static final Creator<MoneyTransaction> CREATOR = new Creator<MoneyTransaction>() {
        @Override
        public MoneyTransaction createFromParcel(Parcel in) {
            return new MoneyTransaction(in);
        }

        @Override
        public MoneyTransaction[] newArray(int size) {
            return new MoneyTransaction[size];
        }
    };

    public int getTransactionAmount() {
        return transactionAmount;
    }

    public void setTransactionAmount(int transactionAmount) {
        this.transactionAmount = transactionAmount;
    }

    public String getTransactionType() {
        return transactionType;
    }

    public void setTransactionType(String transactionType) {
        this.transactionType = transactionType;
    }

    public Category getTransactionCategory() {
        return transactionCategory;
    }

    public void setTransactionCategory(Category transactionCategory) {
        this.transactionCategory = transactionCategory;
    }

    public Date getTransactionDate() { return transactionDate; }
    public String getFormattedTransactionDate() {
        DateFormat df = new SimpleDateFormat("E, dd MMM yyyy");
        return df.format(this.transactionDate);
    }

    public void setTransactionDate(Date transactionDate) {
        this.transactionDate = transactionDate;
    }


    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(transactionAmount);
        parcel.writeString(transactionType);
        parcel.writeParcelable(transactionCategory, i);
    }
}
