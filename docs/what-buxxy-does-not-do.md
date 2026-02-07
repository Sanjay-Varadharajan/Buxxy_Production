# What Buxxy Does NOT Do

Buxxy is intentionally designed as a **fraud detection and decision engine**, not a full transaction or payment system.  
To avoid confusion and misuse, it’s important to clearly state what Buxxy does **not** do.

This clarity helps teams integrate Buxxy correctly and keeps responsibilities well separated.

---

## Buxxy Does Not Process Transactions

Buxxy **never executes, authorizes, or settles transactions**.

- It does not move money  
- It does not interact with banks or UPI networks  
- It does not approve or decline payments directly  

Buxxy only analyzes risk and returns a decision.  
The external system remains fully responsible for executing the transaction.

---

## Buxxy Is Not a Payment Gateway or UPI System

Buxxy is **not**:
- A payment gateway  
- A UPI processor  
- A wallet or banking system  

It does not replace existing financial infrastructure.  
Instead, it sits alongside those systems as an **intelligence layer**.

---

## Buxxy Does Not Override External Systems

Even when Buxxy marks a transaction as fraudulent:

- It does not force-block the transaction  
- It does not shut down user accounts  
- It does not take irreversible actions  

The final action is always taken by the external system based on Buxxy’s decision.

This separation avoids unintended business or legal consequences.

---

## Buxxy Does Not Store Sensitive Financial Data

Buxxy is designed to minimize risk exposure.

It does **not**:
- Store card numbers  
- Store UPI PINs  
- Store bank credentials  
- Act as a vault for sensitive payment data  

Only the **minimum required data** for fraud evaluation is handled.

---

## Buxxy Is Not a Rule-Only Static System

Buxxy is not limited to hardcoded, static rules.

While rules exist, the engine is built to:
- Adapt using user behavior profiles  
- Support dynamic thresholds  
- Improve over time through audit analysis  

This avoids rigid logic that quickly becomes outdated.

---

## Buxxy Is Not a Black Box

Buxxy does not make unexplained decisions.

Every decision:
- Is backed by scan results  
- Is auditable  
- Can be analyzed later  

Explainability is a core design goal, not an afterthought.

---

## Why These Boundaries Matter

By clearly defining what Buxxy does **not** do, Buxxy ensures:
- Safer integrations  
- Clear ownership between systems  
- Easier compliance and auditing  
- Reduced operational risk  

Buxxy focuses on doing **one thing extremely well**:  
**detecting fraud and guiding decisions — nothing more, nothing less.**
