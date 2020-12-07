package com.example.smartcity.api

import com.example.smartcity.api.OutputPost
import org.json.JSONObject
import retrofit2.Call
import retrofit2.http.*


interface EndPoints {

    @FormUrlEncoded
    @POST("smartcity/login")
    fun login(@Field("username") username: String,
              @Field("password") password: String): Call<UserLogin>

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
                @Field ("utilizador_id") utilizador_id: String,
                @Field ("tipo_id") tipo_id: String): Call<Object>

    //@FormUrlEncoded
    @POST("smartcity/markers")
    fun markers(): Call<List<Locations>>

    @FormUrlEncoded
    @POST("smartcity/markersbyiduser")
    fun markersbyiduser(@Field("id") id: String): Call<String>

    @FormUrlEncoded
    @POST("smartcity/markersbyid")
    fun getMarkerById(@Field("id") id: Int): Call<Locations>

    @FormUrlEncoded
    @POST("smartcity/markersbytipo")
    fun markersbytipo(@Field("tipo_id") tipo_id: String): Call<List<Locations>>

    @FormUrlEncoded
    @POST("smartcity/update")
    fun update(@Field("texto") texto: String,
                @Field ("morada") morada: String,
                @Field ("id") id: Int,
                @Field ("tipo_id") tipo_id: String): Call<Object>

    @FormUrlEncoded
    @POST("smartcity/delete")
    fun apagarmarker(@Field("id") id: Int): Call<Object>

    @FormUrlEncoded
    @POST("smartcity/buscaridtipo")
    fun buscaridtipo(@Field("tipo") tipo: String): Call<Int>
}
