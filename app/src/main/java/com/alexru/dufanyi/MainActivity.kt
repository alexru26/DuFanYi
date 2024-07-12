package com.alexru.dufanyi

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.alexru.dufanyi.database.getSeriesDatabase
import com.alexru.dufanyi.ui.theme.DuFanYiAppTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val dao = getSeriesDatabase(applicationContext).seriesDao()
        setContent {
            DuFanYiAppTheme {
                ChineseSupportReaderApp(
                    seriesDao = dao
                )
            }
        }
    }
}