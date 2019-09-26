package com.android.mahindra.data.model.api
import com.google.gson.annotations.SerializedName


data class ContactAdminResponse(
    @SerializedName("option_list")
    val optionList: List<Option>? = listOf()
)

data class ContactAdminRequest(
    @SerializedName("change_list")
    val optionList: List<Option>? = listOf(),
    @SerializedName("sap_code")
    val sapCode: String
)

data class Option(
    @SerializedName("id")
    val id: Int? = 0,
    @SerializedName("option")
    val option: String? = "",
    @SerializedName("is_checked")
    var isChecked: Boolean = false,
    @SerializedName("description")
    var description: String? = ""
)