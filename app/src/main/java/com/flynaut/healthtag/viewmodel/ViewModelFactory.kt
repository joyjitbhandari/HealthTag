package com.flynaut.healthtag.viewmodel

import androidx.lifecycle.ViewModelProvider

import androidx.lifecycle.ViewModel
import com.flynaut.healthtag.network.ApiService

class ViewModelFactory(private val apiService: ApiService) : ViewModelProvider.NewInstanceFactory(){

    override fun <T : ViewModel> create(modelClass: Class<T>): T {

        return when{

            modelClass.isAssignableFrom(LoginViewModel::class.java) -> LoginViewModel(apiService) as T

            modelClass.isAssignableFrom(SignupViewModel::class.java) -> SignupViewModel(apiService) as T

            modelClass.isAssignableFrom(VerifyOtpViewModel::class.java) -> VerifyOtpViewModel(apiService) as T

            modelClass.isAssignableFrom(ForgotPasswordViewModel::class.java) -> ForgotPasswordViewModel(apiService) as T

            modelClass.isAssignableFrom(ResetPasswordViewModel::class.java) -> ResetPasswordViewModel(apiService) as T

            modelClass.isAssignableFrom(CompleteProfileViewModel::class.java) -> CompleteProfileViewModel(apiService) as T

            modelClass.isAssignableFrom(ShopViewModel::class.java) -> ShopViewModel(apiService) as T

            modelClass.isAssignableFrom(CategoryViewModel::class.java) -> CategoryViewModel(apiService) as T

            modelClass.isAssignableFrom(QuestionViewModel::class.java) -> QuestionViewModel(apiService) as T

            modelClass.isAssignableFrom(ResultViewModel::class.java) -> ResultViewModel(apiService) as T

            modelClass.isAssignableFrom(AddNewAddressViewModel::class.java) -> AddNewAddressViewModel(apiService) as T

            modelClass.isAssignableFrom(CardViewModel::class.java) -> CardViewModel(apiService) as T

            modelClass.isAssignableFrom(ProductDetailViewModel::class.java) -> ProductDetailViewModel(apiService) as T

            modelClass.isAssignableFrom(CartViewModel::class.java) -> CartViewModel(apiService) as T
            modelClass.isAssignableFrom(FamilyMemberViewModel::class.java) -> FamilyMemberViewModel(apiService) as T
            modelClass.isAssignableFrom(SubscriptionViewModel::class.java) -> SubscriptionViewModel(apiService) as T
            modelClass.isAssignableFrom(BlogsViewModel::class.java) -> BlogsViewModel(apiService) as T

            modelClass.isAssignableFrom(MyOrderViewModel::class.java) -> MyOrderViewModel(apiService) as T

            modelClass.isAssignableFrom(DeviceInstructionViewModel::class.java) -> DeviceInstructionViewModel(apiService) as T

            modelClass.isAssignableFrom(AllTicketsViewModel::class.java) -> AllTicketsViewModel(apiService) as T

            modelClass.isAssignableFrom(ProviderDetailsViewModel::class.java) -> ProviderDetailsViewModel(apiService) as T
            modelClass.isAssignableFrom(AddDeviceViewModel::class.java) -> AddDeviceViewModel(apiService) as T
            modelClass.isAssignableFrom(DeviceListViewModel::class.java) -> DeviceListViewModel(apiService) as T

            modelClass.isAssignableFrom(FaqViewModel::class.java) -> FaqViewModel(apiService) as T
            modelClass.isAssignableFrom(AllLogsListViewModel::class.java) -> AllLogsListViewModel(apiService) as T

            else -> throw IllegalArgumentException("ViewModel Not Found")
        }

    }


}