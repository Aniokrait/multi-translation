package io.github.aniokrait.multitranslation

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import io.github.aniokrait.multitranslation.datasource.LanguageModelDatasource
import io.github.aniokrait.multitranslation.ui.TopBar
import io.github.aniokrait.multitranslation.ui.screen.initialsetting.InitialDownloadScreen
import io.github.aniokrait.multitranslation.ui.theme.MultiTranslationTheme
import io.github.aniokrait.multitranslation.viewmodel.InitialDownloadViewModel
import org.koin.androidx.compose.koinViewModel

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Box(Modifier.safeDrawingPadding()) {
                MultiTranslationTheme {
                    Scaffold(
                        modifier = Modifier.fillMaxSize(),
                        topBar = { TopBar() },
                    ) { innerPadding ->
//                        TranslationScreen(
//                            modifier = Modifier.padding(innerPadding),
//                            translateResults = mapOf(),
//                            onTranslateClick = {},
//                        )
                        val repository = LanguageModelDatasource(context = LocalContext.current)
                        val vm: InitialDownloadViewModel = koinViewModel()
                        InitialDownloadScreen(vm = vm)
                    }
                }
            }
        }
    }
}
