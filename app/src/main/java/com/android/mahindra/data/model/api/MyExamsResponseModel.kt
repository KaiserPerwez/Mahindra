package com.android.mahindra.data.model.api


import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class MyExamsResponseModel {

    @SerializedName("data")
    @Expose
    var data: List<ExamsModel>? = null

}

class ExamsModel {

    @SerializedName("status")
    @Expose
    var status: String? = null
    @SerializedName("to_date")
    @Expose
    var toDate: String? = null
    @SerializedName("test_duration")
    @Expose
    var testDuration: String? = null
    @SerializedName("from_date")
    @Expose
    var fromDate: String? = null
    @SerializedName("test_name")
    @Expose
    var testName: String? = null
    @SerializedName("total_questions_no")
    @Expose
    var totalQuestionsNo: String? = null
    @SerializedName("test_id")
    @Expose
    var testId: Int? = null

}