package com.agentoxygen.speed;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.PolygonShape;
import com.badlogic.gdx.physics.box2d.World;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Level {
    /*
    Generating a level is done procedurally
    1. Create floors
    2. Create borders
    3. Remove sections of floors to create ports
    4. Create walls based on location of ports
     */

    public static final float TILE_SIZE = 16;
    public static final int BARRIER = 1, LEVEL_WIDTH = 50, LEVEL_HEIGHT = 40;

    private World world;
    private int[][] levelMap = new int[LEVEL_WIDTH][LEVEL_HEIGHT];

    public Level(World world_){
        world = world_;
    }

    private void loadJson(String path){
        Json json = new Json();
    }

    private class Rectangle{
        public float x, y, w, h;

        public Rectangle(float x_, float y_, float w_, float h_){
            x = x_;
            y = y_;
            w = w_;
            h = h_;
        }
    }


    private void generateFloors(int numFloors, float width, float height, float gap){
        for (int floorIndex = 0; floorIndex < numFloors; floorIndex++){
            BodyDef groundBodyDef = new BodyDef();
            groundBodyDef.position.set(new Vector2(width/2, height*floorIndex + gap));

            Body body = world.createBody(groundBodyDef);
            PolygonShape box = new PolygonShape();
            box.setAsBox(height, width);
            body.createFixture(box, 0.0f);
            box.dispose();
        }
    }

    @Deprecated
    private void generateRandom(){
        int layers = 20;
        float layerRadius = 100f;
        int density = 3;

        Random rand = new Random();

        for (int layer = 0; layer < layers; layer++){
            rand.nextFloat();
            for (int shape = 0; shape < (layer + 1)*density; shape++){
                double angle = rand.nextDouble() * Math.PI * 2;
                float radialPos = rand.nextFloat() * layerRadius + layerRadius * layer;
                float x = (float) Math.cos(angle) * radialPos, y = (float) Math.sin(angle) * radialPos, height = rand.nextFloat() * layerRadius, width = rand.nextFloat() * layerRadius;
                height = 20;
                width = 20;

                BodyDef groundBodyDef = new BodyDef();
                groundBodyDef.position.set(new Vector2(x, y));

                Body body = world.createBody(groundBodyDef);
                body.setTransform(body.getPosition(), rand.nextFloat() * (float) Math.PI * 2);
                PolygonShape box = new PolygonShape();
                box.setAsBox(height, width);
                body.createFixture(box, 0.0f);
                box.dispose();
            }
        }

        BodyDef groundBodyDef = new BodyDef();
        groundBodyDef.position.set(new Vector2(200, 200));

        Body groundBody = world.createBody(groundBodyDef);
        groundBody.setTransform(groundBody.getPosition(), 2);

        PolygonShape groundBox = new PolygonShape();
        groundBox.setAsBox(10f, 10.0f);
        groundBody.createFixture(groundBox, 0.0f);
        groundBox.dispose();
    }
}
