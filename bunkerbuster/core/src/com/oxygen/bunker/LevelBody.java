package com.oxygen.bunker;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

@Deprecated
public class LevelBody {
    private BodyDef bodyDef;
    private FixtureDef fixDef;
    private String data;

    private boolean hasDataString = false;

    public LevelBody(BodyDef setBodyDef, FixtureDef setFixDef){
        bodyDef = setBodyDef;
        fixDef = setFixDef;
    }

    public LevelBody(BodyDef setBodyDef, FixtureDef setFixDef, String setData){
        bodyDef = setBodyDef;
        fixDef = setFixDef;
        data = setData;
        hasDataString = true;
    }

    public boolean hasDataString(){
        return hasDataString;
    }

    public String getDataString(){
        return data;
    }

    public BodyDef getBodyDef(){
        return bodyDef;
    }

    public FixtureDef getFixDef(){
        return fixDef;
    }
}