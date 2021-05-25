package com.goddoro.butcommit.data.api

import retrofit2.http.FieldMap
import retrofit2.http.FormUrlEncoded
import retrofit2.http.PATCH
import retrofit2.http.POST

interface UserAPI {

    @POST("/user")
    @FormUrlEncoded
    suspend fun register( @FieldMap parameters : HashMap<String,Any>) : ApiResponse<Any>


    @PATCH("/user")
    @FormUrlEncoded
    suspend fun update ( @FieldMap parameters : HashMap<String,Any> ) : ApiResponse<Any>

}