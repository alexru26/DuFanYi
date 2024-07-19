package com.alexru.dufanyi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexru.dufanyi.ui.DuFanYiApp
import com.alexru.dufanyi.ui.theme.DuFanYiAppTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            DuFanYiAppTheme {
                DuFanYiApp()
            }
        }
    }
}