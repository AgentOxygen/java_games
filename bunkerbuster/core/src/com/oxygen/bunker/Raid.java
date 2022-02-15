package com.oxygen.bunker;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.viewport.StretchViewport;
import com.badlogic.gdx.utils.viewport.Viewport;

public class Raid implements Screen {
    public static final float TIME_STEP = 1/60f;
    public static final int VIEWPORT_WIDTH = 800;
    public static final int VIEWPORT_HEIGHT = 800;

    private OrthographicCamera camera;
    private Viewport viewport;
    private World world;
    private Box2DDebugRenderer debugRenderer;
    private Player player;

    @Override
    public void show() {
        camera = new OrthographicCamera();
        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        world = new World(new Vector2(0, -10), true);
        debugRenderer = new Box2DDebugRenderer();
        player = new Player(world, new Vector2(0,0));
    }

    @Override
    public void render(float delta) {
        Gdx.gl.glClearColor(0, 0, 0, 1);
        Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

        camera.position.set(player.body.getPosition().x, player.body.getPosition().y, 0);

        debugRenderer.render(world, camera.combined);
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
    public void resize(int width, int height) {}

    @Override
    public void pause() {}

    @Override
    public void resume() {}

    @Override
    public void hide() {}

    @Override
    public void dispose() {
        world.dispose();
        player.dispose();
    }
}
