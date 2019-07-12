package com.android.mahindra.data.model.api

import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

data class QuestionsResponseModel(
    @SerializedName("questions")
    @Expose
    var questions: List<Question>? = null
)

@Parcelize
data class Question(
    @SerializedName("question_id")
    @Expose
    var questionId: String? = null,
    @SerializedName("question")
    @Expose
    var question: String? = null,
    @SerializedName("type")
    @Expose
    var type: String? = null,
    @SerializedName("mandatory")
    @Expose
    var mandatory: Boolean? = null,
    @SerializedName("options")
    @Expose
    var options: List<String>? = null,
    @SerializedName("hint")
    @Expose
    var hint: String? = null,
    var answer:String?=null
) : Parcelable


data class AnswerModel(val quesnId:String,
                       val quesnType:String,
                       var answer:String
                       )