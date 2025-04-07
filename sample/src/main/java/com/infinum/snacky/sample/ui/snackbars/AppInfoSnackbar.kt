package com.infinum.snacky.sample.ui.snackbars

import androidx.annotation.DrawableRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinum.snacky.SnackyData
import com.infinum.snacky.SnackyDuration
import com.infinum.snacky.SnackyState
import com.infinum.snacky.sample.R

private const val BACKGROUND_COLOR = 0xFF242C32

@Composable
fun AppInfoSnackbar(
    message: String,
    actionLabel: String,
    @DrawableRes icon: Int,
    performAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(dimensionResource(id = R.dimen.spacing_1_5x)),
        shape = RoundedCornerShape(12.dp),
        color = Color(BACKGROUND_COLOR),
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier.padding(
                vertical = dimensionResource(id = R.dimen.spacing_1_5x),
                horizontal = dimensionResource(id = R.dimen.spacing_2x),
            ),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(id = R.dimen.spacing_1_5x)),
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.White,
            )

            Text(
                modifier = Modifier.weight(1f),
                text = message,
                color = Color.White,
            )

            Text(
                modifier = Modifier.clickable(onClick = performAction),
                text = actionLabel,
                color = Color.White,
            )
        }
    }
}

class AppInfoSnackbarData(
    private val message: String,
    private val actionLabel: String,
    @DrawableRes val icon: Int,
    override val duration: SnackyDuration = SnackyDuration.Indefinite,
    override val onMainActionCallback: () -> Unit,
) : SnackyData {
    @Composable
    override fun Render(snackbarState: SnackyState) {
        AppInfoSnackbar(
            message = message,
            actionLabel = actionLabel,
            icon = icon,
            performAction = snackbarState::performMainAction,
        )
    }
}

@Composable
@Preview
fun AppInfoSnackbarPreview() {
    AppInfoSnackbar(
        message = "Action Required",
        actionLabel = "Action",
        icon = android.R.drawable.ic_dialog_alert,
        performAction = { },
    )
}
