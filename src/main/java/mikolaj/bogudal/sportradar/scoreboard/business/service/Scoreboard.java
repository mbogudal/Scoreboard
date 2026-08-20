package mikolaj.bogudal.sportradar.scoreboard.business.service;

import mikolaj.bogudal.sportradar.scoreboard.business.domain.Match;
import mikolaj.bogudal.sportradar.scoreboard.business.domain.Score;
import mikolaj.bogudal.sportradar.scoreboard.business.domain.Team;

import java.time.Instant;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public interface Scoreboard {
    List<Match> getCurrentMatches();

    default Match startANewMatch(String homeTeam, String awayTeam) {
        Match match = new Match(
                new Team(homeTeam),
                new Team(awayTeam),
                Instant.now(),
                new Score(0, 0));
        getCurrentMatches().add(match);
        return match;
    }

    default Match updateScore(Match match, int newHomeScore, int newAwayScore) {
        getCurrentMatches().remove(match);
        match = match.withScore(newHomeScore, newAwayScore);
        getCurrentMatches().add(match);
        return match;
    }

    default void finishMatch(Match match) {
        getCurrentMatches().remove(match);
    }

    default List<Match> getSummaryOfMatchesInProgress() {
        return getCurrentMatches().stream().sorted(new Comparator<Match>() {
            @Override
            public int compare(Match o1, Match o2) {
                return o1.compare(o2);
            }
        }).collect(Collectors.toList());
    }

}
