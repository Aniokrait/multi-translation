import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import io.github.aniokrait.multitranslation.R

/**
 * A button that trigger loading state function.
 * @param modifier Modifier
 * @param isSuspending Whether some suspending function is running.
 * @param text Text to show.
 * @param onClick Called when the button is clicked.
 */
@Composable
fun SuspendableButton(
    modifier: Modifier = Modifier,
    isSuspending: Boolean,
    text: String,
    onClick: () -> Unit,
) {
    Button(
        modifier = modifier,
        enabled = !isSuspending,
        onClick = onClick,
    ) {
        var textButtonSize by remember {
            mutableStateOf(IntSize(0, 0))
        }
        if (isSuspending) {
            val density = LocalDensity.current
            with(density) {
                CircularProgressIndicator(
                    modifier = Modifier
                        .then(Modifier.size(32.dp))  // change indicator size.
                        .height(textButtonSize.height.toDp()) // fix button size.
                        .width(textButtonSize.width.toDp()), // fix button size.
                    strokeWidth = 4.dp,
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.surfaceVariant,
                )
            }
        } else {
            Text(
                modifier = Modifier.onGloballyPositioned {
                    textButtonSize = it.size
                },
                text = text,
            )
        }
    }
}

@Preview
@Composable
fun SuspendableButtonSuspendingPreview() {
    SuspendableButton(
        isSuspending = true,
        text = stringResource(id = R.string.btn_translation_button),
        onClick = {},
    )
}

@Preview
@Composable
fun SuspendableButtonNotSuspendingPreview() {
    SuspendableButton(
        isSuspending = false,
        text = stringResource(id = R.string.btn_translation_button),
        onClick = {},
    )
}
