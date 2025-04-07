package com.infinum.snacky.sample.ui.snackbars

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinum.snacky.SnackyData
import com.infinum.snacky.SnackyDuration
import com.infinum.snacky.SnackyState
import com.infinum.snacky.sample.R

@Composable
fun PersonalMessageSnackbar(
    title: String,
    message: String,
    @DrawableRes icon: Int,
    actionLabel: String,
    withDismissAction: Boolean,
    dismiss: () -> Unit,
    performAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.spacing_1_5x)),
        shape = RoundedCornerShape(12.dp),
        color = Color.DarkGray,
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier
                .height(IntrinsicSize.Max)
                .padding(dimensionResource(id = R.dimen.spacing_2x)),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_0_5x)),
        ) {
            PersonalMessageSnackbarMessages(
                title = title,
                message = message,
                icon = icon,
                modifier = Modifier.weight(1f),
            )
            PersonalMessageSnackbarActions(
                actionLabel = actionLabel,
                dismiss = dismiss,
                performAction = performAction,
                withDismissAction = withDismissAction,
            )
        }
    }
}

@Composable
fun PersonalMessageSnackbarMessages(
    title: String,
    message: String,
    @DrawableRes icon: Int,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_1x)),
        horizontalAlignment = Alignment.Start,
    ) {
        Row(
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_1x)),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
            )

            Text(
                text = title,
                fontWeight = FontWeight.Bold,
                color = Color.White,
            )
        }

        Text(
            text = message,
            color = Color.White,
        )
    }
}

@Composable
fun PersonalMessageSnackbarActions(
    actionLabel: String,
    withDismissAction: Boolean,
    dismiss: () -> Unit,
    performAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier.fillMaxHeight(),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.End,
    ) {
        if (withDismissAction) {
            IconButton(
                modifier = Modifier.size(24.dp),
                onClick = dismiss,
            ) {
                Icon(
                    painter = painterResource(id = android.R.drawable.ic_menu_close_clear_cancel),
                    contentDescription = "Dismiss",
                    tint = Color.White,
                )
            }
        }

        Button(
            onClick = performAction,
        ) {
            Text(
                text = actionLabel,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}

data class PersonalMessageSnackbarData(
    private val title: String,
    private val message: String,
    private val actionLabel: String,
    private val withDismissAction: Boolean,
    @DrawableRes val icon: Int,
    override val duration: SnackyDuration = SnackyDuration.Indefinite,
    override val onMainActionCallback: () -> Unit,
    override val onDismissCallback: () -> Unit,
) : SnackyData {
    @Composable
    override fun Render(snackbarState: SnackyState) {
        PersonalMessageSnackbar(
            title = title,
            message = message,
            icon = icon,
            actionLabel = actionLabel,
            withDismissAction = withDismissAction,
            dismiss = snackbarState::dismiss,
            performAction = snackbarState::performMainAction,
        )
    }
}

@Composable
@Preview
fun PersonalMessageSnackbarPreview() {
    PersonalMessageSnackbar(
        title = "Title",
        message = "Message",
        icon = android.R.drawable.ic_dialog_alert,
        actionLabel = "Action",
        withDismissAction = true,
        dismiss = { },
        performAction = { },
    )
}

@Composable
@Preview
fun PersonalMessageSnackbarWithoutDismissPreview() {
    PersonalMessageSnackbar(
        title = "Title",
        message = "Message",
        icon = android.R.drawable.ic_dialog_alert,
        actionLabel = "Action",
        withDismissAction = false,
        dismiss = { },
        performAction = { },
    )
}
