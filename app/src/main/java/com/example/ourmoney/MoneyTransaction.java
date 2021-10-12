package com.example.ourmoney;

public class MoneyTransaction {
    private int transactionAmount;
    private String transactionType;
    private Category transactionCategory;

    public MoneyTransaction(int transactionAmount, String transactionType, Category transactionCategory) {
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.transactionCategory = transactionCategory;
    }

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
}
