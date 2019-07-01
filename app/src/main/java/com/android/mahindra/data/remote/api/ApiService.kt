package com.android.mahindra.data.remote.api

/**
 * @author Kaiser Perwez
 */

import com.android.mahindra.BuildConfig
import com.android.mahindra.data.model.api.BaseResponse
import com.android.mahindra.data.model.api.QuestionsResponseModel
import com.android.mahindra.data.model.api.UserLoginData
import io.reactivex.Observable
import okhttp3.MultipartBody
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.*
import java.util.concurrent.TimeUnit

interface ApiService {

    //SAMPLES . METHOD-->GET 

    @GET("api/login/")
    fun userLogin(@Query("sap_code") sap_code: String): Observable<UserLoginData>


    @GET("api/generate_otp/")
    fun validatePhone(@Query("ph_no") ph_no: String): Observable<BaseResponse<Any>>

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

    companion object {
        val debug = true
        val devUrl = "~devProject/"
        val liveUrl = "~liveProject/"
        // Dev URL
      //  private val BASE_URL: String = "http://134.209.153.25/${if (debug) devUrl else liveUrl}"
        private val BASE_URL: String = "http://134.209.153.25:9090/"
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
