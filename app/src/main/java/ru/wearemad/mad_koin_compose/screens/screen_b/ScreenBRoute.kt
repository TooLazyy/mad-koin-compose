package ru.wearemad.mad_koin_compose.screens.screen_b

import android.os.Bundle
import androidx.compose.runtime.Composable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.api.route.Route
import java.util.UUID

@Parcelize
class ScreenBRoute(
    override val key: String = UUID.randomUUID().toString()
) : Route() {

    @IgnoredOnParcel
    override val content: @Composable (id: String, args: Bundle?) -> Unit = { id, args ->
        ScreenBContent(id, args)
    }
}