package io.github.aniokrait.multitranslation.ui.screen.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.RowScope
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ButtonElevation
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.tooling.preview.Preview
import io.github.aniokrait.multitranslation.ui.theme.DeepOrange500

/**
 * Button for dangerous action.
 * Acts like normal button, but has red background.
 */
@Composable
fun DangerActionButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier,
    enabled: Boolean = true,
    shape: Shape = ButtonDefaults.shape,
    elevation: ButtonElevation? = ButtonDefaults.buttonElevation(),
    border: BorderStroke? = null,
    contentPadding: PaddingValues = ButtonDefaults.ContentPadding,
    interactionSource: MutableInteractionSource = remember { MutableInteractionSource() },
    content: @Composable() (RowScope.() -> Unit),
) {
    val colors = ButtonColors(
        containerColor = DeepOrange500,
        contentColor = Color.Black.copy(alpha = 0.87f),
        disabledContainerColor = ButtonDefaults.buttonColors().disabledContainerColor,
        disabledContentColor = ButtonDefaults.buttonColors().disabledContainerColor
    )
    Button(
        onClick = onClick,
        modifier = modifier,
        enabled = enabled,
        shape = shape,
        colors = colors,
        elevation = elevation,
        border = border,
        contentPadding = contentPadding,
        interactionSource = interactionSource,
        content = content,
    )
}

@Preview
@Composable
fun DeleteButtonPreview() {
    DangerActionButton(onClick = {}) {
        Text("Delete")
    }
}
