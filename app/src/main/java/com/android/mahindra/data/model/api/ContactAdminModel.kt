package com.android.mahindra.data.model.api
import com.google.gson.annotations.SerializedName


data class ContactAdminResponse(
    @SerializedName("option_list")
    val optionList: List<Option>? = listOf()
)

data class Option(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("option")
    val option: String? = ""
)