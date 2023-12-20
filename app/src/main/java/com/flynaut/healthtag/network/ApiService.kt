package com.flynaut.healthtag.network

import com.flynaut.healthtag.model.Add_DeviceResponse
import com.flynaut.healthtag.model.request.*
import com.flynaut.healthtag.model.response.*
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.*

interface  ApiService {
    @POST("api/v1/login")
    suspend fun login(@Body userLogin: UserLogin): Response<LoginResponse>

    @POST("api/v1/signup")
    suspend fun signup(@Body signupRequest: SignupRequest): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("api/v1/verify-otp")
    suspend fun verifyOtp(@FieldMap fields : Map<String, String>): Response<OtpVerifyResponse>

    @FormUrlEncoded
    @POST("api/v1/reset-password")
    suspend fun resetPassword(@FieldMap fields : Map<String, String>): Response<SignUpResponse>

    @FormUrlEncoded
    @POST("api/v1/forgot-password")
    suspend fun forgotPassword(@FieldMap fields : Map<String, String>): Response<SignUpResponse>

    @POST("api/v1/my-profile/{userId}")
    suspend fun completeProfile(@Path("userId") userId : String, @Body completeProfileRequest: CompleteProfileRequest): Response<ProfileResponse>

    @POST("api/v1/add-cart/{userId}")
    suspend fun addToCart(@Path("userId") userId : String, @Body addToCartRequest: AddToCartRequest): Response<AddCartResponse>

    @PATCH("api/v1/update-cart//{userId}/{shopId}")
    suspend fun updateCart(@Path("userId") userId : String, @Path("shopId") productId : String, @FieldMap fields : Map<String, String> ): Response<AddCartResponse>


    @POST("api/v1/add-address/{userId}")
    suspend fun addAddress(@Path("userId") userId : String, @Body addressRequest: AddressRequest): Response<AddAddressResponse>

    @GET("api/v1/default-address/{userId}")
    suspend fun defaultAddress(@Path("userId") userId : String): Response<AddAddressResponse>


    @FormUrlEncoded
    @POST("api/v1/add-card/{userId}")
    suspend fun addCard(@Path("userId") userId : String, @FieldMap fields : Map<String, String>): Response<AddCardResponse>

    //AddCardFragment
    @POST("api/v1/customer/{userId}")
    suspend fun createCustomer(@Path("userId") userId : String,  @Body createCustomer: CreateCustomerRequest): Response<CreateCustomerResponse>



    @POST("api/v1/create-card/{userId}")
    suspend fun createCard(@Path("userId") userId : String,  @Body createCard: CreateCardRequest): Response<AddCardResponse>



    @POST("api/v1/create-charge/{userId}")
    suspend fun createCharge(@Path("userId") userId : String,  @Body createCharge: CreateChargeRequest): Response<Any>

    @POST("api/v1/place-order/{userId}")
    suspend fun placeOrder(@Path("userId") userId : String,  @Body createCharge: PlaceOrderRequest): Response<Any>


    @POST("api/v1/get-cards/{userId}")
    suspend fun getCardData(@Path("userId") userId : String,  @Body getCard: GetCardRequest): Response<AllCardDetailsResponse>


    @POST("api/v1/delete-card/{userId}")
    suspend fun deleteCard(@Path("userId") userId : String, @Body deleteCard: DeleteCardRequest): Response<DeleteCardResponse>

    @POST("api/v1/add-rating/{userId}/{productId}")
    suspend fun addRating(@Path("userId") userId : String,@Path("productId") productId : String, @Body addRatingRequest: AddRatingRequest): Response<BaseResponse>

    @GET("api/v1/get-all-ratings/{userId}")
    suspend fun getAllRating(@Path("userId") userId : String): Response<AllRatingResponse>

    @POST("api/v1/add-member/{userId}")
    suspend fun addFamilyMember(@Path("userId") userId : String, @Body addFamilyMemberRequest: AddFamilyMemberRequest): Response<AddFamilyMemberResponse>


    @DELETE("api/v1/delete-cart/{userId}/{productId}")
    suspend fun deleteCartProduct(@Path("userId") userId : String,@Path("productId") productId : String): Response<AddCartResponse>


    @GET("api/v1/get-shop/{userId}")
    suspend fun getShopProducts(@Path("userId") userId : String): Response<ShopListResponse>

    @GET("api/v1/get-shop-category/{userId}/{catId}")
    suspend fun getShopByCategory(@Path("userId") userId : String, @Path("catId") categoryId: String): Response<ShopListResponse>
    @GET("api/v1/get-address/{userId}")
    suspend fun getAddress(@Path("userId") userId : String): Response<AddressResponse>

    @GET("api/v1/get-cart/{userId}")
    suspend fun getCart(@Path("userId") userId : String): Response<CartItemResponse>

    @DELETE("api/v1/clear-bag/{userId}")
    suspend fun clearCart(@Path("userId") userId :String): Response<GetCartResponse>

    @GET("get-category")
    suspend fun getCategory(): Response<CategoryResponse>
    @GET("get-category/{categoryId}")
    suspend fun getCategoryDetail(@Path("categoryId") categoryId : String): Response<CategoryDetailResponse>
    @GET("get-questions/{categoryId}")
    suspend fun getQuestions(@Path("categoryId") categoryId : String): Response<QuestionResponse>
    @GET("productdetails/{productId}")
    suspend fun getProductDetail(@Path("productId") productId : String): Response<ProductDetailResponse>

    @GET("get-total/{categoryId}")
    suspend fun getResult(@Path("categoryId") categoryId : String, @FieldMap fields : Map<String, Int>): Response<ResultResponse>

    @GET("api/v1/get-allcard/{userId}")
    suspend fun getAllCardDetails(@Path("userId") userId : String): Response<AllCardDetailsResponse>
    @GET("api/v1/get-card/{userId}/{cardId}")
    suspend fun getCardDetails(@Path("userId") userId : String,@Path("cardId") cardId : String): Response<CardDetailsResponse>

    @GET("api/v1/get-allmembers/{userId}")
    suspend fun getAllFamilyMember(@Path("userId") userId :String): Response<FamilyResponse>

    @DELETE("api/v1/delete-member/{userId}/{memberId}")
    suspend fun deleteFamilyMember( @Path("userId") userId :String,@Path("memberId") memberId : String): Response<FamilyMemberResponse>

    @PATCH("api/v1/update-member/{userId}/{memberId}")
    suspend fun updateFamilyMember(@Path("userId") userId :String,@Path("memberId") memberId : String,  @Body addFamilyMemberRequest: AddFamilyMemberRequest): Response<FamilyMemberResponse>

    @GET("api/v1/get-user-subscription/{userId}")
    suspend fun getAllSubscriptions(@Path("userId") userId : String): Response<AllSubscriptionDetailsResponse>

    @GET("api/v1//get-subscription/{userId}/{subscriptionId}")
    suspend fun getSubscriptionDetails(@Path("userId") userId : String, @Path("subscriptionId") subscriptionId : String): Response<SubscriptionDetailsResponse>

    @GET("/blogs")
    suspend fun getBlogs(): Response<BlogResponse>

    @GET("/get-resources-blogs/category")
    suspend fun getBlogsByCategory(@Query("category") category: String): Response<BlogResponse>

    @GET("/get-related-resources/{blogId}")
    suspend fun getRelated(@Path("blogId") blogId: String): Response<BlogResponse>

    @GET("api/v1/recommended-products/{userId}/{categoryId}/{productId}")
    suspend fun getRecommendedProducts(
        @Path("userId") userId: String,
        @Path("categoryId") categoryId: String,
        @Path("productId") productId: String
    ): Response<RecommendedDetailResponse>

    @GET("/api/v1/feedback/{userId}")
    fun getLastSubmittedFeedback(@Path("userId") userId: String): Call<FeedbackResponse>

    @POST("/api/v1/feedback/{userId}")
    @FormUrlEncoded
    fun submitFeedback(
        @Path("userId") userId: String,
        @Field("feedback") feedback: String,
        @Field("emoji") emoji: Int
    ): Call<FeedbackResponse>

    @POST("/api/v1/feedback/{userId}")
    @FormUrlEncoded
    fun updateUserFeedback(
        @Path("userId") userId: String,
        @FieldMap fields: Map<String, String>
    ): Call<FeedbackResponse>

//    @GET("/api/v1/my-orders/{user_id}")
//    suspend fun myOrders(@Path("user_id") user_id : String): Response<MyOrderResponse>

    @POST("api/v1/cancel-order/{user_id}/{order_id}")
    suspend fun cancelOrder(@Path("user_id") user_id : String, @Path("order_id") order_id : String): Response<CancelOrderResponse>

    @GET("api/v1/my-orders/{user_id}")
    suspend fun getMyOrdersFilter(@Path("user_id") user_id : String, @Query("type") type : String, @Query("month") month : String, @Query("year") year : String): Response<OrderDetailResponse>

    @GET("api/v1/recent-tickets/{user_id}")
    suspend fun getRecentTicket(@Path("user_id") user_id : String): Response<AllTicketResponse>

    @POST("api/v1/ticket/{user_id}")
    suspend fun addTicket(@Path("user_id") user_id : String, @Body addTicketRequest : AddTicketRequest): Response<AddTicketResponse>
     @GET("api/v1/all-tickets")
    suspend fun getAllTicketsDetail(): Response<AllTicketResponse>

    @GET("api/v1/get-ticket/{user_id}")// /?status=0 open  or 1 solved
    suspend fun getAllTicketFilter(@Path("user_id") user_id : String,@Query("status") status : String): Response<AllTicketResponse>

    @GET("api/v1/get-ticket-category/{user_id}")
    suspend fun getTicketCategory(@Path("user_id") user_id : String): Response<AllTicketResponse>

//    @GET("api/v1/get-all-providers/{user_id}")// /?status=0 open  or 1 solved
//    suspend fun getAllProviderApi(@Path("user_id") user_id : String): Response<GetAllProviderResponse>
    @GET("api/v1/get-provider-list/{user_id}")
    suspend fun getAllProviderApi(@Path("user_id") userId : String): Response<ProviderResponse>

    @POST("api/v1/add-provider-list/{userId}/{providerId}")
    suspend fun addProviderDetails(@Path("userId") userId :String, @Path("providerId") providerId :String): Response<SingleProviderResponse>

    @GET("api/v1/get-provider/{providerId}/{user_id}")
    suspend fun getProviderApi(@Path("providerId") provider_id : String, @Path("user_id") user_id : String): Response<SingleProviderResponse>

    @POST("api/v1/search-provider")
    suspend fun searchProvider(@Body searchProvider: SearchFaq): Response<ProviderSearchResponse>

    @GET("get-faqs-categories")
    suspend fun getFaqCategories(): Response<FaqCategoryResponse>

    @GET("get-faqs-by-categories/{category_id}")
    suspend fun getFaqCategoriesDetails(@Path("category_id") category_id: String): Response<FaqDetailResponse>
    @POST("search-faqs")
    suspend fun searchFaq(@Body searchModel: SearchFaq): Response<FaqDetailResponse>
    @GET("aboutUs")
    suspend fun getAboutUs(): Response<AboutUsResponse>

    @GET("api/v1/my-profile/{user_id}")
    suspend fun getProfile(@Path("user_id") user_id : String): Response<ProfileResponse>

    @PUT("api/v1/my-profile/{user_id}")
    suspend fun updateProfile(@Path("user_id") user_id : String, @Body completeProfileRequest: CompleteProfileRequest): Response<ProfileResponse>


    @POST("api/v1/add-device/{userId}")
    suspend fun add_Device(@Path("userId")userId : String,@Body addDevice: AddDevice): Response<Add_DeviceResponse>

    @GET("api/v1/get-all-devices/{userId}")
    suspend fun getAllDevice(@Path("userId")userId : String): Response<AllDeviceResponse>

    @DELETE("/api/v1/delete-device/{userId}/{id}")
    suspend fun deleteDevice(@Path("userId") userId : String,@Path("id") _id : String): Response<DeleteDeviceResponse>

    @PATCH("api/v1/update-device/{userId}/{id}")
    suspend fun updateDevice(@Path("userId") userId : String,@Path("id") id : String, @Body editDeviceRequest: EditDeviceRequest): Response<UpdateDeviceResponse>


    @GET("api/v1/devicelog/{userId}")
    suspend fun getDeviceLog(@Path("userId") userId : String): Response<LogsListDetailsResponse>

}
