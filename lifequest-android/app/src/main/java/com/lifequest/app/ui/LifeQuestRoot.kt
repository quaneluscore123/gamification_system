package com.lifequest.app.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
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
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.compose.LifecycleEventEffect
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.lifequest.app.domain.game.GameEvent
import com.lifequest.app.domain.game.GameState
import com.lifequest.app.ui.components.DevConsoleDialog
import com.lifequest.app.ui.inventory.inventorySection
import com.lifequest.app.ui.navigation.DungeonsRoute
import com.lifequest.app.ui.navigation.InventoryRoute
import com.lifequest.app.ui.navigation.QuestsRoute
import com.lifequest.app.ui.navigation.ShopRoute
import com.lifequest.app.ui.navigation.StatsRoute
import com.lifequest.app.ui.navigation.TopLevelDestination
import com.lifequest.app.ui.quest.questSection
import com.lifequest.app.ui.shop.shopSection
import com.lifequest.app.ui.stats.StatsScreen
import com.lifequest.app.ui.theme.LqColors
import com.lifequest.app.ui.world.worldSection

/**
 * GameViewModel được lấy ở đây, NGOÀI NavHost, nên nó gắn với Activity và được
 * mọi màn hình dùng chung. ViewModel lấy bên trong composable<Route> thì chỉ sống
 * cùng màn hình đó.
 */
@Composable
fun LifeQuestRoot(viewModel: GameViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(viewModel) {
        viewModel.events.collect { snackbarHostState.showSnackbar(it.toMessage()) }
    }

    // Mỗi lần người dùng quay lại app, kiểm tra xem đã sang ngày mới chưa
    LifecycleEventEffect(Lifecycle.Event.ON_RESUME) { viewModel.onAppResumed() }

    when (val state = uiState) {
        GameUiState.Loading -> Box(Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator()
        }
        is GameUiState.Ready -> GameScaffold(state.game, viewModel, snackbarHostState)
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun GameScaffold(game: GameState, viewModel: GameViewModel, snackbarHostState: SnackbarHostState) {
    val navController = rememberNavController()
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
        bottomBar = { BottomBar(navController) },
    ) { innerPadding ->
        NavHost(
            navController = navController,
            startDestination = DungeonsRoute,
            modifier = Modifier.padding(innerPadding),
        ) {
            composable<DungeonsRoute> {
                GameTabScreen(game) { worldSection(game, onFight = viewModel::completeQuest, onNextWorld = viewModel::nextWorld) }
            }
            composable<QuestsRoute> {
                GameTabScreen(game) { questSection(game, onComplete = viewModel::completeQuest) }
            }
            composable<InventoryRoute> {
                GameTabScreen(game) { inventorySection(game, onUse = viewModel::useItem) }
            }
            composable<ShopRoute> {
                GameTabScreen(game) { shopSection(game, onBuy = viewModel::buyItem) }
            }
            // StatsViewModel được tạo bên trong composable<StatsRoute> nên chỉ sống cùng màn hình này
            composable<StatsRoute> { StatsScreen(game.player) }
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

@Composable
private fun BottomBar(navController: NavHostController) {
    val currentDestination = navController.currentBackStackEntryAsState().value?.destination

    NavigationBar {
        TopLevelDestination.entries.forEach { dest ->
            NavigationBarItem(
                selected = currentDestination?.hierarchy?.any { it.hasRoute(dest.route::class) } == true,
                onClick = {
                    navController.navigate(dest.route) {
                        // Chuẩn cho bottom bar: không chồng nhiều bản sao màn hình lên back stack,
                        // và giữ lại vị trí cuộn của từng tab khi chuyển qua lại
                        popUpTo(navController.graph.findStartDestination().id) { saveState = true }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                icon = { Text(dest.icon, style = MaterialTheme.typography.titleLarge) },
                label = { Text(dest.label) },
            )
        }
    }
}

private fun GameEvent.toMessage(): String = when (this) {
    is GameEvent.LevelUp -> "⬆️ Lên cấp $level — $title!"
    is GameEvent.BossDefeated -> "🎉 Đã hạ gục Boss $bossName!"
    is GameEvent.ItemBought -> "Mua $itemName thành công! Đã chuyển vào Hành Trang."
    is GameEvent.ItemUsed -> "Đã sử dụng $itemName. Hiệu ứng đã được kích hoạt."
    is GameEvent.NewDay ->
        if (weeklyReset) "🌅 Tuần mới! Quest Daily và Weekly đã được làm mới." else "🌅 Ngày mới! Quest Daily đã được làm mới."
    is GameEvent.StreakExtended -> "🔥 Chuỗi $days ngày liên tiếp!"
    GameEvent.NotEnoughGold -> "Bạn không đủ Vàng!"
    GameEvent.ActionFailed -> "Không thể thực hiện lúc này."
}
