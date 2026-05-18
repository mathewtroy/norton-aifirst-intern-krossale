package com.krossale.antiscam.ui

import androidx.lifecycle.ViewModel
import com.krossale.antiscam.data.ExampleMessage
import com.krossale.antiscam.domain.AnalysisLogEntry
import com.krossale.antiscam.domain.ScamAnalyzer
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class ScamDetectorViewModel : ViewModel() {

    private val analyzer = ScamAnalyzer()

    private val _uiState = MutableStateFlow(ScamDetectorUiState())
    val uiState: StateFlow<ScamDetectorUiState> = _uiState.asStateFlow()

    fun onInputChanged(text: String) {
        _uiState.value = _uiState.value.copy(inputText = text, analysisResult = null)
    }

    fun onExampleSelected(example: ExampleMessage) {
        _uiState.value = _uiState.value.copy(inputText = example.content, analysisResult = null)
    }

    fun onAnalyzeClicked() {
        val text = _uiState.value.inputText
        if (text.isBlank()) return
        val result = analyzer.analyze(text)
        val entry = AnalysisLogEntry(
            inputSnippet = text.take(60).trimEnd().let { if (text.length > 60) "$it…" else it },
            result = result
        )
        val current = _uiState.value
        _uiState.value = current.copy(
            analysisResult = result,
            history = listOf(entry) + current.history
        )
    }

    fun onClearHistory() {
        _uiState.value = _uiState.value.copy(history = emptyList())
    }
}
