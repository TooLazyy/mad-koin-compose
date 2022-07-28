package ru.wearemad.mad_koin_compose.di

import org.koin.dsl.module
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.router.RouterProvider
import ru.wearemad.mad_compose_navigation.impl.navigator.navigator_factory.DefaultNavigatorFactory
import ru.wearemad.mad_compose_navigation.impl.router.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.utils.defaultMainNavigatorFactory
import ru.wearemad.mad_compose_navigation.utils.defaultNestedNavigatorFactory

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

    single<NavigatorFactory> {
        DefaultNavigatorFactory(
            defaultMainNavigatorFactory(),
            defaultNestedNavigatorFactory()
        )
    }
}