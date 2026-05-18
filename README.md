# AntiScam

AI-first Android scam message detector prototype inspired by Norton Genie.

Repository: **norton-aifirst-intern-krossale**

---

# 1. Project Overview

AntiScam is an Android application prototype developed as part of the **Gen Digital AI-First Mobile Engineering Internship assignment**.

The application is inspired by **Norton Genie** and focuses on helping users identify potentially fraudulent content such as:

* SMS messages
* Email snippets
* Suspicious URLs
* Scam attempts using urgency or social engineering techniques

Users can paste suspicious content into the application and receive an AI-style risk assessment including:

* Risk level
* Confidence score
* Explanation of detected patterns

The project prioritizes:

* AI-assisted development workflow
* Clean architecture
* Readable code
* Testability
* Simple and maintainable design

---

# 2. Features

Current implemented features:

✅ Paste suspicious messages, URLs, or email snippets

✅ Analyze button for triggering scam analysis

✅ Risk level classification:

* Safe
* Suspicious
* Dangerous

✅ Confidence score

✅ Explanation of why the message was flagged

✅ Example scam messages with auto-fill functionality

✅ Local heuristic-based scam analysis

✅ Unit tests for business logic and ViewModel

---

# 3. Architecture (MVVM)

The application follows the **MVVM (Model–View–ViewModel)** architecture pattern.

### UI Layer

Responsible for:

* Displaying input field
* Displaying analysis results
* User interactions
* Example message selection

### ViewModel Layer

Responsible for:

* Managing UI state
* Handling user actions
* Triggering analysis
* Updating screen state

### Domain Layer

Responsible for:

* Scam detection logic
* Risk calculations
* Confidence calculation
* Explanation generation

---

## Data Flow

```text
User Input
    ↓
Compose UI
    ↓
ScamDetectorViewModel
    ↓
ScamAnalyzer
    ↓
ScamAnalysisResult
    ↓
UI updates automatically
```

---

# 4. Project Structure

```text
norton-aifirst-intern-krossale/
│
├── README.md
│
├── app/
│   └── src/
│       ├── main/
│       │   └── java/com/krossale/antiscam/
│       │
│       │   ├── data/
│       │   │   └── ExampleMessages.kt
│       │   │
│       │   ├── domain/
│       │   │   ├── ScamAnalyzer.kt
│       │   │   ├── ScamAnalysisResult.kt
│       │   │   └── RiskLevel.kt
│       │   │
│       │   ├── ui/
│       │   │   ├── ScamDetectorScreen.kt
│       │   │   ├── ScamDetectorViewModel.kt
│       │   │   └── ScamDetectorUiState.kt
│       │   │
│       │   └── theme/
│       │
│       └── test/
│           ├── ScamAnalyzerTest.kt
│           └── ScamDetectorViewModelTest.kt
│
└── build.gradle.kts
```

---

# 5. Setup Instructions

## Requirements

* Ubuntu/Linux (developed on Ubuntu)
* Android Studio
* Android SDK
* JDK 17+
* Gradle

---

Clone repository:

```bash
git clone https://github.com/mathewtroy/norton-aifirst-intern-krossale.git
```

Move into project:

```bash
cd norton-aifirst-intern-krossale
```

---

# 6. Build and Run Instructions

Open project:

```text
Android Studio → Open Existing Project
```

Allow Gradle synchronization.

Run application:

```text
Select emulator/device
↓
Click Run
```

Or use Gradle:

```bash
./gradlew assembleDebug
```

Run tests:

```bash
./gradlew test
```

---

# 7. Screenshots

## Home Screen

[Insert screenshot here]

---

## Analysis Result Screen

[Insert screenshot here]

---

## Example Scam Messages

[Insert screenshot here]

---

# 8. Testing

The project contains unit tests covering:

### ScamAnalyzer tests

* Dangerous message detection
* Safe message detection
* Suspicious URL detection

### ViewModel tests

* Example message selection
* Analyze action updates UI state

Run tests:

```bash
./gradlew test
```

---

# 9. AI Interaction Log

This project follows an AI-first development workflow.

## Prompt #1

**Prompt:**

> [Insert prompt]

**Result:**

[Insert result]

**Notes:**

[Insert refinement notes]

---

## Prompt #2

**Prompt:**

> [Insert prompt]

**Result:**

[Insert result]

**Notes:**

[Insert refinement notes]

---

## Prompt #3

**Prompt:**

> [Insert prompt]

**Result:**

[Insert result]

**Notes:**

[Insert refinement notes]

---

## Prompt #4

**Prompt:**

> [Insert prompt]

**Result:**

[Insert result]

**Notes:**

[Insert refinement notes]

---

## Prompt #5

**Prompt:**

> [Insert prompt]

**Result:**

[Insert result]

**Notes:**

[Insert refinement notes]

---

# 10. AI Code Review Summary

## AI feedback received

* [Feedback item]
* [Feedback item]
* [Feedback item]

### Changes made after review

* [Change]
* [Change]
* [Change]

### Rejected suggestions

* [Rejected suggestion and reason]

---

# 11. Reflection

## What I learned

* [Learning point]
* [Learning point]
* [Learning point]

### What worked well

* [Point]

### What I would improve with more time

* Add richer UI animations
* Improve detection logic using real AI APIs
* Add persistence and history functionality
* Expand test coverage

---

# 12. Author

**Name:** Your Name

**GitHub:** https://github.com/mathewtroy

**Project:** Gen Digital AI-First Internship Assignment

---
