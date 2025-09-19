package com.example.loamtp1.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.systemBars
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import com.example.loamtp1.ui.auxiliar.ConversorUnidades
import com.example.loamtp1.ui.principal.AppFooter
import com.example.loamtp1.ui.principal.AppHeader
import com.example.loamtp1.ui.principal.AppTheme

class UnidadesActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            AppTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Box(
                        Modifier
                            .fillMaxSize()
                            .padding(WindowInsets.systemBars.asPaddingValues())
                    ) {
                        Column(modifier = Modifier.fillMaxSize()) {
                            AppHeader(Modifier.weight(1f))
                            ConversorUnidades()
                            AppFooter(Modifier.weight(1f))
                        }
                    }
                }
            }
        }
    }
}
