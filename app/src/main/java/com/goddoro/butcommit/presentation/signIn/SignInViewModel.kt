package com.goddoro.butcommit.presentation.signIn

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goddoro.butcommit.data.repository.UserRepository
import com.goddoro.butcommit.utils.AppPreference
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userRepository: UserRepository,
    private val appPreference: AppPreference
) : ViewModel() {


    val githubId: MutableLiveData<String> = MutableLiveData()

    val onRegisterCompleted: MutableLiveData<Boolean> = MutableLiveData()
    val errorInvoked: MutableLiveData<Throwable> = MutableLiveData()

    fun register() {

        viewModelScope.launch {

            kotlin.runCatching {

                if ( appPreference.githubId == "") userRepository.register(githubId.value ?: "", appPreference.curFcmToken)
                else userRepository.update(
                    username = githubId.value,
                    isDoing = true,
                    fcmToken = appPreference.curFcmToken
                )
            }.onSuccess {
                onRegisterCompleted.value = true
            }.onFailure {
                errorInvoked.value = it
            }

        }

    }

}