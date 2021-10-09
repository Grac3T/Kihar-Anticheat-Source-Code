package net.kihar.anticheat.commands;

import net.kihar.anticheat.AnticheatPlugin;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class AlertsCommand extends Command {

	private final AnticheatPlugin plugin = AnticheatPlugin.getInstance();

	public AlertsCommand() {
		super("alerts");
		this.setPermission("ac.alerts");
	}

	@Override
	public boolean execute(CommandSender sender, String s, String[] strings) {
		if (!this.testPermission(sender)) {
			return true;
		}
		if (!(sender instanceof Player)) {
			sender.sendMessage(ChatColor.RED + "You need to be a player to run this command.");
			return true;
		}

		Player player = (Player) sender;

		if (this.plugin.getAlertsManager().getPlayers().remove(player.getUniqueId())) {
			player.sendMessage(ChatColor.RED + "You are no longer subscribed to anticheat alerts.");
		} else if (this.plugin.getAlertsManager().getPlayers().add(player.getUniqueId())) {
			player.sendMessage(ChatColor.GREEN + "You are now subscribed to anticheat alerts.");
		}

		return true;
	}
}
