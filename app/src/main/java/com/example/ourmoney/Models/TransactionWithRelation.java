package com.example.ourmoney.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TransactionWithRelation implements Parcelable {
    @Embedded public MoneyTransaction transaction;
    @Relation(
            parentColumn = "category_id",
            entityColumn = "categoryId"
    )
    public Category category;
    @Relation(
            parentColumn = "wallet_id",
            entityColumn = "wallet_id"
    )
    public Wallet wallet;

    public TransactionWithRelation() {
        // kenapa kosong? karena parcelable merengek minta constructor
    }

    protected TransactionWithRelation(Parcel in) {
        transaction = in.readParcelable(MoneyTransaction.class.getClassLoader());
        category = in.readParcelable(Category.class.getClassLoader());
        wallet = in.readParcelable(Wallet.class.getClassLoader());
    }

    public static final Creator<TransactionWithRelation> CREATOR = new Creator<TransactionWithRelation>() {
        @Override
        public TransactionWithRelation createFromParcel(Parcel in) {
            return new TransactionWithRelation(in);
        }

        @Override
        public TransactionWithRelation[] newArray(int size) {
            return new TransactionWithRelation[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeParcelable(transaction, i);
        parcel.writeParcelable(category, i);
        parcel.writeParcelable(wallet, i);
    }
}
