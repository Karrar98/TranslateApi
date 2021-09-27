package com.example.translateapi.network

import android.util.Log
import com.example.translateapi.model.Languages
import com.example.translateapi.model.ResultTranslated
import com.example.translateapi.utils.Constant
import com.example.translateapi.utils.Status
import com.google.gson.Gson
import okhttp3.FormBody
import okhttp3.HttpUrl
import okhttp3.OkHttpClient
import okhttp3.Request

object Client {

    val client = OkHttpClient()
    val gson = Gson()


    private fun BaseUrl(pathSegment: String, queryMap: Map<String, String>?) =
        HttpUrl.Builder().scheme(Constant.Url.SCHEME).
        host(Constant.Url.HOST).addPathSegment(pathSegment).apply {
        if (pathSegment == Constant.TRANSLATE) {
            addQueryParameter(Constant.Translate.QUERY, queryMap?.get(Constant.Translate.QUERY))
            addQueryParameter(Constant.Translate.SOURCE, queryMap?.get(Constant.Translate.SOURCE))
            addQueryParameter(Constant.Translate.TARGET, queryMap?.get(Constant.Translate.TARGET))
        }
    }.toString()

    fun initRequestLanguage(): Status<Languages> {
        val request = Request.Builder().url(BaseUrl(Constant.LANGUAGE, null)).build()
        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val parserResponse = gson.fromJson(
                response.body?.string(),
                Languages::class.java
            )
            Status.Success(parserResponse)
        } else {
            Status.Error(response.message)
        }
    }

    fun initRequestTranslate(queryMap: Map<String, String>): Status<ResultTranslated> {
        val postBody = FormBody.Builder().build()
//            .add(Constant.Translate.QUERY, q)
//            .add(Constant.Translate.SOURCE, source)
//            .add(Constant.Translate.TARGET, target)
//            .build()
        Log.i("Karrar_j_d", BaseUrl(Constant.TRANSLATE, queryMap))
        val url = "https://translate.argosopentech.com/translate?q=book&source=en&target=ar"
        val request = Request.Builder().url(url).post(postBody).build()
        val response = client.newCall(request).execute()

        return if (response.isSuccessful) {
            val parserResponse = gson.fromJson(
                response.body?.string(),
                ResultTranslated::class.java
            )
            Status.Success(parserResponse)
        } else {
            Status.Error(response.message)
        }
    }
}