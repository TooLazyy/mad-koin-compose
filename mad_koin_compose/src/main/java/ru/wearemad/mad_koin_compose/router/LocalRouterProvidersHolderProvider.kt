package ru.wearemad.mad_koin_compose.router

import androidx.compose.runtime.staticCompositionLocalOf
import ru.wearemad.mad_compose_navigation.api.router.RouterProvidersHolder
import ru.wearemad.mad_core_compose.utils.noLocalProvidedFor

val LocalRouterProvidersHolderProvider = staticCompositionLocalOf<RouterProvidersHolder<*>> {
    noLocalProvidedFor("LocalRouterProvidersHolderProvider")
}