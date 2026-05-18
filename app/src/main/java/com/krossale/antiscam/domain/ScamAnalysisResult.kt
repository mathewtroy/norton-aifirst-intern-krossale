package com.krossale.antiscam.domain

data class ScamAnalysisResult(
    val riskLevel: RiskLevel,
    val confidenceScore: Int,
    val explanation: String
)
