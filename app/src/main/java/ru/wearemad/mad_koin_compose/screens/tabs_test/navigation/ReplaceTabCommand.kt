package ru.wearemad.mad_koin_compose.screens.tabs_test.navigation

import ru.wearemad.mad_compose_navigation.api.command.Command
import ru.wearemad.mad_compose_navigation.api.command.CommandInput
import ru.wearemad.mad_compose_navigation.api.command.CommandOutput
import ru.wearemad.mad_compose_navigation.api.route.Route
import ru.wearemad.mad_compose_navigation.impl.router.Router

class ReplaceTabCommand(
    private val route: Route
) : Command {

    override fun execute(
        input: CommandInput
    ): CommandOutput {

        val currentTabPosition = input.screensStack.indexOfFirst {
            it.screenKey == route.screenKey
        }
        val newRoutesList = input.screensStack.toMutableList()
        when (currentTabPosition) {
            -1 -> {
                //tab not present. add
                newRoutesList.add(route)
            }
            newRoutesList.lastIndex -> {
                //current tab is last already
            }
            else -> {
                val removed = newRoutesList.removeAt(currentTabPosition)
                newRoutesList.add(removed)
            }
        }
        return CommandOutput(
            newRoutesList,
            input.dialogsStack,
            withAnimation = false,
        )
    }
}

suspend fun Router.replaceTab(route: Route) {
    executeCommands(ReplaceTabCommand(route))
}