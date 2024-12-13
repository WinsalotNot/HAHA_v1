package com.example.HAHA.Data

import android.os.Parcel
import android.os.Parcelable

data class RegisterData(
    val email : String,
    val password : String,
    val name : String,
    val address : String
) : Parcelable {

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(email)
        parcel.writeString(password)
        parcel.writeString(name)
        parcel.writeString(address)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RegisterData> {
        override fun createFromParcel(parcel: Parcel): RegisterData {
            return RegisterData(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
            )
        }

        override fun newArray(size: Int): Array<RegisterData?> {
            return arrayOfNulls(size)
        }
    }
}