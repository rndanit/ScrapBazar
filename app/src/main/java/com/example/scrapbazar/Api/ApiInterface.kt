package com.example.scrapbazar.Api

import com.example.scrapbazar.DataModel.AddressRequest
import com.example.scrapbazar.DataModel.AddressResponse
import com.example.scrapbazar.DataModel.CreateQueryRequest
import com.example.scrapbazar.DataModel.CreateQueryResponse
import com.example.scrapbazar.DataModel.DeleteAddressRequest
import com.example.scrapbazar.DataModel.DeleteAddressResponse
import com.example.scrapbazar.DataModel.GetProfileDataResponse
import com.example.scrapbazar.DataModel.LoginRequest
import com.example.scrapbazar.DataModel.LoginResponse
import com.example.scrapbazar.DataModel.OtpRequest
import com.example.scrapbazar.DataModel.OtpResponse
import com.example.scrapbazar.DataModel.ProductDataClass
import com.example.scrapbazar.DataModel.RequestQueryRequest
import com.example.scrapbazar.DataModel.RequestQueryResponseSubListItem
import com.example.scrapbazar.DataModel.RescheduleRequest
import com.example.scrapbazar.DataModel.RescheduleResponse
import com.example.scrapbazar.DataModel.SelectDataClassSubListItem
import com.example.scrapbazar.DataModel.UpdateProfileResponse
import com.example.scrapbazar.DataModel.UserAddressRequest
import com.example.scrapbazar.DataModel.UserAddressResponseSubListItem
import com.example.scrapbazar.DataModel.UserRequest
import com.example.scrapbazar.DataModel.UserResponseSubListItem
import com.example.scrapbazar.DataModel.cancelInquiryRequest
import com.example.scrapbazar.DataModel.cancelInquiryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiInterface {

    //Product API Endpoint
    @GET("allProducts")
    fun getData(): Call<List<List<ProductDataClass>>>

    //Volume API Endpoint
    @GET("allVolume")
    fun selectData(): Call<List<List<SelectDataClassSubListItem>>>

    //Login API Endpoint
    @POST("login")
    fun login(@Body request: LoginRequest):Call<LoginResponse>

    //Send-Otp API Endpoint
    @POST("send-otp")
    fun sendOtp(@Body request: OtpRequest): Call<OtpResponse>

    //Map Address Post API Endpoint
    @POST("createAddress")
    fun address(@Body request: AddressRequest): Call<AddressResponse>

    //Map Address Fetch Data Post API Endpoint
    //@GET("createAddress") // Replace with your actual endpoint
    //fun getAddresses(): Call<List<AddressResponse>>
    @POST("userAddress")
    fun fetchAddresses(@Body request: UserRequest): Call<List<List<UserResponseSubListItem>>>

    //Create Query Post API EndPoint
    @POST("createInquiry")
    fun createQuery(@Body request: CreateQueryRequest): Call<CreateQueryResponse>

    //User Query End Point Isme request page me data api se ahayega
    @POST("userInquiry")
    fun userQuery(@Body request: RequestQueryRequest): Call<List<List<RequestQueryResponseSubListItem>>>

    //Create Cancel Inquiry (End Point) API.
    @POST("cancelInquiry")
    fun cancelDataPass(@Body request: cancelInquiryRequest): Call<cancelInquiryResponse>

    //Reschedule Pickup(End Point)
    @POST("reschedule")
    fun reschedule(@Body request: RescheduleRequest): Call<RescheduleResponse>

    @Multipart
    @POST("updateprofile")
       fun submitForm(
        @Part("id") id: RequestBody,
        @Part("userName") userName: RequestBody,
        @Part("email") email: RequestBody,
        @Part image: MultipartBody.Part?
    ): Call<UpdateProfileResponse>

    //User Address EndPoint
    @POST("userAddress")
    fun userAddress(@Body request:UserAddressRequest): Call<List<List<UserAddressResponseSubListItem>>>

    //Delete Address EndPoint
    @POST("deleteAddress")
    fun deleteAddress(@Body request:DeleteAddressRequest): Call<DeleteAddressResponse>

    //Get Profile Data Endpoint
    @GET("getCustomerData")
    fun getProfileData(@Query("id")id:Int): Call<GetProfileDataResponse>
}