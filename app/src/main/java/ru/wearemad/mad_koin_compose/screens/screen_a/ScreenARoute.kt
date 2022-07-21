package ru.wearemad.mad_koin_compose.screens.screen_a

import android.os.Bundle
import androidx.compose.runtime.Composable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.api.route.Route

@Parcelize
class ScreenARoute : Route() {

    @IgnoredOnParcel
    override val content: @Composable (id: String, args: Bundle?) -> Unit = { id, args ->
        ScreenAContent(id, args)
    }
}