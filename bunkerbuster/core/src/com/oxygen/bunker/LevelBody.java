package com.oxygen.bunker;

import com.badlogic.gdx.physics.box2d.BodyDef;
import com.badlogic.gdx.physics.box2d.FixtureDef;

public class LevelBody {
    private BodyDef bodyDef;
    private FixtureDef fixDef;
    private Object data;

    private boolean hasData = false;

    public LevelBody(BodyDef setBodyDef, FixtureDef setFixDef){
        bodyDef = setBodyDef;
        fixDef = setFixDef;
    }

    public LevelBody(BodyDef setBodyDef, FixtureDef setFixDef, Object setData){
        bodyDef = setBodyDef;
        fixDef = setFixDef;
        data = setData;
        hasData = true;
    }

    public boolean hasData(){
        return hasData;
    }

    public Object getData(){
        return data;
    }

    public BodyDef getBodyDef(){
        return bodyDef;
    }

    public FixtureDef getFixDef(){
        return fixDef;
    }
}