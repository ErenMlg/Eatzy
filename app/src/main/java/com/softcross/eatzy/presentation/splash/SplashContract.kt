package com.softcross.eatzy.presentation.splash

object SplashContract {
    sealed class UiEffect {
        data object NavigateToMainScreen : UiEffect()
        data object NavigateToIntroduceScreen : UiEffect()
    }
}