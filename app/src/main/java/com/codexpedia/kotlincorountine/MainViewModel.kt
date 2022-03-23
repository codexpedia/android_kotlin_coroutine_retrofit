package com.codexpedia.kotlincorountine

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.codexpedia.kotlincorountine.service.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

class MainViewModel : ViewModel() {
    private val reqData: RequestData = RequestData("your-github-username", "your-github-token-settings->developer-settings->personal-access-token", "kotlin")
    private val service = createGitHubService(reqData.username, reqData.password)

    val logMsg: LiveData<String> get() = _logMsg
    private val _logMsg = MutableLiveData<String>()

    val logMsgs: LiveData<MutableList<String>> get() = _logMsgs
    private val _logMsgs = MutableLiveData<MutableList<String>>()

    private var job: Job? = null

    private fun handleUpdateMsg(tag: String, msg: String) {
        Log.d(tag, msg)
        _logMsg.postValue(msg)
        if (_logMsgs.value === null) {
            _logMsgs.postValue(mutableListOf(msg))
        } else {
            _logMsgs.value!!.add(0,msg)
            _logMsgs.postValue(_logMsgs.value)
        }
    }

    fun onAction(actionType: ActionType) {
        _logMsgs.value = mutableListOf()

        when (actionType) {
            ActionType.BLOCKING -> {
                viewModelScope.launch(Dispatchers.IO) {
                    loadContributorsBlocking(service, reqData) {
                        handleUpdateMsg(ActionType.BLOCKING.toString(), it)
                    }
                }
            }
            ActionType.BACKGROUND -> {
                loadContributorsBackground(service, reqData) {
                    handleUpdateMsg(ActionType.BACKGROUND.toString(), it)
                }
            }
            ActionType.CALLBACKS -> {
                loadContributorsCallbacks(service, reqData) {
                    handleUpdateMsg(ActionType.CALLBACKS.toString(), it)
                }
            }
            ActionType.SUSPEND -> {
                job = viewModelScope.launch(Dispatchers.IO) {
                    loadContributorsSuspend(service, reqData) {
                        handleUpdateMsg(ActionType.SUSPEND.toString(), it)
                    }
                }
            }
            ActionType.CONCURRENT -> {
                job = viewModelScope.launch(Dispatchers.IO) {
                    loadContributorsConcurrent(service, reqData) {
                        handleUpdateMsg(ActionType.CONCURRENT.toString(), it)
                    }
                }
            }
            ActionType.NOT_CANCELABLE -> {
                job = viewModelScope.launch(Dispatchers.IO) {
                    loadContributorsNotCancellable(service, reqData) {
                        handleUpdateMsg(ActionType.NOT_CANCELABLE.toString(), it)
                    }
                }
            }
            ActionType.PROGRESS -> {
                job = viewModelScope.launch(Dispatchers.IO) {
                    loadContributorsProgress(service, reqData) {
                        handleUpdateMsg(ActionType.PROGRESS.toString(), it)
                    }
                }
            }
            ActionType.CHANNELS -> {
                job = viewModelScope.launch(Dispatchers.IO) {
                    loadContributorsChannels(service, reqData) {
                        handleUpdateMsg(ActionType.CHANNELS.toString(), it)
                    }
                }
            }
            ActionType.CANCEL -> {
                job?.cancel()
            }
            else -> {
                print("Not a valid action type")
            }
        }
    }

}