package com.krossale.antiscam.domain

class ScamAnalyzer {

    private data class Category(
        val name: String,
        val keywords: List<String>,
        val baseScore: Int,
        val perExtraScore: Int,
        val maxScore: Int
    )

    private val urgency = Category(
        name = "urgency indicators",
        keywords = listOf(
            "urgent", "immediately", "act now", "expires today", "final warning",
            "last chance", "within 24 hours", "right away", "do not delay", "respond now"
        ),
        baseScore = 12, perExtraScore = 4, maxScore = 25
    )

    private val credentials = Category(
        name = "credential requests",
        keywords = listOf(
            "password", "login", "verify account", "verify your email",
            "security code", "confirm identity", "confirm account",
            "account suspended", "unusual activity on your account",
            "click here to unlock"
        ),
        baseScore = 20, perExtraScore = 5, maxScore = 35
    )

    private val financial = Category(
        name = "financial indicators",
        keywords = listOf(
            "bank account", "payment", "transaction", "wire transfer", "credit card",
            "debit card", "bitcoin", "cryptocurrency", "gift card",
            "western union", "social security", "ssn"
        ),
        baseScore = 12, perExtraScore = 4, maxScore = 25
    )

    private val prize = Category(
        name = "prize or reward claims",
        keywords = listOf(
            "congratulations", "you have won", "you've won", "winner", "prize",
            "lottery", "reward", "claim prize", "free gift",
            "you have been selected", "claim your", "won a"
        ),
        baseScore = 12, perExtraScore = 4, maxScore = 25
    )

    private val allCategories = listOf(urgency, credentials, financial, prize)

    private val suspiciousUrlPatterns = listOf(
        Regex("""https?://\d+\.\d+\.\d+\.\d+"""),
        Regex("""bit\.ly|tinyurl\.com|goo\.gl|short\.link|t\.co"""),
        Regex("""[a-z0-9\-]+\.(xyz|top|click|link|work|loan|win|cf|tk|ml|ga|gq)/""")
    )

    private val suspiciousPhrases = listOf(
        "click here", "click this link", "follow this link",
        "limited time offer", "exclusive offer", "security alert",
        "unusual sign-in", "login immediately", "update your information"
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

        val categoryHits: Map<Category, List<String>> = allCategories.associateWith { cat ->
            cat.keywords.filter { lower.contains(it) }
        }

        val urlHits = suspiciousUrlPatterns.sumOf { it.findAll(lower).count() }
        val phraseHits = suspiciousPhrases.filter { lower.contains(it) }

        var score = 0
        val triggeredCategoryNames = mutableListOf<String>()

        for ((cat, hits) in categoryHits) {
            if (hits.isNotEmpty()) {
                score += minOf(cat.maxScore, cat.baseScore + (hits.size - 1) * cat.perExtraScore)
                triggeredCategoryNames.add(cat.name)
            }
        }

        if (urlHits > 0) {
            score += minOf(40, 25 + (urlHits - 1) * 8)
        }

        if (phraseHits.isNotEmpty()) {
            score += minOf(15, 8 + (phraseHits.size - 1) * 3)
        }

        val hasUrgency = categoryHits[urgency]?.isNotEmpty() == true
        val hasCredentials = categoryHits[credentials]?.isNotEmpty() == true
        val hasFinancial = categoryHits[financial]?.isNotEmpty() == true
        val hasPrize = categoryHits[prize]?.isNotEmpty() == true
        val hasUrl = urlHits > 0

        val bonuses = mutableListOf<String>()
        if (hasCredentials && hasUrl) {
            score += 20
            bonuses.add("credential request with suspicious link")
        }
        if (hasUrgency && hasFinancial) {
            score += 20
            bonuses.add("urgency combined with financial request")
        }
        if (hasPrize && hasUrl) {
            score += 15
            bonuses.add("prize claim with suspicious link")
        }
        if (hasCredentials && hasFinancial) {
            score += 15
            bonuses.add("credential request with financial indicator")
        }

        return when {
            score >= 45 -> ScamAnalysisResult(
                riskLevel = RiskLevel.DANGEROUS,
                confidenceScore = minOf(97, 70 + (score - 45)),
                explanation = buildExplanation(triggeredCategoryNames, hasUrl, phraseHits, bonuses, RiskLevel.DANGEROUS)
            )
            score >= 20 -> ScamAnalysisResult(
                riskLevel = RiskLevel.SUSPICIOUS,
                confidenceScore = minOf(79, 50 + (score - 20)),
                explanation = buildExplanation(triggeredCategoryNames, hasUrl, phraseHits, bonuses, RiskLevel.SUSPICIOUS)
            )
            else -> ScamAnalysisResult(
                riskLevel = RiskLevel.SAFE,
                confidenceScore = 93,
                explanation = "No known scam indicators detected. This message appears safe."
            )
        }
    }

    private fun buildExplanation(
        categories: List<String>,
        hasUrl: Boolean,
        phrases: List<String>,
        bonuses: List<String>,
        riskLevel: RiskLevel
    ): String {
        val parts = mutableListOf<String>()

        val detected = categories.toMutableList()
        if (hasUrl) detected.add("suspicious URL patterns")
        if (detected.isNotEmpty()) {
            parts.add("Detected: ${detected.joinToString(", ")}.")
        }

        if (phrases.isNotEmpty()) {
            parts.add("Also found: ${phrases.take(2).joinToString(", ")}.")
        }

        if (bonuses.isNotEmpty()) {
            parts.add("High-risk combination: ${bonuses.joinToString("; ")}.")
        }

        parts.add(
            when (riskLevel) {
                RiskLevel.DANGEROUS -> "This message shows strong signs of a phishing or scam attempt. Do not click any links or provide personal information."
                RiskLevel.SUSPICIOUS -> "Proceed with caution. Verify the sender through an official channel before taking any action."
                RiskLevel.SAFE -> "No known scam indicators detected. This message appears safe."
            }
        )

        return parts.joinToString(" ")
    }
}
