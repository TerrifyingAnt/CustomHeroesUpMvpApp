package jg.coursework.customheroesapp.presentation.viewmodel

import android.content.SharedPreferences
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.gson.Gson
import dagger.hilt.android.lifecycle.HiltViewModel
import jg.coursework.customheroesapp.data.dto.AuthResponseDTO
import jg.coursework.customheroesapp.data.dto.LoginDTO.LoginRequestDTO
import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderDTO
import jg.coursework.customheroesapp.data.dto.OrderDTO.OrderItemDTO
import jg.coursework.customheroesapp.data.dto.User
import jg.coursework.customheroesapp.domain.model.FigureModel
import jg.coursework.customheroesapp.domain.repository.CustomHeroesRepository
import jg.coursework.customheroesapp.util.Resource
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserViewModel @Inject constructor(
    private val customHeroesRepository: CustomHeroesRepository,
    private val sharedPreferences: SharedPreferences
): ViewModel() {

    private val _meState = MutableStateFlow<Resource<User>>(Resource.loading(null))
    val meState: StateFlow<Resource<User>> = _meState

    private val _ordersState = MutableStateFlow<Resource<List<OrderItemDTO>>>(Resource.loading(null))
    val ordersState: StateFlow<Resource<List<OrderItemDTO>>> = _ordersState

    val orderList = mutableStateOf<List<OrderItemDTO>>(emptyList())
    var coolOrderList = mutableStateListOf<List<List<OrderItemDTO>>>(emptyList())
    fun getMe(): User?{
        val userJson = sharedPreferences.getString("user", null)
        var user: User? = null
        if (userJson != null) {
            user = Gson().fromJson(userJson, User::class.java)
        }
        return user
    }

    fun getRemoteMe() {
        viewModelScope.launch {
            try {
                val meResponse =
                    customHeroesRepository.getMe("xd")
                if(meResponse.code() == 200) {
                    _meState.value = Resource.success(meResponse.body())
                }
                else
                    _meState.value = Resource.error("")
            } catch (e: Exception) {
                _meState.value = Resource.error(e.message ?: "Возникла ошибка")
            }
        }
    }

    fun logout() {
        sharedPreferences.edit().putString("token", "").apply()
        sharedPreferences.edit().putString("basket", "").apply()
        sharedPreferences.edit().putString("login", "").apply()
    }

    fun getOrders() {
        viewModelScope.launch {
            try{
                val response = customHeroesRepository.getOrders("xd")
                _ordersState.value = Resource.success(response.body())
                val tempList: MutableList<OrderItemDTO> = emptyList<OrderItemDTO>().toMutableList()
                val orderIdList: MutableList<Int> = emptyList<Int>().toMutableList()
                for(item in response.body()!!) {
                    if(item.order.id !in orderIdList) {
                        orderIdList.add(item.order.id)
                    }
                }
                for(orderId in orderIdList) {
                    val tempOrderItemByOrderId: MutableList<OrderItemDTO> = emptyList<OrderItemDTO>().toMutableList()
                    for(item in response.body()!!) {
                        if(item.order.id == orderId) {
                            tempOrderItemByOrderId.add(item)
                        }
                    }
                    coolOrderList += listOf(tempOrderItemByOrderId)
                }

                orderList.value = response.body()!!
            }catch (e: Exception) {
                _meState.value = Resource.error(e.message ?: "Возникла ошибка")
            }
        }
    }
}