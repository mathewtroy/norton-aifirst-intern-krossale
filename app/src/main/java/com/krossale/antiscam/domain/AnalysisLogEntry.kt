package com.krossale.antiscam.domain

data class AnalysisLogEntry(
    val id: Long = System.currentTimeMillis(),
    val inputSnippet: String,
    val result: ScamAnalysisResult
)
