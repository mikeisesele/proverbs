package com.michael.proverbs.core.di

import com.michael.proverbs.feature.proverbs.data.ProverbsRepository
import dagger.hilt.EntryPoint
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent

@EntryPoint
@InstallIn(SingletonComponent::class)
interface DailyAlarmReceiverEntryPoint {
    fun proverbsRepository(): ProverbsRepository
}