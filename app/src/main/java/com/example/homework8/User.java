package com.example.homework8;

import android.os.Parcel;
import android.os.Parcelable;

import com.shaded.fasterxml.jackson.annotation.JsonProperty;

/**
 * Created by revati on 13-04-2016.
 */
public class User implements Parcelable {
    public String fullName,email,password,phoneNumber,picture;

    public User()
    {

    }

    protected User(Parcel in) {
        fullName = in.readString();
        email = in.readString();
        password = in.readString();
        phoneNumber = in.readString();
        picture = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @JsonProperty("email")
    public String getEmail() {
        return email;
    }
    @JsonProperty("fullName")
    public String getFullName() {
        return fullName;
    }
    @JsonProperty("password")
    public String getPassword() {
        return password;
    }
    @JsonProperty("phoneNumber")
    public String getPhoneNumber() {
        return phoneNumber;
    }
    @JsonProperty("picture")
    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public User(String fullName, String email, String password, String phoneNumber) {
        this.fullName = fullName;
        this.email = email;
        this.password = password;
        this.phoneNumber = phoneNumber;
        //this.picture = picture;
    }

    @Override
    public String toString() {
        return "User{" +
                "uname='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", picture='" + picture + '\'' +
                '}';
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(fullName);
        dest.writeString(email);
        dest.writeString(password);
        dest.writeString(phoneNumber);
        dest.writeString(picture);
    }
}
