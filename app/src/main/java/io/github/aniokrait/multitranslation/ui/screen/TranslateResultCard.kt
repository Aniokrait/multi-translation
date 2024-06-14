package io.github.aniokrait.multitranslation.ui.screen

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp

/**
 * The card shows the translation result.
 */
@Composable
fun TranslateResultCard(
    modifier: Modifier = Modifier,
    textBlockHeight: Dp,
    language: String,
    content: String,
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
        ,
    ){
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp)
        ) {
            Text(
                modifier = Modifier
                    .align(Alignment.Start)
                ,
                text = language,
                fontWeight = FontWeight.Bold,
            )

            val scrollState = rememberScrollState()
//        Box {
            val indentedText = buildAnnotatedString {
                withStyle(style = SpanStyle()) {
                    // indentation
                    append("  ")
                }
                append(content)
            }
            Text(
                modifier = Modifier
                    .height(textBlockHeight)
                    .verticalScroll(scrollState)
                ,
                text = indentedText,
            )

            // TODO Fix to support Text label.
            //  https://qiita.com/yasukotelin/items/fcf5b538fac922cb08a5
//            ScrollBar(
//                scrollState = scrollState,
//                isAlwaysShowScrollBar = true,)
//        }
        }

    }
}

@Preview
@Composable
fun TranslateResultCardPreview(
    @PreviewParameter(UserPreviewParameterProvider::class) content: Pair<String, String>
) {
    TranslateResultCard(
        textBlockHeight = 200.dp,
        language = content.first,
        content = content.second,
    )
}

private class UserPreviewParameterProvider : PreviewParameterProvider<Pair<String, String>> {
    override val values = sequenceOf(
        Pair("Japanese", "こんにちは"),
        Pair("English", "Hello2"),
        Pair("Japanese", "こんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちは"),
        Pair("hoge", "fuga")
    )
}

