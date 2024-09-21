package com.softcross.eatzy.presentation.splash

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.softcross.eatzy.common.EatzySingleton
import com.softcross.eatzy.common.EatzySingleton.currentUser
import com.softcross.eatzy.domain.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(
    private val firebaseRepository: FirebaseRepository
) : ViewModel() {

    private val _uiEffect by lazy { Channel<SplashContract.UiEffect>() }
    val uiEffect: Flow<SplashContract.UiEffect> by lazy { _uiEffect.receiveAsFlow() }

    init {
        isUserLoggedIn()
    }

    private fun isUserLoggedIn() = viewModelScope.launch {
        if (firebaseRepository.checkLoggedUser()) {
            try {
                currentUser = firebaseRepository.getUserDetailFromFirestore()
                EatzySingleton.currentUsername = currentUser?.username ?: ""
                delay(2000)
                emitUiEffect(SplashContract.UiEffect.NavigateToMainScreen)
            } catch (e: Exception) {
                emitUiEffect(SplashContract.UiEffect.NavigateToIntroduceScreen)
            }
        }else{
            delay(2000)
            emitUiEffect(SplashContract.UiEffect.NavigateToIntroduceScreen)
        }
    }


    private suspend fun emitUiEffect(uiEffect: SplashContract.UiEffect) {
        _uiEffect.send(uiEffect)
    }

}