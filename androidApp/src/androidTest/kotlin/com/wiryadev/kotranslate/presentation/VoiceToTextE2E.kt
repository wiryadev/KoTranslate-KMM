package com.wiryadev.kotranslate.presentation

import android.Manifest
import android.content.Context
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.core.app.ApplicationProvider
import androidx.test.rule.GrantPermissionRule
import com.wiryadev.kotranslate.android.MainActivity
import com.wiryadev.kotranslate.android.R
import com.wiryadev.kotranslate.android.di.AppModule
import com.wiryadev.kotranslate.android.voice_to_text.di.VoiceToTextModule
import com.wiryadev.kotranslate.translate.data.remote.FakeTranslateClient
import com.wiryadev.kotranslate.translate.domain.translate.TranslateClient
import com.wiryadev.kotranslate.voice_to_text.data.FakeVoiceToTextParser
import com.wiryadev.kotranslate.voice_to_text.domain.VoiceToTextParser
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import dagger.hilt.android.testing.UninstallModules
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject

@HiltAndroidTest
@UninstallModules(
    AppModule::class,
    VoiceToTextModule::class,
)
class VoiceToTextE2E {

    @get:Rule
    val composeRule = createAndroidComposeRule<MainActivity>()

    @get:Rule
    val hiltRule = HiltAndroidRule(this)

    @get:Rule
    val permissionRule: GrantPermissionRule = GrantPermissionRule.grant(
        Manifest.permission.RECORD_AUDIO
    )

    @Inject
    lateinit var voiceParser: VoiceToTextParser

    @Inject
    lateinit var client: TranslateClient

    private val context = ApplicationProvider.getApplicationContext<Context>()

    @Before
    fun setUp() {
        hiltRule.inject()
    }

    @Test
    fun recordAndTranslate() = runBlocking<Unit> {
        val fakeParser = voiceParser as FakeVoiceToTextParser
        val fakeClient = client as FakeTranslateClient

        // move to VoiceToTextScreen
        composeRule
            .onNodeWithContentDescription(context.getString(R.string.record_audio))
            .performClick()

        // start recording
        composeRule
            .onNodeWithContentDescription(context.getString(R.string.record_audio))
            .performClick()

        // stop recording
        composeRule
            .onNodeWithContentDescription(context.getString(R.string.stop_recording))
            .performClick()

        composeRule
            .onNodeWithText(fakeParser.voiceResult)
            .assertIsDisplayed()

        composeRule
            .onNodeWithContentDescription(context.getString(R.string.apply))
            .performClick()

        composeRule
            .onNodeWithText(fakeParser.voiceResult)
            .assertIsDisplayed()

        composeRule
            .onNodeWithText(context.getString(R.string.translate), ignoreCase = true)
            .performClick()

        composeRule
            .onNodeWithText(fakeClient.translatedText)
            .assertIsDisplayed()
    }

}