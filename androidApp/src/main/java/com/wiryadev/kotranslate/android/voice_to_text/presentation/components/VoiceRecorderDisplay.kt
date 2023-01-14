package com.wiryadev.kotranslate.android.voice_to_text.presentation.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.wiryadev.kotranslate.android.core.theme.KoTranslateTheme
import com.wiryadev.kotranslate.android.translate.presentation.components.gradientSurface
import kotlin.random.Random

@Composable
fun VoiceRecorderDisplay(
    powerRatios: List<Float>,
    modifier: Modifier = Modifier,
) {
    val primaryColor = MaterialTheme.colors.primary

    Box(
        modifier = modifier
            .shadow(
                elevation = 4.dp,
                shape = RoundedCornerShape(24.dp),
            )
            .clip(RoundedCornerShape(24.dp))
            .gradientSurface()
            .padding(
                vertical = 8.dp,
                horizontal = 32.dp,
            )
            .drawBehind {
                val powerRatioWidth = 3.dp.toPx()
                val powerRatioCount = (size.width / (2 * powerRatioWidth)).toInt()

                clipRect {
                    powerRatios
                        .takeLast(powerRatioCount)
                        .reversed()
                        .forEachIndexed { i, ratio ->
                            val yTopStart = center.y - (size.height / 2f) * ratio
                            drawRoundRect(
                                color = primaryColor,
                                topLeft = Offset(
                                    x = size.width - (i * 2 * powerRatioWidth),
                                    y = yTopStart
                                ),
                                size = Size(
                                    width = powerRatioWidth,
                                    height = (center.y - yTopStart) * 2f,
                                ),
                                cornerRadius = CornerRadius(100f),
                            )
                        }
                }
            }
    )
}

@Preview
@Composable
fun VoiceRecorderDisplayPreview() {
    KoTranslateTheme {
        VoiceRecorderDisplay(
            powerRatios = (0..100).map {
                Random.nextFloat()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(100.dp)
        )
    }
}