package com.lifequest.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.lifequest.app.domain.game.GameEvent
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.ui.components.CharacterCard
import com.lifequest.app.ui.components.DevConsoleDialog
import com.lifequest.app.ui.components.MentorAstra
import com.lifequest.app.ui.inventory.inventorySection
import com.lifequest.app.ui.quest.questSection
import com.lifequest.app.ui.shop.shopSection
import com.lifequest.app.ui.theme.LqColors
import com.lifequest.app.ui.world.worldSection

enum class Tab(val icon: String, val label: String) {
    WORLD("🏰", "Dungeons"),
    QUESTS("📜", "Quests"),
    INVENTORY("🎒", "Hành Trang"),
    SHOP("🏪", "Cửa Hàng"),
}

@Composable
fun LifeQuestRoot(viewModel: GameViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { snackbarHostState.showSnackbar(it.toMessage()) }
    }

    when (val state = uiState) {
        GameUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is GameUiState.Ready -> GameScreen(state.game, viewModel, snackbarHostState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScreen(game: GameState, viewModel: GameViewModel, snackbarHostState: SnackbarHostState) {
    var activeTab by rememberSaveable { mutableStateOf(Tab.WORLD) }
    var showDevConsole by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = MaterialTheme.colorScheme.background,
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text("LifeQuest", color = LqColors.Primary, style = MaterialTheme.typography.titleLarge)
                        Text("The Backend Journey", color = LqColors.TextMuted, style = MaterialTheme.typography.labelMedium)
                    }
                },
                actions = { TextButton(onClick = { showDevConsole = true }) { Text("🛠️ Dev") } },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = MaterialTheme.colorScheme.background),
            )
        },
        bottomBar = {
            NavigationBar {
                Tab.entries.forEach { tab ->
                    NavigationBarItem(
                        selected = tab == activeTab,
                        onClick = { activeTab = tab },
                        icon = { Text(tab.icon, style = MaterialTheme.typography.titleLarge) },
                        label = { Text(tab.label) },
                    )
                }
            }
        },
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier.padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
        ) {
            item { MentorAstra(game.player.level) }
            item { CharacterCard(game.player, Modifier.padding(vertical = 12.dp)) }
            tabContent(activeTab, game, viewModel)
        }
    }

    if (showDevConsole) {
        DevConsoleDialog(
            onDismiss = { showDevConsole = false },
            onCheatXp = viewModel::cheatXp,
            onCheatGold = viewModel::cheatGold,
            onKillBoss = viewModel::killBoss,
            onReset = viewModel::resetSave,
        )
    }
}

private fun LazyListScope.tabContent(tab: Tab, game: GameState, viewModel: GameViewModel) = when (tab) {
    Tab.WORLD -> worldSection(game, onFight = viewModel::completeQuest, onNextWorld = viewModel::nextWorld)
    Tab.QUESTS -> questSection(game, onComplete = viewModel::completeQuest)
    Tab.INVENTORY -> inventorySection(game, onUse = viewModel::useItem)
    Tab.SHOP -> shopSection(game, onBuy = viewModel::buyItem)
}

private fun GameEvent.toMessage(): String = when (this) {
    is GameEvent.LevelUp -> "⬆️ Lên cấp $level — $title!"
    is GameEvent.BossDefeated -> "🎉 Đã hạ gục Boss $bossName!"
    is GameEvent.ItemBought -> "Mua $itemName thành công! Đã chuyển vào Hành Trang."
    is GameEvent.ItemUsed -> "Đã sử dụng $itemName. Hiệu ứng đã được kích hoạt."
    GameEvent.NotEnoughGold -> "Bạn không đủ Vàng!"
    GameEvent.ActionFailed -> "Không thể thực hiện lúc này."
}
