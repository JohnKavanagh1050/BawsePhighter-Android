package com.bawsephighter;

import org.andengine.engine.camera.Camera;
import org.andengine.entity.scene.menu.MenuScene;
import org.andengine.entity.scene.menu.MenuScene.IOnMenuItemClickListener;
import org.andengine.entity.scene.menu.item.IMenuItem;
import org.andengine.entity.scene.menu.item.SpriteMenuItem;
import org.andengine.entity.scene.menu.item.decorator.ScaleMenuItemDecorator;
import org.andengine.entity.sprite.Sprite;
import org.andengine.opengl.util.GLState;

import com.bawsephighter.SceneManager.SceneType;
import com.bawsephighter.base.BaseScene;

public class MainMenuScene extends BaseScene implements IOnMenuItemClickListener{

	@Override
	public void createScene() {
		createBackground();
		createMenuChildScene();
	}

	@Override
	public void onBackKeyPressed() {
		System.exit(0);
	}

	@Override
	public SceneType getSceneType() {
		return SceneType.SCENE_MENU;
	}
	
	private void createBackground()
	{
	    attachChild(new Sprite(0, 0, resourcesManager.menu_background_region, vbom)
	    {
	        @Override
	        protected void preDraw(GLState pGLState, Camera pCamera) 
	        {
	            super.preDraw(pGLState, pCamera);
	            pGLState.enableDither();
	        }
	    });
	}
	
	private MenuScene menuChildScene;
	private final int MENU_PLAY = 0;
	private final int MENU_INSTRUCTIONS = 1;

	private void createMenuChildScene(){
	    menuChildScene = new MenuScene(camera);
	    menuChildScene.setPosition(20, 120);
	    
	    final IMenuItem playMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_PLAY, resourcesManager.play_region, vbom), 1.2f, 1);
	    final IMenuItem optionsMenuItem = new ScaleMenuItemDecorator(new SpriteMenuItem(MENU_INSTRUCTIONS, resourcesManager.options_region, vbom), 1.2f, 1);
	    
	    menuChildScene.addMenuItem(playMenuItem);
	    menuChildScene.addMenuItem(optionsMenuItem);
	    
	    menuChildScene.buildAnimations();
	    menuChildScene.setBackgroundEnabled(false);
	    
	    playMenuItem.setPosition(playMenuItem.getX(), playMenuItem.getY() - 100);
	    optionsMenuItem.setPosition(optionsMenuItem.getX(), optionsMenuItem.getY() - 80);
	    
	    menuChildScene.setOnMenuItemClickListener(this);
	    
	    setChildScene(menuChildScene);
	}
	
	public boolean onMenuItemClicked(MenuScene pMenuScene, IMenuItem pMenuItem, float pMenuItemLocalX, float pMenuItemLocalY)
	{
	    switch(pMenuItem.getID())
	    {
	        case MENU_PLAY:
	            //Load Game Scene!
	            SceneManager.getInstance().loadGameScene(engine);
	            return true;
	        case MENU_INSTRUCTIONS:
	            return true;
	        default:
	            return false;
	    }
	}

	@Override
	public void disposeScene(){
		// TODO Auto-generated method stub
		
	}
	

}
