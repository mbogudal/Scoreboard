package mikolaj.bogudal.sportradar.Library;

import mikolaj.bogudal.sportradar.scoreboard.business.domain.Match;
import mikolaj.bogudal.sportradar.scoreboard.business.service.Scoreboard;

import java.util.List;

public class ScoreboardTestImpl implements Scoreboard {
    private final List<Match> currentMatches;

    public ScoreboardTestImpl(List<Match> currentMatches) {
        this.currentMatches = currentMatches;
    }

    @Override
    public List<Match> getCurrentMatches() {
        return currentMatches;
    }
}
