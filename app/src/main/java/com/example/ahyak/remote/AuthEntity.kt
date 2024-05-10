package com.example.ahyak.remote

import android.os.Parcel
import android.os.Parcelable
import com.google.gson.annotations.SerializedName

data class DrugSearchNameRequest(
    //키 값 지정
    @SerializedName("QUERY") val query : String
)

data class DrugSearchNameResponse(
    @SerializedName("RESULT") val result : List<RESULT>
)

data class DrugSearchShapeRequest(
    //키 값 지정
    @SerializedName("PRINT") val _print: String,
    @SerializedName("DRUG_SHAPE") val _drug_shape:String,
    @SerializedName("COLOR") val _color:String,
    @SerializedName("TYPE") val _type:String,
    @SerializedName("LINE") val _line:String
)

data class DrugSearchShapeResponse(
    @SerializedName("RESULT") val result : List<RESULT>
)
data class RESULT(
    @SerializedName("ITEM_SEQ") val item_seq: Int,
    @SerializedName("ITEM_NAME") val item_name: String,
    @SerializedName("PRINT") val print: String,
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: "",
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(item_seq)
        parcel.writeString(item_name)
        parcel.writeString(print)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RESULT> {
        override fun createFromParcel(parcel: Parcel): RESULT {
            return RESULT(parcel)
        }

        override fun newArray(size: Int): Array<RESULT?> {
            return arrayOfNulls(size)
        }
    }
}

