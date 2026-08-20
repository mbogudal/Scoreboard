package mikolaj.bogudal.sportradar.scoreboard;

import mikolaj.bogudal.sportradar.scoreboard.business.domain.Match;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;

public class ScoreboardTests {
    private ScoreboardTestImpl scoreboardTest;

    @BeforeEach
    public void setUp() {
        List<Match> currentMatches = new ArrayList<>();
        scoreboardTest = new ScoreboardTestImpl(currentMatches);
    }

    @Test
    public void startANewMatch() {
        Assertions.assertEquals(0, scoreboardTest.getCurrentMatches().size());
        scoreboardTest.startANewMatch("France", "Poland");
        Assertions.assertEquals(1, scoreboardTest.getCurrentMatches().size());
    }

    @Test
    public void startANewMatchEmptyHomeName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> scoreboardTest.startANewMatch("", "Poland"));
    }

    @Test
    public void startANewMatchEmptyAwayName() {
        Assertions.assertThrows(IllegalArgumentException.class, () -> scoreboardTest.startANewMatch("France", ""));
    }

    @Test
    public void updateTheScore() {
        scoreboardTest.startANewMatch("France", "Poland");
        scoreboardTest.updateScore(scoreboardTest.getCurrentMatches().get(0), 0, 1);
        Match match = scoreboardTest.getCurrentMatches().get(0);
        Assertions.assertEquals(0, match.score().homeTeamScores());
        Assertions.assertEquals(1, match.score().awayTeamScores());
    }

    @Test
    public void updateTheScoreNullMatch() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> scoreboardTest.updateScore(null, 0, 1));
    }

    @Test
    public void updateTheScoreNegativeHomeScore() {
        scoreboardTest.startANewMatch("France", "Poland");
        scoreboardTest.updateScore(scoreboardTest.getCurrentMatches().get(0), 0, 1);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> scoreboardTest.updateScore(scoreboardTest.getCurrentMatches().get(0), -1, 1));
    }

    @Test
    public void updateTheScoreNegativeAwayScore() {
        scoreboardTest.startANewMatch("France", "Poland");
        scoreboardTest.updateScore(scoreboardTest.getCurrentMatches().get(0), 0, 1);
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> scoreboardTest.updateScore(scoreboardTest.getCurrentMatches().get(0), 0, -1));
    }

    @Test
    public void finishAMatch() {
        scoreboardTest.startANewMatch("France", "Poland");
        scoreboardTest.finishMatch(scoreboardTest.getCurrentMatches().get(0));
        Assertions.assertEquals(0, scoreboardTest.getCurrentMatches().size());
    }

    @Test
    public void finishAMatchNullMatch() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> scoreboardTest.finishMatch(null));
    }

    @Test
    public void resumeMatchWithScoresAndStartTime() {
        scoreboardTest.resumeMatchWithScoresAndStartTime("Mexico", "Canada", 0, 5, Instant.now());
        Match match = scoreboardTest.getCurrentMatches().get(0);
        Assertions.assertEquals(0, match.score().homeTeamScores());
        Assertions.assertEquals(5, match.score().awayTeamScores());
    }

    @Test
    public void resumeMatchWithScoresAndStartTimeWithNullStartTime() {
        Assertions.assertThrows(IllegalArgumentException.class,
                () -> scoreboardTest.resumeMatchWithScoresAndStartTime("Mexico", "Canada", 0, 5, null));
    }

    @Test
    public void getSummaryOfMatchesInProgress() {
        scoreboardTest.resumeMatchWithScoresAndStartTime("Mexico", "Canada", 0, 5, Instant.now().plusMillis(10));
        scoreboardTest.resumeMatchWithScoresAndStartTime("Spain", "Brazil", 10, 2, Instant.now().plusMillis(20));
        scoreboardTest.resumeMatchWithScoresAndStartTime("Germany", "France", 2, 2, Instant.now().plusMillis(30));
        scoreboardTest.resumeMatchWithScoresAndStartTime("Uruguay", "Italy", 6, 6, Instant.now().plusMillis(40));
        scoreboardTest.resumeMatchWithScoresAndStartTime("Argentina", "Australia", 3, 1, Instant.now().plusMillis(50));

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

    }

}
