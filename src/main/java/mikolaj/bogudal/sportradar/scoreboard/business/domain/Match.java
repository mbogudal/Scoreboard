package mikolaj.bogudal.sportradar.scoreboard.business.domain;

import java.time.Instant;

public record Match(
        Team homeTeam,
        Team awayTeam,
        Instant startTime,
        Score score) {
    public Match withScore(int newHomeScore, int newAwayScore) {
        return new Match(
                this.homeTeam,
                this.awayTeam,
                this.startTime,
                new Score(newHomeScore, newAwayScore)

        );
    }

    public Match withStartTime(Instant startTime) {
        return new Match(
                this.homeTeam,
                this.awayTeam,
                startTime,
                this.score

        );
    }

    public int totalScore() {
        return this.score.homeTeamScores() + this.score.awayTeamScores();
    }

    public int compare(Match other) {
        int scoreCompare = Integer.compare(other.totalScore(), this.totalScore());

        return scoreCompare == 0 ? Long.compare(other.startTime.toEpochMilli(), this.startTime.toEpochMilli()) : scoreCompare;
    }

    public String toString() {
        return homeTeam.name()
                + ":"
                + score.homeTeamScores()
                + " - "
                + awayTeam.name()
                + ":"
                + score.awayTeamScores()
                + " -> "
                + startTime.toEpochMilli();
    }
}
