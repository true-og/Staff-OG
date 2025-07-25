package uk.hotten.staffog.punish.data;

import java.util.UUID;
import lombok.Getter;

public class ChatReportEntry {

    @Getter
    private UUID uuid;

    @Getter
    private String name;

    @Getter
    private String message;

    @Getter
    private long time;

    public ChatReportEntry(UUID uuid, String name, String message, long time) {

        this.uuid = uuid;
        this.name = name;
        this.message = message;
        this.time = time;
    }
}
