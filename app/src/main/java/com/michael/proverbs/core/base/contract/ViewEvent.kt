package com.michael.proverbs.core.base.contract

import com.michael.proverbs.core.base.model.MessageState

sealed class ViewEvent {
    data class Navigate(val target: NavigationTarget) : ViewEvent()
    data class DisplayMessage(val message: MessageState) : ViewEvent()
    data class Effect(val effect: SideEffect) : ViewEvent()
}
