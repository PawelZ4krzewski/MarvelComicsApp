package com.example.feature_main.ui.components

import android.widget.TextView
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.text.HtmlCompat

@Composable
fun HtmlText(
    html: String,
    style: TextStyle,
    modifier: Modifier = Modifier,
    lineSpacing: Float = 1.2f,
    color: Color = Color.Black) {
    val myTextSize = style.fontSize.value
    AndroidView(
        modifier = modifier,
        factory = { context -> TextView(context).apply {
            textSize = myTextSize
            setTextColor(color.toArgb())
            setLineSpacing(1f,lineSpacing)
        }},
        update = { it.text = HtmlCompat.fromHtml(html, HtmlCompat.FROM_HTML_MODE_COMPACT) }
    )
}