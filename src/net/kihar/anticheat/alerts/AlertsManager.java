package net.kihar.anticheat.alerts;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import java.util.UUID;
import lombok.Getter;
import net.kihar.anticheat.AnticheatPlugin;

@Getter
public class AlertsManager {

	private final AnticheatPlugin plugin = AnticheatPlugin.getInstance();

	private final Set<UUID> players = new HashSet<>();

	public void sendMessage(String message) {
		this.players.stream()
				.map(this.plugin.getServer()::getPlayer)
				.filter(Objects::nonNull)
				.forEach(p -> p.sendMessage(message));
	}
}
