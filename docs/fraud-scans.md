# Fraud Detection Scans

Buxxy uses multiple independent fraud detection scans instead of relying on a single rule. Each scan looks at a different behavioral signal that is commonly associated with fraud. The final decision is based on how these signals combine, rather than one isolated condition.

This layered approach helps reduce false positives while still catching high-risk transactions.

---

## Why Multiple Scans?

Fraud rarely looks the same in every case. A transaction might look normal in terms of amount but suspicious in terms of location or device behavior. By combining multiple scans, Buxxy evaluates transactions more realistically, similar to how a human analyst would assess risk.

Each scan focuses on one dimension of user behavior and contributes to the overall fraud evaluation score.

---

## Scan Types

### High Amount Scan

The High Amount scan detects transactions that are unusually large compared to a user’s historical behavior. Instead of using a fixed amount limit, Buxxy maintains a dynamic profile for each user and continuously updates their average transaction amount.

If a transaction exceeds a calculated dynamic threshold (based on the user’s past behavior and configurable multipliers), it is flagged as suspicious. This approach adapts over time and avoids penalizing users whose spending patterns naturally change.

---

### Transaction Velocity Scan

The Velocity scan checks how frequently a user is performing transactions within a short time window. A sudden burst of transactions in a short period is a common fraud pattern, especially in automated or scripted attacks.

Buxxy evaluates recent transactions within configurable time windows and flags activity that exceeds normal behavior. Both dynamic thresholds and optional static limits can be applied to detect abnormal transaction frequency.

---

### Location Consistency Scan

The Location scan analyzes whether a transaction originates from a location that is unusual for the user. Using IP-based geolocation data, Buxxy evaluates country and city-level consistency.

It also checks for:
- First-time transactions from a new city or country
- Activity from locations that do not match known user patterns
- Physically impossible travel scenarios, where two transactions occur too far apart in too little time

This helps detect account compromise, proxy usage, and cross-border fraud attempts.

---

### Time Window Analysis

The Time Window scan evaluates when transactions occur relative to a user’s typical activity pattern. Buxxy calculates an average transaction time window based on recent behavior and checks whether a new transaction falls significantly outside that range.

Transactions occurring at unusual times may indicate automated fraud or account misuse, especially when combined with other risk signals.

---

### Device Change Detection

This scan identifies changes in the device used by a user. Buxxy extracts device context information such as:
- User agent
- Time zone
- Language settings

If a transaction originates from a completely new device or a device that does not match known characteristics, the scan flags it as suspicious. This is useful for detecting account takeovers and unauthorized access.

---

### IP Change Detection

The IP Change scan analyzes IP behavior for a known device. Even if the device remains the same, a sudden or abnormal IP change may indicate proxy usage, VPN abuse, or network-based fraud.

Buxxy tracks device-to-IP history and detects anomalies based on past patterns. IP-based geolocation is powered using `.mmdb` databases to ensure accurate and efficient location resolution.

---

## How Scans Affect the Final Decision

Each scan contributes to the overall fraud evaluation score. No single scan automatically blocks a transaction. Instead, the combined risk signals are evaluated by the decision engine to classify the transaction as legitimate, suspicious, or fraudulent.

This design allows Buxxy to remain flexible, explainable, and adaptable across different systems and risk profiles.
