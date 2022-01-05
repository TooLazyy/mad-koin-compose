package ru.wearemad.mad_koin_compose.di

import org.koin.dsl.module
import ru.wearemad.mad_compose_navigation.router.Router
import ru.wearemad.mad_compose_navigation.router.provider.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.router.provider.RouterProvider

/**
 * root router module
 */
val navigationModule = module {

    single {
        RouterProvider.create()
    }

    single {
        get<RouterProvider<Router>>().getHolder()
    }

    single {
        get<RouterProvider<Router>>().router
    }

    single { DefaultRouterProvidersHolder() }
}