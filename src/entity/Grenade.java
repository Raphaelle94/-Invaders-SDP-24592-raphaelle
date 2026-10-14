package entity;

import java.awt.Color;

public class Grenade extends Entity {

    public static final int SIZE = 10;
    private static final int SPEED = -3;

    private final int detonationY;

    public Grenade(final int centerX, final int positionY,
                   final int detonationY) {
        super(centerX - SIZE / 2, positionY, SIZE, SIZE, Color.ORANGE);
        this.detonationY = detonationY;
    }

    public final void update() {
        this.positionY += SPEED;
    }

    public final boolean hasReachedAltitude() {
        return this.positionY <= this.detonationY;
    }

    public final int getCenterX() {
        return this.positionX + this.width / 2;
    }

    public final int getCenterY() {
        return this.positionY + this.height / 2;
    }
}