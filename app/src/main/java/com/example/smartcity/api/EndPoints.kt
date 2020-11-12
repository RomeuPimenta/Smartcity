package com.example.smartcity.api

import com.example.smartcity.api.OutputPost
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface EndPoints {

   /* @GET("/users/") //devolve array <list> se retornar objeto nao precisamos list
    fun getUsers(): Call<List<User>>

    @GET("/users/{id}")
    fun getUserById(@Path("id") id: Int): Call<User>

    @FormUrlEncoded
    @POST("/posts")
    fun postTest(@Field("title") first: String?): Call<OutputPost>*/

    @FormUrlEncoded
    @POST("smartcity/login")
    fun login(@Field("username") username: String,
              @Field("password") password: String): Call<User>

    @FormUrlEncoded
    @POST("smartcity/registo")
    fun registo(@Field("username") username: String,
              @Field("password") password: String): Call<Object>
}
