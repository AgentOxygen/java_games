package com.oxygen.dust;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;

public class Player extends SpaceObject{

    public Player(World world){
        super(world, 5, 5);

    }

    public void applyForce(Vector2 force){
        this.getBody().applyForce(force, this.getBody().getPosition(), true);
    }

    @Override
    public Body createBody(World world, float spawnX, float spawnY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX, spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        PolygonShape boundaryPolygon = new PolygonShape();
        boundaryPolygon.setAsBox(80, 80);

        body.createFixture(boundaryPolygon, 0.0f);
        boundaryPolygon.dispose();
        return body;
    }

    @Override
    public TextureRegion getTextureReg(Settings settings){
        Pixmap pmap = new Pixmap(80, 80, Pixmap.Format.RGBA8888);
        pmap.setColor(settings.PLAYER_COLOR);
        pmap.fillRectangle(0, 0, 80, 80);
        return new TextureRegion(new Texture(pmap));
    }

    public void anglePlayer(float angle) {
        float x = this.getBody().getPosition().x;
        float y = this.getBody().getPosition().y;
        this.getBody().setTransform(x, y, angle);
    }

    public void thrustPlayer(Vector2 direction){
        this.getBody().setLinearVelocity(direction.scl(100));
    }
}
