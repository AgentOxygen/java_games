package com.oxygen.bunker;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public final class GameObjects {

    public static Body RECT(World world, float x, float y, float w, float h, String id){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));

        FixtureDef fixDef = new FixtureDef();
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(w, h);

        fixDef.shape = polyShape;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixDef);
        body.setUserData(id);

        return body;
    }
}
