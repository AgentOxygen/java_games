package com.oxygen.dust;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class Meteoroid extends SpaceObject{

    private Vector2[] vertices;
    public static final float MAX_RADIUS = 0.1f;
    public static final float MIN_RADIUS = 0.05f;

    public Meteoroid(World world, float spawnX, float spawnY, Random random) {
        super(world, spawnX, spawnY, random);
    }

    @Override
    public Sprite getSprite() {
        return null;
    }

    private Vector2[] createAsteroidVertices(float minRadius, float maxRadius){
        // Note that PolygonShape has max 8 vertices
        Vector2 vertices[] = new Vector2[7];
        vertices[0] = new Vector2(1, 2);
        double angleDelta = (2*Math.PI) / vertices.length;
        double angle = 0;
        for (int i = 0; i < vertices.length; i++){
            float radius = minRadius + this.random.nextFloat()*(maxRadius - minRadius);
            vertices[i] = new Vector2((float) Math.cos(angle) * radius, (float) Math.sin(angle) * radius);
            angle += angleDelta;
        }
        return vertices;
    }

    @Override
    public Body createBody(World world, float spawnX, float spawnY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX, spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        PolygonShape boundaryPolygon = new PolygonShape();
        vertices = createAsteroidVertices(MIN_RADIUS, MAX_RADIUS);
        boundaryPolygon.set(vertices);
        body.createFixture(boundaryPolygon, Asteroid.ASTEROID_DENSITY);
        boundaryPolygon.dispose();
        return body;
    }

    @Override
    public void onCollision(Fixture self, Fixture other) {

    }
}
