package com.krossale.antiscam.ui

import com.krossale.antiscam.domain.AnalysisLogEntry
import com.krossale.antiscam.domain.ScamAnalysisResult

data class ScamDetectorUiState(
    val inputText: String = "",
    val analysisResult: ScamAnalysisResult? = null,
    val history: List<AnalysisLogEntry> = emptyList()
)
