package com.example.ourmoney.Models;

import androidx.room.Embedded;
import androidx.room.Relation;

public class TransactionWithRelation {
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
}
