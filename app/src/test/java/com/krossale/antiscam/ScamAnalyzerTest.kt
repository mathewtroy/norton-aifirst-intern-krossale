package com.krossale.antiscam

import com.krossale.antiscam.domain.RiskLevel
import com.krossale.antiscam.domain.ScamAnalyzer
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test

class ScamAnalyzerTest {

    private lateinit var analyzer: ScamAnalyzer

    @Before
    fun setUp() {
        analyzer = ScamAnalyzer()
    }

    @Test
    fun `safe message returns Safe risk level`() {
        val result = analyzer.analyze("Hey, are we still on for lunch tomorrow?")
        assertEquals(RiskLevel.SAFE, result.riskLevel)
    }

    @Test
    fun `phishing message with password and account keywords returns Dangerous`() {
        val result = analyzer.analyze(
            "Your bank account has been suspended. Verify your account password immediately."
        )
        assertEquals(RiskLevel.DANGEROUS, result.riskLevel)
    }

    @Test
    fun `message with click here and exclusive offer returns Suspicious`() {
        val result = analyzer.analyze(
            "Click here to verify your email and claim your exclusive offer."
        )
        assertEquals(RiskLevel.SUSPICIOUS, result.riskLevel)
    }

    @Test
    fun `empty message returns Safe with confidence 50`() {
        val result = analyzer.analyze("")
        assertEquals(RiskLevel.SAFE, result.riskLevel)
        assertEquals(50, result.confidenceScore)
    }

    // AI-generated test — reviewed and refined by developer
    @Test
    fun `dangerous message confidence score is between 70 and 97`() {
        val result = analyzer.analyze(
            "Send your credit card details and password now. Bitcoin transfer required. gift card."
        )
        assertEquals(RiskLevel.DANGEROUS, result.riskLevel)
        assertTrue(
            "Confidence should be between 70 and 97, was ${result.confidenceScore}",
            result.confidenceScore in 70..97
        )
    }

    @Test
    fun `safe message has non-empty explanation`() {
        val result = analyzer.analyze("Good morning! How are you doing today?")
        assertTrue(result.explanation.isNotBlank())
    }

    @Test
    fun `prize scam message returns Dangerous`() {
        val result = analyzer.analyze(
            "Congratulations! You've won a \$1000 gift card. Click here to claim your prize: bit.ly/win"
        )
        assertEquals(RiskLevel.DANGEROUS, result.riskLevel)
    }
}
