package com.krossale.antiscam.data

data class ExampleMessage(
    val label: String,
    val content: String
)

val exampleMessages = listOf(
    ExampleMessage(
        label = "Bank Alert Scam",
        content = "URGENT: Your bank account has been suspended due to unusual activity. " +
                "Verify your account password immediately to avoid permanent closure: " +
                "http://secure-bank-verify.xyz/login"
    ),
    ExampleMessage(
        label = "Prize Winner Scam",
        content = "Congratulations! You've won a \$1,000 Amazon gift card. " +
                "You have been selected as our lucky winner. " +
                "Click here to claim your prize before it expires: bit.ly/claim-prize-now"
    ),
    ExampleMessage(
        label = "Safe Message",
        content = "Hey, are we still on for lunch tomorrow? Let me know if the time works for you."
    )
)
