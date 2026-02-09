# Decision Engine

The Decision Engine is the core brain of Buxxy. Its job is simple but critical:  
take multiple fraud signals, understand the overall risk, and return a clear decision that external systems can act on.

Buxxy does not guess. It evaluates.

---

## Inputs to the Decision Engine

The Decision Engine receives structured results from all fraud detection scans, such as:

- High Amount checks
- Transaction velocity analysis
- Location consistency
- Time window behavior
- Device change detection
- IP address change detection

Each scan produces signals that indicate whether the behavior looks normal or suspicious.

---

## Risk Evaluation Logic

Instead of relying on a single rule, Buxxy evaluates **combined behavior patterns**.

Examples:
- A high transaction amount alone may be normal
- A high amount + sudden device change + new location is risky
- Multiple small transactions in a short time window may indicate abuse

The Decision Engine aggregates these signals into an overall risk evaluation score.

This approach reduces false positives and avoids blocking legitimate users unnecessarily.

---

## Decision Outcomes

Based on the evaluated risk, the engine produces one of three decisions:

### Legitimate
- Behavior matches expected patterns
- No strong fraud indicators detected
- External system can safely proceed

---

### Suspicious
- Some abnormal behavior detected
- Risk is present but not conclusive
- External system may apply step-up verification (OTP, biometric, manual review, etc.)

---

### Fraudulent
- Strong indicators of fraud detected
- High confidence risk evaluation
- External system should block or reject the transaction

---

## Decision, Not Execution

The Decision Engine **never executes actions**.

It does not:
- Block users
- Cancel transactions
- Move money
- Trigger UPI operations

It only returns a decision and supporting context.

This ensures Buxxy stays neutral, safe, and easy to integrate.

---

## Explainability

Every decision made by the engine is:
- Audited
- Traceable
- Explainable

External systems can always understand *why* a decision was made, instead of blindly trusting a black box.

---

## Designed for Evolution

The Decision Engine is built to evolve:

- New scans can be added
- Existing rules can be tuned
- Thresholds can be adjusted per system
- Decisions improve with audit feedback

This allows Buxxy to grow stronger without breaking integrations.

---

Good fraud detection is not about blocking users.
It’s about understanding behavior.
