package com.example.ourmoney.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.util.ArrayList;

@Entity(tableName = "wallets")
public class Wallet implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int wallet_id;
    private String walletName;
    private int walletAmount;

    public Wallet(String walletName, int walletAmount) {
        this.walletName = walletName;
        this.walletAmount = walletAmount;
    }


    protected Wallet(Parcel in) {
        wallet_id = in.readInt();
        walletName = in.readString();
        walletAmount = in.readInt();
    }

    public static final Creator<Wallet> CREATOR = new Creator<Wallet>() {
        @Override
        public Wallet createFromParcel(Parcel in) {
            return new Wallet(in);
        }

        @Override
        public Wallet[] newArray(int size) {
            return new Wallet[size];
        }
    };

    @Override
    public String toString() {
        return walletName;
    }

    public int getWallet_id() {
        return wallet_id;
    }

    public void setWallet_id(int wallet_id) {
        this.wallet_id = wallet_id;
    }

    public String getWalletName() {
        return walletName;
    }

    public void setWalletName(String walletName) {
        this.walletName = walletName;
    }

    public int getWalletAmount() {
        return walletAmount;
    }

    public void setWalletAmount(int walletAmount) {
        this.walletAmount = walletAmount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(wallet_id);
        parcel.writeString(walletName);
        parcel.writeInt(walletAmount);
    }
}

