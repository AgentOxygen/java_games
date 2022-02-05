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

    // Movement directions
    public static final int LEFT = 2;
    public static final int RIGHT = 1;
    public static final int IDLE = 0;

    public Player(World world, float initX, float initY){
        this.world = world;
        playerBody = LevelParts.PLAYER(world, initX, initY);
    }

    public void movePlayer(int direction){
        switch(direction){
            case LEFT:
                break;
            case RIGHT:
                break;
            default:
                break;
        }
    }

    public Vector2 getPos(){
        return playerBody.getPosition();
    }
}
