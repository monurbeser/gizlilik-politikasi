package com.notaday

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.material3.Surface
import com.notaday.presentation.navigation.NotadayNavHost
import com.notaday.theme.NotadayTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            NotadayTheme {
                Surface {
                    NotadayNavHost(initialNoteId = intent?.getLongExtra("note_id", 0L) ?: 0L)
                }
            }
        }
    }
}
