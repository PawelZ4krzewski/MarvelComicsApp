package com.example.marvelcomicsapp.ui.theme

import androidx.compose.material.Typography
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

// Set of Material typography styles to start with
val Typography = Typography(
    body1 = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp
    ),


    /* Other default text styles to override
    button = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 14.sp
    ),
    caption = TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.Normal,
        fontSize = 12.sp
    )
    */
)
val Typography.HeaderComicList: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.W700,
            fontSize = 28.sp,
            lineHeight = 18.sp
        )
    }

val Typography.ComicTitle: TextStyle
@Composable
get() {
    return  TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W500,
        fontSize = 18.sp,
        lineHeight = 21.sp
    )
}

val Typography.ComicAuthorList: TextStyle
@Composable
get() {
    return  TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 14.sp,
        lineHeight = 18.sp
    )
}

val Typography.ComicDescriptionList: TextStyle
@Composable
get() {
    return  TextStyle(
        fontFamily = FontFamily.Default,
        fontWeight = FontWeight.W400,
        fontSize = 16.sp,
        lineHeight = 21.sp
    )
}

val Typography.SearchBoxText: TextStyle
    @Composable
    get() {
        return  TextStyle(
            fontFamily = FontFamily.Default,
            fontWeight = FontWeight.Normal,
            fontSize = 20.sp,
            letterSpacing = 0.15.sp
        )
    }