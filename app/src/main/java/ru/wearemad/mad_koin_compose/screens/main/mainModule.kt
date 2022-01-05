package ru.wearemad.mad_koin_compose.screens.main

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val mainModule = module {

    viewModel {
        MainActivityVm(get(), get(), get())
    }
}