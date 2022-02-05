package com.oxygen.bunker;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class LevelParts {
    public static LevelBody BODY(){
        BodyDef bodyDef = new BodyDef();
        FixtureDef fixDef = new FixtureDef();
        PolygonShape polyShape = new PolygonShape();
        fixDef.shape = polyShape;
        return new LevelBody(bodyDef, fixDef);
    }

    public static Body RECT(World world, float x, float y, float w, float h){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));

        FixtureDef fixDef = new FixtureDef();
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(w, h);

        fixDef.shape = polyShape;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixDef);

        return body;
    }

    public static Body PLAYER(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fixDef = new FixtureDef();
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(0.2f, 1.3f);

        fixDef.shape = polyShape;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixDef);

        return body;
    }
}
