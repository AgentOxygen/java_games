package com.oxygen.dust;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.BitmapFont;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Matrix4;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.*;

import java.math.RoundingMode;
import java.text.DecimalFormat;

public class InFlightScreen implements Screen, InputProcessor {
    public static final float WORLD_PIXEL_PER_UNIT = 500f;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Space space;
    private Settings settings;
    private SpriteBatch batch;

    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;
    private BitmapFont font;
    private Matrix4 worldMatrix;

    private OrthographicCamera cameraHUD;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true);
        camera.update();

        cameraHUD = new OrthographicCamera((float)(DustGame.SCREEN_WIDTH), (float)(DustGame.SCREEN_HEIGHT));
        cameraHUD.update();

        font = new BitmapFont();
        font.setColor(1, 0, 0, 1);

        viewport = new FillViewport((float)(DustGame.SCREEN_WIDTH) / WORLD_PIXEL_PER_UNIT, (float)(DustGame.SCREEN_HEIGHT) / WORLD_PIXEL_PER_UNIT, camera);
        viewport.apply();
        space = new Space(viewport);
        settings = new Settings();
        batch = new SpriteBatch();

        debugRenderer = new Box2DDebugRenderer();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        worldMatrix = new Matrix4(camera.projection);
        worldMatrix.scale(WORLD_PIXEL_PER_UNIT, WORLD_PIXEL_PER_UNIT, 1f);

        Gdx.input.setInputProcessor(this);
    }


    @Override
    public void render(float delta) {
        ScreenUtils.clear(settings.BKGD_COLOR);
        camera.position.set(space.getPlayerPos(), 0);
        camera.update();

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            space.thrustPlayer();
        }

        batch.setProjectionMatrix(cameraHUD.combined);
        batch.begin();
        font.draw(batch, getHUDDebugLeftInfo(space), cameraHUD.viewportWidth / -2 + 5, cameraHUD.viewportHeight / 2 - 5);
        font.draw(batch, getHUDDebugRightInfo(), cameraHUD.viewportWidth / 2 - 80, cameraHUD.viewportHeight / 2 - 5);
        batch.end();

        debugRenderer.render(space.getWorld(), camera.combined);
        space.doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    public String getHUDDebugLeftInfo(Space space){
        DecimalFormat df = new DecimalFormat("##.##");
        df.setRoundingMode(RoundingMode.DOWN);
        String info = String.format("Player Position: (%s, %s)\nPlayer Velocity: %s\nPlayer Angle: %s",
                df.format(space.getPlayerPos().x),
                df.format(space.getPlayerPos().y),
                df.format(space.getPlayer().getVelocityMag()),
                df.format(space.getPlayer().getBody().getAngle()));
        return info;
    }

    public String getHUDDebugRightInfo(){
        String info = String.format("FPS: %s", Gdx.graphics.getFramesPerSecond());
        return info;
    }


    @Override
    public void resize(int width, int height) {

    }

    @Override
    public void pause() {

    }

    @Override
    public void resume() {

    }

    @Override
    public void hide() {

    }

    @Override
    public void dispose() {
        shapeRenderer.dispose();
        batch.dispose();
        space.dispose();
    }

    @Override
    public boolean keyDown(int keycode) {
        return true;
    }

    @Override
    public boolean keyUp(int keycode) {
        return true;
    }

    @Override
    public boolean keyTyped(char character) {
        return false;
    }

    @Override
    public boolean touchDown(int screenX, int screenY, int pointer, int button) {
        if (button == Input.Buttons.LEFT){
            space.shootPlayerMissile();
        }
        return true;
    }

    @Override
    public boolean touchUp(int screenX, int screenY, int pointer, int button) {
        return false;
    }

    @Override
    public boolean touchDragged(int screenX, int screenY, int pointer) {
        return false;
    }

    @Override
    public boolean mouseMoved(int screenX, int screenY) {
        space.anglePlayer(screenX, screenY);
        return true;
    }

    @Override
    public boolean scrolled(float amountX, float amountY) {
        camera.zoom -= (float) amountY / 10f;
        return true;
    }
}
