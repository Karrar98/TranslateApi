package com.example.translateapi.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.core.widget.doOnTextChanged
import androidx.lifecycle.lifecycleScope
import com.example.translateapi.R
import com.example.translateapi.databinding.ActivityMainBinding
import com.example.translateapi.model.Languages
import com.example.translateapi.model.ResultTranslated
import com.example.translateapi.repositry.LanguageRepository
import com.example.translateapi.repositry.TranslateRepository
import com.example.translateapi.utils.Constant
import com.example.translateapi.utils.Status
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private var queryMap = mutableMapOf<String, String>()
    private val arrayLangName: ArrayList<String> = arrayListOf()
    private val arrayLangCode: ArrayList<String> = arrayListOf()
    private var selectLangSource: String? = null
    private var selectLangTarget: String? = null
    private var positionSourceLang: Int? = null
    private var positionTargetLang: Int? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setup()
    }

    private fun setup() {
        initInput()
        initSpinner()
        initSwapLang()
    }

    private fun initSwapLang() {
        binding.swapLang.setOnClickListener {
            switchLang()
        }
    }

    private fun switchLang() {
        selectLangSource = selectLangTarget.also { selectLangTarget = selectLangSource }
        binding.apply {
            targetText.text = sourceText.text.toString().also { sourceText.setText(targetText.text) }
            positionTargetLang?.let { dropdownLangSource.setSelection(it) }
            positionSourceLang?.let { dropdownLangTarget.setSelection(it) }
        }
    }

    private fun initInput() {
        binding.sourceText.doOnTextChanged { text, start, before, count ->
            queryMap = initMutableMap(text.toString(), selectLangSource, selectLangTarget)
            makeTranslateRequest(queryMap = queryMap)
        }
    }

    private fun initMutableMap(query: String, selectLangSource: String?, selectLangTarget: String?): MutableMap<String, String> {
        queryMap[Constant.Translate.QUERY] = query
        queryMap[Constant.Translate.SOURCE] = selectLangSource.toString()
        queryMap[Constant.Translate.TARGET] = selectLangTarget.toString()
        return queryMap
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
        data.forEach {
            arrayLangName.add(it.name.toString())
            arrayLangCode.add(it.code.toString())
        }
        val arrayAdapter = ArrayAdapter(this, R.layout.item_dropdown, arrayLangName)
        binding.dropdownLangSource.apply {
            setAdapter(arrayAdapter)
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    selectLangSource = arrayLangCode[position]
                    positionSourceLang = position
                    queryMap = initMutableMap(binding.sourceText.text.toString(), selectLangSource, selectLangTarget)
                    makeTranslateRequest(queryMap = queryMap)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
        binding.dropdownLangTarget.apply {
            setAdapter(arrayAdapter)
            onItemSelectedListener = object: AdapterView.OnItemSelectedListener{
                override fun onItemSelected(p0: AdapterView<*>?, p1: View?, position: Int, p3: Long) {
                    selectLangTarget = arrayLangCode[position]
                    positionTargetLang = position
                    queryMap = initMutableMap(binding.sourceText.text.toString(), selectLangSource, selectLangTarget)
                    makeTranslateRequest(queryMap = queryMap)
                }

                override fun onNothingSelected(p0: AdapterView<*>?) {

                }
            }
        }
    }

    private fun makeTranslateRequest(queryMap: Map<String, String>) {

       lifecycleScope.launch {
            TranslateRepository.getTranslateData( queryMap = queryMap).collect(::getResultTranslate)
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
                setData(response.data)
            }
        }
    }

    private fun setData(data: ResultTranslated) {
        binding.targetText.text = data.translatedText
    }
}