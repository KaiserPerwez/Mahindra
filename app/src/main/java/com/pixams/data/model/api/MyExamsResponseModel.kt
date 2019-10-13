package com.pixams.data.model.api


import android.os.Parcelable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class MyExamsResponseModel(
    @SerializedName("data")
    @Expose
    var data: List<ExamsModel>? = null
) : Parcelable

@Parcelize
data class ExamsModel(
    @SerializedName("scheduled_id")
    @Expose
    val scheduledId: Int? = 0,
    @SerializedName("start_date")
    @Expose
    val startDate: String? = "",
    @SerializedName("start_time")
    @Expose
    val startTime: String? = "",
    @SerializedName("status")
    @Expose
    val status: String? = "",
    @SerializedName("test_duration")
    @Expose
    val testDuration: String? = "",
    @SerializedName("test_id")
    @Expose
    val testId: Int? = 0,
    @SerializedName("test_name")
    @Expose
    val testName: String? = "",
    @SerializedName("total_questions_no")
    @Expose
    val totalQuestionsNo: String? = ""
): Parcelable

/*@Parcelize
data class ExamsModel(
    @SerializedName("status")
    @Expose
    var status: String? = null,
    @SerializedName("to_date")
    @Expose
    var toDate: String? = null,
    @SerializedName("test_duration")
    @Expose
    var testDuration: String? = null,
    @SerializedName("from_date")
    @Expose
    var fromDate: String? = null,
    @SerializedName("test_name")
    @Expose
    var testName: String? = null,
    @SerializedName("total_questions_no")
    @Expose
    var totalQuestionsNo: String? = null,
    @SerializedName("test_id")
    @Expose
    var testId: Int? = null,
    @SerializedName("scheduled_id")
    @Expose
    var scheduled_id: Int? = null,
    @SerializedName("completed_in")
    @Expose
    var completedIn: String? = null
) : Parcelable*/

