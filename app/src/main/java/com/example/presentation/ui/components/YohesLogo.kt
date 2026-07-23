package com.example.presentation.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

val YohesBlueDark = Color(0xFF0F2C59)
val YohesBluePrimary = Color(0xFF1E3E62)
val YohesBlueBright = Color(0xFF0052D4)
val YohesGoldAccent = Color(0xFFF59E0B)

@Composable
fun YohesLogoIcon(
    size: Dp = 36.dp,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier.size(size)) {
        val w = this.size.width
        val h = this.size.height

        // Background / Y shape left diagonal
        val leftArm = Path().apply {
            moveTo(w * 0.10f, h * 0.15f)
            lineTo(w * 0.35f, h * 0.15f)
            lineTo(w * 0.50f, h * 0.50f)
            lineTo(w * 0.35f, h * 0.50f)
            close()
        }
        drawPath(leftArm, color = YohesBlueBright)

        // Skyscraper columns on the right (forming right arm of Y)
        drawRoundRect(
            color = YohesBluePrimary,
            topLeft = Offset(w * 0.60f, h * 0.10f),
            size = Size(w * 0.14f, h * 0.45f),
            cornerRadius = CornerRadius(w * 0.02f)
        )
        drawRoundRect(
            color = YohesBlueDark,
            topLeft = Offset(w * 0.74f, h * 0.18f),
            size = Size(w * 0.14f, h * 0.37f),
            cornerRadius = CornerRadius(w * 0.02f)
        )

        // Center House Roof outline
        val roof = Path().apply {
            moveTo(w * 0.18f, h * 0.45f)
            lineTo(w * 0.50f, h * 0.22f)
            lineTo(w * 0.82f, h * 0.45f)
            lineTo(w * 0.74f, h * 0.50f)
            lineTo(w * 0.50f, h * 0.32f)
            lineTo(w * 0.26f, h * 0.50f)
            close()
        }
        drawPath(roof, color = YohesBlueDark)

        // House body & base stem of Y
        drawRoundRect(
            color = YohesBlueDark,
            topLeft = Offset(w * 0.40f, h * 0.42f),
            size = Size(w * 0.20f, h * 0.48f),
            cornerRadius = CornerRadius(w * 0.03f)
        )

        // Gold Accent Ribbon
        val goldRibbon = Path().apply {
            moveTo(w * 0.25f, h * 0.55f)
            lineTo(w * 0.42f, h * 0.45f)
            lineTo(w * 0.42f, h * 0.55f)
            lineTo(w * 0.25f, h * 0.65f)
            close()
        }
        drawPath(goldRibbon, color = YohesGoldAccent)

        // Window grid in center house
        val windowSize = w * 0.05f
        val windowGap = w * 0.02f
        val winX = w * 0.45f
        val winY = h * 0.48f
        drawRect(Color.White, Offset(winX, winY), Size(windowSize, windowSize))
        drawRect(Color.White, Offset(winX + windowSize + windowGap, winY), Size(windowSize, windowSize))
        drawRect(Color.White, Offset(winX, winY + windowSize + windowGap), Size(windowSize, windowSize))
        drawRect(Color.White, Offset(winX + windowSize + windowGap, winY + windowSize + windowGap), Size(windowSize, windowSize))
    }
}

@Composable
fun YohesHeaderLogo(
    modifier: Modifier = Modifier,
    textColor: Color = YohesBlueDark
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.Start
    ) {
        YohesLogoIcon(size = 32.dp)
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = "Yohes",
            fontSize = 22.sp,
            fontWeight = FontWeight.ExtraBold,
            color = textColor,
            letterSpacing = (-0.5).sp,
            fontFamily = FontFamily.Serif
        )
    }
}
