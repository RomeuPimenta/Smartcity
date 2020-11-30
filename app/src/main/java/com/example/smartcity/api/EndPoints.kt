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
              @Field("password") password: String): Call<Object>

    @FormUrlEncoded
    @POST("smartcity/registo")
    fun registo(@Field("username") username: String,
              @Field("password") password: String): Call<Object>

    @FormUrlEncoded
    @POST("smartcity/inserir")
    fun inserir(@Field("texto") texto: String,
                @Field ("lat") lat: Float,
                @Field ("lng") lng: Float,
                @Field ("morada") morada: String,
                @Field ("id_utilizador") id_utilizador: Int): Call<Object>

    //@FormUrlEncoded
    @POST("smartcity/markers")
    fun markers(): Call<List<Locations>>

    @FormUrlEncoded
    @POST("smartcity/markersbyid")
    fun getMarkerById(@Field("id") id: Int): Call<List<Locations>>

    @FormUrlEncoded
    @POST("smartcity/update")
    fun update(@Field("texto") texto: String,
                @Field ("morada") morada: String,
                @Field ("id") id: Int): Call<Object>

    @FormUrlEncoded
    @POST("smartcity/delete")
    fun apagarmarker(@Field("id") id: Int): Call<Object>
}
