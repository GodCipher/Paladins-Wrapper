package me.skiincraft.api.paladins.objects.match;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public enum Queue {
    Custom_Onslaught_Foreman_Rise(462, "Foreman's Rise"),
    Custom_Onslaught_Magistrate_Archives(464, "Magistrate Archives"),
    Custom_Onslaught_Marauders_Port(483, "Marauders Port"),
    Custom_Onslaught_Primal_Court(455, "Primal Court"),
    Custom_Onslaught_Snowfall_Junction(454, "Snowfall Junction"),
    Custom_Siege_Ascension_Peak(473, "Ascension Peak"),
    Custom_Siege_Bazaar(426, "Bazaar"),
    Custom_Siege_Brightmarsh(458, "Brightmarch"),
    Custom_Siege_Fish_Market(431, "Fish Market"),
    Custom_Siege_Frog_Isle(433, "Frog Isle"),
    Custom_Siege_Frozen_Guard(432, "Frozen Guard"),
    Custom_Siege_Ice_Mines(439, "Ice Mines"),
    Custom_Siege_Jaguar_Falls(438, "Jaguar Falls"),
    Custom_Siege_Serpeant_Beach(440, "Serpeant Beach"),
    Custom_Siege_Shattered_Desert(487, "Shattered Desert"),
    Custom_Siege_Splitstone_Quarry(459, "Splitstone Quarry"),
    Custom_Siege_Stone_Keep(423, "Stone Keep"),
    Custom_Siege_Timber_Mill(430, "Timber Mill"),
    Custom_Siege_Warders_Gate(485, "Warder's Gate"),
    Custom_Team_Deathmatch_Abyss(479, "Abyss"),
    Custom_Team_Deathmatch_Dragon_Arena(484, "Dragon Arena"),
    Custom_Team_Deathmatch_Foreman_Rise(471, "Foreman's Rise"),
    Custom_Team_Deathmatch_Magistrates_Archives(472, "Magistrate Archives"),
    Custom_Team_Deathmatch_Throne(480, "Throne"),
    Custom_Team_Deathmatch_Trade_District(468, "Trade District"),
    Live_Competitive_GamePad(428, "Competitive (Gamepad)"),
    Live_Competitive_Keyboard(486, "Competitive (Keyboard)"),
    Live_Onslaught(452, "Onslaught"),
    Live_Onslaught_Practice(453, "Onslaught (Practice)"),
    Live_Siege_Practice(425, "Siege (Practice)"),
    Live_Team_Deathmatch_Practice(470, "Deathmatch (Practice)"),
    Live_Siege(424, "Siege"),
    Live_Team_DeathMatch(469, "Deathmatch"),
    Live_Test_Maps(445, "Test Maps"),
    Payload(437, "Payload"),
    None(0, null);

    private final int queueId;
    private final String name;

    Queue(int queueId, String name) {
        this.queueId = queueId;
        this.name = name;
    }

    public static List<Queue> getLives() {
        return Arrays.stream(values())
                .filter(q -> q.name().contains("Live"))
                .collect(Collectors.toList());
    }

    public static List<Queue> getCustoms() {
        return Arrays.stream(values())
                .filter(q -> q.name().contains("Custom"))
                .collect(Collectors.toList());
    }

    public static Queue getQueueById(int id) {
        return Arrays.stream(values())
                .filter(q -> q.getQueueId() == id)
                .findFirst()
                .orElse(None);
    }

    public static Queue getQueueByName(String name) {
        return Arrays.stream(values())
                .filter(q -> q.name().equalsIgnoreCase(name))
                .findFirst()
                .orElse(None);
    }

    public String getName() {
        return name;
    }

    public int getQueueId() {
        return this.queueId;
    }
}
