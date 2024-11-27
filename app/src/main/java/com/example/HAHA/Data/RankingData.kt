package com.example.HAHA

import android.os.Parcel
import android.os.Parcelable

data class RankingData(
    val name: String,
    val title: String,
    val description: String,
    val rank: String,
    val rating: Float,
    val review: Int,
    val addr: String, // Address
    val fee: Int, // Fee
    val img: String, // Image URL
    val cat: String, // Category
    val shortDesc: String, // Short Description
    val creatorId: String // Creator ID
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readFloat(),
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "",
        parcel.readString() ?: "" // Read creatorId
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(name)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(rank)
        parcel.writeFloat(rating)
        parcel.writeInt(review)
        parcel.writeString(addr)
        parcel.writeInt(fee)
        parcel.writeString(img)
        parcel.writeString(cat)
        parcel.writeString(shortDesc)
        parcel.writeString(creatorId) // Write creatorId to parcel
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RankingData> {
        override fun createFromParcel(parcel: Parcel): RankingData {
            return RankingData(parcel)
        }

        override fun newArray(size: Int): Array<RankingData?> {
            return arrayOfNulls(size)
        }
    }
}
