package com.example.retrofit_ex

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.HTTP
import retrofit2.http.POST
import java.util.*

interface RetrofitInterface {
    @POST("/login")
    fun executeLogin(@Body map: HashMap<String, String>): Call<LoginResult?>?

    @POST("/signup")
    fun executeSignup(@Body map: HashMap<String, String>): Call<Void?>?

    @POST("/post")
    fun executePost(@Body map: HashMap<String?, Any?>?): Call<Void?>?

    @get:GET("/post/all")
    val post: Call<ResponseBody?>?

    @HTTP(method = "DELETE", path = "/post/delete", hasBody = true)
    fun deletePost(
        @Body map: HashMap<String?, String?>?
    ): Call<Void?>?

    @POST("/post/update")
    fun executeUpdate(@Body map: HashMap<String?, Any?>?): Call<UpdateResult?>?

    @POST("/comment/all")
    fun getComment(@Body map: HashMap<String?, Any?>?): Call<ResponseBody?>?

    @POST("/comment/post")
    fun executeCommentPost(@Body map: HashMap<String?, Any?>?): Call<Void?>?

    @HTTP(method = "DELETE", path = "/user/delete", hasBody = true)
    fun deleteUser(
        @Body map: HashMap<String?, String?>?
    ): Call<Void?>?

    @HTTP(method = "DELETE", path = "/comment/delete", hasBody = true)
    fun deleteComment(
        @Body map: HashMap<String, String>
    ): Call<Void?>?
}