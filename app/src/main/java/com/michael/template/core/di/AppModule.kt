package com.michael.template.core.di

import android.app.Application
import android.content.Context
import com.michael.template.core.base.providers.StringProvider
import com.michael.template.core.common.StringProviderImpl
import com.michael.template.core.ui.util.ErrorHandler
import com.michael.template.core.ui.util.ErrorHandlerImpl
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import java.util.*

@Module
@InstallIn(SingletonComponent::class)
object AppModule {
    @Provides
    fun provideApplicationContext(application: Application): Context =
        application.applicationContext

    @Provides
    fun providesResourceProvider(resourceProvider: StringProviderImpl): StringProvider =
        resourceProvider

    @Provides
    fun provideErrorHandler(errorHandler: ErrorHandlerImpl): ErrorHandler = errorHandler
}
