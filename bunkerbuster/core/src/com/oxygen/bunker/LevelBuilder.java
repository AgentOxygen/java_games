package com.oxygen.bunker;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.World;

import java.util.Random;

public class LevelBuilder {
    private World world;
    private long seed;

    private Random random;
    private boolean debugLevel;

    public static final Vector2 GRAVITY_ACCEL = new Vector2(0, -10);

    public LevelBuilder(long levelSeed){
        world = new World(GRAVITY_ACCEL, true);
        random = new Random(levelSeed);
        debugLevel = false;
        buildRandom();
    }

    public LevelBuilder(){
        world = new World(GRAVITY_ACCEL, true);
        seed = 101;
        random = new Random(seed);
        debugLevel = true;
        buildDebug();
    }

    private void buildDebug(){
        // Builds the debugging level
        Body floor = LevelParts.RECT(world, 0, 0, 100, 2);
//        world.createBody(floor.getBodyDef()).createFixture(floor.getFixDef());
//        floor.getFixDef().shape.dispose();
    }

    private void buildRandom(){
        // Builds level randomly with procedural generation
    }

    public long getSeed(){
        return seed;
    }

    public World getWorld(){
        return world;
    }
}
