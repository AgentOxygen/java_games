package com.oxygen.dust;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.physics.box2d.*;

public class MissileFragment extends SpaceObject {

    public static final float bodyWidth = 0.01f;
    public static final float bodyHeight = 0.01f;

    public MissileFragment(World world, float spawnX, float spawnY) {
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
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        PolygonShape boundaryPolygon = new PolygonShape();
        boundaryPolygon.setAsBox(bodyWidth/2, bodyHeight/2);

        body.createFixture(boundaryPolygon, 6f);
        boundaryPolygon.dispose();
        return body;
    }

    @Override
    public void onCollision(Fixture self, Fixture other) {
        this.destroy = 1;
    }
}
