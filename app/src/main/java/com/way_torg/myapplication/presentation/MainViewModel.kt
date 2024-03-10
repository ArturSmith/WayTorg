package com.way_torg.myapplication.presentation

import androidx.lifecycle.ViewModel
import com.way_torg.myapplication.domain.use_case.GetLocaleUseCase
import javax.inject.Inject

class MainViewModel @Inject constructor(
    getLocaleUseCase: GetLocaleUseCase
) : ViewModel() {

    val getCurrentLocale = getLocaleUseCase()
}