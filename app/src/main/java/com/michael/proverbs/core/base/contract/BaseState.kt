package com.michael.proverbs.core.base.contract

import com.michael.proverbs.core.base.model.MessageState

/**
 * Common states for any view state: All can load and have errors.
 * This BaseState is implemented by ViewStates used by the ViewModel.
 *
 */
interface BaseState {
    val isLoading: Boolean
    val errorState: MessageState?
}
