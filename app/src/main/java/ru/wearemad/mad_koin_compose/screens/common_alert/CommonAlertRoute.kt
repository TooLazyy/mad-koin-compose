package ru.wearemad.mad_koin_compose.screens.common_alert

import android.os.Bundle
import androidx.compose.runtime.Composable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.route.Route

@Parcelize
class CommonAlertRoute(
    private val requestKey: String
) : Route(
    args = Bundle().apply {
        putString("data", requestKey)
    }
) {

    @IgnoredOnParcel
    override val content: @Composable (id: String, args: Bundle?) -> Unit = { id, args ->
        CommonAlertContent(id, args)
    }
}