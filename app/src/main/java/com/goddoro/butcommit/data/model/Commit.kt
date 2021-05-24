package com.goddoro.butcommit.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.text.SimpleDateFormat
import java.util.*


@Parcelize
data class Commit(
    @SerializedName("date")
    val date: String,

    @SerializedName("count")
    val count : Int
) : Parcelable {

    fun getDateDay() : String {
//
//        val newDate = SimpleDateFormat().parse(date)
//        val cal = Calendar.getInstance()
//
//        cal.time = newDate
//
//        return when (cal.get(Calendar.DAY_OF_WEEK)) {
//            1 -> "일"
//            2 -> "월"
//            3 -> "화"
//            4 -> "수"
//            5 -> "목"
//            6 -> "금"
//            7 -> "토"
//            else -> throw Exception()
//        }
        return "월"


    }
}