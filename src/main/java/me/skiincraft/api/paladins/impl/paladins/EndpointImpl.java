package me.skiincraft.api.paladins.impl.paladins;

import com.google.gson.*;
import me.skiincraft.api.paladins.Paladins;
import me.skiincraft.api.paladins.entity.champions.Champion;
import me.skiincraft.api.paladins.entity.champions.Champions;
import me.skiincraft.api.paladins.entity.champions.objects.Cards;
import me.skiincraft.api.paladins.entity.champions.objects.ChampionSkin;
import me.skiincraft.api.paladins.entity.champions.objects.Skins;
import me.skiincraft.api.paladins.entity.leaderboard.LeaderBoard;
import me.skiincraft.api.paladins.entity.match.HistoryMatch;
import me.skiincraft.api.paladins.entity.match.LiveMatch;
import me.skiincraft.api.paladins.entity.match.Match;
import me.skiincraft.api.paladins.entity.other.Friend;
import me.skiincraft.api.paladins.entity.other.Friends;
import me.skiincraft.api.paladins.entity.player.Loadout;
import me.skiincraft.api.paladins.entity.player.Player;
import me.skiincraft.api.paladins.entity.player.PlayerChampion;
import me.skiincraft.api.paladins.entity.player.QueueChampion;
import me.skiincraft.api.paladins.entity.player.objects.*;
import me.skiincraft.api.paladins.exceptions.*;
import me.skiincraft.api.paladins.impl.champion.*;
import me.skiincraft.api.paladins.impl.match.*;
import me.skiincraft.api.paladins.impl.player.FriendImpl;
import me.skiincraft.api.paladins.impl.player.LoadoutImpl;
import me.skiincraft.api.paladins.impl.player.PlayerImpl;
import me.skiincraft.api.paladins.internal.logging.PaladinsLogger;
import me.skiincraft.api.paladins.internal.requests.APIRequest;
import me.skiincraft.api.paladins.internal.requests.impl.DefaultAPIRequest;
import me.skiincraft.api.paladins.internal.requests.impl.FakeAPIRequest;
import me.skiincraft.api.paladins.internal.session.EndPoint;
import me.skiincraft.api.paladins.internal.session.Session;
import me.skiincraft.api.paladins.json.ChampionAdapter;
import me.skiincraft.api.paladins.json.PaladinsDateAdapter;
import me.skiincraft.api.paladins.json.QueueAdapter;
import me.skiincraft.api.paladins.objects.champion.Card;
import me.skiincraft.api.paladins.objects.match.Queue;
import me.skiincraft.api.paladins.objects.miscellany.BountyItem;
import me.skiincraft.api.paladins.objects.miscellany.Language;
import me.skiincraft.api.paladins.objects.player.Platform;
import me.skiincraft.api.paladins.objects.player.PlayerStatus;
import me.skiincraft.api.paladins.objects.player.PlayerStatus.Status;
import me.skiincraft.api.paladins.objects.player.SearchPlayer;
import me.skiincraft.api.paladins.objects.ranking.Place;
import me.skiincraft.api.paladins.objects.ranking.Tier;
import me.skiincraft.api.paladins.storage.PaladinsStorage;
import me.skiincraft.api.paladins.storage.impl.PaladinsStorageImpl;
import org.slf4j.Logger;

import javax.annotation.Nonnull;
import javax.annotation.Nullable;
import java.io.IOException;
import java.lang.reflect.Array;
import java.time.OffsetDateTime;
import java.util.*;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.Collector;
import java.util.stream.Collectors;

import static java.util.stream.Collectors.collectingAndThen;
import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;

public class EndpointImpl implements EndPoint {

    private final Paladins paladins;
    private final Session session;
    private final PaladinsStorage paladinsStorage;
    private final Logger logger;

    private final PaladinsStorageImpl storageImpl;

    public EndpointImpl(Session session) {
        this.session = session;
        this.paladins = session.getPaladins();
        this.paladinsStorage = paladins.getStorage();
        this.storageImpl = (PaladinsStorageImpl) paladinsStorage;
        this.logger = PaladinsLogger.getLogger(EndPoint.class);
    }

    public APIRequest<Player> getPlayer(String player) {
        String[] args = new String[]{player};
        return new DefaultAPIRequest<>("getplayer", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string())
                        .getAsJsonArray();
                if (array.size() == 0) {
                    throw new PlayerException(String.format("The player '%s' does not exist.", player), PlayerException.PlayerExceptionType.NOT_EXIST);
                }
                JsonObject object = array.get(0).getAsJsonObject();
                if (!object.get("ret_msg").isJsonNull()) {
                    if (object.get("ret_msg").getAsString().contains("Player Privacy Flag set for"))
                        throw new PlayerException(String.format("The '%s' player has the private profile.", player), PlayerException.PlayerExceptionType.PRIVATE_PROFILE);
                }

                return new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                        .create().fromJson(object, PlayerImpl.class)
                        .setEndpoint(this)
                        .setRaw(object);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    @Override
    public APIRequest<Player> getPlayer(String player, Platform platform) {
        if (platform == Platform.PC || platform == Platform.Console) {
            throw new ContextException("You need to specify the subplatform!");
        }
        String[] args = new String[]{player, String.valueOf(platform.getPortalId()[0])};
        return new DefaultAPIRequest<>("getplayer", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string())
                        .getAsJsonArray();
                if (array.size() == 0) {
                    throw new PlayerException(String.format("The player '%s' does not exist.", player), PlayerException.PlayerExceptionType.NOT_EXIST);
                }
                JsonObject object = array.get(0).getAsJsonObject();
                if (!object.get("ret_msg").isJsonNull()) {
                    if (object.get("ret_msg").getAsString().contains("Player Privacy Flag set for"))
                        throw new PlayerException(String.format("The '%s' player has the private profile.", player), PlayerException.PlayerExceptionType.PRIVATE_PROFILE);
                }

                return new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                        .create().fromJson(object, PlayerImpl.class)
                        .setEndpoint(this)
                        .setRaw(object);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<SearchPlayers> searchPlayer(String queue, Platform platform) {
        String[] args = new String[]{queue};
        return new DefaultAPIRequest<>("searchplayers", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new SearchException("No players found");

                logger.debug("SearchPlayers: found {} total results for {}", array.size(), queue);
                SearchPlayer[] search = new Gson().fromJson(array, SearchPlayer[].class);
                return (platform != null) ? new SearchPlayers(Arrays.stream(search).filter(s -> platform.isSubplatform(Platform.getPlatformByPortalId(s.getPortalId())))
                        .toArray(SearchPlayer[]::new)) : new SearchPlayers(search);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<PlayerStatus> getPlayerStatus(String player) {
        String[] args = new String[]{player};
        return new DefaultAPIRequest<>("getplayerstatus", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new PlayerException("The requested Player does not exist, or has a private profile", PlayerException.PlayerExceptionType.UNDEFINED);

                JsonObject object = array.get(0).getAsJsonObject();
                return new PlayerStatus(player, object.get("Match").getAsLong(),
                        Status.getStatusById(object.get("status").getAsInt()), this);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<Champions> getChampions(Language language) {
        if (paladinsStorage.championsIsStored(language)) {
            return new FakeAPIRequest<>(paladinsStorage.getChampionsFromStorage(language), 200);
        }
        String[] args = new String[]{String.valueOf(language.getLanguagecode())};
        return new DefaultAPIRequest<>("getchampions", session.getSessionId(), args, (response) -> {
            try {
                ChampionImpl[] championsArray = new GsonBuilder().registerTypeAdapter(ChampionImpl.class, new ChampionAdapter(language, this))
                        .create().fromJson(Objects.requireNonNull(response.body(), "json is null").string(), ChampionImpl[].class);

                Champions champions = new Champions(Arrays.asList(championsArray), language);
                storageImpl.store(champions);
                return champions;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    @Nullable
    public APIRequest<Champion> getChampion(long championId, Language language) {
        Champion champion = getChampions(language).get().getById(championId);
        if (champion != null) {
            return new FakeAPIRequest<>(champion, 200);
        }
        throw new ChampionException("This requested champion does not exist.");
    }

    @Nullable
    public APIRequest<Champion> getChampion(String championName, Language language) {
        Champion champion = getChampions(language).get().getAsStream()
                .filter(ch -> ch.getName().equalsIgnoreCase(championName))
                .findFirst()
                .orElse(null);

        if (champion != null) {
            return new FakeAPIRequest<>(champion, 200);
        }
        throw new ChampionException("This requested champion does not exist.");
    }

    public APIRequest<Cards> getChampionCards(long championsId, Language language) {
        if (paladinsStorage.cardsIsStored(championsId, language)) {
            return new FakeAPIRequest<>(paladinsStorage.getCardsFromStorage(championsId, language), 200);
        }
        String[] args = new String[]{String.valueOf(championsId), String.valueOf(language.getLanguagecode())};
        return new DefaultAPIRequest<>("getchampioncards", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string())
                        .getAsJsonArray();
                if (array.size() == 0)
                    throw new ChampionException("This requested champion does not exist.");

                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(Card.class, new CardImpl(championsId, language))
                        .create();

                Cards cards = new Cards(new ArrayList<>(Arrays.asList(gson.fromJson(array, Card[].class))), championsId, language);
                storageImpl.store(cards);
                return cards;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public DefaultAPIRequest<List<Skins>> getChampionsSkins(Language language) {
        String[] args = new String[]{"-1", String.valueOf(language.getLanguagecode())};
        return new DefaultAPIRequest<>("getchampionskins", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new ChampionException("This requested champion does not exist.");

                Map<Long, List<ChampionSkin>> champions = new HashMap<>();
                List<ChampionSkin> skin = new ArrayList<>();
                for (JsonElement element : array) {
                    skin.add(new Gson().fromJson(element.getAsJsonObject(), ChampionSkinImpl.class)
                            .setLanguage(language)
                            .setEndPoint(this));
                }

                return skin.stream().collect(collectionChampionSkins(language));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    private Collector<ChampionSkin, ?, List<Skins>> collectionChampionSkins(Language language) {
        return collectingAndThen(
                groupingBy(ChampionSkin::getChampionId),
                map -> {
                    List<Skins> skins = new ArrayList<>();
                    for (Map.Entry<Long, List<ChampionSkin>> entry : map.entrySet()) {
                        skins.add(new Skins(entry.getValue(), entry.getKey(), language));
                    }
                    return skins;
                }
        );
    }

    public APIRequest<Skins> getChampionSkin(long championsId, Language language) {
        if (paladinsStorage.skinIsStored(championsId, language)) {
            return new FakeAPIRequest<>(paladinsStorage.getSkinFromStorage(championsId, language), 200);
        }
        String[] args = new String[]{String.valueOf(championsId), String.valueOf(language.getLanguagecode())};
        return new DefaultAPIRequest<>("getchampionskins", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new ChampionException("This requested champion does not exist.");

                List<ChampionSkin> skin = new ArrayList<>();
                for (JsonElement element : array) {
                    skin.add(new Gson().fromJson(element.getAsJsonObject(), ChampionSkinImpl.class)
                            .setLanguage(language)
                            .setEndPoint(this));
                }
                return new Skins(skin, championsId, language);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<PlayerBatch> getPlayerBatch(@Nonnull List<Long> idList) {
        if (idList.size() <= 1)
            throw new ContextException("There are only 1 or less players being requested, use the getPlayer() method!");

        String[] args = new String[]{idList.stream().map(String::valueOf)
                .collect(Collectors.joining(","))};

        return new DefaultAPIRequest<>("getplayerbatch", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                List<Player> players = new ArrayList<>();
                for (JsonElement element : array) {
                    JsonObject object = element.getAsJsonObject();
                    if (object.has("ret_msg") && !object.get("ret_msg").isJsonNull()) {
                        if (object.get("ret_msg").getAsString().contains("Player Privacy Flag set for")) {
                            System.err.println(object.get("ret_msg").getAsString());
                            continue;
                        }
                    }
                    players.add(new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                            .create().fromJson(object, PlayerImpl.class)
                            .setEndpoint(this)
                            .setRaw(object));
                }
                return new PlayerBatch(players, players.stream().filter(player -> idList.stream().anyMatch(id -> id == player.getId()))
                        .map(Player::getId)
                        .collect(toList()));

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<PlayerChampions> getPlayerChampions(long userId) {
        String[] args = new String[]{String.valueOf(userId)};
        return new DefaultAPIRequest<>("getchampionranks", args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new PlayerException("The requested Player does not exist, or has a private profile", PlayerException.PlayerExceptionType.UNDEFINED);

                List<PlayerChampion> champions = new ArrayList<>();
                for (JsonElement element : array) {
                    champions.add(new GsonBuilder().registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                            .create()
                            .fromJson(element.getAsJsonObject(), PlayerChampionImpl.class));
                }
                return new PlayerChampions(champions);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }

        }, paladins);
    }

    public APIRequest<QueueChampions> getQueueStats(long userId, @Nonnull Queue queue) {
        String[] args = new String[]{String.valueOf(userId), String.valueOf(queue.getQueueId())};
        return new DefaultAPIRequest<>("getqueuestats", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.isEmpty())
                    throw new PlayerException("The requested Player does not exist, or has a private profile", PlayerException.PlayerExceptionType.UNDEFINED);

                List<QueueChampion> champions = new ArrayList<>();
                for (JsonElement element : array) {
                    champions.add(new GsonBuilder()
                            .registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                            .registerTypeAdapter(Queue.class, new QueueAdapter())
                            .create()
                            .fromJson(element.getAsJsonObject(), QueueChampionImpl.class)
                            .setEndPoint(this));
                }
                System.out.println(champions);
                return new QueueChampions(champions, queue);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    @Override
    public APIRequest<MultiQueueChampions> getQueueStatsBatch(long userId, List<Queue> queues) {
        if (queues.size() <= 1)
            return new FakeAPIRequest<>(
                    new MultiQueueChampions(new QueueChampions[] {getQueueStats(userId, queues.get(0)).get()}), 200);

        String[] args = new String[]{String.valueOf(userId), queues.stream().map(queue -> String.valueOf(queue.getQueueId()))
                .collect(Collectors.joining(","))};

        return new DefaultAPIRequest<>("getqueuestatsbatch", session.getSessionId(), args, (response) -> {
            try {
                List<QueueChampion> queueChampions = new ArrayList<>();
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();

                if (array.isEmpty())
                    throw new PlayerException("The requested Player does not exist, or has a private profile", PlayerException.PlayerExceptionType.UNDEFINED);

                for (JsonElement element : array) {
                    queueChampions.add(new GsonBuilder()
                            .registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                            .registerTypeAdapter(Queue.class, new QueueAdapter())
                            .create()
                            .fromJson(element.getAsJsonObject(), QueueChampionImpl.class)
                            .setEndPoint(this));
                }

                return constructMultiQueueChampions(queueChampions);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    private MultiQueueChampions constructMultiQueueChampions(List<QueueChampion> list) {
        Map<Queue, List<QueueChampion>> map = mapChampionsToQueue(list);
        QueueChampions[] queueChampions = new QueueChampions[map.size()];
        return traceMappedQueueChampions(map, queueChampions);
    }

    private Map<Queue, List<QueueChampion>> mapChampionsToQueue(List<QueueChampion> list) {
        EnumMap<Queue, List<QueueChampion>> map = new EnumMap<>(Queue.class);
        for(QueueChampion queueChampion : list) {
            Queue queue = queueChampion.getQueue();
            if (map.containsKey(queue)) {
                map.get(queue).add(queueChampion);
            } else {
                map.put(queue, new ArrayList<>(Collections.singletonList(queueChampion)));
            }
        }
        return map;
    }

    private MultiQueueChampions traceMappedQueueChampions(Map<Queue, List<QueueChampion>> map, QueueChampions[] queueChampions) {
        int i = 0;
        for (Map.Entry<Queue, List<QueueChampion>> entry : map.entrySet()) {
            queueChampions[i] = new QueueChampions(entry.getValue(), entry.getKey());
            i++;
        }
        return new MultiQueueChampions(queueChampions);
    }

    public APIRequest<Friends> getFriends(long userId) {
        String[] args = new String[]{String.valueOf(userId)};
        return new DefaultAPIRequest<>("getfriends", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new SearchException("It was not possible to find the friends of the requested user, or he does not exist.");

                Gson gson = new Gson();
                List<Friend> friendList = new ArrayList<>();
                for (JsonElement element : array) {
                    friendList.add(gson.fromJson(element.getAsJsonObject(), FriendImpl.class)
                            .setEndPoint(this));
                }
                return new Friends(friendList, this);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<Loadouts> getLoadouts(long userId, Language language) {
        String[] args = new String[]{String.valueOf(userId), String.valueOf(language.getLanguagecode())};
        return new DefaultAPIRequest<>("getplayerloadouts", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new SearchException("It was not possible to find the friends of the requested user, or he does not exist.");

                Gson gson = new Gson();
                List<Loadout> loadoutList = new ArrayList<>();
                for (JsonElement element : array) {
                    loadoutList.add(gson.fromJson(element.getAsJsonObject(), LoadoutImpl.class)
                            .setLanguage(language)
                            .setEndPoint(this));
                }
                return new Loadouts(loadoutList);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<Match> getMatchDetails(long matchId) {
        if (storageImpl.matchIsStored(matchId)) {
            return new FakeAPIRequest<>(storageImpl.getMatchFromStorage(matchId), 200);
        }
        String[] args = new String[]{String.valueOf(matchId)};
        return new DefaultAPIRequest<>("getmatchdetails", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new MatchException("It was not possible to find this match.");

                return new GsonBuilder()
                        .registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                        .create()
                        .fromJson(array.get(0), MatchImpl.class)
                        .buildMethods(array, this);

            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<List<Match>> getMatchDetails(@Nonnull List<Long> matchBatch) {
        if (matchBatch.size() <= 1)
            throw new ContextException("There are only 1 or less matchid being requested, use the getMatchDetails(long) method!");

        List<Match> matchs = storageImpl.getMatchStorage().getAsList()
                .stream()
                .filter(match -> matchBatch.stream().distinct().anyMatch(id -> id == match.getMatchId()))
                .collect(toList());

        if (matchs.size() == matchBatch.stream().distinct().count()) {
            return new FakeAPIRequest<>(matchs, 200);
        }

        String[] args = new String[]{matchBatch.stream()
                .distinct()
                .filter(id -> matchs.stream().noneMatch(match -> match.getMatchId() == id))
                .map(String::valueOf).collect(Collectors.joining(","))};

        return new DefaultAPIRequest<>("getmatchdetailsbatch", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0) {
                    return matchs;
                }

                AtomicInteger num = new AtomicInteger(0);
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                        .create();

                for (int i = 0; i < array.size() / 10; i++) {
                    JsonArray matchArray = new JsonArray();
                    for (int o = num.get(); o < num.get() + 10; o++) {
                        matchArray.add(array.get(o).getAsJsonObject());
                    }
                    Match match = gson.fromJson(matchArray.get(0), MatchImpl.class)
                            .buildMethods(matchArray, this);

                    storageImpl.store(match);
                    matchs.add(match);

                    num.set(num.get() + 10);
                }
                return matchs;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<List<HistoryMatch>> getMatchHistory(long userId) {
        String[] args = new String[]{String.valueOf(userId)};
        return new DefaultAPIRequest<>("getmatchhistory", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new MatchException("It was not possible to find this match.");

                List<HistoryMatch> historyMatches = new ArrayList<>();
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                        .create();

                for (JsonElement element : array) {
                    historyMatches.add(gson.fromJson(element, HistoryMatchImpl.class)
                            .buildMethods(element.getAsJsonObject(), this));
                }
                return historyMatches;
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<LeaderBoard> getLeaderboard(@Nonnull Tier tier, int season) {
        String[] args = new String[]{Queue.Live_Competitive_Keyboard.getQueueId() + "", tier.getRankId() + "", season + ""};
        return new DefaultAPIRequest<>("getLeagueLeaderboard", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new SearchException("It was not possible to find this leaderboard.");

                List<Place> place = new ArrayList<>();
                AtomicInteger integer = new AtomicInteger(1);
                Gson gson = new Gson();
                for (JsonElement element : array) {
                    place.add(gson.fromJson(element, PlaceImpl.class)
                            .setTier(tier)
                            .setPosition(integer.getAndIncrement())
                            .setEndPoint(this));
                }
                return new LeaderboardImpl(place, tier);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public APIRequest<LiveMatch> getMatchPlayerDetails(long matchId) {
        String[] args = new String[]{String.valueOf(matchId)};
        return new DefaultAPIRequest<>("getmatchplayerdetails", session.getSessionId(), args, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new MatchException("It was not possible to find this match.");

                return new Gson().fromJson(array.get(0), LiveMatchImpl.class);
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    @Override
    @Deprecated
    public APIRequest<List<BountyItem>> getBountyItems() {
        return new DefaultAPIRequest<>("getBountyItems", session.getSessionId(), null, (response) -> {
            try {
                JsonArray array = JsonParser.parseString(Objects.requireNonNull(response.body(), "json is null").string()).getAsJsonArray();
                if (array.size() == 0)
                    throw new RequestException("There was a problem ordering items from the bounty store");

                return Arrays.asList(new GsonBuilder()
                        .registerTypeAdapter(OffsetDateTime.class, new PaladinsDateAdapter())
                        .create().fromJson(array, BountyItem[].class));
            } catch (IOException e) {
                e.printStackTrace();
                return null;
            }
        }, paladins);
    }

    public Session getSession() {
        return session;
    }

    @Override
    public String toString() {
        return "Endpoint{" +
                "session=" + session +
                '}';
    }
}
