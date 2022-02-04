package com.agentoxygen.speed;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.ScreenViewport;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;


public class RunScreen implements Screen {
    private Game game;
    private Stage stage;
    private World world;
    private Viewport viewport;
    private Box2DDebugRenderer debugRenderer;
    private OrthographicCamera camera;
    private DebugLevel level;

    private final boolean render_debug = true;
    private static final int VIEWPORT_WIDTH = 18;
    private static final int VIEWPORT_HEIGHT = 9;
    private static final float TIME_STEP = 1/60f;

    public RunScreen(Game gameObj){
        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        camera.position.set(0, 0, 0);

        game = gameObj;
        stage = new Stage(viewport);
        stage.getViewport().apply();
        Gdx.input.setInputProcessor(new InputAdapter() {
            @Override
            public boolean touchDown (int x, int y, int pointer, int button) {
                return true;
            }

            @Override
            public boolean touchUp (int x, int y, int pointer, int button) {
                return true;
            }
            @Override
            public boolean keyDown(int keycode)
            {
                switch (keycode)
                {
                    case Input.Keys.D:
                        level.impulsePlayer(new Vector2(level.getPlayerAccel(), 0));
                        break;
                    case Input.Keys.A:
                        level.impulsePlayer(new Vector2(-1*level.getPlayerAccel(), 0));
                        break;
                }
                return true;
            }
        });
        debugRenderer = new Box2DDebugRenderer();
        world = new World(new Vector2(0, -10), true);
        level = new DebugLevel(world);
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(level.getPlayerPos().x, level.getPlayerPos().y, 0);
        stage.getCamera().update();
        stage.act();
        stage.draw();
        if (render_debug) {
            debugRenderer.render(world, stage.getCamera().combined);
        }
        doPhysicsStep(Gdx.graphics.getDeltaTime());
    }

    private float accumulator = 0;

    private void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
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
        stage.dispose();
    }
}
