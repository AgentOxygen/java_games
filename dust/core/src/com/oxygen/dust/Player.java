package com.oxygen.dust;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.*;

public class Player extends SpaceObject{
    // Forward thrust force
    private float thrustMag = 0.001f;
    // Rate at which the ship readjusts its angular velocity to aim at the mouse
    private float angularThrustMag = 0.1f;

    public static final float bodyWidth = 0.1f, bodyHeight = 0.1f;

    public Player(World world, float spawnX, float spawnY){
        super(world,spawnX, spawnY);
    }

    @Override
    public Sprite getSprite() {
        int[] spriteDimensions = SpaceObject.scaleUp(3.33f, 3.33f);
        int pWidth = spriteDimensions[0];
        int pHeight = spriteDimensions[1];
        float scaling = (float)spriteDimensions[3] / spriteDimensions[2];

        Pixmap pmap = new Pixmap(pWidth, pHeight, Pixmap.Format.RGBA8888);
        pmap.setColor(settings.PLAYER_COLOR);
        pmap.fillRectangle(0, 0, pWidth, pHeight);

        Sprite sprite = new Sprite(new TextureRegion(new Texture(pmap)));
        //sprite.scale(scaling);
        return sprite;
    }

    @Override
    public Body createBody(World world, float spawnX, float spawnY) {
        BodyDef bodyDef = new BodyDef();
        bodyDef.position.set(spawnX, spawnY);
        bodyDef.type = BodyDef.BodyType.DynamicBody;

        Body body = world.createBody(bodyDef);

        PolygonShape boundaryPolygon = new PolygonShape();
        boundaryPolygon.setAsBox(bodyWidth/2, bodyHeight/2);

        body.createFixture(boundaryPolygon, 0.1f);
        boundaryPolygon.dispose();
        return body;
    }

    @Override
    public void onCollision(Fixture self, Fixture other) {

    }

    public void setAngle(float angle) {
        float x = this.getBody().getPosition().x;
        float y = this.getBody().getPosition().y;
        if (this.getBody().getAngularVelocity() < -1*angularThrustMag){
            this.getBody().setAngularVelocity(this.getBody().getAngularVelocity() + angularThrustMag);
        }else if (this.getBody().getAngularVelocity() > angularThrustMag){
            this.getBody().setAngularVelocity(this.getBody().getAngularVelocity() - angularThrustMag);
        }else{
            this.getBody().setTransform(x, y, angle);
        }
    }

    public float getVelocityMag(){
        return (float) (Math.sqrt(Math.pow(getBody().getLinearVelocity().x, 2) + Math.pow(getBody().getLinearVelocity().y, 2)));
    }

    public void thrust(Vector2 direction){
        this.getBody().applyForceToCenter(direction.scl(thrustMag), true);
    }
}
