package com.example.patronmvvn.viewmodels

import androidx.lifecycle.ViewModel
import com.example.patronmvvn.models.Contador
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ContadorViewModel : ViewModel() {
    private val _contador = MutableStateFlow(Contador())
    val contador = _contador.asStateFlow()

    fun incrementar() {
        _contador.update { actual -> actual.copy(valor = actual.valor + 1) }
    }

    fun decrementar() {
        _contador.update { actual ->
            actual.copy(valor = actual.valor - 1)
        }
    }
}