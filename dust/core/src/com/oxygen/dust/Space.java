package com.oxygen.dust;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.Viewport;


public class Space {
    private World world;
    private Viewport viewport;
    private Player player;

    public Space(Viewport viewport) {
        world = new World(new Vector2(0, 0), true);

        this.viewport = viewport;
        player = new Player(world);
    }

    public World getWorld(){
        return world;
    }

    public void render(SpriteBatch batch, Settings settings){
        player.fullRender(batch, viewport.getCamera());

        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    public void renderDebug(ShapeRenderer sRender, Settings settings){
        // Render corners for screen reference
        //renderDebugCorners(sRender, 20, settings.DEBUG_COLOR);
        // Render grid for space reference
        renderDebugGrid(sRender, 50, settings.DEBUG_COLOR);
    }

    public void anglePlayer(float mouseX, float mouseY){
        float angle = (float)(Math.atan2(mouseY - viewport.getWorldHeight() / 2, mouseX - viewport.getWorldWidth() / 2));
        if (angle > 0){
            angle = -1 * (float) (2*Math.PI - angle);
        }
        player.anglePlayer(angle);
    }

    private float accumulator = 0;
    private final float TIME_STEP = 1/60f;
    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
    }

    private void renderDebugGrid(ShapeRenderer sRender, int spacing, Color color){
        sRender.begin(ShapeRenderer.ShapeType.Line);
        sRender.setColor(color);
        Vector3 playerPos = viewport.getCamera().unproject(new Vector3(player.getBody().getPosition().x, player.getBody().getPosition().y, 0));
        float width = ((int) Math.ceil(viewport.getWorldWidth() / spacing));
        float height = ((int) Math.ceil(viewport.getWorldHeight() / spacing));
        float playerXC = playerPos.x;
        float playerYC = playerPos.y;

        for (int i = 0; i < width; i++){
            sRender.rectLine(i*spacing + playerXC, playerYC, i*spacing + playerXC, viewport.getWorldHeight() + playerYC, 1);
        }
        for (int j = 0; j < height; j++){
            sRender.rectLine(playerXC, j*spacing + playerYC, viewport.getWorldWidth() + playerXC, j*spacing + playerYC, 1);
        }

        sRender.end();
    }

    public void dispose(){

    }

    public Vector2 getPlayerPos() {
        return player.getBody().getPosition();
    }

    public void thrustPlayer() {
        float angle = player.getBody().getAngle();
        Vector2 direction = new Vector2(-1*(float)Math.cos(angle), -1*(float)Math.sin(angle));
        player.thrustPlayer(direction.nor());
    }
}
