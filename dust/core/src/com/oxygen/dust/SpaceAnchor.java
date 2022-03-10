package com.oxygen.dust;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class SpaceAnchor extends SpaceObject{
    public SpaceAnchor(World world, float spawnX, float spawnY) {
        super(world, spawnX, spawnY);
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    @Override
    public Body createBody(World world, float spawnX, float spawnY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX, spawnY);
        bodyDef.type = BodyDef.BodyType.StaticBody;

        Body body = world.createBody(bodyDef);

        PolygonShape boundaryPolygon = new PolygonShape();
        boundaryPolygon.setAsBox(0.1f, 0.1f);
        body.createFixture(boundaryPolygon, 0.1f);
        boundaryPolygon.dispose();
        return body;
    }

    @Override
    public void onCollision(Fixture self, Fixture other){
    }

}
