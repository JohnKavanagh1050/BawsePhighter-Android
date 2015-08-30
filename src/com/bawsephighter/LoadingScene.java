package com.bawsephighter;

import org.andengine.entity.scene.background.Background;
import org.andengine.entity.text.Text;

import com.bawsephighter.SceneManager.SceneType;
import com.bawsephighter.base.BaseScene;

import android.R.color;
import android.graphics.Color;

public class LoadingScene extends BaseScene{
    @Override
    public void createScene(){
    	setBackground(new Background(255,255,255));
        attachChild(new Text(200, 120, resourcesManager.font, "Loading...", vbom));
    }

    @Override
    public void onBackKeyPressed(){
        return;
    }

    @Override
    public SceneType getSceneType(){
        return SceneType.SCENE_LOADING;
    }

    @Override
    public void disposeScene(){
    	
    }
}

