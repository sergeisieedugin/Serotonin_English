package com.example.serotoninenglish20.screens.guess

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serotoninenglish20.api.Api
import kotlinx.coroutines.launch


class GuessViewModel : ViewModel() {
    private val _guessItems = MutableLiveData<Sentence>()
    val guessItems: LiveData<Sentence> = _guessItems

    var themePath: String = ""

    var filterState = mutableStateOf(mapOf<String, Boolean>())


    var words: MutableList<String> = mutableStateListOf()
    var chosenWords: MutableList<String> = mutableStateListOf()

    private val _sentenceDescription = MutableLiveData<Description>()
    val sentenceDescription: LiveData<Description> = _sentenceDescription


    private val _guessFilters = MutableLiveData<Filters>()
    val guessFilters: LiveData<Filters> = _guessFilters

    fun fetchSentence() {
        viewModelScope.launch {
            try {
                _guessItems.value = Api.retrofitService.getSentence(themePath)
                chosenWords.clear()
                words.clear()
                words.addAll(_guessItems.value!!.wordsToChoose.toMutableList())
            } catch (e: Exception) {
                Log.d("SentenceMessage", e.message.toString())
            }
        }
    }

    fun fetchFilters() {
        viewModelScope.launch {
            try {
                _guessFilters.value = Api.retrofitService.getFilters()

                val state = mutableMapOf<String, Boolean>()
                _guessFilters.value!!.types.forEach {
                    state[it.titleName] = true
                }

                filterState.value = state

                Log.d("FiltersMessage", "Done")
            } catch (e: Exception) {
                Log.d("FilterMessage", e.message.toString())
            }
        }
    }

    suspend fun checkAnswer(): Boolean {
        try {
            val isValid = Api.retrofitService.checkAnswer(
                _guessItems.value!!.russianPhrase,
                chosenWords.joinToString(separator = ",")
            )
            return isValid.isValid
        } catch (e: Exception) {
            Log.d("CheckAnswer", e.message.toString())
            return false
        }
    }

    fun fetchDescription() {
        viewModelScope.launch {
            try {
                _sentenceDescription.value = Api.retrofitService.getDescription()
                Log.d("Description", "Done")
            } catch (e: Exception) {
                Log.d("Description", e.message.toString())
            }
        }
    }

    fun toChosenWords(index: Int) {
        chosenWords.add(words[index])
        words.removeAt(index)
    }

    fun deleteFromChosen(index: Int) {
        words.add(chosenWords[index])
        chosenWords.removeAt(index)
    }
}