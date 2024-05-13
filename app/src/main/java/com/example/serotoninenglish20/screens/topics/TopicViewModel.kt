package com.example.serotoninenglish20.screens.topics

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.serotoninenglish20.api.Api
import kotlinx.coroutines.launch

class TopicViewModel : ViewModel() {
    private val _topics = MutableLiveData<List<Topic>>()
    val topics: LiveData<List<Topic>> = _topics

    fun fetchTopics() {
        viewModelScope.launch {
            try {
                _topics.value = Api.retrofitService.getTopics()
            } catch (e: Exception) {
                Log.d("TopicMessage", e.message.toString())
            }
        }
    }
}