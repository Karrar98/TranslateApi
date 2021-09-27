package com.example.translateapi.model

import com.google.gson.annotations.SerializedName

data class LanguagesItem(
    @SerializedName("code")
    val code: String?,
    @SerializedName("name")
    val name: String?
)