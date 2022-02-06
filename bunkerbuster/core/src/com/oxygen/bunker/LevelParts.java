package com.oxygen.bunker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class LevelParts {

    public static Vector2 getPartTextureSize(String id){
        switch (id){
            default:
                return new Vector2(1, 1);
        }
    }

    public static Texture getPartTexture(String id){
        switch (id){
            default:
                return new Texture(Gdx.files.internal("debug.png"));
        }
    }

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

    public static Body PLAYER(World world, float x, float y){
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(new Vector2(x, y));
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        FixtureDef fixDef = new FixtureDef();
        PolygonShape polyShape = new PolygonShape();
        polyShape.setAsBox(1f, 1f);
        fixDef.restitution = 0.5f;

        fixDef.shape = polyShape;
        Body body = world.createBody(bodyDef);
        body.createFixture(fixDef);
        body.setUserData("player");

        return body;
    }
}
