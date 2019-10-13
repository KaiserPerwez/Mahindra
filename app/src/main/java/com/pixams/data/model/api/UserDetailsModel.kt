package com.pixams.data.model.api

/**
 * @author Kaiser Perwez
 */
 
import com.google.gson.annotations.SerializedName

data class LoginRequestModel(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("firebase_id") val firebaseToken: String?,
    @SerializedName("device_type") val deviceType: String?
)


data class LoginResponseModel(
    @SerializedName("token") var token: String? = "",
    @SerializedName("user_details") var userDetails: UserDetailsModel? = UserDetailsModel()
)



data class UserDetailsModel(
    @SerializedName("id") var id: Int? = 0,
    @SerializedName("name") var name: String? = "",
    @SerializedName("email") var email: String? = "",
    @SerializedName("phone") var phone: String? = "",
    @SerializedName("device_type") var deviceType: String? = "",
    @SerializedName("firebase_token") var firebaseToken: String? = "",
    @SerializedName("image_user") var imageUser: String? = ""
)

data class ChangePasswordModel(
    @SerializedName("current_password") val currentPassword: String?,
    @SerializedName("new_password") val newPassword: String?
)

data class ForgotPasswordModel(
    @SerializedName("email") val email: String?,
    @SerializedName("password") val password: String?,
    @SerializedName("otp") val code: String?
)
