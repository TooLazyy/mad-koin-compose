package ru.wearemad.mad_koin_compose.screens.main

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.Saver
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.staticCompositionLocalOf
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.wearemad.mad_compose_navigation.navigator.base.Navigator
import ru.wearemad.mad_compose_navigation.router.holder.RouterNavigatorHolder
import ru.wearemad.mad_compose_navigation.router.provider.DefaultRouterProvidersHolder
import ru.wearemad.mad_compose_navigation.utils.createAppNavigator
import ru.wearemad.mad_compose_navigation.utils.createAppNestedNavigator
import ru.wearemad.mad_core_compose.result_handler.RequestResultStore
import ru.wearemad.mad_core_compose.utils.noLocalProvidedFor
import ru.wearemad.mad_koin_compose.content.ActivityContentWithProviders
import ru.wearemad.mad_koin_compose.content.RenderRouteWithSaveableStateHolder
import ru.wearemad.mad_koin_compose.router.rememberNavigator
import ru.wearemad.mad_koin_compose.theme.ComposeNavigationTheme

val LocalRootNavigator = staticCompositionLocalOf<Navigator> {
    noLocalProvidedFor("LocalRootNavigator")
}

class MainActivity : AppCompatActivity() {

    private val vm: MainActivityVm by viewModel()

    private val routerProvidersHolder: DefaultRouterProvidersHolder by inject()
    private val navigatorHolder: RouterNavigatorHolder by inject()
    private val requestResultStore: RequestResultStore by inject()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        vm.onCreated()
        val requestResultStoreFactory = {
            requestResultStore
        }

        setContent {
            ActivityContentWithProviders(
                routerProvidersHolder = routerProvidersHolder
            ) {
                val rootNavigator = rememberNavigator(
                    navigatorHolder = navigatorHolder,
                    onBackPressedDispatcher = onBackPressedDispatcher,
                    navigatorFactory = { createAppNavigator() },
                    nestedNavigatorFactory = { createAppNestedNavigator() },
                )

                val requestResultStore = rememberRequestResultStore(requestResultStoreFactory)

                ComposeNavigationTheme {
                    CompositionLocalProvider(
                        LocalRootNavigator provides rootNavigator
                    ) {
                        RootScreen(savedInstanceState)
                    }
                }
            }
        }
    }

    @Composable
    private fun RootScreen(savedInstanceState: Bundle?) {
        val rootNavigator = LocalRootNavigator.current
        val rootNavigatorState = rootNavigator.navigatorStateFlow.collectAsState()
        val currentRoute = rootNavigatorState.value.currentRoute
        val dialogs = rootNavigatorState.value.currentDialogsStack

        if (currentRoute != null) {
            Crossfade(
                targetState = currentRoute,
                animationSpec = tween(
                    durationMillis = 700
                ),
            ) {
                RenderRouteWithSaveableStateHolder(it)
            }
        }

        dialogs.forEach {
            RenderRouteWithSaveableStateHolder(it)
        }
    }

    @Composable
    private fun rememberRequestResultStore(
        factory: () -> RequestResultStore
    ): RequestResultStore = rememberSaveable(
        Unit,
        saver = createRequestResultStoreSaver(factory = factory),
        init = factory
    )

    private fun createRequestResultStoreSaver(
        factory: () -> RequestResultStore,
    ): Saver<RequestResultStore, Bundle> = Saver(
        save = {
            it.saveState()
        },
        restore = {
            factory()
                .apply { restoreState(it) }
        }
    )
}