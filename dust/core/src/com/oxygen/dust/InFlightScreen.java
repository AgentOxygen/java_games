package com.oxygen.dust;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.ScreenUtils;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class InFlightScreen implements Screen, InputProcessor {
    public static final int VIEWPORT_WIDTH = 1000, VIEWPORT_HEIGHT = 800;
    public static final int PIXELS_PER_METER = 100;

    private OrthographicCamera camera;
    private Viewport viewport;
    private Stage stage;
    private Space space;
    private Settings settings;
    private SpriteBatch batch;

    private Box2DDebugRenderer debugRenderer;
    private ShapeRenderer shapeRenderer;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        camera.setToOrtho(true, (float)(VIEWPORT_WIDTH)/PIXELS_PER_METER, (float)(VIEWPORT_HEIGHT)/PIXELS_PER_METER);
        camera.update();

        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        space = new Space(viewport);
        settings = new Settings();
        batch = new SpriteBatch();

        debugRenderer = new Box2DDebugRenderer();

        stage = new Stage(viewport);
        stage.getViewport().apply();
        shapeRenderer = new ShapeRenderer();
        shapeRenderer.setAutoShapeType(true);

        Gdx.input.setInputProcessor(this);
    }

    @Override
    public void render(float delta) {
        ScreenUtils.clear(settings.BKGD_COLOR);
        camera.position.set(space.getPlayerPos(), 0);
        shapeRenderer.setProjectionMatrix(camera.combined);
        batch.setProjectionMatrix(camera.combined);

        stage.getCamera().update();
        stage.act();
        stage.draw();

        if(Gdx.input.isKeyPressed(Input.Keys.SPACE)){
            space.thrustPlayer();
        }
        space.renderDebug(shapeRenderer, settings);
        space.render(batch, settings);

        debugRenderer.render(space.getWorld(), camera.combined);
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
        return false;
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
        return false;
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
        return false;
    }
}
