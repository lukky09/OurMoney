package com.example.ourmoney.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;
import com.example.ourmoney.Models.SavingTarget;
import com.example.ourmoney.Models.Wallet;

@Database(entities = {MoneyTransaction.class, Category.class, Wallet.class, SavingTarget.class}, version = 1)
@TypeConverters({DateConverter.class})
public abstract class AppDatabase extends RoomDatabase {
    public abstract AppDao appDao();
    public static AppDatabase INSTANCE;

    public static AppDatabase getAppDatabase(Context context){
        if (INSTANCE == null){
            INSTANCE = Room.databaseBuilder(
                    context,
                    AppDatabase.class,
                    "OurMoneyDB"
            ).build();
        }
        return INSTANCE;
    };
}
