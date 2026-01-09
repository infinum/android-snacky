package com.infinum.snacky.sample

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.infinum.snacky.SnackyAnimationSpec
import com.infinum.snacky.SnackyDuration
import com.infinum.snacky.SnackyHost
import com.infinum.snacky.SnackyHostState
import com.infinum.snacky.rememberSnackyHostState
import com.infinum.snacky.sample.ui.snackbars.AppInfoSnackbarData
import com.infinum.snacky.sample.ui.snackbars.DeviceDisconnectedSnackbarData
import com.infinum.snacky.sample.ui.snackbars.PersonalMessageSnackbarData
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

private const val TAG = "SnackySample"

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            MaterialTheme {
                Content()
            }
        }
    }
}

@Composable
fun Content() {
    val snackbarHostState = rememberSnackyHostState()
    var showDeviceDisconnectedSnackbar by remember { mutableStateOf(false) }
    var showPersonalMessageSnackbar by remember { mutableStateOf(false) }
    var showAppInfoMessage by remember { mutableStateOf(false) }
    var showDefaultSnackbar by remember { mutableStateOf(false) }

    AppScaffold(
        snackbarHostState = snackbarHostState,
        content = { innerPadding ->
            Column(
                modifier = Modifier.fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
            ) {
                Spacer(Modifier.height(100.dp))
                Text(
                    text = "Welcome to Snacky! 🍔",
                    fontSize = 28.sp,
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                )
                SnackbarButtons(
                    onShowDeviceDisconnected = { showDeviceDisconnectedSnackbar = true },
                    onShowPersonalMessage = { showPersonalMessageSnackbar = true },
                    onShowAppInfoMessage = { showAppInfoMessage = true },
                    onShowDefault = { showDefaultSnackbar = true },
                    modifier = Modifier.padding(innerPadding),
                )
            }
        },
    )

    LaunchedEffect(showDefaultSnackbar) {
        if (showDefaultSnackbar) {
            launchDefaultSnackbar(snackbarHostState) {
                showDefaultSnackbar = false
            }
        }
    }
    LaunchedEffect(showDeviceDisconnectedSnackbar) {
        if (showDeviceDisconnectedSnackbar) {
            launchDeviceDisconnectedSnackbar(snackbarHostState) {
                showDeviceDisconnectedSnackbar = false
            }
        }
    }
    LaunchedEffect(showAppInfoMessage) {
        if (showAppInfoMessage) {
            launchAppInfoSnackbar(snackbarHostState) {
                showAppInfoMessage = false
            }
        }
    }
    LaunchedEffect(showPersonalMessageSnackbar) {
        if (showPersonalMessageSnackbar) {
            launchPersonalMessageSnackbar(snackbarHostState) {
                showPersonalMessageSnackbar = false
            }
        }
    }
}

@Composable
fun AppScaffold(
    snackbarHostState: SnackyHostState,
    content: @Composable (PaddingValues) -> Unit,
) {
    Scaffold(
        snackbarHost = {
            SnackyHost(
                hostState = snackbarHostState,
                animationSpec = SnackyAnimationSpec(scaleFactor = 0.5f),
            )
        },
        containerColor = colorResource(id = R.color.pinky_dust),
        content = content,
    )
}

@Composable
fun SnackbarButtons(
    onShowDeviceDisconnected: () -> Unit,
    onShowPersonalMessage: () -> Unit,
    onShowAppInfoMessage: () -> Unit,
    onShowDefault: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(40.dp),
    ) {
        Button(onClick = onShowDeviceDisconnected) {
            Text(
                text = "Show device disconnected snackbar",
                textAlign = TextAlign.Center,
            )
        }
        Button(onClick = onShowPersonalMessage) {
            Text(
                text = "Show personal message snackbar",
                textAlign = TextAlign.Center,
            )
        }
        Button(onClick = onShowAppInfoMessage) {
            Text(
                text = "Show app info snackbar",
                textAlign = TextAlign.Center,
            )
        }
        Button(onClick = onShowDefault) {
            Text(
                text = "Show default snackbar",
                textAlign = TextAlign.Center,
            )
        }
    }
}

fun CoroutineScope.launchDeviceDisconnectedSnackbar(
    snackbarHostState: SnackyHostState,
    onDismiss: () -> Unit,
) = this.launch {
    snackbarHostState.showSnackbar(
        DeviceDisconnectedSnackbarData(
            message = "Device disconnected",
            duration = SnackyDuration.Long,
            onMainActionCallback = {
                Log.i(TAG, "Device disconnected snackbar - Main action clicked")
                onDismiss()
            },
            onSecondaryActionCallback = {
                Log.i(TAG, "Device disconnected snackbar - Secondary action clicked")
                onDismiss()
            },
        ),
    )
}

private fun CoroutineScope.launchAppInfoSnackbar(
    snackbarHostState: SnackyHostState,
    onDismiss: () -> Unit,
) {
    this.launch {
        snackbarHostState.showSnackbar(
            AppInfoSnackbarData(
                message = "Action Required",
                actionLabel = "Action",
                duration = SnackyDuration.Long,
                icon = android.R.drawable.ic_dialog_alert,
                onMainActionCallback = {
                    Log.i(TAG, "App info snackbar - Action clicked")
                    onDismiss()
                },
            ),
        )
    }
}

private fun CoroutineScope.launchPersonalMessageSnackbar(
    snackbarHostState: SnackyHostState,
    onDismiss: () -> Unit,
) {
    this.launch {
        snackbarHostState.showSnackbar(
            PersonalMessageSnackbarData(
                title = "Hi Ivy,",
                message = "Welcome to the world of Snacks! Explore the endless possibilities with delicious treats and discover the" +
                    "exciting flavors waiting for you while enjoying every delightful moment with this informative guide!",
                actionLabel = "View",
                duration = SnackyDuration.Long,
                icon = android.R.drawable.ic_dialog_info,
                withDismissAction = true,
                onDismissCallback = {
                    Log.i(TAG, "Personal message snackbar - dismissed")
                    onDismiss()
                },
                onMainActionCallback = {
                    Log.i(TAG, "Personal message snackbar - Action clicked")
                    onDismiss()
                },
            ),
        )
    }
}

private fun CoroutineScope.launchDefaultSnackbar(
    snackbarHostState: SnackyHostState,
    onDismiss: () -> Unit,
) {
    this.launch {
        snackbarHostState.showSnackbar(
            message = "Some info message",
            duration = SnackyDuration.Custom(6.seconds),
            actionLabel = "Action",
            hasDismissAction = true,
            dismissActionContentDescription = "Dismiss",
            onDismissCallback = {
                Log.i(TAG, "Snackbar standard dismissed clicked")
                onDismiss()
            },
            onActionCallback = {
                Log.i(TAG, "Snackbar standard Main action clicked")
                onDismiss()
            },
        )
    }
}

@Composable
@Preview
fun PreviewContent() {
    MaterialTheme {
        Content()
    }
}
