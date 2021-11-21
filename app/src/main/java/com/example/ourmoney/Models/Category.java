package com.example.ourmoney.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;
import androidx.room.Entity;
import androidx.room.PrimaryKey;

@Entity(tableName = "category")
public class Category implements Parcelable {
    @PrimaryKey(autoGenerate = true)
    private int categoryId;
    private String categoryName;
    private boolean isPengeluaran;

    public Category(String categoryName, boolean isPengeluaran) {
        this.categoryName = categoryName;
        this.isPengeluaran = isPengeluaran;
    }

    public boolean isPengeluaran() {
        return isPengeluaran;
    }

    public void setPengeluaran(boolean pengeluaran) {
        isPengeluaran = pengeluaran;
    }

    protected Category(Parcel in) {
        categoryId = in.readInt();
        categoryName = in.readString();
        isPengeluaran = in.readByte() != 0;
    }

    public static final Creator<Category> CREATOR = new Creator<Category>() {
        @Override
        public Category createFromParcel(Parcel in) {
            return new Category(in);
        }

        @Override
        public Category[] newArray(int size) {
            return new Category[size];
        }
    };

    public int getCategoryId() {
        return categoryId;
    }

    public void setCategoryId(int categoryId) {
        this.categoryId = categoryId;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }


    @Override
    public String toString() {
        return  categoryName;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(categoryId);
        parcel.writeString(categoryName);
        parcel.writeByte((byte) (isPengeluaran ? 1 : 0));
    }
}

/*package com.example.ourmoney;

public class Category {
    private String categoryName;
    private String categoryType;

    public Category(String categoryName, String categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    public String getCategoryName() {
        return categoryName;
    }

    public void setCategoryName(String categoryName) {
        this.categoryName = categoryName;
    }

    public String getCategoryType() {
        return categoryType;
    }

    public void setCategoryType(String categoryType) {
        this.categoryType = categoryType;
    }

    @Override
    public String toString() {
        return  categoryName;
    }
}*/





