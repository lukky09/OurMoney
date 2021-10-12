package com.example.ourmoney;

import java.util.ArrayList;

public class Wallet {
    private String walletName;
    private int walletAmount;
    private ArrayList<MoneyTransaction> walletTransactions;

    public Wallet(String walletName, int walletAmount, ArrayList<MoneyTransaction> walletTransactions) {
        this.walletName = walletName;
        this.walletAmount = walletAmount;
        this.walletTransactions = walletTransactions;
    }
}
