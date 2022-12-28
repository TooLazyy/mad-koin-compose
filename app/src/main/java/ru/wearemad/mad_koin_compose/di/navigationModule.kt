package ru.wearemad.mad_koin_compose.di

import org.koin.dsl.module
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.api.router.RouterProvider
import ru.wearemad.mad_compose_navigation.impl.navigator.AppNestedNavigator
import ru.wearemad.mad_compose_navigation.impl.navigator.data.DefaultNestedNavigatorParams
import ru.wearemad.mad_compose_navigation.impl.navigator.navigator_factory.DefaultNavigatorFactory
import ru.wearemad.mad_compose_navigation.impl.router.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.impl.router.Router
import ru.wearemad.mad_compose_navigation.utils.defaultMainNavigatorFactory
import ru.wearemad.mad_compose_navigation.utils.defaultNestedNavigatorFactory
import ru.wearemad.mad_koin_compose.screens.tabs_test.navigation.tabsFlowNavigatorFactory

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
            appNestedNavigatorFactory()
        )
    }
}

private fun appNestedNavigatorFactory(
    canGoBack: Boolean = false
): NestedNavigatorFactory = { screenId, parentInEventChannel ->
    if (screenId.contains("TabsMainRoute")) {
        tabsFlowNavigatorFactory(canGoBack).invoke(screenId, parentInEventChannel)
    } else {
        AppNestedNavigator(
            DefaultNestedNavigatorParams(
                canGoBack = canGoBack,
                screenId = screenId,
                outEventsChannel = parentInEventChannel
            )
        )
    }
}