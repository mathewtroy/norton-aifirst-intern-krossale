package com.krossale.antiscam

import com.krossale.antiscam.data.ExampleMessage
import com.krossale.antiscam.domain.RiskLevel
import com.krossale.antiscam.ui.ScamDetectorViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test

class ScamDetectorViewModelTest {

    private lateinit var viewModel: ScamDetectorViewModel

    @Before
    fun setUp() {
        viewModel = ScamDetectorViewModel()
    }

    @Test
    fun `initial state has empty input and no result`() {
        val state = viewModel.uiState.value
        assertEquals("", state.inputText)
        assertNull(state.analysisResult)
    }

    @Test
    fun `onInputChanged updates input text and clears result`() {
        viewModel.onInputChanged("test input")
        val state = viewModel.uiState.value
        assertEquals("test input", state.inputText)
        assertNull(state.analysisResult)
    }

    // AI-generated test — reviewed and refined by developer
    @Test
    fun `selecting example message updates input text`() {
        val example = ExampleMessage(
            label = "Test Scam",
            content = "Verify your account password immediately."
        )
        viewModel.onExampleSelected(example)
        assertEquals(example.content, viewModel.uiState.value.inputText)
    }

    @Test
    fun `analyze on blank input does not produce a result`() {
        viewModel.onInputChanged("   ")
        viewModel.onAnalyzeClicked()
        assertNull(viewModel.uiState.value.analysisResult)
    }

    @Test
    fun `analyze action updates analysis result in ui state`() {
        viewModel.onInputChanged("Claim your gift card prize now! Click here.")
        viewModel.onAnalyzeClicked()
        assertNotNull(viewModel.uiState.value.analysisResult)
    }

    @Test
    fun `dangerous message through viewmodel produces Dangerous risk level`() {
        viewModel.onInputChanged(
            "Your bank account is suspended. Verify your account password now or lose access."
        )
        viewModel.onAnalyzeClicked()
        assertEquals(RiskLevel.DANGEROUS, viewModel.uiState.value.analysisResult?.riskLevel)
    }

    @Test
    fun `changing input after analysis clears previous result`() {
        viewModel.onInputChanged("Your password has been compromised")
        viewModel.onAnalyzeClicked()
        assertNotNull(viewModel.uiState.value.analysisResult)

        viewModel.onInputChanged("new message")
        assertNull(viewModel.uiState.value.analysisResult)
    }

    @Test
    fun `analyze appends entry to history`() {
        viewModel.onInputChanged("Claim your gift card now")
        viewModel.onAnalyzeClicked()
        assertEquals(1, viewModel.uiState.value.history.size)
    }

    @Test
    fun `multiple analyses build history in reverse order`() {
        viewModel.onInputChanged("first message")
        viewModel.onAnalyzeClicked()
        viewModel.onInputChanged("second message with bitcoin prize")
        viewModel.onAnalyzeClicked()
        val history = viewModel.uiState.value.history
        assertEquals(2, history.size)
        assertEquals("second message with bitcoin prize", history.first().inputSnippet)
    }

    @Test
    fun `clear history empties the log`() {
        viewModel.onInputChanged("your password is stolen")
        viewModel.onAnalyzeClicked()
        viewModel.onClearHistory()
        assertEquals(0, viewModel.uiState.value.history.size)
    }
}
