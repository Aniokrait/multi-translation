package io.github.aniokrait.multitranslation.ui.screen.modeldownload

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.airbnb.lottie.compose.LottieAnimation
import com.airbnb.lottie.compose.LottieCompositionSpec
import com.airbnb.lottie.compose.LottieConstants
import com.airbnb.lottie.compose.animateLottieCompositionAsState
import com.airbnb.lottie.compose.rememberLottieComposition
import io.github.aniokrait.multitranslation.R

/**
 * Dialog to show downloading progress.
 */
@Composable
fun DownloadingDialog() {
    // TODO: find more appropriate composable.
    Dialog(onDismissRequest = {}) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            val composition by rememberLottieComposition(LottieCompositionSpec.RawRes(R.raw.downloadinglottie))
            val progress by animateLottieCompositionAsState(composition)
            LottieAnimation(
                composition = composition,
                iterations = LottieConstants.IterateForever,
            )

            Text(text = stringResource(id = R.string.lbl_download_progress))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DownloadingDialogPreview() {
    DownloadingDialog()
}
