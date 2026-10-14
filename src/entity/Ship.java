package entity;


import java.awt.Color;
import java.util.Set;

import engine.Cooldown;
import engine.Core;
import engine.DrawManager.SpriteType;

/**
 * Implements a ship, to be controlled by the player.
 *
 * @author <a href="mailto:RobertoIA1987@gmail.com">Roberto Izquierdo Amo</a>
 *
 */
public class Ship extends Entity {

	/** Time between shots. */
	private static final int SHOOTING_INTERVAL = 400;
	/** Speed of the bullets shot by the ship. */
	private static final int BULLET_SPEED = -6;
	/** Movement of the ship for each unit of time. */
	private static final int SPEED = 2;



	/** Whether the Hyperfire buff is currently enabled. */
	private boolean hyperfireEnabled = false;

	/** Time of the last shot. */
	private long lastShotTime = 0;

	/** Time spent inactive between hits. */
	private Cooldown destructionCooldown;

	private boolean isShieldActive = false;

	/** Minimum time between two grenade launches. */
	private static final int GRENADE_COOLDOWN = 800;
	/** Maximum number of grenades the ship can carry. */
	private static final int MAX_GRENADES = 1;

	/** Grenades left (given by the Cluster Grenade item). */
	private int grenadeCount = 0;
	/** Minimum time between two grenade launches. */
	private Cooldown grenadeCooldown;

	/**
	 * Constructor, establishes the ship's properties.
	 *
	 * @param positionX
	 *            Initial position of the ship in the X axis.
	 * @param positionY
	 *            Initial position of the ship in the Y axis.
	 */
	public Ship(final int positionX, final int positionY) {
		super(positionX, positionY, 13 * 2, 8 * 2, Color.GREEN);

		this.spriteType = SpriteType.Ship;

		this.destructionCooldown = Core.getCooldown(1000);
		this.grenadeCooldown = Core.getCooldown(GRENADE_COOLDOWN);
	}

	/**
	 * Moves the ship speed uni ts right, or until the right screen border is
	 * reached.
	 */
	public final void moveRight() {
		this.positionX += SPEED;
	}

	/**
	 * Moves the ship speed units left, or until the left screen border is
	 * reached.
	 */
	public final void moveLeft() {
		this.positionX -= SPEED;
	}

	/**
	 * Shoots a bullet upwards.
	 *
	 * @param bullets
	 *            List of bullets on screen, to add the new bullet.
	 * @return Checks if the bullet was shot correctly.
	 */

	public final boolean shoot(final Set<Bullet> bullets) {
		long currentTime = System.currentTimeMillis();

		int shootingInterval = SHOOTING_INTERVAL;

		if (this.hyperfireEnabled) {
			shootingInterval = (int) (SHOOTING_INTERVAL * 0.40);
		}

		if (lastShotTime == 0 ||
				currentTime - lastShotTime >= shootingInterval) {

			lastShotTime = currentTime;

			bullets.add(BulletPool.getBullet(
					positionX + this.width / 2,
					positionY,
					BULLET_SPEED
			));

			return true;
		}

		return false;
	}


	/**
	 * Enables or disables the Hyperfire shooting buff.
	 *
	 * @param enabled True to enable Hyperfire, false to disable it.
	 */
	public final void setHyperfireEnabled(final boolean enabled) {
		this.hyperfireEnabled = enabled;
	}
	/**
	 * Checks whether Hyperfire is currently enabled.
	 */
	public final boolean isHyperfireEnabled() {
		return this.hyperfireEnabled;
	}



	/**
	 * Updates status of the ship.
	 */
	public final void update() {
		if (!this.destructionCooldown.checkFinished())
			this.spriteType = SpriteType.ShipDestroyed;
		else
			this.spriteType = SpriteType.Ship;
	}

	/**
	 * Switches the ship to its destroyed state.
	 */
	public final void destroy() {
		this.destructionCooldown.reset();
	}

	/**
	 * Checks if the ship is destroyed.
	 *
	 * @return True if the ship is currently destroyed.
	 */
	public final boolean isDestroyed() {
		return !this.destructionCooldown.checkFinished();
	}

	/**
	 * Getter for the ship's speed.
	 *
	 * @return Speed of the ship.
	 */
	public final int getSpeed() {
		return SPEED;
	}
	public boolean isShieldActive() {
		return this.isShieldActive;
	}

	public void setShieldActive(final boolean active) {
		this.isShieldActive = active;
	}

	/**
	 * Adds grenades to the ship's stock.
	 *
	 * @param amount
	 *            Number of grenades to add.
	 */
	public void addGrenades(final int amount) {
		this.grenadeCount = Math.min(MAX_GRENADES,
				this.grenadeCount + amount);
	}

	/**
	 * Getter for the number of grenades left.
	 *
	 * @return Number of grenades left.
	 */
	public int getGrenadeCount() {
		return this.grenadeCount;
	}

	/**
	 * Launches a grenade upwards if the ship has one and the launcher is
	 * ready.
	 *
	 * @param grenades
	 *            Set of grenades on screen, to add the new grenade.
	 * @param detonationY
	 *            Altitude at which the grenade explodes if it hits nothing.
	 * @return True if a grenade was launched.
	 */
	public boolean launchGrenade(final Set<Grenade> grenades,
								 final int detonationY) {
		if (this.grenadeCount > 0 && this.grenadeCooldown.checkFinished()) {
			this.grenadeCooldown.reset();
			this.grenadeCount--;
			grenades.add(new Grenade(positionX + this.width / 2, positionY,
					detonationY));
			return true;
		}
		return false;
	}
}