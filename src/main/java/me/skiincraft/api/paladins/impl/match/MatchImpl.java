package me.skiincraft.api.paladins.impl.match;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;
import me.skiincraft.api.paladins.entity.match.Match;
import me.skiincraft.api.paladins.entity.match.MatchPlayer;
import me.skiincraft.api.paladins.entity.match.objects.Ban;
import me.skiincraft.api.paladins.exceptions.MatchException;
import me.skiincraft.api.paladins.internal.requests.APIRequest;
import me.skiincraft.api.paladins.internal.session.EndPoint;
import me.skiincraft.api.paladins.json.PaladinsDateAdapter;
import me.skiincraft.api.paladins.objects.match.Queue;

import java.time.OffsetDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

public class MatchImpl implements Match {

    @SerializedName("Map_Game")
    private final String mapGame;
    @SerializedName("Winning_TaskForce")
    private final int winningTask;
    @SerializedName("Match")
    private final long matchId;
    @SerializedName(value = "Match_Duration", alternate = "Time_In_Match_Seconds")
    private final long matchDuration;
    @SerializedName("Team1Score")
    private final int team1Score;
    @SerializedName("Team2Score")
    private final int team2Score;
    @SerializedName(value = "match_queue_id", alternate = "Match_Queue_Id")
    private final int queueId;
    @SerializedName(value = "name", alternate = "Queue")
    private final String gamemode;
    @JsonAdapter(PaladinsDateAdapter.class)
    @SerializedName("Match_Time")
    private final OffsetDateTime matchTime;
    private final String hasReplay;
    protected EndPoint endPoint;
    private List<Ban> bans;
    private List<MatchPlayer> team1;
    private List<MatchPlayer> team2;

    public MatchImpl(String mapGame, int winningTask, List<Ban> bans, long matchId, long matchDuration, int team1Score, int team2Score, int queueId, String gamemode, OffsetDateTime matchTime, String hasReplay, List<MatchPlayer> team1, List<MatchPlayer> team2) {
        this.mapGame = mapGame;
        this.winningTask = winningTask;
        this.bans = bans;
        this.matchId = matchId;
        this.matchDuration = matchDuration;
        this.team1Score = team1Score;
        this.team2Score = team2Score;
        this.queueId = queueId;
        this.gamemode = gamemode;
        this.matchTime = matchTime;
        this.hasReplay = hasReplay;
        this.team1 = team1;
        this.team2 = team2;
    }

    public MatchImpl buildMethods(JsonArray array, EndPoint endPoint) {
        this.endPoint = endPoint;
        if (bans != null)
            return this;
        Gson gson = new Gson();
        this.bans = buildBans(array.get(0).getAsJsonObject());
        this.team1 = buildPlayers(array, gson, 1);
        this.team2 = buildPlayers(array, gson, 2);
        return this;
    }

    private List<Ban> buildBans(JsonObject object) {
        List<Ban> bans = new ArrayList<>();
        for (int i = 1; i <= 4; i++) {
            if (object.get("BanId" + i).getAsLong() == 0) {
                continue;
            }
            bans.add(new Ban(object.get("BanId" + i).getAsLong(), object.get("Ban_" + i).getAsString(), endPoint));
        }
        return bans;
    }

    private List<MatchPlayer> buildPlayers(JsonArray array, Gson gson, int taskForce) {
        List<MatchPlayer> players = new ArrayList<>();
        for (JsonElement ele : array) {
            JsonObject object = ele.getAsJsonObject();
            if (object.get("TaskForce").getAsInt() != taskForce) {
                continue;
            }
            players.add(gson.fromJson(object, MatchPlayerImpl.class).buildMethods(object, this, endPoint));
        }

        return players;
    }

    public String getWinner() {
        return (winningTask == 1) ? "Blue" : "Red";
    }

    public List<Ban> getBans() {
        return bans;
    }

    public long getMatchId() {
        return matchId;
    }

    public long getMatchDuration() {
        return TimeUnit.SECONDS.toMillis(matchDuration);
    }

    public String getMapGame() {
        return mapGame;
    }

    public long getMatchMinutes() {
        return TimeUnit.SECONDS.toMinutes(matchDuration);
    }

    public int getTeam1Score() {
        return team1Score;
    }

    public int getTeam2Score() {
        return team2Score;
    }

    @Override
    public Queue getQueue() {
        return Queue.getQueueById(getMatchQueueId());
    }

    public List<MatchPlayer> getTeam1() {
        return team1;
    }

    public List<MatchPlayer> getTeam2() {
        return team2;
    }

    public int getWinnerTeam() {
        return winningTask;
    }

    public boolean hasReplay() {
        return hasReplay.equals("y");
    }

    public int getMatchQueueId() {
        return queueId;
    }

    public String getGamemode() {
        return gamemode;
    }

    public boolean isRanked() {
        return getGamemode().equalsIgnoreCase("ranked");
    }

    public List<MatchPlayer> getPlayers() {
        List<MatchPlayer> m = new ArrayList<>();
        m.addAll(team1);
        m.addAll(team2);
        return m;
    }

    public boolean isDetailedMatch() {
        return true;
    }

    public APIRequest<Match> getMatchDetails() {
        if (isDetailedMatch()) {
            throw new MatchException("This match is already a detailed match");
        }
        return endPoint.getMatchDetails(getMatchId());
    }

    @Override
    public OffsetDateTime getMatchDate() {
        return matchTime;
    }

    @Override
    public String toString() {
        return "Match{" +
                "matchId=" + getMatchId() +
                ", winner=" + getWinner() +
                ", isRanked=" + isRanked() +
                '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        MatchImpl match = (MatchImpl) o;
        return matchId == match.matchId && queueId == match.queueId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(matchId, queueId);
    }
}
