package entity.item;

import engine.DrawManager.SpriteType;
import entity.Ship;

/**
 * Cluster Grenade item.
 * Falls from a destroyed enemy ship (15% chance). When collected, it gives
 * the ship a grenade (1 at most) that can be launched with the G key.
 * A grenade explodes on impact (or at a fixed altitude) and splits into
 * 12 fragments that damage the enemies around the explosion.
 */
public class ClusterGrenadeItem extends Item {

    /** Number of grenades given by one item. */
    private static final int GRENADES_PER_PICKUP = 1;
    /** Size of the item on screen (same as DrawManager.drawGrenadeItem). */
    private static final int ITEM_WIDTH = 16;
    private static final int ITEM_HEIGHT = 22;

    /**
     * Creates a Cluster Grenade item at the given position.
     */
    public ClusterGrenadeItem(final int positionX, final int positionY) {
        // The sprite type is not used: ItemManager draws this item with
        // DrawManager.drawGrenadeItem (a green grenade).
        super(positionX, positionY, ITEM_WIDTH, ITEM_HEIGHT,
                SpriteType.Explosion);
    }

    /**
     * Gives grenades to the player ship.
     */
    @Override
    public void applyEffect(final Ship ship) {
        if (ship != null) {
            ship.addGrenades(GRENADES_PER_PICKUP);
        }
    }

    /**
     * Nothing to remove: the item has no duration, grenades stay in the
     * ship's stock until they are launched.
     */
    @Override
    public void removeEffect(final Ship ship) {
        // Instant item, no effect to remove.
    }

    /**
     * Instant item: a duration of 0 means the BuffManager does not track it.
     */
    @Override
    public long getDuration() {
        return 0;
    }

    /**
     * Returns the name of the item.
     */
    @Override
    public String getName() {
        return "Cluster Grenade";
    }
}
