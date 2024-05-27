package com.example.serotoninenglish20.screens.guess

import com.google.gson.annotations.SerializedName



data class Types(
    @SerializedName(value= "id")
    val identification: Int,
    @SerializedName(value = "title")
    val titleName: String,
)


data class Filters(
    val types: List<Types>
)

data class Sentence(
    val id: Int,
    @SerializedName(value = "russian")
    val russianPhrase: String,
    @SerializedName(value = "words")
    val wordsToChoose: List<String>
)

data class CheckValid(
    val isValid: Boolean
)

data class Description(
    val theme: Theme
)

data class Theme(
    val description: String,
    @SerializedName("formula_simple")
    val sentenceFormula: String,
    @SerializedName("formula_question")
    val questionFormula: String,
    val examples: List<String>
)
