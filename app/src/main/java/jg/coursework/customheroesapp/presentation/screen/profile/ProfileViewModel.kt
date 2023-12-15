package jg.coursework.customheroesapp.presentation.screen.profile

import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.data.local.DataStoreManager
import jg.coursework.customheroesapp.data.local.TokenManager
import jg.coursework.customheroesapp.data.repository.CustomHeroesOrderRepository
import jg.coursework.customheroesapp.data.repository.CustomHeroesUserRepository
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileViewModel @Inject constructor(
    private val dataStoreManager: DataStoreManager,
    private val tokenManager: TokenManager,
    private val customHeroesUserRepository: CustomHeroesUserRepository,
    private val customHeroesOrderRepository: CustomHeroesOrderRepository
): ViewModel() {


    private val _meState = MutableStateFlow<Resource<User>>(Resource.loading(null))
    val meState: StateFlow<Resource<User>> = _meState

    private val _ordersState = MutableStateFlow<Resource<List<OrderItemDTO>>>(Resource.loading(null))

    val orderList = mutableStateOf<List<OrderItemDTO>>(emptyList())
    var coolOrderList = mutableStateListOf<List<List<OrderItemDTO>>>(emptyList())

    fun getMe() = viewModelScope.launch {
        val tempUser = dataStoreManager.getUserInfo()
        if(tempUser != null) {
            _meState.value = Resource.success(tempUser)
        }
        else {
            getRemoteMe()
        }
    }

    fun getRemoteMe() = viewModelScope.launch {
        val meResponse = customHeroesUserRepository.getMe()
        if(meResponse.status == Resource.Status.SUCCESS) {
            _meState.value = Resource.success(meResponse.data)
        }
        else {
            _meState.value = Resource.error("")
        }
    }


    fun logout() = viewModelScope.launch {
        dataStoreManager.setUserId(-1)
        dataStoreManager.setBasket("")
        tokenManager.setAccessToken("")
        tokenManager.setRefreshToken("")

    }

    fun getOrders() = viewModelScope.launch {
        val response = customHeroesOrderRepository.getOrders()
        _ordersState.value = Resource.success(response.data)
        val orderIdList: MutableList<Int> = emptyList<Int>().toMutableList()
        val orders = response.data ?: return@launch
        for(item in orders) {
            if(item.order.id !in orderIdList) {
                orderIdList.add(item.order.id)
            }
        }
        for(orderId in orderIdList) {
            val tempOrderItemByOrderId = mutableListOf<OrderItemDTO>()
            for(item in orders) {
                if(item.order.id == orderId) {
                    tempOrderItemByOrderId.add(item)
                }
            }
            coolOrderList += listOf(tempOrderItemByOrderId)
        }
        orderList.value = orders
    }
}