## How Buxxy Works (High-Level)

Buxxy works as a fraud decision engine that sits alongside an external transaction system. When a transaction occurs, the external system sends user and transaction details to Buxxy using a secured API key. Before anything else, Buxxy verifies the API key and request headers to make sure the request is coming from a trusted source.

After validation, the transaction goes through multiple fraud detection scans. Each scan looks at a different behavior pattern that is commonly associated with fraudulent activity, such as unusually high transaction amounts, rapid repeated transactions, or sudden changes in device or location.

The current scans include:
- **High Amount**
- **Transaction Velocity**
- **Location Consistency**
- **Time Window Analysis**
- **Device Change Detection**
- **IP Address Change Detection**

Each scan contributes to an overall evaluation score that represents how risky the transaction appears. This score is then analyzed by a decision evaluator, which classifies the transaction into one of three outcomes:

- **Legitimate** – the transaction appears safe and can proceed.
- **Suspicious** – the transaction shows some risk and may require additional verification.
- **Fraudulent** – the transaction is highly likely to be fraud and should be blocked.

Buxxy does not execute or process transactions by itself. Instead, it sends the final decision back to the external system, which is responsible for allowing, stepping up, or blocking the transaction. This separation ensures that Buxxy remains a pure fraud detection engine rather than a payment or UPI processor.

Every step in this flow is audited. These audit logs help analyze fraud patterns, improve the accuracy of the engine over time, and verify whether external systems are correctly acting on the decisions provided by Buxxy.
