package ru.wearemad.mad_koin_compose.screens.tabs_test.navigation

import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NestedNavigatorFactory
import ru.wearemad.mad_compose_navigation.impl.navigator.data.DefaultNestedNavigatorParams
import ru.wearemad.mad_koin_compose.screens.tabs_test.navigation.TabsFlowNavigator

fun tabsFlowNavigatorFactory(
    canGoBack: Boolean = false
): NestedNavigatorFactory = { instanceId, parentInEventChannel ->
    TabsFlowNavigator(
        DefaultNestedNavigatorParams(
            canGoBack = canGoBack,
            instanceId = instanceId,
            outEventsChannel = parentInEventChannel
        )
    )
}