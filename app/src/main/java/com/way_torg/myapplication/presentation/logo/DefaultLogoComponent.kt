package com.way_torg.myapplication.presentation.logo

import com.arkivanov.decompose.ComponentContext
import dagger.assisted.Assisted
import dagger.assisted.AssistedFactory
import dagger.assisted.AssistedInject

class DefaultLogoComponent @AssistedInject constructor(
    @Assisted("componentContext") private val componentContext: ComponentContext,
    @Assisted("navigate") private val navigate: () -> Unit
) : LogoComponent, ComponentContext by componentContext {
    override fun navigateToHomeScreen() {
        navigate.invoke()
    }

    @AssistedFactory
    interface Factory {
        fun create(
            @Assisted("componentContext") componentContext: ComponentContext,
            @Assisted("navigate") navigate: () -> Unit
        ): DefaultLogoComponent
    }
}