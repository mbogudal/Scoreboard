# Live Football World Cup Scoreboard
## Assumptions:
 - TDD - test driven development. The implementation was built test-first to ensure high test coverage,
   reliable code behavior, and strict alignment with functional requirements.
 - DDD - Domain Driven Design and clean architecture. The models are kept lightweight and focused solely 
   on the live scoreboard context without unnecessary framework dependencies.
## Reasoning
Rejection of bidirectional domains because:
 - Match in this context is main aggregator
 - Without List<Matches> in Team i can improve immutability
 - No problems with loops
Rejection of suffix Service for Scoreboard because, this is a library not a full app.
For new
Scoreboard created as interface with default operations for future use as Service
 - Scoreboard should only accept simple types for API simplification
One additional operation of my choice is resumeMatchWithScores(). 
   This method will simplify tests, it can be used by business in case of random emergency situations
 - I'v decided to append resumeWithScores with  start time instead of delaying test mock
Added data validation
 - data should not be null
 - teams should not be null nor empty
 - i think that negative scores aren't possible, blocking them
## Trade offs made
Rejecting H2 database for making lightweight library
Rejecting spring boot for making lightweight library

