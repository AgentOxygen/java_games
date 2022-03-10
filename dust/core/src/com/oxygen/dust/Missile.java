package com.oxygen.dust;

import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

import java.util.Random;

public class Missile extends SpaceObject{

    public static final float bodyWidth = 0.03f;
    public static final float bodyHeight = 0.005f;

    private float impact_obj_angle;

    public Missile(World world, float spawnX, float spawnY, Vector2 traj, float angle) {
        super(world, spawnX, spawnY);
        this.getBody().setLinearVelocity(traj);
        this.getBody().setTransform(spawnX, spawnY, angle);
        this.getBody().setBullet(true);
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

        body.createFixture(boundaryPolygon, 3f);
        boundaryPolygon.dispose();
        return body;
    }

    public SpaceObject[] explode(World world, Random random){
        int num = random.nextInt(1) + 6;
        SpaceObject[] roids = new SpaceObject[num];

        for (int i = 0; i < num; i++){
            double angle = impact_obj_angle - (Math.PI / 2) + random.nextDouble()*(Math.PI);
            angle *= -1;
            float x = this.getBody().getPosition().x + random.nextFloat() * bodyWidth * ((float) Math.cos(angle));
            float y = this.getBody().getPosition().y + random.nextFloat() * bodyHeight * ((float) Math.sin(angle));
            float v_mag = (float) Math.sqrt(Math.pow(this.getBody().getLinearVelocity().x, 2) + Math.pow(this.getBody().getLinearVelocity().y, 2));
            roids[i] = new MissileFragment(world, x, y);
            roids[i].getBody().setTransform(x, y, (float)angle);
            roids[i].getBody().setLinearVelocity(-1*v_mag*(float) Math.cos(angle), v_mag*(float) Math.sin(-1*angle));
        }
        return roids;
    }

    @Override
    public void onCollision(Fixture self, Fixture other) {
        impact_obj_angle = other.getBody().getAngle();
        this.destroy = 0;
    }
}

