package com.goddoro.butcommit

import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.goddoro.butcommit.data.model.Commit
import com.goddoro.butcommit.data.repository.GithubRepository
import com.goddoro.butcommit.utils.AppPreference
import com.goddoro.butcommit.utils.Once
import kotlinx.coroutines.launch


/**
 * created By DORO 5/21/21
 */

class MainViewModel(
    private val githubRepository: GithubRepository,
    private val appPreference: AppPreference
) : ViewModel() {

    val commits: MutableLiveData<List<Commit>> = MutableLiveData()
    val grassCount = MediatorLiveData<Int>().apply {


        addSource(commits){
            if (it.isNotEmpty()) {
                this.value = it.filter { commit ->
                    commit.count > 0
                }.size
            }
        }
    }

    val clickSignIn : MutableLiveData<Once<Unit>> = MutableLiveData()
    val clickScreenShot : MutableLiveData<Once<Unit>> = MutableLiveData()
    val clickShare : MutableLiveData<Once<Unit>> = MutableLiveData()
    val onLoadCompleted: MutableLiveData<Boolean> = MutableLiveData(false)

    val errorInvoked : MutableLiveData<Throwable> = MutableLiveData()


    private fun listCommits() {

        if ( appPreference.githubId == "" || appPreference.startDate == "") return

        viewModelScope.launch {

            kotlin.runCatching {
                githubRepository.listCommits(appPreference.githubId, appPreference.startDate )
            }.onSuccess {
                commits.value = it
                onLoadCompleted.value = true
            }.onFailure {
                onLoadCompleted.value = true
                errorInvoked.value = it
            }
        }
    }

    fun refresh() {

        listCommits()
    }

    fun onClickSignIn() {

        clickSignIn.value = Once(Unit)
    }

    fun onClickScreenShot() {
        clickScreenShot.value = Once(Unit)
    }

    fun onClickShare() {
        clickShare.value = Once(Unit)
    }

}