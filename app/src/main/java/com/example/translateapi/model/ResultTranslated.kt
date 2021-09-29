package com.example.translateapi.model

import com.google.gson.annotations.SerializedName

data class ResultTranslated(
    @SerializedName("translatedText")
    val translatedText: String?
)