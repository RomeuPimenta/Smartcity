package com.example.smartcity.api


data class OutputPost(

    val results: List<Result>

)

data class Result(
    val id: Int,
    val username: String,
    val password: String
)
