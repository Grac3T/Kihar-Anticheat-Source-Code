package net.kihar.anticheat.util;

import org.bukkit.entity.*;
import org.bukkit.block.*;
import org.bukkit.*;
import java.util.*;

public class BlockUtil
{
	private static Set<Byte> blockSolidPassSet;
	private static Set<Byte> blockStairsSet;
	private static Set<Byte> blockLiquidsSet;
	private static Set<Byte> blockWebsSet;
	private static Set<Byte> blockIceSet;
	private static Set<Byte> blockCarpetSet;
	public static List<Material> blocked;

	public static boolean isOnStairs(final Location location, final int down) {
		return isUnderBlock(location, BlockUtil.blockStairsSet, down);
	}

	public static boolean isOnLiquid(final Location location, final int down) {
		return isUnderBlock(location, BlockUtil.blockLiquidsSet, down);
	}

	public static boolean isOnWeb(final Location location, final int down) {
		return isUnderBlock(location, BlockUtil.blockWebsSet, down);
	}

	public static boolean isOnIce(final Location location, final int down) {
		return isUnderBlock(location, BlockUtil.blockIceSet, down);
	}

	public static boolean isOnCarpet(final Location location, final int down) {
		return isUnderBlock(location, BlockUtil.blockCarpetSet, down);
	}

	public static boolean isSlab(final Player player) {
		return BlockUtil.blocked.contains(player.getLocation().getBlock().getRelative(BlockFace.DOWN).getType());
	}

	public static boolean isBlockFaceAir(final Player player) {
		final Block block = player.getLocation().getBlock().getRelative(BlockFace.DOWN);
		return block.getType().equals((Object)Material.AIR) && block.getRelative(BlockFace.WEST).getType().equals((Object)Material.AIR) && block.getRelative(BlockFace.NORTH).getType().equals((Object)Material.AIR) && block.getRelative(BlockFace.EAST).getType().equals((Object)Material.AIR) && block.getRelative(BlockFace.SOUTH).getType().equals((Object)Material.AIR);
	}

	private static boolean isUnderBlock(final Location location, final Set<Byte> itemIDs, final int down) {
		final double posX = location.getX();
		final double posZ = location.getZ();
		final double fracX = (posX % 1.0 > 0.0) ? Math.abs(posX % 1.0) : (1.0 - Math.abs(posX % 1.0));
		final double fracZ = (posZ % 1.0 > 0.0) ? Math.abs(posZ % 1.0) : (1.0 - Math.abs(posZ % 1.0));
		final int blockX = location.getBlockX();
		final int blockY = location.getBlockY() - down;
		final int blockZ = location.getBlockZ();
		final World world = location.getWorld();
		if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
			return true;
		}
		if (fracX < 0.3) {
			if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
				return true;
			}
			if (fracZ < 0.3) {
				if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
			}
			else if (fracZ > 0.7) {
				if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
			}
		}
		else if (fracX > 0.7) {
			if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId())) {
				return true;
			}
			if (fracZ < 0.3) {
				if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
			}
			else if (fracZ > 0.7) {
				if (itemIDs.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (itemIDs.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
			}
		}
		else if (fracZ < 0.3) {
			if (itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
				return true;
			}
		}
		else if (fracZ > 0.7 && itemIDs.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
			return true;
		}
		return false;
	}

	public static boolean isOnGround(final Location location, final int down) {
		final double posX = location.getX();
		final double posZ = location.getZ();
		final double fracX = (posX % 1.0 > 0.0) ? Math.abs(posX % 1.0) : (1.0 - Math.abs(posX % 1.0));
		final double fracZ = (posZ % 1.0 > 0.0) ? Math.abs(posZ % 1.0) : (1.0 - Math.abs(posZ % 1.0));
		final int blockX = location.getBlockX();
		final int blockY = location.getBlockY() - down;
		final int blockZ = location.getBlockZ();
		final World world = location.getWorld();
		if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ).getTypeId())) {
			return true;
		}
		if (fracX < 0.3) {
			if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ).getTypeId())) {
				return true;
			}
			if (fracZ < 0.3) {
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
			}
			else if (fracZ > 0.7) {
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
			}
		}
		else if (fracX > 0.7) {
			if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ).getTypeId())) {
				return true;
			}
			if (fracZ < 0.3) {
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ - 1).getTypeId())) {
					return true;
				}
			}
			else if (fracZ > 0.7) {
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX - 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
				if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX + 1, blockY, blockZ + 1).getTypeId())) {
					return true;
				}
			}
		}
		else if (fracZ < 0.3) {
			if (!BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ - 1).getTypeId())) {
				return true;
			}
		}
		else if (fracZ > 0.7 && !BlockUtil.blockSolidPassSet.contains((byte)world.getBlockAt(blockX, blockY, blockZ + 1).getTypeId())) {
			return true;
		}
		return false;
	}

	static {
		BlockUtil.blocked = new ArrayList<Material>();
		BlockUtil.blockSolidPassSet = new HashSet<Byte>();
		BlockUtil.blockStairsSet = new HashSet<Byte>();
		BlockUtil.blockLiquidsSet = new HashSet<Byte>();
		BlockUtil.blockWebsSet = new HashSet<Byte>();
		BlockUtil.blockIceSet = new HashSet<Byte>();
		BlockUtil.blockCarpetSet = new HashSet<Byte>();
		BlockUtil.blockSolidPassSet.add((byte)0);
		BlockUtil.blockSolidPassSet.add((byte)6);
		BlockUtil.blockSolidPassSet.add((byte)8);
		BlockUtil.blockSolidPassSet.add((byte)9);
		BlockUtil.blockSolidPassSet.add((byte)10);
		BlockUtil.blockSolidPassSet.add((byte)11);
		BlockUtil.blockSolidPassSet.add((byte)27);
		BlockUtil.blockSolidPassSet.add((byte)28);
		BlockUtil.blockSolidPassSet.add((byte)30);
		BlockUtil.blockSolidPassSet.add((byte)31);
		BlockUtil.blockSolidPassSet.add((byte)32);
		BlockUtil.blockSolidPassSet.add((byte)37);
		BlockUtil.blockSolidPassSet.add((byte)38);
		BlockUtil.blockSolidPassSet.add((byte)39);
		BlockUtil.blockSolidPassSet.add((byte)40);
		BlockUtil.blockSolidPassSet.add((byte)50);
		BlockUtil.blockSolidPassSet.add((byte)51);
		BlockUtil.blockSolidPassSet.add((byte)55);
		BlockUtil.blockSolidPassSet.add((byte)59);
		BlockUtil.blockSolidPassSet.add((byte)63);
		BlockUtil.blockSolidPassSet.add((byte)66);
		BlockUtil.blockSolidPassSet.add((byte)68);
		BlockUtil.blockSolidPassSet.add((byte)69);
		BlockUtil.blockSolidPassSet.add((byte)70);
		BlockUtil.blockSolidPassSet.add((byte)72);
		BlockUtil.blockSolidPassSet.add((byte)75);
		BlockUtil.blockSolidPassSet.add((byte)76);
		BlockUtil.blockSolidPassSet.add((byte)77);
		BlockUtil.blockSolidPassSet.add((byte)78);
		BlockUtil.blockSolidPassSet.add((byte)83);
		BlockUtil.blockSolidPassSet.add((byte)90);
		BlockUtil.blockSolidPassSet.add((byte)104);
		BlockUtil.blockSolidPassSet.add((byte)105);
		BlockUtil.blockSolidPassSet.add((byte)115);
		BlockUtil.blockSolidPassSet.add((byte)119);
		BlockUtil.blockSolidPassSet.add((byte)(-124));
		BlockUtil.blockSolidPassSet.add((byte)(-113));
		BlockUtil.blockSolidPassSet.add((byte)(-81));
		BlockUtil.blockStairsSet.add((byte)53);
		BlockUtil.blockStairsSet.add((byte)67);
		BlockUtil.blockStairsSet.add((byte)108);
		BlockUtil.blockStairsSet.add((byte)109);
		BlockUtil.blockStairsSet.add((byte)114);
		BlockUtil.blockStairsSet.add((byte)(-128));
		BlockUtil.blockStairsSet.add((byte)(-122));
		BlockUtil.blockStairsSet.add((byte)(-121));
		BlockUtil.blockStairsSet.add((byte)(-120));
		BlockUtil.blockStairsSet.add((byte)(-100));
		BlockUtil.blockStairsSet.add((byte)(-93));
		BlockUtil.blockStairsSet.add((byte)(-92));
		BlockUtil.blockStairsSet.add((byte)(-76));
		BlockUtil.blockStairsSet.add((byte)126);
		BlockUtil.blockStairsSet.add((byte)(-74));
		BlockUtil.blockStairsSet.add((byte)44);
		BlockUtil.blockStairsSet.add((byte)78);
		BlockUtil.blockStairsSet.add((byte)99);
		BlockUtil.blockStairsSet.add((byte)(-112));
		BlockUtil.blockStairsSet.add((byte)(-115));
		BlockUtil.blockStairsSet.add((byte)(-116));
		BlockUtil.blockStairsSet.add((byte)(-105));
		BlockUtil.blockStairsSet.add((byte)(-108));
		BlockUtil.blockStairsSet.add((byte)100);
		BlockUtil.blockLiquidsSet.add((byte)8);
		BlockUtil.blockLiquidsSet.add((byte)9);
		BlockUtil.blockLiquidsSet.add((byte)10);
		BlockUtil.blockLiquidsSet.add((byte)11);
		BlockUtil.blockWebsSet.add((byte)30);
		BlockUtil.blockIceSet.add((byte)79);
		BlockUtil.blockIceSet.add((byte)(-82));
		BlockUtil.blockCarpetSet.add((byte)(-85));
		BlockUtil.blocked.add(Material.ACTIVATOR_RAIL);
		BlockUtil.blocked.add(Material.ANVIL);
		BlockUtil.blocked.add(Material.BED_BLOCK);
		BlockUtil.blocked.add(Material.POTATO);
		BlockUtil.blocked.add(Material.POTATO_ITEM);
		BlockUtil.blocked.add(Material.CARROT);
		BlockUtil.blocked.add(Material.CARROT_ITEM);
		BlockUtil.blocked.add(Material.BIRCH_WOOD_STAIRS);
		BlockUtil.blocked.add(Material.BREWING_STAND);
		BlockUtil.blocked.add(Material.BOAT);
		BlockUtil.blocked.add(Material.BRICK_STAIRS);
		BlockUtil.blocked.add(Material.BROWN_MUSHROOM);
		BlockUtil.blocked.add(Material.CAKE_BLOCK);
		BlockUtil.blocked.add(Material.CARPET);
		BlockUtil.blocked.add(Material.CAULDRON);
		BlockUtil.blocked.add(Material.COBBLESTONE_STAIRS);
		BlockUtil.blocked.add(Material.COBBLE_WALL);
		BlockUtil.blocked.add(Material.DARK_OAK_STAIRS);
		BlockUtil.blocked.add(Material.DIODE);
		BlockUtil.blocked.add(Material.DIODE_BLOCK_ON);
		BlockUtil.blocked.add(Material.DIODE_BLOCK_OFF);
		BlockUtil.blocked.add(Material.DEAD_BUSH);
		BlockUtil.blocked.add(Material.DETECTOR_RAIL);
		BlockUtil.blocked.add(Material.DOUBLE_PLANT);
		BlockUtil.blocked.add(Material.DOUBLE_STEP);
		BlockUtil.blocked.add(Material.DRAGON_EGG);
		BlockUtil.blocked.add(Material.PAINTING);
		BlockUtil.blocked.add(Material.FLOWER_POT);
		BlockUtil.blocked.add(Material.GOLD_PLATE);
		BlockUtil.blocked.add(Material.HOPPER);
		BlockUtil.blocked.add(Material.STONE_PLATE);
		BlockUtil.blocked.add(Material.IRON_PLATE);
		BlockUtil.blocked.add(Material.HUGE_MUSHROOM_1);
		BlockUtil.blocked.add(Material.HUGE_MUSHROOM_2);
		BlockUtil.blocked.add(Material.IRON_DOOR_BLOCK);
		BlockUtil.blocked.add(Material.IRON_DOOR);
		BlockUtil.blocked.add(Material.FENCE);
		BlockUtil.blocked.add(Material.IRON_FENCE);
		BlockUtil.blocked.add(Material.IRON_PLATE);
		BlockUtil.blocked.add(Material.ITEM_FRAME);
		BlockUtil.blocked.add(Material.JUKEBOX);
		BlockUtil.blocked.add(Material.JUNGLE_WOOD_STAIRS);
		BlockUtil.blocked.add(Material.LADDER);
		BlockUtil.blocked.add(Material.LEVER);
		BlockUtil.blocked.add(Material.LONG_GRASS);
		BlockUtil.blocked.add(Material.NETHER_FENCE);
		BlockUtil.blocked.add(Material.NETHER_STALK);
		BlockUtil.blocked.add(Material.NETHER_WARTS);
		BlockUtil.blocked.add(Material.MELON_STEM);
		BlockUtil.blocked.add(Material.PUMPKIN_STEM);
		BlockUtil.blocked.add(Material.QUARTZ_STAIRS);
		BlockUtil.blocked.add(Material.RAILS);
		BlockUtil.blocked.add(Material.RED_MUSHROOM);
		BlockUtil.blocked.add(Material.RED_ROSE);
		BlockUtil.blocked.add(Material.SAPLING);
		BlockUtil.blocked.add(Material.SEEDS);
		BlockUtil.blocked.add(Material.SIGN);
		BlockUtil.blocked.add(Material.SIGN_POST);
		BlockUtil.blocked.add(Material.SKULL);
		BlockUtil.blocked.add(Material.SMOOTH_STAIRS);
		BlockUtil.blocked.add(Material.NETHER_BRICK_STAIRS);
		BlockUtil.blocked.add(Material.SPRUCE_WOOD_STAIRS);
		BlockUtil.blocked.add(Material.STAINED_GLASS_PANE);
		BlockUtil.blocked.add(Material.REDSTONE_COMPARATOR);
		BlockUtil.blocked.add(Material.REDSTONE_COMPARATOR_OFF);
		BlockUtil.blocked.add(Material.REDSTONE_COMPARATOR_ON);
		BlockUtil.blocked.add(Material.REDSTONE_LAMP_OFF);
		BlockUtil.blocked.add(Material.REDSTONE_LAMP_ON);
		BlockUtil.blocked.add(Material.REDSTONE_TORCH_OFF);
		BlockUtil.blocked.add(Material.REDSTONE_TORCH_ON);
		BlockUtil.blocked.add(Material.REDSTONE_WIRE);
		BlockUtil.blocked.add(Material.SANDSTONE_STAIRS);
		BlockUtil.blocked.add(Material.STEP);
		BlockUtil.blocked.add(Material.ACACIA_STAIRS);
		BlockUtil.blocked.add(Material.SUGAR_CANE);
		BlockUtil.blocked.add(Material.SUGAR_CANE_BLOCK);
		BlockUtil.blocked.add(Material.ENCHANTMENT_TABLE);
		BlockUtil.blocked.add(Material.SOUL_SAND);
		BlockUtil.blocked.add(Material.TORCH);
		BlockUtil.blocked.add(Material.TRAP_DOOR);
		BlockUtil.blocked.add(Material.TRIPWIRE);
		BlockUtil.blocked.add(Material.TRIPWIRE_HOOK);
		BlockUtil.blocked.add(Material.WALL_SIGN);
		BlockUtil.blocked.add(Material.VINE);
		BlockUtil.blocked.add(Material.WATER_LILY);
		BlockUtil.blocked.add(Material.WEB);
		BlockUtil.blocked.add(Material.WOOD_DOOR);
		BlockUtil.blocked.add(Material.WOOD_DOUBLE_STEP);
		BlockUtil.blocked.add(Material.WOOD_PLATE);
		BlockUtil.blocked.add(Material.WOOD_STAIRS);
		BlockUtil.blocked.add(Material.WOOD_STEP);
		BlockUtil.blocked.add(Material.HOPPER);
		BlockUtil.blocked.add(Material.WOODEN_DOOR);
		BlockUtil.blocked.add(Material.YELLOW_FLOWER);
		BlockUtil.blocked.add(Material.LAVA);
		BlockUtil.blocked.add(Material.WATER);
		BlockUtil.blocked.add(Material.STATIONARY_WATER);
		BlockUtil.blocked.add(Material.STATIONARY_LAVA);
		BlockUtil.blocked.add(Material.CACTUS);
		BlockUtil.blocked.add(Material.CHEST);
		BlockUtil.blocked.add(Material.PISTON_BASE);
		BlockUtil.blocked.add(Material.PISTON_MOVING_PIECE);
		BlockUtil.blocked.add(Material.PISTON_EXTENSION);
		BlockUtil.blocked.add(Material.PISTON_STICKY_BASE);
		BlockUtil.blocked.add(Material.TRAPPED_CHEST);
		BlockUtil.blocked.add(Material.SNOW);
		BlockUtil.blocked.add(Material.ENDER_CHEST);
		BlockUtil.blocked.add(Material.THIN_GLASS);
		BlockUtil.blocked.add(Material.ENDER_PORTAL_FRAME);
	}
}
