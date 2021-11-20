package com.example.ourmoney.Database;

import android.content.Context;

import androidx.room.Database;
import androidx.room.Room;
import androidx.room.RoomDatabase;
import androidx.room.TypeConverters;

import com.example.ourmoney.Models.Category;
import com.example.ourmoney.Models.MoneyTransaction;

@Database(entities = {MoneyTransaction.class, Category.class}, version = 1)
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
