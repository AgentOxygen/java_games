package com.oxygen.bunker;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.World;

public class Player {
    // Contains all data pertaining to the player
    private World world;
    private Body playerBody;

    // Player settings
    public static final float PLAYER_ACCEL = 1.0f;
    public static final float PLAYER_MAX_BASE_SPEED = 1.0f;
    public static final float PLAYER_JUMP_FORCE = 10f;

    // Movement directions
    public static final int LEFT = 2;
    public static final int RIGHT = 1;
    public static final int IDLE = 0;

    public Player(World world, float initX, float initY){
        this.world = world;
        playerBody = LevelParts.PLAYER(world, initX, initY);
        playerBody.setBullet(true);
    }

    public void jumpPlayer(){
        playerBody.applyLinearImpulse(new Vector2(0, PLAYER_JUMP_FORCE), playerBody.getPosition(), true);
    }

    public void movePlayer(int direction){
        switch (direction) {
            case LEFT:
                playerBody.setLinearVelocity(playerBody.getLinearVelocity().x - PLAYER_MAX_BASE_SPEED, playerBody.getLinearVelocity().y);
                break;
            case RIGHT:
                playerBody.setLinearVelocity(playerBody.getLinearVelocity().x + PLAYER_MAX_BASE_SPEED, playerBody.getLinearVelocity().y);
                break;
            default:
                playerBody.setLinearVelocity(0, 0);
                break;
        }
    }

    public Vector2 getPos(){
        return playerBody.getPosition();
    }
}
