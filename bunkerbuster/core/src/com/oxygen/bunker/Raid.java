package com.oxygen.bunker;

import com.badlogic.gdx.*;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Sprite;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.Box2DDebugRenderer;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.utils.Array;
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
    private SpriteBatch spriteBatch;

    private Player player;

    private boolean renderDebugInfo = true;
    private static final float VIEWPORT_WIDTH_FACTOR = 20;
    private static final float VIEWPORT_HEIGHT_FACTOR = 20;
    private static final float VIEWPORT_WIDTH = BunkerBuster.WINDOW_WIDTH / 20;
    private static final float VIEWPORT_HEIGHT = BunkerBuster.WINDOW_HEIGHT / 20;
    private static final float TIME_STEP = 1/60f;


    public Raid(Game game){
        this.game = game;
        LevelBuilder builder = new LevelBuilder();
        world = builder.getWorld();
        worldSeed = builder.getSeed();
        player = new Player(world, 0, 10);
        spriteBatch = new SpriteBatch();

        camera = new OrthographicCamera(VIEWPORT_WIDTH, VIEWPORT_HEIGHT);
        viewport = new StretchViewport(VIEWPORT_WIDTH, VIEWPORT_HEIGHT, camera);
        camera.position.set(0, 0, 0);

        stage = new Stage(viewport);
        stage.getViewport().apply();

        setInputProcessor();

        debugRenderer = new Box2DDebugRenderer();
    }

    public void setInputProcessor(){
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
            public boolean keyDown(int keycode) {
                switch (keycode)
                {
                    case Input.Keys.D:
                        player.movePlayer(Player.RIGHT);
                        break;
                    case Input.Keys.A:
                        player.movePlayer(Player.LEFT);
                        break;
                    case Input.Keys.SPACE:
                        player.jumpPlayer();
                        break;
                    default:
                        break;
                }
                return true;
            }
            @Override
            public boolean keyUp(int keycode) {
                if (keycode == Input.Keys.A || keycode == Input.Keys.D){
                    player.movePlayer(Player.IDLE);
                }
                return true;
            }
        });
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
        Array<Body> bodies = new Array<Body>();
        world.getBodies(bodies);

        spriteBatch.begin();
        for (Body body : bodies){
            if (body.getUserData() instanceof String){
                // Need to figure out what the mapping form world position to screen position is
                // Also accounting for the change from float to int
                //Texture texture = LevelParts.getPartTexture((String) body.getUserData());
                //Vector2 textureSize = LevelParts.getPartTextureSize((String) body.getUserData());
                //Sprite sprite = new Sprite(texture, 0, 0, mapWorldXToScreenX(textureSize.x), mapWorldYToScreenY(textureSize.y));
                //sprite.setPosition(mapWorldXToScreenX(body.getPosition().x), mapWorldYToScreenY(body.getPosition().y));
                //sprite.draw(spriteBatch);
            }
        }
        spriteBatch.end();

        doPhysicsStep(Gdx.graphics.getDeltaTime());

    }

    public int mapWorldXToScreenX(float worldX){
        int ret = Math.round(((worldX - camera.position.x) + VIEWPORT_WIDTH/2) * VIEWPORT_WIDTH_FACTOR);
        BunkerBuster.print(ret);
        return ret;
    }

    public int mapWorldYToScreenY(float worldY){
        int ret = Math.round(((worldY - camera.position.y) + VIEWPORT_HEIGHT/2) * VIEWPORT_HEIGHT_FACTOR);
        BunkerBuster.print(ret);
        return ret;
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
