package com.deema.v1.ui.component

import android.content.Context
import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.setViewTreeLifecycleOwner
import androidx.lifecycle.setViewTreeViewModelStoreOwner
import androidx.lifecycle.viewmodel.compose.LocalViewModelStoreOwner
import androidx.savedstate.setViewTreeSavedStateRegistryOwner
import com.deema.v1.R
import com.deema.v1.ui.theme.*

interface SweetToastProperty {
    fun getResourceId(): Int
    fun getBackgroundColor(): Color
    fun getBorderColor(): Color
    fun getTextColor(): Color
}

class Error : SweetToastProperty {
    override fun getResourceId(): Int = R.drawable.error_ic
    override fun getBackgroundColor(): Color = backgroundErrorColor
    override fun getBorderColor(): Color = errorColor
    override fun getTextColor(): Color = errorColor
}

class Info : SweetToastProperty {
    override fun getResourceId(): Int = R.drawable.success_ic
    override fun getBackgroundColor(): Color = white
    override fun getBorderColor(): Color  = greenColor
    override fun getTextColor(): Color = greenColor
}


class Success : SweetToastProperty {
    override fun getResourceId(): Int = R.drawable.success_ic
    override fun getBackgroundColor(): Color = white
    override fun getBorderColor(): Color  = greenColor
    override fun getTextColor(): Color = greenColor
}

class Warning : SweetToastProperty {
    override fun getResourceId(): Int = R.drawable.success_ic
    override fun getBackgroundColor(): Color = white
    override fun getBorderColor(): Color  = greenColor
    override fun getTextColor(): Color = greenColor
}

object SweetToastUtil {

    @Composable
    fun SweetSuccess(
        message: String,
        duration: Int = Toast.LENGTH_LONG,
        padding: PaddingValues = PaddingValues(top = 24.dp, bottom = 84.dp),
        contentAlignment: Alignment = Alignment.TopCenter,
        color: Color = greenColor
    ) {
        val sweetSuccessToast = CustomSweetToast(LocalContext.current)
        sweetSuccessToast.MakeToast(
            message = message,
            duration = duration,
            type = Success(),
            padding = padding,
            contentAlignment = contentAlignment,
            colorText = color
        )
        sweetSuccessToast.show()
    }

    @Composable
    fun SweetError(
        message: String,
        duration: Int = Toast.LENGTH_LONG,
        padding: PaddingValues = PaddingValues(top = 24.dp, bottom = 84.dp),
        contentAlignment: Alignment = Alignment.TopCenter,
    ) {
        val sweetErrorToast = CustomSweetToast(LocalContext.current)
        sweetErrorToast.MakeToast(
            message = message,
            duration = duration,
            type = Error(),
            padding = padding,
            contentAlignment = contentAlignment
        )
        sweetErrorToast.show()
    }

    @Composable
    fun SweetInfo(
        message: String,
        duration: Int = Toast.LENGTH_LONG,
        padding: PaddingValues = PaddingValues(top = 24.dp, bottom = 84.dp),
        contentAlignment: Alignment = Alignment.TopCenter,
    ) {
        val sweetInfoToast = CustomSweetToast(LocalContext.current)
        sweetInfoToast.MakeToast(
            message = message,
            duration = duration,
            type = Info(),
            padding = padding,
            contentAlignment = contentAlignment
        )
        sweetInfoToast.show()
    }

    @Composable
    fun SweetWarning(
        message: String,
        duration: Int = Toast.LENGTH_LONG,
        padding: PaddingValues = PaddingValues(top = 24.dp, bottom = 84.dp),
        contentAlignment: Alignment = Alignment.TopCenter,
    ) {
        val sweetWarningToast = CustomSweetToast(LocalContext.current)
        sweetWarningToast.MakeToast(
            message = message,
            duration = duration,
            type = Warning(),
            padding = padding,
            contentAlignment = contentAlignment
        )
        sweetWarningToast.show()
    }

    @Composable
    fun SetView(
        messageTxt: String,
        resourceIcon: Int,
        backgroundColor: Color,
        borderColor:Color,
        textColor: Color,
        padding: PaddingValues,
        contentAlignment: Alignment
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding),
            contentAlignment = contentAlignment
        ) {
            Surface(
                modifier = Modifier
                    .wrapContentSize(),
                color = Color.Transparent
            ) {
                Row(
                    modifier = Modifier//.clip(shape = RoundedCornerShape(8.dp))
                        .defaultMinSize(minHeight = 36.dp)
                        .fillMaxWidth()
                        .background(
                            color = backgroundColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .border(
                            width = 1.dp,
                            color = borderColor,
                            shape = RoundedCornerShape(16.dp)
                        )
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    
                    AppImage(id = resourceIcon)
                    
                    Spacer(modifier = Modifier.width(14.dp))
                    
                    AppMultilineText(modifier = Modifier.weight(1f), text = messageTxt, style = MaterialTheme.typography.labelMedium.copy(color = textColor))

                    Spacer(modifier = Modifier.width(14.dp))

                    ImageButton(icon = Icons.Default.Clear, iconColor = borderColor) {
                        ///
                    }
                }
            }
        }
    }
}

class CustomSweetToast(context: Context) : Toast(context) {
    @Composable
    fun MakeToast(
        message: String,
        duration: Int = LENGTH_LONG,
        type: SweetToastProperty,
        padding: PaddingValues,
        contentAlignment: Alignment,
        colorText: Color = type.getTextColor()
    ) {
        val context = LocalContext.current
        val views = ComposeView(context)

        views.setContent {

            SweetToastUtil.SetView(
                messageTxt = message,
                resourceIcon = type.getResourceId(),
                backgroundColor = type.getBackgroundColor(),
                borderColor=type.getBorderColor(),
                textColor =colorText,
                padding = padding,
                contentAlignment = contentAlignment
            )

        }

        views.setViewTreeSavedStateRegistryOwner(LocalSavedStateRegistryOwner.current)
        views.setViewTreeLifecycleOwner(LocalLifecycleOwner.current)
        views.setViewTreeViewModelStoreOwner(LocalViewModelStoreOwner.current)

        this.duration = duration
        this.view = views
    }
}

@Composable
fun ImageButton(modifier: Modifier = Modifier, icon: ImageVector, iconColor: Color = Color.White, callback: () -> Unit){
    Box(modifier = modifier
        .clickable {
            callback()
        })
    {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = iconColor,
            modifier = Modifier.size(18.dp),
        )
    }
}