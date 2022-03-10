package com.oxygen.dust;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;
import java.util.UUID;

public abstract class SpaceObject {
    private Body body;
    private Sprite sprite;
    public Settings settings = new Settings();
    public Random random;
    // When true, the game will eventually delete the body from the world and remove this object when Space.checkAndDestroySpaceObjs() is called.
    public int destroy = -1;
    private final UUID uuid = UUID.randomUUID();

    public SpaceObject(World world, float spawnX, float spawnY){
        this.sprite = getSprite();
        body = createBody(world, spawnX, spawnY);
        body.setUserData(this);
    }
    public SpaceObject(World world, float spawnX, float spawnY, Random random){
        this.sprite = getSprite();
        this.random = random;
        body = createBody(world, spawnX, spawnY);
        body.setUserData(this);
    }
    public Body getBody(){
        return body;
    }
    public UUID getUUID(){
        return uuid;
    }

    // Called when rendering the object
    public abstract Sprite getSprite();
    // Called when creating the object (in constructor)
    public abstract Body createBody(World world, float spawnX, float spawnY);
    @Deprecated
    public static int[] scaleUp(float x, float y){
        /**
         *  Work in progress for scaling world to screen
         */
        int[] scaled = new int[4];
        String xString = Float.toString(Math.abs(x));
        String yString = Float.toString(Math.abs(y));

        int scale = ((int) Math.pow(10, Math.max(xString.length() - xString.indexOf('.') - 1, yString.length() - yString.indexOf('.') - 1)));
        int x1 = Math.round(scale*x);
        int y1 = Math.round(scale*y);
        int greatComDen = x1, divisor = y1;

        while (divisor > 0) {
            int temp = divisor;
            divisor = greatComDen % divisor;
            greatComDen = temp;
        }
        scaled[0] = x1 / greatComDen;
        scaled[1] = y1 / greatComDen;
        scaled[2] = greatComDen;
        scaled[3] = scale;
        return scaled;
    }
    // Called on collision with another object
    public abstract void onCollision(Fixture self, Fixture other);
}
