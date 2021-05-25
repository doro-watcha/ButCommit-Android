package com.goddoro.butcommit.presentation.signIn

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goddoro.butcommit.data.repository.UserRepository
import com.goddoro.butcommit.utils.AppPreference
import com.goddoro.butcommit.utils.Once
import kotlinx.coroutines.launch

class SignInViewModel(
    private val userRepository: UserRepository,
    private val appPreference: AppPreference
) : ViewModel() {


    val githubId: MutableLiveData<String> = MutableLiveData(appPreference.githubId)

    val onRegisterCompleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val isGithubIdChanged = MediatorLiveData<Boolean>().apply {
        addSource(githubId){
            this.value = (githubId.value ?: "") != appPreference.githubId
        }
    }

    val clickLoginButton: MutableLiveData<Once<Unit>> = MutableLiveData()

    val errorInvoked: MutableLiveData<Throwable> = MutableLiveData()


    fun register() {

        viewModelScope.launch {

            kotlin.runCatching {
                userRepository.register(githubId.value ?: "", appPreference.curFcmToken)
            }.onSuccess {
                onRegisterCompleted.value = true
            }.onFailure {
                errorInvoked.value = it
            }
        }
    }

    fun update() {

        viewModelScope.launch {

            kotlin.runCatching {
                userRepository.update(
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

    fun onClickLoginButton() {
        if (appPreference.githubId != "") clickLoginButton.value = Once(Unit)
        else register()

    }

}