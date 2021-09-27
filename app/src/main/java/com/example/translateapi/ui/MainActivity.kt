package com.example.translateapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.translateapi.databinding.ActivityMainBinding
import com.example.translateapi.model.Languages
import com.example.translateapi.model.ResultTranslated
import com.example.translateapi.repositry.LanguageRepository
import com.example.translateapi.repositry.TranslateRepository
import com.example.translateapi.utils.Constant
import com.example.translateapi.utils.Status
import kotlinx.coroutines.async
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var queryMap: MutableMap<String, String>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.sourceText.doOnTextChanged { text, start, before, count ->
            queryMap?.set(Constant.Translate.QUERY, text.toString())
            queryMap?.set(Constant.Translate.SOURCE, "en")
            queryMap?.set(Constant.Translate.TARGET, "ar")
            queryMap?.let { makeTranslateRequest(it) }
            initSpinner()
        }
        initSpinner()
    }

    private fun initSpinner() {
        makeLanguageRequest()
    }

    private fun makeLanguageRequest() {
        lifecycleScope.launch {
            LanguageRepository.getLanguage()
                .onCompletion {

                }.catch {

                }.collect(::getResultLanguage)
        }
    }

    private fun getResultLanguage(response: Status<Languages>){
        return when(response){
            is Status.Error -> {
//                Toast.makeText(
//                    this@MainActivity,
//                    "error can't access sorry",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            is Status.Loading -> {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Loading access",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            is Status.Success -> {
                setSpinner(response.data)
            }
        }
    }

    private fun setSpinner(data: Languages) {
        Log.i("Karrar_j_d", data.toString())
    }

    private fun makeTranslateRequest(queryMap: Map<String, String>) {
        lifecycleScope.async {
            TranslateRepository.getTranslateQuery( queryMap = queryMap).onCompletion {

            }.catch {

            }.collect{
                Log.i("Karrar_j_d", it.toString())
                getResultTranslate(it)
            }
        }
    }

    private fun getResultTranslate(response: Status<ResultTranslated>) {
        return when (response) {
            is Status.Error -> {
//                Toast.makeText(
//                    this@MainActivity,
//                    "error can't access sorry",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            is Status.Loading -> {
//                Toast.makeText(
//                    this@MainActivity,
//                    "Loading access",
//                    Toast.LENGTH_SHORT
//                ).show()
            }
            is Status.Success -> {
                Log.i("Karrar_j_d", response.data.translatedText.toString())
                setData(response.data)
            }
        }
    }

    private fun setData(data: ResultTranslated) {
        binding.targetText.text = data.translatedText
    }
}