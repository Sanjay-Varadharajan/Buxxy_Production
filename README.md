# Buxxy-Fraud-Engine
Buxxy Fraud Engine

-Okay, so here’s the deal. I built this thing called Buxxy Fraud Engine.

-It’s basically the backend brain for catching shady transactions. No frontend, no dashboard, no hosted app—just the logic that does the thinking. Think of it like the engine of a car without the body.

-I know it’s not perfect, probably has bugs, and there are smarter ways to do some things, but it works. And instead of hiding it in my laptop, I figured I’d throw it out to the open-source world so bigger brains than me can make it better.

# The Stuff It Does
# Fraud Rules
-Flags weird transaction timing—like if someone’s trying to do ten payments in a second
-Spots suspicious devices or when devices suddenly change
-You can add your own rules if you want to get creative
# Transaction Engine
-Lets you simulate transactions so you can see it in action
-Logs everything, because without logs, who even knows what’s happening?
-Gives a “fraud score” to each transaction, so you know what’s sketchy
# Idempotency & Reliability
-Uses Redis so sending the same request twice doesn’t crash the system
-TTL caching to keep things smooth and not repeat work
# Security Stuff
-JWT auth with refresh tokens
-Role-based access control
-OTP integration, because why not

# Why I Made This
-Honestly, I just wanted to build something real. Something I could look at and say, “Yeah, that works.”
-I didn’t bother with a frontend or anything pretty, because I figured if the engine works, people smarter than me can take it and do cooler things.
-Think of it like handing someone LEGO blocks and saying, “Here, build whatever you want.”

# Tech Stuff
-Java 17
-Spring Boot 3.x
-MySQL
-Redis
-Maven

# How to Use
-git clone https://github.com/Sanjay-Varadharajan/Buxxy_Production.git
-Set up MySQL and Redis
-Run it:mvn spring-boot:run
-Check out the main services:
@TransactionService
@FraudRuleEngine
@IdempotentService
-Build your own frontend, integrate it, or just mess around and learn.

# Contribution
-This is open-source because, honestly, I know there are people out there way smarter than me.
-Fork it, improve it, add rules, or just learn from it. I’ll happily sit back and watch the bigger brains make it better.

