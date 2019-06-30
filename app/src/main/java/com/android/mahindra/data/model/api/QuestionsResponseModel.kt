package com.android.mahindra.data.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class QuestionsResponseModel {

    @SerializedName("questions")
    @Expose
    var questions: List<Question>? = null

}

class Question {

    @SerializedName("question_id")
    @Expose
    var questionId: String? = null
    @SerializedName("question")
    @Expose
    var question: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("mandatory")
    @Expose
    var mandatory: Boolean? = null
    @SerializedName("options")
    @Expose
    var options: List<String>? = null
    @SerializedName("hint")
    @Expose
    var hint: String? = null

}