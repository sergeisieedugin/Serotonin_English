package com.example.serotoninenglish20.screens.guess

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
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

    var filterState: MutableList<Int> = mutableStateListOf()


    var words: MutableList<String> = mutableStateListOf()
    var chosenWords: MutableList<String> = mutableStateListOf()

    private val _sentenceDescription = MutableLiveData<Description>()
    val sentenceDescription: LiveData<Description> = _sentenceDescription


    private val _guessFilters = MutableLiveData<Filters>()
    val guessFilters: LiveData<Filters> = _guessFilters

    fun fetchSentence() {
        viewModelScope.launch {
            try {
                val types = filterState.joinToString(",")
                _guessItems.value = Api.retrofitService.getSentence(themePath, types)
                chosenWords.clear()
                words.clear()
                words.addAll(_guessItems.value!!.wordsToChoose.toMutableList())
            } catch (e: Exception) {
                Log.d("SentenceMessage", e.message.toString())
            }
        }
    }

    fun setNewFilterState (value: MutableList<Int>) {
        filterState.clear()
        filterState.addAll(value)
        fetchSentence()
    }

    fun fetchFilters() {
        viewModelScope.launch {
            try {
                _guessFilters.value = Api.retrofitService.getFilters()

                filterState.clear()
                _guessFilters.value!!.types.forEach {
                    filterState.add(it.identification)
                }

                Log.d("FiltersMessage", _guessFilters.value?.types?.size.toString())
            } catch (e: Exception) {
                Log.d("FilterMessage", e.message.toString())
            }
        }
    }

    suspend fun checkAnswer(): Boolean {
        return try {
            val isValid = Api.retrofitService.checkAnswer(
                _guessItems.value!!.russianPhrase,
                chosenWords.joinToString(separator = ",")
            )
            isValid.isValid
        } catch (e: Exception) {
            Log.d("CheckAnswer", e.message.toString())
            false
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