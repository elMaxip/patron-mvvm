package com.example.patronmvvn.view

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.patronmvvn.viewmodels.ContadorViewModel

@Composable
@Preview
fun ContadorScreen(
    viewModel: ContadorViewModel = viewModel()
) {
    val contador by viewModel.contador.collectAsStateWithLifecycle()

    Column(
        modifier = Modifier.systemBarsPadding()
    ) {
        Text("Contador: ${contador.valor}")

        Button(
            onClick = { viewModel.incrementar() }
        ) {
            Text("Incrementar")
        }

        Button(
            onClick = { viewModel.decrementar() }
        ) {
            Text("Decrementar")
        }
    }
}
