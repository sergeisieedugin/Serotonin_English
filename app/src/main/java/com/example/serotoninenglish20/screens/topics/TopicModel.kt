package com.example.serotoninenglish20.screens.topics



data class Topic(
    val title: String,
    val child: List<SubTopic>
)


data class SubTopic(
    val title: String,
    val description: String,
    val path: String
)