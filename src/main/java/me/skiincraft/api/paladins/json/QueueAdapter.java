package me.skiincraft.api.paladins.json;

import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import me.skiincraft.api.paladins.objects.match.Queue;

import java.io.IOException;

public class QueueAdapter extends TypeAdapter<Queue> {

    @Override
    public void write(JsonWriter out, Queue queue) throws IOException {
        out.value(queue.getName());
    }

    @Override
    public Queue read(JsonReader in) throws IOException {
        String input = in.nextString();
        try {
            return Queue.getQueueByName(input.replace(" ", "_"));
        } catch (NumberFormatException e) {
            return null;
        }
    }
}
