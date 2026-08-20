## Short summary:
I used Gemini as a pair programmer to validate design decisions, discuss trade-offs and review parts of the implementation.

## Prompt history

## prompt 1
mam takie zadanie rekrutacyjne do rozwiązania 

## prompt 2
myślisz że dodawac bazę danych h2 do tego? łatwiej to napisać uzywając hibernate niż robić operacje w pamięci

## prompt 3
w zadaniu nie ma nic napisanego o testach jednostkowych 

## prompt 4
jaki się pisze biznes po angielsku? bussines? 

## prompt 5
paczka na modele domenowe to powinno być business.domain? 

## prompt 6
nie powinnio zawierać local date time zamiast instant? "Match – zawiera obiekty drużyn, bieżący wynik oraz znacznik czasu (Instant lub numer sekwencyjny long do sprawdzania kolejności rozpoczęcia)." 

## prompt 7
jak nazwać drużynę gospodarzy w modelu match 

## prompt 8
co myślisz o tych modelach? "package mikolaj.bogudal.sportradar.Library.business.domain;

import java.time.Instant;

public record Match(
Long id,
Team homeTeam,
Team awayTeam,
Instant startTime,
Instant endTime,
Score score) {
}" "package mikolaj.bogudal.sportradar.Library.business.domain;

import java.time.Instant;
import java.util.List;

public record Team(
Long id,
String name,
List<Match> matches
) {
}" "package mikolaj.bogudal.sportradar.Library.business.domain;

public record Score(Long id,
Match match,
Short homeTeamScores,
Short awayTeamScores,
Team homeTeam,
Team awayTeam,
Team winnerTeam,) {
}"

to dalczego sugerujesz rezygnację z bidirectional modeli? 

okej dodałem takie wpisy w readme "Assumptions:

- TDD - test driven development

- clean architecture

resings from bidirectional domains because:

- Match in this context ia main agregator

- Without List<Matches> in Team i can imporve immutability

- No problems with loops" 

## prompt 9
resign chodziło mi o rezygnację 

## prompt 10
myślisz że to powinno być w klasie MatchService? "Implementacja Wymagań (Podstawowe Operacje)
Start a new match (startMatch(String homeTeam, String awayTeam)):
Tworzy nowy mecz z wynikiem 0-0.
Rzuca wyjątek (np. IllegalArgumentException), jeśli któraś z drużyn już gra w innym aktywnym meczu lub nazwy są nieprawidłowe.
Update the score (updateScore(UUID matchId, int homeScore, int awayScore) lub wg nazw drużyn):
Aktualizuje wynik.
Waliduje, czy wynik nie jest ujemny.
Finish a match (finishMatch(...)):
Usuwa mecz z tablicy wyników.
Get a summary of matches in progress (getSummary()):
Sortuje mecze przy użyciu Comparator:
Suma bramek malejąco: (m1, m2) -> Integer.compare(m2.getTotalScore(), m1.getTotalScore())
Czas rozpoczęcia malejąco (najnowszego meczu): (m1, m2) -> m2.getStartTime().compareTo(m1.getStartTime())"

## prompt 11
okej ale to jak Match i Score są niemutowalne to jak zroibc updateScore? 

## prompt 12
co myslisz o takich testach? 

## prompt 13
sprawdź moje sortowanie "return getCurrentMatches().stream().sorted(new Comparator<Match>() {
@Override
public int compare(Match o1, Match o2) {
return o1.compare(o2);
}
}).collect(Collectors.toList());", "public int compare(Match other){
int scoreCompare = Integer.compare(this.totalScore(), other.totalScore());

    return scoreCompare==0?scoreCompare:Long.compare(this.startTime.toEpochMilli(), other.startTime.toEpochMilli());
}"

## prompt 14
czy w tych testach można opóźnić dodawwanie nowych pozycji? "@Test
public void getSummaryOfMatchesInProgress(){
scoreboardTest.resumeMatchWithScores("Mexico", "Canada", 0, 5);
scoreboardTest.resumeMatchWithScores("Spain", "Brazil", 10, 2);
scoreboardTest.resumeMatchWithScores("Germany", "France", 2, 2);
scoreboardTest.resumeMatchWithScores("Uruguay", "Italy", 6, 6);
scoreboardTest.resumeMatchWithScores("Argentina", "Australia", 3, 1);

    scoreboardTest
            .getCurrentMatches()
            .stream().forEach(i-> System.out.println(i.toString()));
    System.out.println("!!!!!!!");
    scoreboardTest
            .getSummaryOfMatchesInProgress()
            .stream().forEach(i-> System.out.println(i.toString()));

    Assertions.assertEquals(
            scoreboardTest.getCurrentMatches().get(3),
            scoreboardTest.getSummaryOfMatchesInProgress().get(0));
    Assertions.assertEquals(
            scoreboardTest.getCurrentMatches().get(1),
            scoreboardTest.getSummaryOfMatchesInProgress().get(1));
    Assertions.assertEquals(
            scoreboardTest.getCurrentMatches().get(0),
            scoreboardTest.getSummaryOfMatchesInProgress().get(2));
    Assertions.assertEquals(
            scoreboardTest.getCurrentMatches().get(4),
            scoreboardTest.getSummaryOfMatchesInProgress().get(3));
    Assertions.assertEquals(
            scoreboardTest.getCurrentMatches().get(2),
            scoreboardTest.getSummaryOfMatchesInProgress().get(4));

}"

## prompt 15
dla bilbioteki mam taki pom "<?xml version="1.0" encoding="UTF-8"?>
<project xmlns="http://maven.apache.org/POM/4.0.0" xmlns:xsi="http://www.w3.org/2001/XMLSchema-instance"
xsi:schemaLocation="http://maven.apache.org/POM/4.0.0 https://maven.apache.org/xsd/maven-4.0.0.xsd">
<modelVersion>4.0.0</modelVersion>
<parent>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter-parent</artifactId>
<version>4.1.0</version>
<relativePath/> <!-- lookup parent from repository -->
</parent>
<groupId>mikolaj.bogudal.sportradar</groupId>
<artifactId>scoreboard</artifactId>
<version>0.0.1-SNAPSHOT</version>
<name/>
<description/>
<url/>
<licenses>
<license/>
</licenses>
<developers>
<developer/>
</developers>
<scm>
<connection/>
<developerConnection/>
<tag/>
<url/>
</scm>
<properties>
<java.version>17</java.version>
</properties>
<dependencies>
<dependency>
<groupId>org.springframework.boot</groupId>
<artifactId>spring-boot-starter</artifactId>
</dependency>

      <dependency>
         <groupId>org.springframework.boot</groupId>
         <artifactId>spring-boot-starter-test</artifactId>
         <scope>test</scope>
      </dependency>
   </dependencies>

   <build>
      <plugins>
         <plugin>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-maven-plugin</artifactId>
         </plugin>
      </plugins>
   </build>

</project>" co tu zmienić żeby było barzdiej pod bibliotekę?
