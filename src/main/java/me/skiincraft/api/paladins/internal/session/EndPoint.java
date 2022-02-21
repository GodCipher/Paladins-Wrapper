package me.skiincraft.api.paladins.internal.session;

import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.champions.Champions;
import me.skiincraft.api.paladins.entity.champions.objects.Cards;
import me.skiincraft.api.paladins.entity.champions.objects.Skins;
import me.skiincraft.api.paladins.entity.leaderboard.LeaderBoard;
import me.skiincraft.api.paladins.entity.match.HistoryMatch;
import me.skiincraft.api.paladins.entity.match.LiveMatch;
import me.skiincraft.api.paladins.entity.match.Match;
import me.skiincraft.api.paladins.entity.other.Friends;
import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.entity.player.objects.*;
import me.skiincraft.api.paladins.internal.requests.APIRequest;
import me.skiincraft.api.paladins.objects.match.Queue;
import me.skiincraft.api.paladins.objects.miscellany.BountyItem;
import me.skiincraft.api.paladins.objects.miscellany.Language;
import me.skiincraft.api.paladins.objects.player.Platform;
import me.skiincraft.api.paladins.objects.player.PlayerStatus;
import me.skiincraft.api.paladins.objects.ranking.Tier;

import javax.annotation.Nullable;
import java.util.List;

/**
 * <h1>EndPoint</h1>
 *
 * <p>This class contains all Paladins API methods.
 * <br>All methods will use data such as SessionId, DevId, Signature and etc.</br>
 * @see Session
 * </p>
 */
public interface EndPoint {

    /**
     * <p>Make an api request to return a Player</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param userId the player id;
     * @return {@link Player}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     */
    default APIRequest<Player> getPlayer(long userId) {
        return getPlayer(String.valueOf(userId));
    }

    /**
     * <p>Make an api request to return a Player</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param player the player nickname;
     * @return {@link Player}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     */
    APIRequest<Player> getPlayer(String player);

    /**
     * <p>Make an api request to return a Player</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param player the player nickname;
     * @param platform platform;
     * @return {@link Player}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     */
    APIRequest<Player> getPlayer(String player, Platform platform);

    /**
     * <p>Make an API request to search for and return a list of players</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * <p><br>Platform can be filled in as null, and will return all searches found</br></p>
     *
     * @param queue    search
     * @param platform Platform than the player you want
     * @return {@link SearchPlayers}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.SearchException  If no player is found.
     */
    APIRequest<SearchPlayers> searchPlayer(String queue, @Nullable Platform platform);

    /**
     * <p>Make an api request to return a Player Status</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param userId the player id;
     * @return {@link PlayerStatus}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     */
    default APIRequest<PlayerStatus> getPlayerStatus(long userId) {
        return getPlayerStatus(String.valueOf(userId));
    }

    /**
     * <p>Make an api request to return a Player Status</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param player the player nickname;
     * @return {@link PlayerStatus}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     */
    APIRequest<PlayerStatus> getPlayerStatus(String player);

    /**
     * <p>Make an api request to return all champions</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     * <p>This request will count as 1, and will return all champions</p>
     *
     * @param language the language you want to receive the champions;
     * @return {@link Champions}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     */
    APIRequest<Champions> getChampions(Language language);

    /**
     * <p>Make an API request to return a specific champion.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * <p>This request is the same as getChampions, but you get 1 specific champion, and the other champions will be stored depending on the language</p>
     *
     * @param championId the id of the champion
     * @param language   the language you want to receive the champions;
     * @return {@link Champion}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException  If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ChampionException In case the champion Id is wrong.
     */
    APIRequest<Champion> getChampion(long championId, Language language);

    /**
     * <p>Make an API request to return a specific champion.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * <p>This request is the same as getChampions, but you get 1 specific champion, and the other champions will be stored depending on the language</p>
     *
     * @param championName the name of the champion
     * @param language     the language you want to receive the champions;
     * @return {@link Champion}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException  If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ChampionException In case the champion Id is wrong.
     */
    APIRequest<Champion> getChampion(String championName, Language language);

    /**
     * <p>Make an API request to return all cards from the champion.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * <p>It is the same as getChampionsCard (long, Language), but the values are filled according to the Champion</p>
     *
     * @param champion the champion
     * @return {@link Cards}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException  If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ChampionException In case the champion Id is wrong.
     */
    default APIRequest<Cards> getChampionCards(Champion champion) {
        return getChampionCards(champion.getId(), champion.getLanguage());
    }

    /**
     * <p>Make an API request to return all cards from the champion.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param championsId the name of the champion
     * @param language    the language you want to receive the champion cards;
     * @return {@link Cards}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException  If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ChampionException In case the champion Id is wrong.
     */
    APIRequest<Cards> getChampionCards(long championsId, Language language);

    /**
     * <p>Make an API request to return all skins from the champion.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * <p>It is the same as getChampionsSkins(long, Language), but the values are filled according to the Champion</p>
     *
     * @param champion the champion
     * @return {@link Cards}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException  If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ChampionException In case the champion Id is wrong.
     */
    default APIRequest<Skins> getChampionSkin(Champion champion) {
        return getChampionSkin(champion.getId(), champion.getLanguage());
    }

    /**
     * <p>Make an API request to return all skins from the champion.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param championsId the name of the champion
     * @param language    the language you want to receive the champion skins;
     * @return {@link Skins}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException  If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ChampionException In case the champion Id is wrong.
     */
    APIRequest<Skins> getChampionSkin(long championsId, Language language);


    /**
     * <p>Make an API request to return all champion skins.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param language    the language you want to receive the champions skins;
     * @return {@link List<Skins>}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException  If anything is wrong with the session.
     */
    APIRequest<List<Skins>> getChampionsSkins(Language language);

    /**
     * <p>Make an API request to return a batch of players.</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     * <p><br>It will not raise the exception {@link me.skiincraft.api.paladins.exceptions.PlayerException} but will not be added to the list if the player's profile is private or does not exist</br></p>
     *
     * @param ids the player ids
     * @return {@link PlayerBatch}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ContextException If only 1 player is requested in the batch.
     */
    APIRequest<PlayerBatch> getPlayerBatch(List<Long> ids);

    /**
     * <p>Make an API request to return a statistic of all champions played by a player</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param userId the player id
     * @return {@link PlayerChampions}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     */
    APIRequest<PlayerChampions> getPlayerChampions(long userId);

    /**
     * <p>Make an API request to return a statistic of all champions played by a player</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param userId the player id
     * @return {@link QueueChampions}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     */
    APIRequest<QueueChampions> getQueueStats(long userId, Queue queue);

    /**
     * <p>Make an API request to return a player's friends</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param userId the player id
     * @return {@link Friends}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.SearchException  If you can't find any friends
     */
    APIRequest<Friends> getFriends(long userId);

    /**
     * <p>Make an API request to return all a player's loadouts</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param userId   the player id
     * @param language the language the items that will be returned
     * @return {@link Loadouts}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.PlayerException  If the player has a private profile or does not exist.
     * @throws me.skiincraft.api.paladins.exceptions.SearchException  If no loadout is found for this player
     */
    APIRequest<Loadouts> getLoadouts(long userId, Language language);

    /**
     * <p>Make an API request to return to a match</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param matchId Id of any match, as long as it is valid
     * @return {@link Match}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.MatchException   If no match is found
     */
    APIRequest<Match> getMatchDetails(long matchId);

    /**
     * <p>Make an API request to return a batch of matches</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * <p><br>It will not raise the exception {@link me.skiincraft.api.paladins.exceptions.MatchException} but will not be added to the list if the match is not found</br></p>
     *
     * @param matchbatch Id of any match, as long as it is valid
     * @return List&lt;{@link Match}&gt;
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ContextException If only 1 match is requested in the batch.
     */
    APIRequest<List<Match>> getMatchDetails(List<Long> matchbatch);

    /**
     * <p>Make an API request to return a player's match history</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * <p><br>Remember that this request will not request all the details of the match,
     * to request all details use getMatchDetails</br>
     * </p>
     *
     * @param userId the player id
     * @return {@link HistoryMatch}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.MatchException   If no match is found
     */
    APIRequest<List<HistoryMatch>> getMatchHistory(long userId);

    /**
     * <p>Make an API request to return a leaderboard of some tier</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param tier   the tier you want to get
     * @param season Season you want to get information
     * @return {@link LeaderBoard}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.SearchException  If no results are returned
     */
    APIRequest<LeaderBoard> getLeaderboard(Tier tier, int season);

    /**
     * <p>Make an API request to return an ongoing match</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @param matchId the tier you want to get
     * @return {@link LiveMatch}
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.MatchException   If no results are returned
     */
    APIRequest<LiveMatch> getMatchPlayerDetails(long matchId);

    /**
     * <p>Make an API request to return items from the bounty store</p>
     * <p>After the order is completed, the API will receive a Json, which will be converted into a class and returned</p>
     *
     * @return List&lt;{@link me.skiincraft.api.paladins.objects.miscellany.BountyItem}&gt;
     * @deprecated Bounty Store has been withdrawn indefinitely from the game, as it will have some rework.
     *
     * @throws me.skiincraft.api.paladins.exceptions.RequestException If anything is wrong with the session.
     * @throws me.skiincraft.api.paladins.exceptions.ContextException If no results are returned
     */
    @Deprecated
    APIRequest<List<BountyItem>> getBountyItems();

    /**
     * <p>It is the current session that maintains this EndPoint</p>
     */
    Session getSession();
}
