package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import com.example.ui.screens.CodexScreen
import com.example.ui.screens.CreateRoomScreen
import com.example.ui.screens.GameplayScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.JoinRoomScreen
import com.example.ui.screens.LobbyScreen
import com.example.ui.screens.ProfileScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.theme.BackgroundDark
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.AppScreen
import com.example.ui.viewmodel.GameViewModel

class MainActivity : ComponentActivity() {

    private val viewModel: GameViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MyApplicationTheme {
                val uiState by viewModel.uiState.collectAsState()
                val snackbarHostState = remember { SnackbarHostState() }

                LaunchedEffect(uiState.toastMessage) {
                    val msg = uiState.toastMessage
                    if (msg != null) {
                        snackbarHostState.showSnackbar(msg)
                        viewModel.clearToast()
                    }
                }

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    containerColor = BackgroundDark,
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    BoxWithPadding(modifier = Modifier.padding(innerPadding)) {
                        when (uiState.currentScreen) {
                            AppScreen.HOME -> HomeScreen(
                                profile = uiState.userProfile,
                                onNavigate = { viewModel.navigateTo(it) },
                                onQuickMatch = { viewModel.startQuickMatch() },
                                onPracticeMode = { viewModel.startPracticeMode() }
                            )
                            AppScreen.CREATE_ROOM -> CreateRoomScreen(
                                onBack = { viewModel.navigateTo(AppScreen.HOME) },
                                onCreateRoom = { settings -> viewModel.createRoom(settings) }
                            )
                            AppScreen.JOIN_ROOM -> JoinRoomScreen(
                                onBack = { viewModel.navigateTo(AppScreen.HOME) },
                                onJoinCode = { code -> viewModel.joinRoomByCode(code) }
                            )
                            AppScreen.LOBBY -> LobbyScreen(
                                gameState = uiState.gameState,
                                currentUserId = uiState.userProfile.id,
                                onStartGame = { viewModel.startGame() },
                                onToggleReady = { viewModel.toggleReady() },
                                onAddBot = { viewModel.addBot() },
                                onRemovePlayer = { playerId -> viewModel.removePlayer(playerId) },
                                onLeaveRoom = { viewModel.navigateTo(AppScreen.HOME) },
                                onShowToast = { msg -> viewModel.showToast(msg) }
                            )
                            AppScreen.GAMEPLAY -> GameplayScreen(
                                uiState = uiState,
                                onClueInputChanged = { viewModel.onClueInputChanged(it) },
                                onSubmitClue = { viewModel.submitMyClue() },
                                onSelectSuspect = { viewModel.onSelectSuspect(it) },
                                onSubmitVote = { viewModel.submitMyVote() },
                                onImposterGuessInputChanged = { viewModel.onImposterGuessInputChanged(it) },
                                onSubmitImposterGuess = { viewModel.submitImposterGuess() },
                                onSkipImposterGuess = { viewModel.skipImposterGuess() },
                                onPlayAgain = { viewModel.playAgain() },
                                onReturnToLobby = { viewModel.returnToLobby() }
                            )
                            AppScreen.CODEX -> CodexScreen(
                                characters = uiState.allCharacters,
                                selectedCategory = uiState.selectedCodexCategory,
                                customName = uiState.customCharacterName,
                                customAnime = uiState.customCharacterAnime,
                                customCategory = uiState.customCharacterCategory,
                                customTraits = uiState.customCharacterTraits,
                                customAbilities = uiState.customCharacterAbilities,
                                onCategoryFilter = { viewModel.filterCodexCategory(it) },
                                onCustomFieldsChanged = { name, anime, cat, traits, ab ->
                                    viewModel.onCustomCharacterFieldChanged(name, anime, cat, traits, ab)
                                },
                                onAddCustomCharacter = { viewModel.addCustomCharacter() },
                                onDeleteCharacter = { viewModel.deleteCharacter(it) },
                                onBack = { viewModel.navigateTo(AppScreen.HOME) }
                            )
                            AppScreen.PROFILE -> ProfileScreen(
                                profile = uiState.userProfile,
                                achievements = uiState.achievements,
                                recentMatches = uiState.recentMatches,
                                onUpdateProfile = { name, avatar -> viewModel.updateProfile(name, avatar) },
                                onBack = { viewModel.navigateTo(AppScreen.HOME) }
                            )
                            AppScreen.SETTINGS -> SettingsScreen(
                                isSoundEnabled = uiState.isSoundEnabled,
                                isHapticsEnabled = uiState.isHapticsEnabled,
                                onToggleSound = { viewModel.toggleSound(it) },
                                onToggleHaptics = { viewModel.toggleHaptics(it) },
                                onBack = { viewModel.navigateTo(AppScreen.HOME) }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun BoxWithPadding(
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    androidx.compose.foundation.layout.Box(modifier = modifier) {
        content()
    }
}
