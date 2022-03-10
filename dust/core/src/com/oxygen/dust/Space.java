package com.oxygen.dust;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.physics.box2d.*;
import com.badlogic.gdx.utils.viewport.Viewport;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Random;


public class Space implements ContactListener {
    private World world;
    private Viewport viewport;
    private Player player;
    private ArrayList<SpaceObject> spaceObjects = new ArrayList<SpaceObject>();
    private Random random;

    public static final int OBJECT_CLEAN_TICK_RATE = 5;

    public Space(Viewport viewport) {
        world = new World(new Vector2(0, 0), true);
        world.setContactListener(this);
        this.viewport = viewport;
        player = new Player(world, 1f, -0.5f);

        random = new Random();
        random.setSeed(System.currentTimeMillis());
        spaceObjects.add(new Asteroid(world, 0.5f, 0.5f, random));
        spaceObjects.add(new Asteroid(world, -0.5f, -0.5f, random));
        spaceObjects.add(new Asteroid(world, 0.5f, -0.5f, random));
        spaceObjects.add(new SpaceAnchor(world, 0, 0));
    }

    public World getWorld(){
        return world;
    }

    public ArrayList<SpaceObject> getSpaceObjects(){
        return spaceObjects;
    }

    private void checkAndDestroySpaceObjs(){
        /**
         * Called every so often according to OBJECT_CLEAN_TICK_RATE
         */
        ArrayList<SpaceObject> destroyedObjs = new ArrayList<SpaceObject>();
        ArrayList<SpaceObject> newObjs = new ArrayList<SpaceObject>();
        for (SpaceObject sobj : spaceObjects){
            if (sobj.destroy == 0){
                // Check if asteroid
                if (Asteroid.class.isAssignableFrom(sobj.getClass())){
                    newObjs.addAll(Arrays.asList(((Asteroid) sobj).fracture(world, random)));
                }else if (Missile.class.isAssignableFrom(sobj.getClass())){
                    newObjs.addAll(Arrays.asList(((Missile) sobj).explode(world, random)));
                }
                destroyedObjs.add(sobj);
                world.destroyBody(sobj.getBody());
            }else if(sobj.destroy > 0){
                sobj.destroy -= 1;
            }
        }
        spaceObjects.removeAll(destroyedObjs);
        spaceObjects.addAll(newObjs);
    }

    public void anglePlayer(float mouseX, float mouseY){
        Vector3 screenPos = viewport.getCamera().project(new Vector3(player.getBody().getPosition(), 0));

        float angle = (float)(Math.atan2(mouseY - screenPos.y, mouseX - screenPos.x));
        if (angle > 0){
            angle = -1 * (float) (2*Math.PI - angle);
        }
        angle += (float) Math.PI;
        player.setAngle(angle);
    }

    private float accumulator = 0;
    private final float TIME_STEP = 1/60f;
    private int tick_accumulator = 0;
    public void doPhysicsStep(float deltaTime) {
        float frameTime = Math.min(deltaTime, 0.25f);
        accumulator += frameTime;
        while (accumulator >= TIME_STEP) {
            world.step(TIME_STEP, 6, 2);
            accumulator -= TIME_STEP;
        }
        if (tick_accumulator > OBJECT_CLEAN_TICK_RATE){
            checkAndDestroySpaceObjs();
            tick_accumulator = 0;
        }else{
            tick_accumulator += 1;
        }
    }

    public void dispose(){
        world.dispose();
    }

    public Vector2 getPlayerPos() {
        return player.getBody().getPosition();
    }

    public Player getPlayer(){
        return player;
    }

    public void thrustPlayer() {
        float angle = player.getBody().getAngle();
        Vector2 direction = new Vector2(-1*(float)Math.cos(angle), -1*(float)Math.sin(angle));
        player.thrust(direction.nor());
    }

    public void shootPlayerMissile(){
        float radius = Math.max(Player.bodyHeight, Player.bodyWidth) + (Missile.bodyHeight + Missile.bodyWidth) / 2;
        float angle = player.getBody().getAngle();
        float x = getPlayerPos().x + -1*(float)Math.cos(angle)*radius;
        float y = getPlayerPos().y + -1*(float)Math.sin(angle)*radius;
        float speed = 3f;
        Vector2 trajectory = new Vector2(-1*(float)Math.cos(angle)*speed, -1*(float)Math.sin(angle)*speed);
        spaceObjects.add(new Missile(world, x, y, trajectory, angle));
    }

    @Override
    public void beginContact(Contact contact) {
        if (contact.getFixtureA().getBody().getUserData() != null && contact.getFixtureB().getBody().getUserData() != null){
            Object obj1 = contact.getFixtureA().getBody().getUserData();
            Object obj2 = contact.getFixtureB().getBody().getUserData();
            if(SpaceObject.class.isAssignableFrom(obj1.getClass()) && SpaceObject.class.isAssignableFrom(obj2.getClass())){
                ((SpaceObject) obj1).onCollision(contact.getFixtureA(), contact.getFixtureB());
                ((SpaceObject) obj2).onCollision(contact.getFixtureB(), contact.getFixtureA());
            }
        }
    }

    @Override
    public void endContact(Contact contact) {
    }

    @Override
    public void preSolve(Contact contact, Manifold oldManifold) {

    }

    @Override
    public void postSolve(Contact contact, ContactImpulse impulse) {

    }
}
