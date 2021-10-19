package com.example.ourmoney;

import android.os.Parcel;
import android.os.Parcelable;

public class Category implements Parcelable {
    private String categoryName;
    private String categoryType;

    public Category(String categoryName, String categoryType) {
        this.categoryName = categoryName;
        this.categoryType = categoryType;
    }

    protected Category(Parcel in) {
        categoryName = in.readString();
        categoryType = in.readString();
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

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeString(categoryName);
        parcel.writeString(categoryType);
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





