package com.krossale.antiscam

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.krossale.antiscam.theme.AntiScamTheme
import com.krossale.antiscam.ui.ScamDetectorScreen

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            AntiScamTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    ScamDetectorScreen()
                }
            }
        }
    }
}
