package ru.wearemad.mad_koin_compose.screens.tabs_test.tab

import android.os.Bundle
import androidx.compose.runtime.Composable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.api.route.Route
import java.util.UUID

@Parcelize
class TabsSubRoute(
    private val text: String
) : Route(
    key = text,
    args = Bundle().apply {
        putString("data", text)
    }
) {

    @IgnoredOnParcel
    override val content: @Composable (id: String, args: Bundle?) -> Unit = { id, args ->
        TabsSubScreen(id, args)
    }
}