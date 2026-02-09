# Architecture & Module Responsibilities

Buxxy follows a **modular, layered architecture** designed to keep fraud detection logic isolated, explainable, and easy to integrate with external transaction systems.

Each module has a **single, well-defined responsibility**, making the engine easier to extend, audit, and maintain.

---

## High-Level Architecture Overview

Buxxy operates as a **decision engine** that sits alongside an external transaction system.

**High-level flow:**

1. External system sends user and transaction data to Buxxy
2. Request is authenticated using API key and headers
3. Fraud rules are applied through multiple scans
4. A risk score is evaluated
5. A final decision is generated
6. Decision is returned to the external system
7. All actions are audited

Buxxy does **not** execute transactions — it only evaluates risk.

---

## Core Architectural Layers

### 1. API & Integration Layer

**Responsibility:**
- Accept incoming requests from external systems
- Validate API keys and request headers
- Act as the entry point into the fraud engine

**Why it exists:**
- Ensures only trusted systems can interact with Buxxy
- Keeps integration concerns separate from fraud logic

---

### 2. Context Extraction Layer

**Key Components:**
- DeviceContextExtractor

**Responsibility:**
- Extract contextual data from requests, such as:
  - User-Agent
  - Timezone
  - Language
  - IP address

**Why it exists:**
- Converts raw HTTP data into meaningful signals
- Feeds downstream fraud detection modules

---

### 3. Rule Engine & Scan Layer

**Key Component:**
- RuleApplyingService

**Responsibility:**
- Apply fraud detection rules to transactions
- Execute multiple scans independently
- Return a boolean or score indicating suspicious behavior

**Current Scans Include:**
- High Amount Detection
- Transaction Velocity
- Location Anomaly
- Time Window Analysis
- Device Change Detection
- IP Address Change Detection

Each scan focuses on **one fraud pattern**, keeping logic isolated and testable.

---

### 4. User Profiling & Dynamic Rules Layer

**Key Components:**
- UserProfileService
- UserRuleProfile

**Responsibility:**
- Maintain behavioral profiles per user
- Track:
  - Average transaction amount
  - Transaction frequency
  - Usual locations
- Dynamically adjust thresholds over time

**Why it exists:**
- Prevents rigid, one-size-fits-all rules
- Allows fraud detection to adapt to user behavior

---

### 5. Device Intelligence Layer

**Key Components:**
- DetectionService
- Device
- DeviceIpHistory

**Responsibility:**
- Detect new or mismatched devices
- Track device fingerprints using:
  - User-Agent
  - Timezone
  - Language
- Detect IP anomalies per device

**Why it exists:**
- Device consistency is a strong fraud signal
- Helps identify account takeovers and impersonation

---

### 6. Geo-Location & Movement Analysis Layer

**Key Components:**
- GeoLocationService
- CalculateDistance

**Responsibility:**
- Resolve IP addresses using MMDB
- Detect:
  - Country and city anomalies
  - New locations
  - Impossible travel between transactions

**Why it exists:**
- Location-based fraud is common in online transactions
- Movement speed checks catch impossible behavior

---

### 7. Decision Evaluation Layer

**Responsibility:**
- Aggregate scan results
- Convert risk signals into a final decision:
  - Legitimate
  - Suspicious (step-up required)
  - Fraudulent (block recommended)

**Important Note:**
- Decisions are **recommendations**
- External systems always take the final action

---

### 8. Audit & Observability Layer

**Responsibility:**
- Log:
  - Scan results
  - Decisions
  - Actions taken
- Enable post-analysis and rule tuning
- Verify external system behavior

**Why it exists:**
- Improves explainability
- Supports compliance and investigations
- Enables continuous improvement

---

## Design Principles Followed

- **Separation of Concerns** – each module does one job
- **Explainability First** – no hidden or black-box decisions
- **Extensibility** – new scans can be added easily
- **Security by Design** – minimal sensitive data exposure
- **Engine, Not Executor** – decisions only, no transaction execution

---

## Summary

Buxxy’s architecture is designed to be:
- Modular
- Transparent
- Secure
- Adaptable

By separating fraud detection from transaction execution, Buxxy remains focused on its core mission:

**Detect fraud accurately and guide decisions responsibly.**
