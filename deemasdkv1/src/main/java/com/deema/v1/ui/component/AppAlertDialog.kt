package com.deema.v1.ui.component

import androidx.annotation.DrawableRes
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.outlined.Check
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Warning
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.deema.v1.R
import com.deema.v1.ui.theme.black_0
import com.deema.v1.ui.theme.black_1
import com.deema.v1.ui.theme.green_color
import com.deema.v1.ui.theme.red_color
import com.deema.v1.ui.theme.transparent
import com.deema.v1.ui.theme.white_1
import com.deema.v1.ui.theme.yellow_1
import kotlinx.coroutines.delay


enum class ToastAlertType {
    Success, Error, Warning, Info
}

@Composable
fun AppAlertDialog(shouldShowDialog: Boolean = false, title: String? = null, desc: String, onDismiss: ()->Unit, onConfirm: () -> Unit ? = {}) {
    if(!shouldShowDialog) return

    val styleTitle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    val styleButton = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
    val style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)

    AlertDialog(
        containerColor = white_1,
        onDismissRequest = {
            onDismiss()
//          shouldShowDialog.value = false
        },
        title = {
            if(title.isNullOrEmpty()) {
            } else { Text(text = title, style = styleTitle) }
        },
        text = { Text(text = desc, style = style) },
        confirmButton = {
            TextButton(onClick = {
                onConfirm()
            }) {
                Text("OK", style = styleButton)
            }
        }
    )
}

@Composable
fun AppConfirmAlertDialog(shouldShowDialog: Boolean = false, title: String, desc: String, onDismiss: ()->Unit, onConfirm: ()->Unit) {
    if (!shouldShowDialog) return

    val styleTitle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    val styleButton = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)
    val style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)

    AlertDialog(
        containerColor = white_1,
        onDismissRequest = {
            onDismiss()
            },
        title = { Text("Are you sure you want to delete this?", style = styleTitle) },
        text = { Text("This action cannot be undone", style = style) },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Yes Delete", style = styleButton)
            }
        },
        dismissButton = {
            TextButton(onClick = {
                onDismiss()
            }) {
                Text("No Thank You", style = styleButton)
            }
        },
    )
}


@Composable
fun AppToastAlert(shouldShowDialog: Boolean = false, title: String? = null, desc: String, toastAlertType: ToastAlertType = ToastAlertType.Info, onDismiss: ()->Unit) {
    if(!shouldShowDialog) return

    val icon = when(toastAlertType) {
        ToastAlertType.Success -> Icons.Outlined.Check
        ToastAlertType.Error -> Icons.Default.Close
        ToastAlertType.Warning -> Icons.Outlined.Warning
        ToastAlertType.Info -> Icons.Outlined.Info
    }

    val iconColor =  when(toastAlertType) {
        ToastAlertType.Success -> green_color
        ToastAlertType.Error -> red_color
        ToastAlertType.Warning -> yellow_1
        ToastAlertType.Info -> black_0
    }

    val styleTitle = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.SemiBold)
    val style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Normal)
    val styleButton = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Medium)


    LaunchedEffect(Unit) {
        delay(5240)
        onDismiss()
    }

    AlertDialog(
        containerColor = white_1,
        onDismissRequest = {
            onDismiss()
//          shouldShowDialog.value = false
        },
//        icon = {
//            Icon(
//                modifier = Modifier.size(42.dp),
//                imageVector = icon,
//                contentDescription = "",
//                tint = iconColor
//            )
//
//        },
        title = { if(title == null) null else  { Text(text = title, color = black_1, style = styleTitle) } },
        text = { Text(modifier = Modifier.fillMaxWidth(), text = desc, style = style, color = black_1, textAlign = TextAlign.Center) },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("OK", style = styleButton)
            }
        }
    )
}


@Composable
fun AppDialogPopup(content: @Composable ColumnScope.() -> Unit = {}, onDismiss: () -> Unit) {

    Dialog(onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false
        )
    ) {
        Box(modifier = Modifier
            .fillMaxWidth(0.88f)
            .fillMaxHeight(0.50f)
            .padding(20.dp)) {
            ElevatedCard(modifier = Modifier.padding(horizontal = 10.dp, vertical = 14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = white_1
                ),
                elevation = CardDefaults.cardElevation(
                    defaultElevation = 8.dp
                )
            ) {
                content()
            }

            Row(modifier = Modifier.fillMaxWidth().padding(end = 20.dp, top = 24.dp) , horizontalArrangement = Arrangement.End){
                ImageButton(modifier = Modifier.size(28.dp), id = R.drawable.close_circle) {
                    onDismiss()
                }
            }
        }
    }
}


@Composable
fun ImageButton(modifier: Modifier = Modifier, bgColor: Color = transparent, @DrawableRes id: Int, callback: () -> Unit){
    Box(modifier = modifier
        .background(color = bgColor)
        .clickable {
            callback()
        })
    {
        AppImage(modifier = modifier
            .padding(0.dp), id = id)
    }
}