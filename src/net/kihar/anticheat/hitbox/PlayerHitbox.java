package net.kihar.anticheat.hitbox;

public class PlayerHitbox {
    private static final double HITBOX_SIZE = 0.41;

    private final double x, y, z;

    private final double minX, maxX;
    private final double minZ, maxZ;

    public PlayerHitbox(double x, double y, double z) {
        this.x = x;
        minX = x - HITBOX_SIZE;
        maxX = x + HITBOX_SIZE;

        this.y = y;

        this.z = z;
        minZ = z - HITBOX_SIZE;
        maxZ = z + HITBOX_SIZE;
    }

    public double getDistanceSquared(PlayerHitbox hitbox) {
        double dx = Math.min(Math.abs(hitbox.x - minX), Math.abs(hitbox.x - maxX));
        double dz = Math.min(Math.abs(hitbox.z - minZ), Math.abs(hitbox.z - maxZ));

        return dx * dx + dz * dz;
    }
}
