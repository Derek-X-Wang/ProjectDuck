package com.intbridge.projectduck.utils;

import android.os.Parcel;
import android.os.Parcelable;

import com.arlib.floatingsearchview.suggestions.model.SearchSuggestion;

/**
 *
 * Created by Derek on 12/21/2015.
 */
public class LocationSuggestion implements SearchSuggestion, Comparable<LocationSuggestion> {

    private String locationName = "undefined";

    public LocationSuggestion(String name){
        locationName = name;
    }
    public LocationSuggestion(Parcel source) {
        locationName = source.readString();
    }

    public String getLocationName(){
        return locationName;
    }

    public static final Parcelable.Creator<LocationSuggestion> CREATOR = new Parcelable.Creator<LocationSuggestion>() {
        @Override
        public LocationSuggestion createFromParcel(Parcel in) {
            return new LocationSuggestion(in);
        }

        @Override
        public LocationSuggestion[] newArray(int size) {
            return new LocationSuggestion[size];
        }
    };

    @Override
    public int compareTo(LocationSuggestion another) {
        locationName.compareTo(another.getLocationName());
        return 0;
    }

    @Override
    public String getBody() {
        return locationName;
    }

    @Override
    public Parcelable.Creator getCreator() {
        return CREATOR;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(locationName);
    }
}
