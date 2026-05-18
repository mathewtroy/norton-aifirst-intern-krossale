package com.krossale.antiscam.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.krossale.antiscam.data.ExampleMessage
import com.krossale.antiscam.data.exampleMessages
import com.krossale.antiscam.domain.AnalysisLogEntry
import com.krossale.antiscam.domain.RiskLevel
import com.krossale.antiscam.domain.ScamAnalysisResult
import com.krossale.antiscam.theme.AntiScamTheme
import com.krossale.antiscam.theme.Blue
import com.krossale.antiscam.theme.Cream
import com.krossale.antiscam.theme.DangerousRed
import com.krossale.antiscam.theme.LightGray
import com.krossale.antiscam.theme.MediumGray
import com.krossale.antiscam.theme.Navy
import com.krossale.antiscam.theme.SafeGreen
import com.krossale.antiscam.theme.SuspiciousAmber
import com.krossale.antiscam.theme.White
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ScamDetectorScreen(viewModel: ScamDetectorViewModel = viewModel()) {
    val uiState by viewModel.uiState.collectAsState()

    ScamDetectorContent(
        uiState = uiState,
        onInputChanged = viewModel::onInputChanged,
        onExampleSelected = viewModel::onExampleSelected,
        onAnalyzeClicked = viewModel::onAnalyzeClicked,
        onClearHistory = viewModel::onClearHistory
    )
}

@Composable
fun ScamDetectorContent(
    uiState: ScamDetectorUiState,
    onInputChanged: (String) -> Unit,
    onExampleSelected: (ExampleMessage) -> Unit,
    onAnalyzeClicked: () -> Unit,
    onClearHistory: () -> Unit = {}
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(LightGray)
            .statusBarsPadding()
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AppHeader()

        InputSection(
            inputText = uiState.inputText,
            onInputChanged = onInputChanged
        )

        ExampleMessagesSection(onExampleSelected = onExampleSelected)

        Button(
            onClick = onAnalyzeClicked,
            enabled = uiState.inputText.isNotBlank(),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue)
        ) {
            Text(
                text = "Analyze",
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
        }

        uiState.analysisResult?.let { result ->
            ResultCard(result = result)
        }

        if (uiState.history.isNotEmpty()) {
            HistorySection(
                history = uiState.history,
                onClearHistory = onClearHistory
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun AppHeader() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Navy)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 18.dp)
        ) {
            Text(
                text = "AntiScam",
                fontSize = 26.sp,
                fontWeight = FontWeight.Bold,
                color = White
            )
            Text(
                text = "Paste a message or URL to check for scam indicators",
                fontSize = 13.sp,
                color = Cream.copy(alpha = 0.75f),
                lineHeight = 18.sp
            )
        }
    }
}

@Composable
private fun InputSection(
    inputText: String,
    onInputChanged: (String) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Suspicious message, URL, or email",
                fontWeight = FontWeight.SemiBold,
                fontSize = 14.sp,
                color = Navy
            )
            OutlinedTextField(
                value = inputText,
                onValueChange = onInputChanged,
                modifier = Modifier
                    .fillMaxWidth()
                    .heightIn(min = 120.dp),
                placeholder = {
                    Text(
                        text = "Paste content here to analyze...",
                        color = MediumGray,
                        fontSize = 14.sp
                    )
                },
                shape = RoundedCornerShape(12.dp),
                maxLines = 10
            )
        }
    }
}

@Composable
private fun ExampleMessagesSection(onExampleSelected: (ExampleMessage) -> Unit) {
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        Text(
            text = "Try an example",
            fontWeight = FontWeight.SemiBold,
            fontSize = 14.sp,
            color = Navy
        )
        LazyRow(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
            items(exampleMessages) { example ->
                OutlinedButton(
                    onClick = { onExampleSelected(example) },
                    shape = RoundedCornerShape(20.dp),
                    border = BorderStroke(1.dp, Blue.copy(alpha = 0.6f)),
                    contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp)
                ) {
                    Text(text = example.label, fontSize = 13.sp, color = Blue)
                }
            }
        }
    }
}

@Composable
private fun ResultCard(result: ScamAnalysisResult) {
    val riskColor = riskColor(result.riskLevel)
    val riskLabel = riskLabel(result.riskLevel)

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White),
        border = BorderStroke(2.dp, riskColor.copy(alpha = 0.45f))
    ) {
        Column(
            modifier = Modifier.padding(20.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            Text(
                text = "Analysis Result",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = Navy
            )

            Surface(
                color = riskColor.copy(alpha = 0.12f),
                shape = RoundedCornerShape(8.dp)
            ) {
                Text(
                    text = riskLabel,
                    color = riskColor,
                    fontWeight = FontWeight.Bold,
                    fontSize = 18.sp,
                    modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
                )
            }

            Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "Confidence", color = MediumGray, fontSize = 13.sp)
                    Text(
                        text = "${result.confidenceScore}%",
                        fontWeight = FontWeight.SemiBold,
                        color = Navy,
                        fontSize = 13.sp
                    )
                }
                LinearProgressIndicator(
                    progress = { result.confidenceScore / 100f },
                    modifier = Modifier.fillMaxWidth().height(6.dp),
                    color = riskColor,
                    trackColor = riskColor.copy(alpha = 0.18f)
                )
            }

            HorizontalDivider(color = LightGray, thickness = 1.dp)

            Text(
                text = result.explanation,
                color = Navy.copy(alpha = 0.8f),
                fontSize = 14.sp,
                lineHeight = 21.sp
            )
        }
    }
}

@Composable
private fun HistorySection(
    history: List<AnalysisLogEntry>,
    onClearHistory: () -> Unit
) {
    var expanded by remember { mutableStateOf(true) }
    val timeFormatter = remember { SimpleDateFormat("HH:mm:ss", Locale.getDefault()) }

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = White)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Analysis Log  (${history.size})",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    color = Navy
                )
                Row {
                    TextButton(
                        onClick = { expanded = !expanded },
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = if (expanded) "Hide" else "Show",
                            fontSize = 12.sp,
                            color = Blue
                        )
                    }
                    TextButton(
                        onClick = onClearHistory,
                        contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(text = "Clear", fontSize = 12.sp, color = MediumGray)
                    }
                }
            }

            AnimatedVisibility(
                visible = expanded,
                enter = expandVertically(),
                exit = shrinkVertically()
            ) {
                Column(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    history.forEach { entry ->
                        HistoryEntryRow(entry = entry, timeFormatter = timeFormatter)
                        if (entry != history.last()) {
                            HorizontalDivider(color = LightGray, thickness = 0.5.dp)
                        }
                    }
                }
            }
        }
    }
}

@Composable
private fun HistoryEntryRow(
    entry: AnalysisLogEntry,
    timeFormatter: SimpleDateFormat
) {
    val color = riskColor(entry.result.riskLevel)
    val label = riskLabel(entry.result.riskLevel)

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Surface(
            modifier = Modifier.size(10.dp),
            shape = CircleShape,
            color = color
        ) {}

        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = entry.inputSnippet,
                fontSize = 13.sp,
                color = Navy,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis
            )
            Text(
                text = "$label · ${entry.result.confidenceScore}% · ${timeFormatter.format(Date(entry.id))}",
                fontSize = 11.sp,
                color = MediumGray
            )
        }
    }
}

private fun riskColor(level: RiskLevel) = when (level) {
    RiskLevel.SAFE -> SafeGreen
    RiskLevel.SUSPICIOUS -> SuspiciousAmber
    RiskLevel.DANGEROUS -> DangerousRed
}

private fun riskLabel(level: RiskLevel) = when (level) {
    RiskLevel.SAFE -> "Safe"
    RiskLevel.SUSPICIOUS -> "Suspicious"
    RiskLevel.DANGEROUS -> "Dangerous"
}

@Preview(showBackground = true, backgroundColor = 0xFFF2F4F7)
@Composable
private fun PreviewSafe() {
    AntiScamTheme {
        ScamDetectorContent(
            uiState = ScamDetectorUiState(inputText = "See you at lunch!"),
            onInputChanged = {},
            onExampleSelected = {},
            onAnalyzeClicked = {}
        )
    }
}
