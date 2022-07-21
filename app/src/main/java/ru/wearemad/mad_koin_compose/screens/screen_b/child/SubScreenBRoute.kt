package ru.wearemad.mad_koin_compose.screens.screen_b.child

import android.os.Bundle
import androidx.compose.runtime.Composable
import kotlinx.parcelize.IgnoredOnParcel
import kotlinx.parcelize.Parcelize
import ru.wearemad.mad_compose_navigation.api.route.Route
import java.util.UUID

val colorsList = listOf(
    "Color.Black",
    "Color.Red",
    "Color.Green",
    "Color.Yellow",
    "Color.Cyan",
    "Color.Magenta",
    "Color.Blue",
    "Color.White",
    "Color.LightGray",
)

@Parcelize
class SubScreenBRoute(
    private val parentId: String,
    private val color: String = colorsList.random(),
    override val key: String = UUID.randomUUID().toString()
) : Route(
    args = Bundle().apply {
        putString("data", parentId)
        putString("data2", color)
    }
) {

    @IgnoredOnParcel
    override val content: @Composable (id: String, args: Bundle?) -> Unit = { id, args ->
        SubScreenBContent(id, args)
    }
}