package com.flynaut.healthtag.model.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class OrderDetailResponse(

    @field:SerializedName("data")
    val data: List<DataItemOrderDetail>? = null,

    @field:SerializedName("message")
    val message: String? = null,

    @field:SerializedName("status")
    val status: Int? = null,

    ) : Parcelable

@Parcelize
data class DataItemOrderDetail(

    @field:SerializedName("__v")
    val __v: Int? = null,

    @field:SerializedName("userId")
    val userId: UserDetails,

    @field:SerializedName("totalAmount")
    val totalAmount: Int? = null,

    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("addressId")
    val addressId: AddressData,

    @field:SerializedName("orderDate")
    val orderDate: String? = null,

    @field:SerializedName("orderId")
    val orderId: String? = null,

    @field:SerializedName("paymentMethod")
    val paymentMethod: String? = null,

    @field:SerializedName("products")
    val products: List<Product>? = null,

    @field:SerializedName("status")
    val status: String? = null,

    ) : Parcelable

@Parcelize
data class Product(
    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("productId")
    val productId: ProductDetails,

    @field:SerializedName("quantity")
    val quantity: Int,
) : Parcelable

@Parcelize
data class ProductDetails(
    @field:SerializedName("__v")
    val __v: Int? = null,

    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("category")
    val category: String? = null,

    @field:SerializedName("category_name")
    val category_name: String? = null,

    @field:SerializedName("coverImage")
    val coverImage: String? = null,

    @field:SerializedName("deliveryRate")
    var deliveryRate: Int? = null,

    @field:SerializedName("howToUse")
    val howToUse: String? = null,

    @field:SerializedName("image")
    val image: String? = null,

    @field:SerializedName("longDescription")
    val longDescription: String? = null,

    @field:SerializedName("price")
    val price: Int? = null,

    @field:SerializedName("productName")
    val productName: String? = null,

    @field:SerializedName("productType")
    val productType: String? = null,

    @field:SerializedName("quantity")
    val quantity: Int? = null,

    @field:SerializedName("shortDescription")
    val shortDescription: String? = null,

    @field:SerializedName("status")
    val status: String? = null,
) : Parcelable

@Parcelize
data class UserDetails(
    @field:SerializedName("__v")
    val __v: Int? = null,

    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("blocked")
    val blocked: Boolean? = null,

    @field:SerializedName("confirmPassword")
    val confirmPassword: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("email")
    val email: String? = null,

    @field:SerializedName("isApproved")
    val isApproved: Boolean? = null,

    @field:SerializedName("otp")
    val otp: String? = null,

    @field:SerializedName("password")
    val password: String? = null,

    @field:SerializedName("refercode")
    val refercode: String? = null,

    @field:SerializedName("role")
    val role: Int? = null,

    @field:SerializedName("status")
    val status: String? = null,

    @field:SerializedName("updatedAt")
    val updatedAt: String? = null,

    @field:SerializedName("userId")
    val userId: String? = null,

    @field:SerializedName("phoneNumber")
    val phoneNumber: Int? = null,
) : Parcelable

@Parcelize
data class AddressData(
    @field:SerializedName("zipcode")
    val zipcode: String? = null,

    @field:SerializedName("address")
    val address: String? = null,

    @field:SerializedName("phoneNumber")
    val phoneNumber: String? = null,

    @field:SerializedName("city")
    val city: String? = null,

    @field:SerializedName("addressType")
    val addressType: String? = null,

    @field:SerializedName("__v")
    val __v: Int? = null,

    @field:SerializedName("_id")
    val _id: String? = null,

    @field:SerializedName("state")
    val state: String? = null,

    @field:SerializedName("user")
    val user: String? = null,
) : Parcelable
