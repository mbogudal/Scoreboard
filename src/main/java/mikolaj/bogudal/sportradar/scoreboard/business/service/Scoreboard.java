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
        if(homeTeam == null || homeTeam.isEmpty()){
            throw new IllegalArgumentException("homeTeam should not be null nor empty");
        }

        if(awayTeam == null || awayTeam.isEmpty()){
            throw new IllegalArgumentException("awayTeam should not be null nor empty");
        }
        Match match = new Match(
                new Team(homeTeam),
                new Team(awayTeam),
                Instant.now(),
                new Score(0, 0));
        getCurrentMatches().add(match);
        return match;
    }

    default Match updateScore(Match match, int newHomeScore, int newAwayScore) {
        if(match == null){
            throw new IllegalArgumentException("match should not be null");
        }

        getCurrentMatches().remove(match);
        match = match.withScore(newHomeScore, newAwayScore);
        getCurrentMatches().add(match);
        return match;
    }

    default void finishMatch(Match match) {
        if(match == null){
            throw new IllegalArgumentException("match should not be null");
        }
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

    default Match resumeMatchWithScoresAndStartTime(String homeTeam, String awayTeam, int homeScore, int awayScore, Instant startTime) {
        if(startTime == null){
            throw new IllegalArgumentException("startTime should not be null");
        }
        Match match = updateScore(startANewMatch(homeTeam, awayTeam), homeScore, awayScore);
        getCurrentMatches().remove(match);
        match = match.withStartTime(startTime);
        getCurrentMatches().add(match);
        return match;
    }

}
