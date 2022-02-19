package com.oxygen.dust;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;

public abstract class SpaceObject {
    private Body body;
    public Settings settings = new Settings();

    public SpaceObject(World world, float spawnX, float spawnY){
        body = createBody(world, spawnX, spawnY);
    }
    public Body getBody(){
        return body;
    }
    public void fullRender(SpriteBatch batch, Camera camera){
        TextureRegion textRegion = getTextureReg(settings);
        Vector3 bodyPos = camera.project(new Vector3(body.getPosition(), 0));

        float x = (bodyPos.x - camera.viewportWidth / 2);
        float y = (bodyPos.y - camera.viewportHeight / 2);
        float width = textRegion.getRegionWidth()*2;
        float height = textRegion.getRegionHeight()*2;

        batch.begin();
        batch.draw(textRegion, x - width/2, y - height/2, width/2, height/2, width, height, 1f, 1f, (float)Math.toDegrees(this.getBody().getAngle()));
        batch.end();
    }
    public abstract Body createBody(World world, float spawnX, float spawnY);
    public abstract TextureRegion getTextureReg(Settings settings);
}
