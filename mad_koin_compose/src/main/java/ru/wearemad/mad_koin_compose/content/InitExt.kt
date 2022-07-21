package ru.wearemad.mad_koin_compose.content

import androidx.activity.ComponentActivity
import androidx.activity.OnBackPressedDispatcher
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import ru.wearemad.mad_compose_navigation.api.router.RouterProvidersHolder
import ru.wearemad.mad_core_compose.vm.vm_store_holder.ComposeScreenViewModelStoreHolder
import ru.wearemad.mad_core_compose.vm.vm_store_holder.LocalComposeScreenViewModelStoreHolder
import ru.wearemad.mad_core_compose.vm.vm_store_holder.getComposeViewModelStoreHolder
import ru.wearemad.mad_koin_compose.router.LocalRouterProvidersHolderProvider
import ru.wearemad.mad_koin_compose.router.back_press.LocalRootBackPressedDispatcher
import ru.wearemad.mad_koin_compose.scopes.LocalOpenedScopesHolder
import ru.wearemad.mad_koin_compose.scopes.OpenedScopesHolder
import ru.wearemad.mad_koin_compose.scopes.rememberOpenedScopesHolder

@Composable
fun ContentWithProviders(
    composeScreenViewModelStoreHolder: ComposeScreenViewModelStoreHolder = getComposeViewModelStoreHolder(),
    routerProvidersHolder: RouterProvidersHolder<*>,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalComposeScreenViewModelStoreHolder provides composeScreenViewModelStoreHolder,
        LocalRouterProvidersHolderProvider provides routerProvidersHolder
    ) {
        content()
    }
}

@Composable
fun ComponentActivity.ActivityContentWithProviders(
    composeScreenViewModelStoreHolder: ComposeScreenViewModelStoreHolder = getComposeViewModelStoreHolder(),
    backPressedDispatcher: OnBackPressedDispatcher = onBackPressedDispatcher,
    openedScopesHolder: OpenedScopesHolder = rememberOpenedScopesHolder(),
    routerProvidersHolder: RouterProvidersHolder<*>,
    content: @Composable () -> Unit
) {
    CompositionLocalProvider(
        LocalRootBackPressedDispatcher provides backPressedDispatcher,
        LocalComposeScreenViewModelStoreHolder provides composeScreenViewModelStoreHolder,
        LocalRouterProvidersHolderProvider provides routerProvidersHolder,
        LocalOpenedScopesHolder provides openedScopesHolder
    ) {
        content()
    }
}