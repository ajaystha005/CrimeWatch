package com.mvp.crimewatch.model;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.android.gms.maps.model.LatLng;
import com.google.gson.annotations.SerializedName;
import com.j256.ormlite.field.DatabaseField;
import com.mvp.crimewatch.constant.DbConstant;
import com.mvp.crimewatch.utils.CoordinateUtils;


/**
 * Created by ajayshrestha on 3/17/17.
 */

public class Crime implements Parcelable {

    public Crime() {
    }

    @DatabaseField(generatedId = true, columnName = DbConstant.CRIME_ID)
    private int mId;

    @DatabaseField(columnName = DbConstant.CRIME_BEAT)
    @SerializedName("beat")
    private String mBeat;

    @DatabaseField(columnName = DbConstant.CRIME_BLOCK)
    @SerializedName("block")
    private String mBlock;

    @DatabaseField(columnName = DbConstant.CRIME_RD_NO)
    @SerializedName("rdNo")
    private String mRdNo;

    @DatabaseField(columnName = DbConstant.CRIME_COMMUNITY_AREA)
    @SerializedName("communityArea")
    private String mCommunityArea;

    @DatabaseField(columnName = DbConstant.CRIME_DATE_OCCURRED)
    @SerializedName("dateOccurred")
    private String mDateOccurred;


    @DatabaseField(columnName = DbConstant.CRIME_DESCRIPTION)
    @SerializedName("iucrDescription")
    private String mCrimeDescription;

    @DatabaseField(columnName = DbConstant.CRIME_CPDISTRICT)
    @SerializedName("cpdDistrict")
    private String mCPDistrict;

    @DatabaseField(columnName = DbConstant.CRIME_LAST_UPDATED)
    @SerializedName("lastUpdated")
    private String mLastUpdated;

    @DatabaseField(columnName = DbConstant.CRIME_LOCATON_DESCRIPTION)
    @SerializedName("locationDesc")
    private String mLocationDescription;

    @DatabaseField(columnName = DbConstant.CRIME_PRIMARY)
    @SerializedName("primary")
    private String mPrimary;

    @DatabaseField(columnName = DbConstant.CRIME_WARD)
    @SerializedName("ward")
    private String mWard;

    @DatabaseField(columnName = DbConstant.CRIME_X_COORDINATE)
    @SerializedName("xCoordinate")
    private long mXCoordinate;

    @DatabaseField(columnName = DbConstant.CRIME_Y_COORDINATE)
    @SerializedName("yCoordinate")
    private long mYCoordinate;


    private LatLng mLatLng;


    public String getBeat() {
        return mBeat;
    }

    public String getBlock() {
        return mBlock;
    }

    public String getRdNo() {
        return mRdNo;
    }

    public String getCommunityArea() {
        return mCommunityArea;
    }

    public String getDateOccurred() {
        return mDateOccurred;
    }

    public String getCrimeDescription() {
        return mCrimeDescription;
    }

    public String getCpdDistrict() {
        return mCPDistrict;
    }

    public String getLastUpdated() {
        return mLastUpdated;
    }

    public String getLocationDesc() {
        return mLocationDescription;
    }

    public String getPrimary() {
        return mPrimary;
    }

    private String getWard() {
        return mWard;
    }

    public long getXCoordinate() {
        return mXCoordinate;
    }

    public long getYCoordinate() {
        return mYCoordinate;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.mBeat);
        dest.writeString(this.mBlock);
        dest.writeString(this.mRdNo);
        dest.writeString(this.mCommunityArea);
        dest.writeString(this.mDateOccurred);
        dest.writeString(this.mCrimeDescription);
        dest.writeString(this.mCPDistrict);
        dest.writeString(this.mLastUpdated);
        dest.writeString(this.mLocationDescription);
        dest.writeString(this.mPrimary);
        dest.writeString(this.mWard);
        dest.writeLong(this.mXCoordinate);
        dest.writeLong(this.mYCoordinate);
    }

    public Crime(Parcel in) {
        mBeat = in.readString();
        mBlock = in.readString();
        mRdNo = in.readString();
        mCommunityArea = in.readString();
        mDateOccurred = in.readString();
        mCrimeDescription = in.readString();
        mCPDistrict = in.readString();
        mLastUpdated = in.readString();
        mLocationDescription = in.readString();
        mPrimary = in.readString();
        mWard = in.readString();
        mXCoordinate = in.readLong();
        mYCoordinate = in.readLong();
    }


    public static final Creator<Crime> CREATOR = new Creator<Crime>() {
        @Override
        public Crime createFromParcel(Parcel in) {
            return new Crime(in);
        }

        @Override
        public Crime[] newArray(int size) {
            return new Crime[size];
        }
    };

    @Override
    public boolean equals(Object obj) {
        Crime crime = (Crime) obj;
        if (crime.getRdNo() == this.getRdNo())
            return true;

        return false;
    }

    /**
     * Convert the state plane coordinate into LatLng
     *
     * @param xCoordinate
     * @param yCoordinate
     * @return
     */
    public LatLng getLatLng(double xCoordinate, double yCoordinate) {
        if (mLatLng != null) {
            return this.mLatLng;
        }
        return this.mLatLng = CoordinateUtils.getLatLng(xCoordinate, yCoordinate);
    }
}
