package com.pixams.data.remote.api

/**
 * @author Kaiser Perwez
 */

import com.pixams.BuildConfig
import com.pixams.data.model.api.*
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.RequestBody
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    //SAMPLES

    @POST("api/validate_sap_code/")
    @FormUrlEncoded
    fun userValidate(@Field("sap_code") sap_code: String): Observable<UserLoginData>

    @POST("api/forgot_pin/")
    @FormUrlEncoded
    fun forgotPin(@Field("sap_code") sap_code: String): Observable<UserLoginData>

    @POST("api/change_pin/")
    @FormUrlEncoded
    fun changePin(
        @Field("sap_code") sap_code: String,
        @Field("old_pin") old_pin: String,
        @Field("new_pin") new_pin: String
    ): Observable<UserLoginData>

    @POST("api/login/")
    @FormUrlEncoded
    fun userLogin(@Field("sap_code") sap_code: String, @Field("user_pin") user_pin: String): Observable<UserLoginData>

    @POST("api/change_status/")
    @FormUrlEncoded
    fun changeStatus(@Field("sap_code") sap_code: String, @Field("test_id") test_id: String, @Field("scheduled_id") scheduled_id: String): Observable<BaseResponse<Any>>

    @POST("api/validate_scheduled_datetime/")
    @FormUrlEncoded
    fun validateScheduledDatetime(@Field("sap_code") sap_code: String, @Field("test_id") test_id: String, @Field("scheduled_id") scheduled_id: String): Observable<BaseResponse<Any>>

    @POST("api/generate_otp/")
    @FormUrlEncoded
    fun validatePhone(@Field("ph_no") ph_no: String?): Observable<BaseResponse<Any>>

    @POST("api/update_profile/")
    fun updateProfile(@Body file: RequestBody): Observable<UpdateProfileData>

    @POST("api/save_capture_image/")
    fun saveCaptureImage(@Body file: RequestBody): Observable<UpdateProfileData>

    @GET("api/change_requests/")
    fun getChangeRequests(): Observable<ContactAdminResponse>

    @GET("api/header")
    fun getByHeader(@Header("Authorizations") token: String? = ""): Observable<BaseResponse<Any>>

    @GET("api/query")
    fun getByQuery(@Query("id") id: String): Observable<BaseResponse<Any>>

    @GET("api/path/{ID}")
    fun getByPath(@Path("ID") jobId: String): Observable<BaseResponse<Any>>


    //SAMPLES . METHOD-->POST
    @POST("api/asModelBody")
    fun postByModelBody(@Body requestModel: Any): Observable<BaseResponse<Any>>

    @POST("api/asParamKeys")
    @FormUrlEncoded
    fun postAsParamKeys(@Field("key") key: String): Observable<BaseResponse<Any>>

    @POST("api/multipart")
    fun postAsMultipart(@Header("Authorizations") token: String?, @Body requestBody: MultipartBody): Observable<BaseResponse<Array<Any>>>


    @POST("api/get_questions/")
    @FormUrlEncoded
    fun getQuestions(@Field("test_id") testId: String): Observable<QuestionsResponseModel>


    @POST("api/end_test/")
    fun submitAnswers(@Body submitAnswerModel: SubmitAnswerModel): Observable<BaseResponse<Any>>

    @POST("api/contact_admin/")
    fun contactAdmin(@Body contactAdminList: ContactAdminRequest): Observable<BaseResponse<Any>>

    @POST("api/contact_admin/")
    @FormUrlEncoded
    fun contactAdmin( @Field("change_list") optionList: String?, @Field("sap_code") sapCode: String): Observable<BaseResponse<Any>>

    @POST("api/fetch_scheduled_test/")
    @FormUrlEncoded
    fun getExams(@Field("sap_code") sapCode: String?): Observable<MyExamsResponseModel>

    companion object {
        val debug = true
        val devUrl = "~devProject/"
        val liveUrl = "~liveProject/"
        // Dev URL
        //  private val BASE_URL: String = "http://134.209.153.25/${if (debug) devUrl else liveUrl}"
//        val BASE_URL: String = "http://192.168.1.100:9090/"
//        val BASE_URL_FILE: String = "http://192.168.1.100:9090"
//        val BASE_URL: String = "http://134.209.153.25:9090/"
//        val BASE_URL_FILE: String = "http://134.209.153.25:9090"
        val BASE_URL: String = "https://pixams.com/"
        val BASE_URL_FILE: String = "https://pixams.com"
        val BASE_URL_FILES = "http://209.59.156.100/${if (debug) devUrl else liveUrl}public/uploads/"

        fun create(baseUrl: String = BASE_URL): ApiService {

            var retrofit: Retrofit? = null

            retrofit ?: let {
                val interceptor = HttpLoggingInterceptor()
                interceptor.level = HttpLoggingInterceptor.Level.BODY

                val builder = OkHttpClient.Builder()
                if (BuildConfig.DEBUG) {
                    builder.addInterceptor(interceptor)
                }
                val client = builder
                    .connectTimeout(500, TimeUnit.SECONDS)
                    .writeTimeout(500, TimeUnit.SECONDS)
                    .readTimeout(500, TimeUnit.SECONDS)
                    .build()

                retrofit = Retrofit.Builder()
                    .client(client)
                    .addCallAdapterFactory(
                        RxJava2CallAdapterFactory.create()
                    )
                    .addConverterFactory(
                        GsonConverterFactory.create()
                    )
                    .baseUrl(baseUrl)
                    .build()
            }

            return retrofit!!.create(ApiService::class.java)
        }
    }

}
