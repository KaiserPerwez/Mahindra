package com.pixams.data.model.api

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

data class SubmitAnswerModel(
    @SerializedName("test_id")
    @Expose
    var testId: String? = null,

    @SerializedName("scheduled_id")
    @Expose
    var scheduled_id: String? = null,

    @SerializedName("sap_code")
    @Expose
    var sapCode: String? = null,

    @SerializedName("end_date_time")
    @Expose
    var endDateTime: String? = null,

    @SerializedName("questions")
    @Expose
    var answers: List<AnswerModel>? = null
)

data class AnswerModel(
    @SerializedName("question_id")
    @Expose
    var questionId: String? = null,
    @SerializedName("type")
    @Expose
    var type: String? = null,
    @SerializedName("answer")
    @Expose
    var answer: String = "0000"
)