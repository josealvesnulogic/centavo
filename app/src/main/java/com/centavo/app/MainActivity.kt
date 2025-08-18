package com.centavo.app

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.centavo.app.ui.CentavoApp
import com.centavo.app.ui.theme.CentavoTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            CentavoTheme {
                CentavoApp()
            }
        }
    }
}