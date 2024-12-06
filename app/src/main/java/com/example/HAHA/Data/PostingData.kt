package com.example.HAHA.Data

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class PostingData(
    val username: String,
    val title: String,
    val description: String,
    val rank: String,
    val rating: Float,
    val review: Int,
    val addr: String,
    val fee: Float,
    val cat: String,
    val shortDesc: String,
    val creatorid: Int,
    val img: String,
    @SerializedName("completed") val isCompleted: Boolean,
    @SerializedName("bought") val isBought: Boolean,
    val purchasedfor: Float
) : Parcelable, Comparable<PostingData> {

    override fun compareTo(other: PostingData): Int {
        return this.rating.compareTo(other.rating) // Example: compare by rating
    }

    // Parcelable implementation
    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeString(username)
        parcel.writeString(title)
        parcel.writeString(description)
        parcel.writeString(rank)
        parcel.writeFloat(rating)
        parcel.writeInt(review)
        parcel.writeString(addr)
        parcel.writeFloat(fee)
        parcel.writeString(img) // Write the img ByteArray
        parcel.writeString(cat)
        parcel.writeString(shortDesc)
        parcel.writeInt(creatorid)
        parcel.writeBoolean(isCompleted)
        parcel.writeBoolean(isBought)
        parcel.writeFloat(purchasedfor)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<PostingData> {
        override fun createFromParcel(parcel: Parcel): PostingData {
            return PostingData(
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readFloat(),
                parcel.readInt(),
                parcel.readString() ?: "",
                parcel.readFloat(),
                parcel.readString() ?: "",
                parcel.readString() ?: "",
                parcel.readInt(),
                parcel.readString() ?: "", // Read img as ByteArray
                parcel.readBoolean(),
                parcel.readBoolean(),
                parcel.readFloat()
            )
        }

        override fun newArray(size: Int): Array<PostingData?> {
            return arrayOfNulls(size)
        }
    }
}
