package com.android.mahindra.data.model.api

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class UserLoginData(
    @SerializedName("email_id")
    val emailId: String? = "",
    @SerializedName("first_name")
    val firstName: String? = "",
    @SerializedName("is_first_login")
    val isFirstLogin: Boolean? = false,
    @SerializedName("last_name")
    val lastName: String? = "",
    @SerializedName("profile_pic")
    var profilePic: String? = "",
    @SerializedName("sap_code")
    val sapCode: String? = "",
    @SerializedName("status")
    val status: Boolean? = false,
    @SerializedName("msg")
    val message: String? = ""
) : Parcelable

@Parcelize
data class UpdateProfileData(
    @SerializedName("msg")
    val message: String? = "",
    @SerializedName("profile_pic")
    val profilePic: String? = "",
    @SerializedName("sap_code")
    val sapCode: String? = "",
    @SerializedName("status")
    val status: Boolean? = false
) : Parcelable