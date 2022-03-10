package com.oxygen.dust;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class Asteroid extends SpaceObject{

    private Vector2[] vertices;
    public static final float ASTEROID_DENSITY = 0.1f;
    public static final float MAX_RADIUS = 0.3f;
    public static final float MIN_RADIUS = 0.2f;
    public static final int MIN_FRACTURE = 5;
    public static final int MAX_FRACTURE = 6;
    private float impact_obj_momenta = 0;
    private float impact_obj_angle = 0;

    public Asteroid(World world, float spawnX, float spawnY, Random random) {
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
        body.setBullet(true);

        PolygonShape boundaryPolygon = new PolygonShape();
        vertices = createAsteroidVertices(MIN_RADIUS, MAX_RADIUS);
        boundaryPolygon.set(vertices);
        body.createFixture(boundaryPolygon, ASTEROID_DENSITY);
        boundaryPolygon.dispose();
        return body;
    }

    @Override
    public void onCollision(Fixture self, Fixture other) {
        Vector2 obj_vel = other.getBody().getLinearVelocity();
        impact_obj_momenta = (float) Math.pow(Math.pow(obj_vel.x, 2) + Math.pow(obj_vel.y, 2), 0.5) * other.getBody().getMass();
        impact_obj_angle = other.getBody().getAngle();
        this.destroy = 1;
    }

    public SpaceObject[] fracture(World world, Random random){
        int num = this.random.nextInt(MAX_FRACTURE - MIN_FRACTURE) + MIN_FRACTURE;
        SpaceObject[] roids = new SpaceObject[num];

        // For now, I am going to spawn them on top of each other and let Box2D break them apart
        // If this proves to be CPU intensive, then I will write something smarter.
        //double angleDelta = (2*Math.PI) / vertices.length;
        //double angle = 0;
        for (int i = 0; i < num; i++){
            //float radius = MIN_RADIUS + this.random.nextFloat()*(Meteoroid.MAX_RADIUS - Meteoroid.MIN_RADIUS);
            float x = this.getBody().getPosition().x;//(float)(Math.cos(angle)* radius) + this.getBody().getPosition().x;
            float y = this.getBody().getPosition().y;//(float)(Math.sin(angle)* radius) + this.getBody().getPosition().y;
            roids[i] = new Meteoroid(world, x, y, random);
            float v_mag = impact_obj_momenta/(this.getBody().getMass() / num);
            double angle = impact_obj_angle - (Math.PI / 2) + random.nextDouble()*(Math.PI);
            roids[i].getBody().setLinearVelocity(-1*v_mag*(float) Math.cos(angle), v_mag*(float) Math.sin(-1*angle));
            //angle += angleDelta;
        }
        return roids;
    }
}
