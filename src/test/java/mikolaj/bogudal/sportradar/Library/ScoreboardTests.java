package mikolaj.bogudal.sportradar.Library;

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
    public void setUp(){
        List<Match> currentMatches = new ArrayList<>();
        scoreboardTest = new ScoreboardTestImpl(currentMatches);
    }
    @Test
    public void startANewMatch(){
        Assertions.assertEquals(0, scoreboardTest.getCurrentMatches().size());
        scoreboardTest.startANewMatch("Francja", "Polska");
        Assertions.assertEquals(1, scoreboardTest.getCurrentMatches().size());
    }

    @Test
    public void updateTheScore(){
        scoreboardTest.startANewMatch("Francja", "Polska");
        scoreboardTest.updateScore(scoreboardTest.getCurrentMatches().get(0), 0,1);
        Match match = scoreboardTest.getCurrentMatches().get(0);
        Assertions.assertEquals(0, match.score().homeTeamScores());
        Assertions.assertEquals(1, match.score().awayTeamScores());
    }

    @Test
    public void finishAMatch(){
        scoreboardTest.startANewMatch("Francja", "Polska");
        scoreboardTest.finishMatch(scoreboardTest.getCurrentMatches().get(0));
    }


}
