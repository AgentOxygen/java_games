package com.oxygen.bunker;

import com.badlogic.gdx.Game;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Raid implements Screen {
    // Contains all data pertaining to the player in raid
    private Game game;
    private World world;
    private long worldSeed;
    private Stage stage;
    private Viewport viewport;
    private OrthographicCamera camera;
    private Box2DDebugRenderer debugRenderer;

    private Player player;

    private boolean renderDebugInfo = true;
    private static final int VIEWPORT_WIDTH = 18;
    private static final int VIEWPORT_HEIGHT = 9;
    private static final float TIME_STEP = 1/60f;


    public Raid(Game game){
        this.game = game;
        LevelBuilder builder = new LevelBuilder();
        world = builder.getWorld();
        worldSeed = builder.getSeed();
        player = new Player(world, 0, 10);

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        camera.position.set(0, 0, 0);

        stage = new Stage(viewport);
        stage.getViewport().apply();

        debugRenderer = new Box2DDebugRenderer();
    }

    @Override
    public void show() {

    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);
        camera.position.set(player.getPos().x, player.getPos().y, 0);
        stage.getCamera().update();
        stage.act();
        stage.draw();
        if (renderDebugInfo) {
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
        world.dispose();
    }
}
