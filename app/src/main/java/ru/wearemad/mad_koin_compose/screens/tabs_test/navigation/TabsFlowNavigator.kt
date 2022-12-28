package ru.wearemad.mad_koin_compose.screens.tabs_test.navigation

import android.os.Bundle
import ru.wearemad.mad_compose_navigation.api.navigator.data.NestedNavigatorParams
import ru.wearemad.mad_compose_navigation.api.navigator.navigator_factory.NavigatorFactory
import ru.wearemad.mad_compose_navigation.api.restorer.NavigatorRestorerParams
import ru.wearemad.mad_compose_navigation.impl.navigator.BaseNavigator
import ru.wearemad.mad_compose_navigation.impl.navigator.data.NestedStackChanged

class TabsFlowNavigator(
    params: NestedNavigatorParams,
) : BaseNavigator(params) {

    //Tab navigator is suited for tabs only. so canGoBack is possible for dialog routes
    override val canGoBack: Boolean
        get() = dialogRoutesList.isNotEmpty()

    override suspend fun afterStackChanged() {
        params.outEventsChannel?.send(NestedStackChanged)
    }

    override fun restoreState(
        state: Bundle,
        factory: NavigatorFactory
    ) {
        val result = params.restorer.restore(
            NavigatorRestorerParams(
                state,
                params.screenId,
                factory,
                params.inEventsChannel
            )
        )
        routesList = result.currentStack
        dialogRoutesList = result.currentDialogsStack
        setNavigators(result.nestedNavigators)
        updateNavigatorState(createNavigatorState())
    }
}