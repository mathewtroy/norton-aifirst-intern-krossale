package com.krossale.antiscam.domain

class ScamAnalyzer {

    private val dangerousKeywords = listOf(
        "password", "bank account", "credit card", "debit card",
        "verify your account", "confirm your identity",
        "bitcoin", "crypto", "gift card", "wire transfer",
        "social security", "ssn",
        "you have won", "you've won", "winner", "prize", "lottery",
        "account suspended", "click here to unlock",
        "western union", "login immediately",
        "unusual activity on your account"
    )

    private val suspiciousKeywords = listOf(
        "click here", "click this link", "follow this link",
        "limited time offer", "act now", "expires soon", "exclusive offer",
        "update your information", "verify your email",
        "security alert", "unusual sign-in",
        "free gift", "you have been selected", "confirm your account"
    )

    private val suspiciousUrlPatterns = listOf(
        Regex("""https?://\d+\.\d+\.\d+\.\d+"""),
        Regex("""bit\.ly|tinyurl\.com|goo\.gl|short\.link|t\.co"""),
        Regex("""[a-z0-9\-]+\.(xyz|top|click|link|work|loan|win|cf|tk|ml|ga|gq)/""")
    )

    fun analyze(message: String): ScamAnalysisResult {
        val lower = message.lowercase().trim()

        if (lower.isEmpty()) {
            return ScamAnalysisResult(
                riskLevel = RiskLevel.SAFE,
                confidenceScore = 50,
                explanation = "No message provided to analyze."
            )
        }

        val dangerousHits = dangerousKeywords.filter { lower.contains(it) }
        val suspiciousHits = suspiciousKeywords.filter { lower.contains(it) }
        val urlHits = suspiciousUrlPatterns.filter { it.containsMatchIn(lower) }

        val urlHitCount = urlHits.size

        return when {
            dangerousHits.isNotEmpty() || urlHitCount >= 2 -> {
                val score = minOf(97, 70 + dangerousHits.size * 8 + urlHitCount * 6)
                ScamAnalysisResult(
                    riskLevel = RiskLevel.DANGEROUS,
                    confidenceScore = score,
                    explanation = buildExplanation(dangerousHits, urlHitCount, dangerous = true)
                )
            }
            suspiciousHits.isNotEmpty() || urlHitCount > 0 -> {
                val score = minOf(85, 50 + suspiciousHits.size * 6 + urlHitCount * 12)
                ScamAnalysisResult(
                    riskLevel = RiskLevel.SUSPICIOUS,
                    confidenceScore = score,
                    explanation = buildExplanation(suspiciousHits, urlHitCount, dangerous = false)
                )
            }
            else -> ScamAnalysisResult(
                riskLevel = RiskLevel.SAFE,
                confidenceScore = 93,
                explanation = "No known scam indicators detected. This message appears safe."
            )
        }
    }

    private fun buildExplanation(keywords: List<String>, urlCount: Int, dangerous: Boolean): String {
        val parts = mutableListOf<String>()
        if (keywords.isNotEmpty()) {
            val label = if (dangerous) "high-risk keywords" else "suspicious phrases"
            parts.add("Detected $label: ${keywords.take(3).joinToString(", ")}.")
        }
        if (urlCount > 0) {
            parts.add("Contains suspicious URL patterns commonly used in phishing.")
        }
        val conclusion = if (dangerous) {
            "This message shows strong signs of a phishing or scam attempt. Do not click any links or provide personal information."
        } else {
            "Proceed with caution. Verify the sender through an official channel before taking any action."
        }
        parts.add(conclusion)
        return parts.joinToString(" ")
    }
}
