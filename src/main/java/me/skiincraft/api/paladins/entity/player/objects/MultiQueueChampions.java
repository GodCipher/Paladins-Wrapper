package me.skiincraft.api.paladins.entity.player.objects;

import me.skiincraft.api.paladins.objects.match.Queue;

import java.util.Arrays;

/**
 * <h1>MultiQueueChampions</h1>
 *
 * <p>This class will have all the champions that were played by a player in multiple queues</p>
 */
public class MultiQueueChampions {

    private final QueueChampions[] queueChampions;

    public MultiQueueChampions(QueueChampions[] queueChampions) {
        this.queueChampions = queueChampions;
    }

    public QueueChampions[] getQueueChampions() {
        return queueChampions;
    }

    public QueueChampions getQueueChampions(Queue queue) {
        for (QueueChampions queueChampion : queueChampions) {
            if (queueChampion.getQueue().equals(queue)) {
                return queueChampion;
            }
        }
        return null;
    }

    public float getTotalKDA() {
        float totalKDA = 0;
        for (QueueChampions queueChampion : queueChampions) {
            totalKDA += queueChampion.getTotalKDA();
        }
        return totalKDA;
    }

    public float getTotalKDA(Queue queue) {
        QueueChampions queueChampions = getQueueChampions(queue);
        return queueChampions == null ? 0 : queueChampions.getTotalKDA();
    }

    @Override
    public String toString() {
        return "MultiQueueChampions{" +
                "queueChampions=" + Arrays.toString(queueChampions) +
                '}';
    }
}
