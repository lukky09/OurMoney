package com.example.ourmoney.Models;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.room.Entity;
import androidx.room.PrimaryKey;

import java.io.Serializable;
import java.util.Date;

@Entity(tableName = "savingtarget")
public class SavingTarget implements Parcelable, Serializable {
    @PrimaryKey(autoGenerate = true)
    private int id;
    private Double targetAmount;
    private Double accumulated;
    private Date targetCreated;
    private Date targetDeadline;
    private boolean active;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public Double getTargetAmount() {
        return targetAmount;
    }

    public void setTargetAmount(Double targetAmount) {
        this.targetAmount = targetAmount;
    }

    public Double getAccumulated() {
        return accumulated;
    }

    public void setAccumulated(Double accumulated) {
        this.accumulated = accumulated;
    }

    public Date getTargetCreated() {
        return targetCreated;
    }

    public void setTargetCreated(Date targetCreated) {
        this.targetCreated = targetCreated;
    }

    public Date getTargetDeadline() {
        return targetDeadline;
    }

    public void setTargetDeadline(Date targetDeadline) {
        this.targetDeadline = targetDeadline;
    }

    public boolean isActive() {
        return active;
    }

    public void setActive(boolean active) {
        this.active = active;
    }

    public SavingTarget(Double targetAmount, Date targetCreated, Date targetDeadline) {
        this.targetAmount = targetAmount;
        this.accumulated = 0d;
        this.targetCreated = targetCreated;
        this.targetDeadline = targetDeadline;
        this.active=true;
    }

    protected SavingTarget(Parcel in) {
        id = in.readInt();
        if (in.readByte() == 0) {
            targetAmount = null;
        } else {
            targetAmount = in.readDouble();
        }
        if (in.readByte() == 0) {
            accumulated = null;
        } else {
            accumulated = in.readDouble();
        }
        targetCreated = new Date(in.readLong());
        targetDeadline = new Date(in.readLong());
        active = (in.readByte() == 1 ? true : false);
    }

    public static final Creator<SavingTarget> CREATOR = new Creator<SavingTarget>() {
        @Override
        public SavingTarget createFromParcel(Parcel in) {
            return new SavingTarget(in);
        }

        @Override
        public SavingTarget[] newArray(int size) {
            return new SavingTarget[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel parcel, int i) {
        parcel.writeInt(id);
        if (targetAmount == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(targetAmount);
        }
        if (accumulated == null) {
            parcel.writeByte((byte) 0);
        } else {
            parcel.writeByte((byte) 1);
            parcel.writeDouble(accumulated);
        }
        parcel.writeLong(targetCreated.getTime());
        parcel.writeLong(targetDeadline.getTime());
        parcel.writeByte((byte) (active ? 1 : 0) );
    }
}
