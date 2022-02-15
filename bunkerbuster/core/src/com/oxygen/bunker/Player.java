package com.oxygen.bunker;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player {
    public static final float PLAYER_WIDTH = 1f;
    public static final float PLAYER_HEIGHT = 1f;
    public static final String PLAYER_ID = "PLAYER";
    public Body body;
    private PolygonShape polyShape;

    public Player(World world, Vector2 initPos){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(initPos);

        FixtureDef fixDef = new FixtureDef();
        polyShape = new PolygonShape();
        polyShape.setAsBox(PLAYER_WIDTH, PLAYER_HEIGHT);

        fixDef.shape = polyShape;
        body = world.createBody(bodyDef);
        body.createFixture(fixDef);
        body.setUserData(PLAYER_ID);
    }

    public PolygonShape getShape(){
        return polyShape;
    }

    public void dispose(){
        polyShape.dispose();
    }
}
