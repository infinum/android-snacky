package com.infinum.snacky.default

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.dimensionResource
import com.infinum.android.snacky.R
import com.infinum.snacky.SnackyData
import com.infinum.snacky.SnackyDuration
import com.infinum.snacky.SnackyState

/**
 * For displaying a widely applicable and commonly used material3 [Snackbar]
 *
 * @param message message to be displayed in the snackbar
 * @param modifier for customizing the appearance and layout of the snackbar
 * @param hasDismissAction a boolean indicating whether the snackbar should have a dismiss action
 * @param actionLabel text for the action button, if any
 * @param dismiss the callback to be executed when the dismiss action is clicked
 * @param performAction the callback to be executed when the action button is clicked
 * @param actionOnNewLine a boolean indicating whether the action button should be displayed on a new line
 * @param shape the shape of the snackbar container
 * @param containerColor the color of the snackbar container
 * @param contentColor the color of the snackbar content
 * @param actionColor the color of the action button
 * @param actionContentColor the color of the action button content
 * @param dismissActionContentColor the color of the dismiss action content
 * @param dismissActionContentDescription the content description for the dismiss action
 */
@Composable
public fun DefaultSnackbar(
    message: String,
    dismissActionContentDescription: String,
    modifier: Modifier = Modifier,
    hasDismissAction: Boolean = false,
    actionLabel: String? = null,
    dismiss: () -> Unit = {},
    performAction: () -> Unit = {},
    actionOnNewLine: Boolean = false,
    shape: Shape = SnackbarDefaults.shape,
    containerColor: Color = SnackbarDefaults.color,
    contentColor: Color = SnackbarDefaults.contentColor,
    actionColor: Color = SnackbarDefaults.actionColor,
    actionContentColor: Color = SnackbarDefaults.actionContentColor,
    dismissActionContentColor: Color = SnackbarDefaults.dismissActionContentColor,
) {
    val actionComposable: (@Composable () -> Unit)? = actionLabel?.let {
        @Composable {
            TextButton(
                colors = ButtonDefaults.textButtonColors(contentColor = actionColor),
                onClick = performAction,
                content = { Text(actionLabel) },
            )
        }
    }
    val dismissActionComposable: (@Composable () -> Unit)? =
        if (hasDismissAction) {
            @Composable {
                IconButton(
                    onClick = dismiss,
                    content = {
                        Icon(
                            Icons.Filled.Close,
                            contentDescription = dismissActionContentDescription,
                        )
                    },
                )
            }
        } else {
            null
        }
    Snackbar(
        modifier = modifier.padding(dimensionResource(id = R.dimen.standard_snackbar_padding)),
        action = actionComposable,
        dismissAction = dismissActionComposable,
        actionOnNewLine = actionOnNewLine,
        shape = shape,
        containerColor = containerColor,
        contentColor = contentColor,
        actionContentColor = actionContentColor,
        dismissActionContentColor = dismissActionContentColor,
        content = { Text(message) },
    )
}

/**
 * For storing the necessary information to display a [DefaultSnackbar]
 *
 * @property message message to be displayed in the snackbar
 * @property actionLabel label for the action button, if any
 * @property withDismissAction a boolean indicating whether the snackbar should have a dismiss action
 * @property duration duration for which the snackbar should be displayed
 * @property onDismissCallback the callback to be executed when the snackbar is dismissed
 * @property onMainActionCallback the callback to be executed when the main action button is clicked
 */
public class DefaultSnackbarData(
    public val message: String,
    public val actionLabel: String?,
    public val withDismissAction: Boolean,
    public val dismissActionContentDescription: String,
    override val duration: SnackyDuration,
    override val onDismissCallback: () -> Unit = {},
    override val onMainActionCallback: () -> Unit = {},
) : SnackyData {
    @Composable
    override fun Render(
        snackbarState: SnackyState,
    ) {
        DefaultSnackbar(
            message = message,
            hasDismissAction = withDismissAction,
            actionLabel = actionLabel,
            dismiss = snackbarState::dismiss,
            performAction = snackbarState::performMainAction,
            dismissActionContentDescription = dismissActionContentDescription,
        )
    }
}
