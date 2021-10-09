package net.kihar.anticheat.check;

import java.util.HashSet;
import java.util.Set;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import net.evilblock.stark.Stark;
import net.evilblock.stark.core.StarkCore;
import net.evilblock.stark.core.rank.Rank;
import net.evilblock.stark.profile.BukkitProfile;
import net.kihar.anticheat.AnticheatPlugin;
import net.kihar.anticheat.player.PlayerData;
import net.kihar.anticheat.player.tracker.ActionTracker;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_7_R4.entity.CraftPlayer;
import org.bukkit.entity.Player;

@RequiredArgsConstructor
@Getter
public abstract class AbstractCheck<T> implements ICheck<T> {

	protected final AnticheatPlugin plugin = AnticheatPlugin.getInstance();

	protected final PlayerData playerData;
	protected final Class<T> type;

	protected final String name;

	protected final ActionTracker actionTracker;

	protected double vl;

	@Setter
	private boolean useViolation = true;

	private final Set<Long> violations = new HashSet<>();

	public AbstractCheck(PlayerData playerData, Class<T> type, String name) {
		this.playerData = playerData;
		this.type = type;
		this.name = name;

		this.actionTracker = this.playerData.getActionTracker();
	}


	protected void flag(Player player, String alert) {
		BukkitProfile profile = Stark.instance.core.getProfileHandler().getByUUID(player.getUniqueId());
		Rank rank = profile.getRank();

		String theAlert = ChatColor.GRAY + "[" + ChatColor.DARK_RED + "Phantom" + ChatColor.GRAY + "] " + ChatColor.RED + profile.getPlayerListName() + ChatColor.GRAY + " flagged " + ChatColor.RED + this.name +
				ChatColor.GRAY + " " + alert + ChatColor.GRAY + " [" + ChatColor.RED + "Ping: " + ChatColor.GRAY + ((CraftPlayer) player).getHandle().ping + "]" ;

		this.plugin.getAlertsManager().sendMessage(theAlert);

		this.violations.removeIf(v -> System.currentTimeMillis() > v + 1200L);
		this.violations.add(System.currentTimeMillis());
		if (this.violations.size() == 5 && this.useViolation) {
			this.ban(player);
		}
	}

	protected void flag(Player player) {
		this.flag(player, "");
	}

	protected void ban(Player player) {
		if (playerData.isBanning())
			return;
		if (!player.hasPermission("ac.bypass")) {

			playerData.setBanning(true);
			this.plugin.getServer().getScheduler().runTask(this.plugin, () -> {
				this.plugin.getServer().dispatchCommand(this.plugin.getServer().getConsoleSender(), "ban -s " + player.getName() + " Cheating");

				this.plugin.getServer().broadcastMessage(ChatColor.RED + " " + ChatColor.RED);
				this.plugin.getServer().broadcastMessage(ChatColor.DARK_RED + "Phantom" + ChatColor.RED + " removed " + ChatColor.DARK_RED + player.getName() + ChatColor.RED + " for cheating!");
				this.plugin.getServer().broadcastMessage(ChatColor.RED + " " + ChatColor.RED);
			});
		} else {}


		this.plugin.getPlayerManager().getPlayerDataMap().remove(this.playerData.getUuid());
	}

	protected Player getPlayer() {
		return this.plugin.getServer().getPlayer(this.playerData.getUuid());
	}

	protected void setVl(double vl) {
		this.vl = Math.max(0D, vl);
	}
}
