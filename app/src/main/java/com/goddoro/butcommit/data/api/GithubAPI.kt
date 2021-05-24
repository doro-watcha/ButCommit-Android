package com.goddoro.butcommit.data.api

import android.os.Parcelable
import com.goddoro.butcommit.data.model.Commit
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.http.QueryMap

interface GithubAPI {

    @GET("commit")
    suspend fun listCommits (@QueryMap parameters : HashMap<String,Any> ) : ApiResponse<CommitListResponse>

    @GET("commit/check")
    suspend fun checkUsername ( @Query("username") username : String) : ApiResponse<Any>
}

@Parcelize
data class CommitListResponse(

    @SerializedName("commits")
    val commits : List<Commit>
) : Parcelable