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


    val githubId: MutableLiveData<String> = MutableLiveData(appPreference.githubId)

    val onRegisterCompleted: MutableLiveData<Boolean> = MutableLiveData(false)
    val onUpdateCompleted : MutableLiveData<Boolean> = MutableLiveData(false)
    val errorInvoked: MutableLiveData<Throwable> = MutableLiveData()

    fun register() {

        viewModelScope.launch {

            if ( appPreference.githubId == "") {

                kotlin.runCatching {
                    userRepository.register(githubId.value ?: "", appPreference.curFcmToken)
                }.onSuccess {
                    onRegisterCompleted.value = true
                }.onFailure {
                    errorInvoked.value = it
                }
            }
            else {

                kotlin.runCatching {
                    userRepository.update(
                        username = githubId.value,
                        isDoing = true,
                        fcmToken = appPreference.curFcmToken
                    )
                }.onSuccess {
                    onUpdateCompleted.value = true
                }.onFailure {
                    errorInvoked.value = it
                }
            }
        }

    }

}