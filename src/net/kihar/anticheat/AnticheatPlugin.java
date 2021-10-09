package net.kihar.anticheat;

import java.util.Arrays;
import java.util.Optional;
import lombok.Getter;
import net.kihar.anticheat.alerts.AlertsManager;
import net.kihar.anticheat.commands.AlertsCommand;
import net.kihar.anticheat.listener.PlayerListener;
import net.kihar.anticheat.player.PlayerManager;
import net.minecraft.server.v1_7_R4.MinecraftServer;
import org.bukkit.command.Command;
import org.bukkit.plugin.java.JavaPlugin;

@Getter
public class AnticheatPlugin extends JavaPlugin {

	private static Optional<AnticheatPlugin> instance;

	private PlayerManager playerManager;
	private AlertsManager alertsManager;

	@Override
	public void onEnable() {
		AnticheatPlugin.instance = Optional.of(this);

		this.playerManager = new PlayerManager();
		this.alertsManager = new AlertsManager();

		Arrays.asList(
				new PlayerListener()
		).forEach(l -> this.getServer().getPluginManager().registerEvents(l, this));

		Arrays.asList(
				new AlertsCommand()
		).forEach(this::registerCommand);
	}

	public static AnticheatPlugin getInstance() {
		return AnticheatPlugin.instance.orElseThrow(() -> new IllegalArgumentException("AntiCheatPlugin instance is null"));
	}

	private void registerCommand(Command cmd) {
		this.registerCommand(cmd, this.getName());
	}

	public void registerCommand(Command cmd, String fallbackPrefix) {
		MinecraftServer.getServer().server.getCommandMap().register(cmd.getName(), fallbackPrefix, cmd);
	}
}
