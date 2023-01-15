package com.wiryadev.kotranslate.android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.wiryadev.kotranslate.android.core.presentation.Routes
import com.wiryadev.kotranslate.android.core.theme.KoTranslateTheme
import com.wiryadev.kotranslate.android.translate.presentation.AndroidTranslateViewModel
import com.wiryadev.kotranslate.android.translate.presentation.TranslateScreen
import com.wiryadev.kotranslate.android.voice_to_text.presentation.AndroidVoiceToTextViewModel
import com.wiryadev.kotranslate.android.voice_to_text.presentation.VoiceToTextScreen
import com.wiryadev.kotranslate.core.domain.language.Language
import com.wiryadev.kotranslate.translate.presentation.TranslateEvent
import com.wiryadev.kotranslate.voice_to_text.presentation.VoiceToTextEvent
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            KoTranslateTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colors.background
                ) {
                    TranslateRoot()
                }
            }
        }
    }
}

@Composable
fun TranslateRoot() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = Routes.TRANSLATE,
    ) {
        composable(route = Routes.TRANSLATE) { backStackEntry ->
            val viewModel = hiltViewModel<AndroidTranslateViewModel>()
            val state by viewModel.state.collectAsState()

            val voiceResult by backStackEntry.savedStateHandle
                .getStateFlow<String?>("voiceResult", null)
                .collectAsState()

            LaunchedEffect(key1 = voiceResult) {
                viewModel.onEvent(
                    TranslateEvent.SubmitVoiceResult(voiceResult)
                )
                backStackEntry.savedStateHandle["voiceResult"] = null
            }

            TranslateScreen(
                state = state,
                onEvent = { event ->
                    when (event) {
                        is TranslateEvent.RecordAudio -> {
                            navController.navigate(
                                route = "${Routes.VOICE_TO_TEXT}/${state.fromLanguage.language.langCode}"
                            )
                        }
                        else -> viewModel.onEvent(event)
                    }
                },
            )
        }
        composable(
            route = "${Routes.VOICE_TO_TEXT}/{languageCode}",
            arguments = listOf(
                navArgument("languageCode") {
                    type = NavType.StringType
                    defaultValue = "en"
                }
            ),
        ) { backStackEntry ->
            val languageCode = backStackEntry.arguments?.getString("languageCode")
                ?: Language.ENGLISH.langCode
            val viewModel = hiltViewModel<AndroidVoiceToTextViewModel>()
            val state by viewModel.state.collectAsState()

            VoiceToTextScreen(
                state = state,
                languageCode = languageCode,
                onResult = { spokenText ->
                    navController.previousBackStackEntry
                        ?.savedStateHandle
                        ?.set("voiceResult", spokenText)

                    navController.popBackStack()
                },
                onEvent = { event ->
                    when (event) {
                        is VoiceToTextEvent.Close -> {
                            navController.popBackStack()
                        }
                        else -> viewModel.onEvent(event)
                    }
                }
            )
        }
    }
}