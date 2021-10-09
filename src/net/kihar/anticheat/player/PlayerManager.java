package net.kihar.anticheat.player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;
import lombok.Getter;
import net.kihar.anticheat.AnticheatPlugin;

@Getter
public class PlayerManager {

	private final AnticheatPlugin plugin = AnticheatPlugin.getInstance();

	private final Map<UUID, PlayerData> playerDataMap = new HashMap<>();

}
