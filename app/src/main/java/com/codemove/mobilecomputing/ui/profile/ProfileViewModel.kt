package com.codemave.mobilecomputing.ui.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codemave.mobilecomputing.Graph
import com.codemave.mobilecomputing.data.entity.Category
import com.codemave.mobilecomputing.data.entity.LoginInfo
import com.codemave.mobilecomputing.data.repository.LoginInfoRepository
import com.codemave.mobilecomputing.ui.home.HomeViewState
import com.codemave.mobilecomputing.ui.task.TaskViewState
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

class ProfileViewModel(
    private val loginInfoRepository: LoginInfoRepository = Graph.loginInfoRepository,

) : ViewModel() {
    private val _selectedLogin = MutableStateFlow(LoginInfo(username="bgmrsln",password="123456", currentLocationY = null, currentLocationX = null))
    private val _state = MutableStateFlow(ProfileViewState(selectedLoginInfo = _selectedLogin.value))


    val state: StateFlow<ProfileViewState>
        get() = _state

    fun onLoginSelected(login: LoginInfo) {
        _selectedLogin.value = login
    }
    suspend fun getPasswordWithUsername(username: String): LoginInfo?{
        return loginInfoRepository.getPasswordWithUsername(username)
    }

}
data class ProfileViewState(
    val selectedLoginInfo: LoginInfo
)