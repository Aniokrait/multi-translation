package io.github.aniokrait.multitranslation.ui.screen.translate

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R

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
        modifier =
        modifier
            .fillMaxWidth(),
    ) {
        Column(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
        ) {
            Row {
                Text(
                    modifier = Modifier,
                    text = language,
                    fontWeight = FontWeight.Bold,
                )
                Spacer(modifier = Modifier.weight(1f))

                if (content.isNotEmpty()) {
                    Box {
                        val clipboardManager = LocalClipboardManager.current
                        IconButton(
                            modifier =
                            Modifier
                                .offset(x = 8.dp, y = (-8).dp),
                            onClick = {
                                clipboardManager.setText(AnnotatedString(content))
                            },
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.content_copy_24px),
                                contentDescription = "copy",
                            )
                        }
                    }
                }
            }

            val scrollState = rememberScrollState()
//        Box {
            val indentedText =
                buildAnnotatedString {
                    withStyle(style = SpanStyle()) {
                        // indentation
                        append("  ")
                    }
                    append(content)
                }
            Text(
                modifier =
                Modifier
                    .height(textBlockHeight)
                    .verticalScroll(scrollState),
                text = indentedText,
            )

            // TODO Fix to support Text label.
            //  https://qiita.com/yasukotelin/items/fcf5b538fac922cb08a5
//            ScrollBar(
//                scrollState = scrollState,
//                isAlwaysShowScrollBar = true,)
//        }

            if (content.isNotEmpty()) {
                Spacer(modifier = Modifier.weight(1f))
                Row {
                    Spacer(modifier = Modifier.weight(1f))

                    val uriHandler = LocalUriHandler.current
                    Image(
                        modifier =
                        Modifier.clickable {
                            uriHandler.openUri("http://translate.google.com")
                        },
                        painter = painterResource(id = R.drawable.powered_by_google),
                        contentDescription = null,
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun TranslateResultCardPreview(
    @PreviewParameter(UserPreviewParameterProvider::class) content: Pair<String, String>,
) {
    TranslateResultCard(
        textBlockHeight = 200.dp,
        language = content.first,
        content = content.second,
    )
}

private class UserPreviewParameterProvider : PreviewParameterProvider<Pair<String, String>> {
    override val values =
        sequenceOf(
            Pair("Japanese", "こんにちは"),
            Pair("English", "Hello2"),
            Pair(
                "Japanese",
                "こんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちはこんにちは",
            ),
            Pair("hoge", "fuga"),
        )
}
