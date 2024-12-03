package com.deema.sdkandroid

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.deema.sdkandroid.ui.theme.DeemaSDKAndroidTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        //enableEdgeToEdge()

        setContent {
            val context = LocalContext.current
            ///
            var customerPhone by remember { mutableStateOf("96599123444") }
            var currency by remember { mutableStateOf("kwd") }
            var purchaseAmount by remember { mutableStateOf("20") }
            var SDKKey by remember { mutableStateOf("sk_test_d5gntxxdoRNGkAweKjWZMr8iocXd3oNO1Wz5VJuW_65") }
            var merchantOrderId by remember { mutableStateOf("1726") }

            DeemaSDKAndroidTheme {
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    topBar = {
                        Box(modifier = Modifier.fillMaxWidth().height(48.dp).background(Color(0xffF18E9B)), contentAlignment = Alignment.Center) {
                            Text("DeemaSDKV1 Merchant Example", color = Color.White)
                        }
                    },
                ) {
                    innerPadding ->
                    Box(modifier = Modifier.padding(innerPadding)) {

                        Column(modifier = Modifier
                            .fillMaxSize()
                            .padding(horizontal = 28.dp, vertical = 20.dp), horizontalAlignment =Alignment.CenterHorizontally) {
                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = customerPhone,
                                onValueChange = { customerPhone = it },
                                label = { Text("Customer Phone") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = currency,
                                onValueChange = { currency = it },
                                label = { Text("Currency: (kwd or bhd)") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = purchaseAmount,
                                onValueChange = { purchaseAmount = it },
                                label = { Text("Purchase Amount") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = SDKKey,
                                onValueChange = { SDKKey = it },
                                label = { Text("SDK Key") }
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            OutlinedTextField(
                                modifier = Modifier.fillMaxWidth(),
                                value = merchantOrderId,
                                onValueChange = { merchantOrderId = it },
                                label = { Text("Merchant Order Id") }
                            )

                            Spacer(modifier = Modifier.height(20.dp))

                            Button(onClick = {
                                context.startActivity(Intent(this@MainActivity, com.deema.v1.MainActivity::class.java).also {
                                    it.putExtra("customerPhone", customerPhone)
                                    it.putExtra("currency", currency.uppercase())
                                    it.putExtra("purchaseAmount", purchaseAmount)
                                    it.putExtra("SDKKey", SDKKey)
                                    it.putExtra("merchantOrderId", merchantOrderId)
                                })
//                            activity.startActivity(Intent(this@MainActivity, com.deema.MainActivity::class.java))
                            }) {
                                Text("Submit")
                            }
                        }
                    }
                }
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    DeemaSDKAndroidTheme {
    }
}