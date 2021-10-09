package net.kihar.anticheat.listener;

import net.kihar.anticheat.AnticheatPlugin;
import net.kihar.anticheat.player.PlayerData;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerMoveEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class PlayerListener implements Listener {

	private final AnticheatPlugin plugin = AnticheatPlugin.getInstance();

	@EventHandler
	public void onJoin(PlayerJoinEvent event) {
		this.plugin.getPlayerManager().getPlayerDataMap().put(event.getPlayer().getUniqueId(), new PlayerData(event.getPlayer()));

		if (event.getPlayer().hasPermission("ac.alerts")){
			this.plugin.getAlertsManager().getPlayers().add(event.getPlayer().getUniqueId());
		}
	}

	@EventHandler
	public void onQuit(PlayerQuitEvent event) {
		this.plugin.getAlertsManager().getPlayers().removeIf(u -> u == event.getPlayer().getUniqueId());
	}

	@EventHandler
	public void onMove(PlayerMoveEvent event) {
		Player player = event.getPlayer();
		PlayerData playerData = this.plugin.getPlayerManager().getPlayerDataMap().get(player.getUniqueId());

		if (event.getTo().getY() < 2D)
			return;

		if (playerData != null) {
			playerData.handleMove(event);
		}
	}
}
