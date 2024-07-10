package io.github.aniokrait.multitranslation.ui.screen.translate

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.ads.AdRequest
import com.google.android.gms.ads.AdSize
import com.google.android.gms.ads.AdView
import io.github.aniokrait.multitranslation.R

/**
 * AdMob banner.
 */
@Composable
fun BannerAd(
    modifier: Modifier = Modifier,
) {
    AndroidView(
        modifier = modifier.fillMaxWidth(),
        factory = { context ->
            AdView(context).apply {
                setAdSize(AdSize.BANNER)
                adUnitId = context.getString(R.string.admob_ad_unit_id)
                loadAd(AdRequest.Builder().build())
            }
        }
    )
}
