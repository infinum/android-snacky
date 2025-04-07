package com.infinum.snacky.sample.ui.snackbars

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.infinum.snacky.SnackyData
import com.infinum.snacky.SnackyDuration
import com.infinum.snacky.SnackyState

private const val MAIN_ACTION_BUTTON_TEXT_COLOR = 0xFF006CE0

@Composable
fun DeviceDisconnectedSnackbar(
    message: String,
    performMainAction: () -> Unit,
    performSecondaryAction: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Surface(
        modifier = modifier
            .fillMaxWidth()
            .padding(
                horizontal = 12.dp,
                vertical = 12.dp,
            ),
        shape = RoundedCornerShape(12.dp),
        shadowElevation = 6.dp,
    ) {
        Row(
            modifier = Modifier.padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(16.dp),
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_manage),
                contentDescription = null,
                tint = Color.Unspecified,
            )
            Column {
                Text(
                    text = message,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black,
                )
                Text(
                    text = "Connect device to continue",
                    color = Color.DarkGray,
                )
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.clickable(onClick = performMainAction),
                    text = "How to connect device?",
                    fontWeight = FontWeight.Bold,
                    color = Color(MAIN_ACTION_BUTTON_TEXT_COLOR),
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    modifier = Modifier.clickable(onClick = performSecondaryAction),
                    text = "Ok",
                    fontWeight = FontWeight.Bold,
                    color = Color.DarkGray,
                )
            }
        }
    }
}

class DeviceDisconnectedSnackbarData(
    val message: String,
    override val duration: SnackyDuration = SnackyDuration.Indefinite,
    override val onMainActionCallback: () -> Unit,
    override val onSecondaryActionCallback: () -> Unit,
) : SnackyData {
    @Composable
    override fun Render(snackbarState: SnackyState) {
        DeviceDisconnectedSnackbar(
            message = message,
            performMainAction = snackbarState::performMainAction,
            performSecondaryAction = snackbarState::performSecondaryAction,
        )
    }
}

@Composable
@Preview
fun DeviceDisconnectedSnackbarPreview() {
    DeviceDisconnectedSnackbar(
        message = "Device disconnected",
        performMainAction = { },
        performSecondaryAction = { },
    )
}
