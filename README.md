# Android Snacky

Android library for enhancing the snackbar experience with Jetpack Compose. Snacky allows developers to create fully customizable snackbars without the limitations of the built-in options. Easily implement dynamic snackbars with various styles, actions, and behaviors tailored to your application’s unique needs.

## Getting started

To include _Snacky_ in your project, you have to add buildscript dependencies in your project
level `build.gradle` or `build.gradle.kts`:

**Groovy**

```groovy
buildscript {
    repositories {
        mavenCentral()
    }
}
```

**KotlinDSL**

```kotlin
buildscript {
    repositories {
        mavenCentral()
    }
}
```

Then, you can include the library in your module's `build.gradle` or `build.gradle.kts`:

**Groovy**

```groovy
implementation "com.infinum.snacky:snacky:0.1.0"
```

**KotlinDSL**

```kotlin
implementation("com.infinum.snacky:snacky:0.1.0")
```

Don't forget to sync your project.

### Usage
#### Displaying a Custom Snackbar

If you want to display a custom-looking snackbar with a specific duration, you first need to define a data model that holds the necessary information.
To create a snackbar with custom data, implement the `SnackyData` interface.`Render` function in the model should handle passing the necessary data to your custom snackbar composable (`PersonalMessageSnackbar`), ensuring it is properly configured with all required properties:

```kotlin
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
```

Then, to integrate the snackbar into your `Scaffold`:
* Provide a Snackbar Host: Use `SnackyHost` inside a Scaffold to manage snackbar display.
* Manage Snackbar State: Use `rememberSnackyHostState()` to control when the snackbar appears.
* Trigger the Snackbar: Launch a coroutine to show the snackbar when needed.

```kotlin
val snackbarHostState = rememberSnackyHostState()
val scope = rememberCoroutineScope()

Scaffold(
    snackbarHost = {
        SnackyHost(
            hostState = snackbarHostState,
            animationSpec = SnackyAnimationSpec(scaleFactor = 0.5f),
        )
    },
) { _ ->
    Box(
        modifier = Modifier
            .fillMaxSize(),
    ) {
        Button(
            onClick = {
                scope.launch {
                    snackbarHostState.showSnackbar(
                        PersonalMessageSnackbarData(
                            title = "Hi John,",
                            message = "Welcome to the world of Snacks! Explore the endless possibilities with delicious treats!",
                            actionLabel = "Explore",
                            duration = SnackyDuration.Custom(6.seconds),
                            icon = android.R.drawable.ic_dialog_info,
                            withDismissAction = true,
                            onDismissCallback = { },
                            onMainActionCallback = { 
                                // navigate to the next screen
                            },
                        ),
                    )
                }
            }
        ) {
            Text(
                text = "Show snackbar",
                textAlign = TextAlign.Center,
            )
        }
    }
}
```

With this setup, you can now display multiple customized snackbars on the same screen using a single `SnackyHost`. This means different types of snackbars - each with unique styles, actions, duration and content - can seamlessly coexist in your app.

Additionally, `SnackyHost` supports animation customization through `animationSpec`, allowing you to fine-tune:

* Fade in/out duration
* In between delay
* Scale factor
* Scale easing
* Opacity easing

#### Displaying a Default Snackbar

And if you ever need a default - looking snackbar, you can simply call:

```kotlin
scope.launch {
    snackbarHostState.showSnackbar(
        message = "Some info message",
    )
}
```

Also, check the [sample app](sample) for more detailed examples.

## Requirements

Minimum supported Android version: API level 28 (Android 9)

## Contributing

Feedback and code contributions are very much welcome. Just make a pull request with a short description of your changes. By making contributions to this project you give permission for your code to be used under the same [license](LICENSE).

## License

```
Copyright 2025 Infinum

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

   http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
```

## Credits
Maintained and sponsored by [Infinum](http://www.infinum.com).

<p align="center">
  <a href='https://infinum.com'>
    <picture>
        <source srcset="https://assets.infinum.com/brand/logo/static/white.svg" media="(prefers-color-scheme: dark)">
        <img src="https://assets.infinum.com/brand/logo/static/default.svg">
    </picture>
  </a>
</p>
