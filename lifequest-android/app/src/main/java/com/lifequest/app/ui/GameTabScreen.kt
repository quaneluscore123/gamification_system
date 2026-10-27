package com.lifequest.app.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.ui.components.CharacterCard
import com.lifequest.app.ui.components.MentorAstra

/** Khung chung cho các tab game: lời Mentor, thẻ nhân vật, rồi tới nội dung riêng của tab. */
@Composable
fun GameTabScreen(
    game: GameState,
    modifier: Modifier = Modifier,
    content: LazyListScope.() -> Unit,
) {
    LazyColumn(modifier = modifier, contentPadding = PaddingValues(16.dp)) {
        item(key = "mentor") { MentorAstra(game.player.level) }
        item(key = "character") { CharacterCard(game.player, Modifier.padding(vertical = 12.dp)) }
        content()
    }
}
