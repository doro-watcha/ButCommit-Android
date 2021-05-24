package com.goddoro.butcommit.data.api

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import io.reactivex.Completable
import kotlinx.android.parcel.Parcelize
import kotlinx.android.parcel.RawValue


@Parcelize
data class ApiResponse<out T>(
    @SerializedName("success")
    @Expose
    val isSuccess: Boolean,

    @SerializedName("message")
    @Expose
    val message: String?,

    @SerializedName("ecode")
    @Expose
    val errorCode: Int?,

    @SerializedName("data")
    @Expose
    val data: @RawValue T?
) : Parcelable {

    fun unWrapData() = run {
        when (isSuccess) {
            true -> data!!

            else -> throw UnWrappingDataException(
                errorCode ?: ResponseCode.UNKNOWN.code,
                "$errorCode : $message"
            )
        }
    }

    fun unWrapCompletable(): Completable =

        when (isSuccess) {
            true -> Completable.complete()
            else -> throw UnWrappingDataException(errorCode ?: ResponseCode.UNKNOWN.code, "${errorCode} : ${message}")
        }

}


class UnWrappingDataException(val errorCode: Int, message: String) : Exception(message)


enum class ResponseCode(val code: Int) {

    UNKNOWN(-999),

    REQUEST_BODY_VALIATION_FAIL(102),

    UNVERIFIED_EMAIL(202),

    EXIST_EMAIL(500),
    SERVER_ERROR(700),

    NOT_FOUND(404),
    ENTRY_DELETED(601),

}