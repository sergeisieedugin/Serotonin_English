package com.example.serotoninenglish20.screens.topics
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable


data class Topic(
    val title: String,
    val child: List<SubTopic>
)


data class SubTopic(
    val title: String,
    val description: String
)