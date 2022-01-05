package ru.wearemad.mad_koin_compose.screens.splash

import android.os.Bundle
import androidx.compose.runtime.Composable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.route.Route

@Parcelize
class SplashRoute : Route() {

    @IgnoredOnParcel
    override val content: @Composable (id: String, args: Bundle?) -> Unit = { id, args ->
        SplashContent(id, args)
    }
}