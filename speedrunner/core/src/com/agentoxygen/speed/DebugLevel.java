package com.agentoxygen.speed;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class DebugLevel {
    private World world;
    private Body player;

    public DebugLevel(World world_){
        world = world_;
        createDebugStuff();
    }

    private void createDebugStuff(){
        // Create Player
        BodyDef playerDef = new BodyDef();
        playerDef.type = BodyDef.BodyType.DynamicBody;
        playerDef.position.set(new Vector2(7, 3));

        player = world.createBody(playerDef);
        PolygonShape playerBox = new PolygonShape();
        playerBox.setAsBox(0.3f, 1f);
        player.createFixture(playerBox, 0.0f);

        FixtureDef fixtureDef = new FixtureDef();
        fixtureDef.shape = playerBox;
        fixtureDef.density = 0.5f;
        fixtureDef.friction = 0.4f;
        fixtureDef.restitution = 0.6f; // Make it bounce a little bit

        // Create our fixture and attach it to the body
        Fixture fixture = player.createFixture(fixtureDef);
        playerBox.dispose();

        // Add floor
        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(80, 0));

        Body body = world.createBody(groundBodyDef);
        PolygonShape floorBox = new PolygonShape();
        floorBox.setAsBox(80, 0.2f);
        body.createFixture(floorBox, 0.0f);
        floorBox.dispose();
    }

    public Vector2 getPlayerPos(){
        return player.getPosition();
    }

    public float getPlayerAccel(){
        return 1f;
    }

    public void impulsePlayer(Vector2 force){
        player.applyLinearImpulse(force.x, force.y, getPlayerPos().x, getPlayerPos().y, true);
    }
}
