package com.bawsephighter;

import org.andengine.engine.camera.hud.HUD;
import org.andengine.entity.scene.IOnSceneTouchListener;
import org.andengine.entity.scene.Scene;
import org.andengine.entity.scene.background.Background;
import org.andengine.entity.sprite.Sprite;
import org.andengine.entity.text.Text;
import org.andengine.entity.text.TextOptions;
import org.andengine.extension.physics.box2d.FixedStepPhysicsWorld;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.input.touch.TouchEvent;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.util.HorizontalAlign;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;
import com.badlogic.gdx.physics.box2d.FixtureDef;
import com.bawsephighter.SceneManager.SceneType;
import com.bawsephighter.base.BaseScene;

import android.graphics.Color;

public class GameScene<SimpleLevelEntityLoaderData> extends BaseScene implements IOnSceneTouchListener{
	
	private HUD gameHUD;
	
	private Text bossHealth;
	private Text playerHealth;
	private int bossHealthNumber = 100;
	private int playerHealthNumber = 100;
	private PhysicsWorld physicsWorld;
	
	private BitmapTextureAtlas mTexturePlayer;
	
	private ITextureRegion mPlayerTextureRegion;
	private ITextureRegion mBoundryTextureRegion;
	
	private Sprite player;
	private Sprite boss1;
	private Sprite boss2;

	private void createHUD(){
	    gameHUD = new HUD();
	    
	    // CREATE boss health
	    bossHealth = new Text(0, 0, resourcesManager.font, "Health: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    bossHealth.setSkewCenter(0, 0);
	    bossHealth.setScale(0.5f);
	    bossHealth.setText("Bawse Health: 100");
	    bossHealth.setColor(0,0,0);
	    gameHUD.attachChild(bossHealth);
	    
	    // CREATE player health
	    playerHealth = new Text(0, 400, resourcesManager.font, "Health: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    playerHealth.setSkewCenter(0, 0);
	    playerHealth.setScale(0.5f);
	    playerHealth.setText("Player Health: 100");
	    playerHealth.setColor(0,0,0);
	    gameHUD.attachChild(playerHealth);
	    
	    camera.setHUD(gameHUD);
	}

	private void BawseHit(int i){
	    bossHealthNumber -= i;
	    bossHealth.setText("Health: " + bossHealthNumber );
	}
	
	private void PlayerHit(int i){
	    playerHealthNumber -= i;
	    playerHealth.setText("Health: " + playerHealthNumber );
	}

	private void createPhysics(){
	    physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, -17), false); 
	    registerUpdateHandler(physicsWorld);
	}
    
    private void createBackground(){
        setBackground(new Background(255, 255, 255));
    }
    
    @Override
    public void createScene(){
    	createBackground();
        createHUD();
        createPhysics();  
        createPlayer();
        createBoundry();
        //onSceneTouchEvent(this, null);
    }
    
	private void createBoundry(){
		final FixtureDef fixDef = PhysicsFactory.createFixtureDef(0f,0f, 1.0f);
		
		BitmapTextureAtlas mTextureBoundryFloor = new BitmapTextureAtlas(engine.getTextureManager(), 800, 10);
		mBoundryTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTextureBoundryFloor, this.activity, "floor.png", 0, 0);
		mTextureBoundryFloor.load();
		
		Sprite floor = new Sprite(0,470, mBoundryTextureRegion, engine.getVertexBufferObjectManager());
		Body bodyFloor = PhysicsFactory.createBoxBody(physicsWorld, floor, BodyType.StaticBody, fixDef);
		bodyFloor.setUserData("floor");
		floor.setUserData(bodyFloor);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(floor, bodyFloor, true, true));
		attachChild(floor);
		
		Sprite roof = new Sprite(0,0, mBoundryTextureRegion, engine.getVertexBufferObjectManager());
		Body bodyRoof = PhysicsFactory.createBoxBody(physicsWorld, roof, BodyType.StaticBody, fixDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(roof, bodyRoof, true, true));
		attachChild(roof);
		
		BitmapTextureAtlas mTextureBoundryWall = new BitmapTextureAtlas(engine.getTextureManager(), 10, 460);
		mBoundryTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTextureBoundryWall, this.activity, "wall.png", 0, 0);
		mTextureBoundryWall.load();
		
		Sprite wallLeft = new Sprite(0,10, mBoundryTextureRegion, engine.getVertexBufferObjectManager());
		Body bodyWallLeft = PhysicsFactory.createBoxBody(physicsWorld, wallLeft, BodyType.StaticBody, fixDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(wallLeft, bodyWallLeft, true, true));
		attachChild(wallLeft);
		
		Sprite wallRight = new Sprite(790,10, mBoundryTextureRegion, engine.getVertexBufferObjectManager());
		Body bodyWallRight = PhysicsFactory.createBoxBody(physicsWorld, wallRight, BodyType.StaticBody, fixDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(wallRight, bodyWallRight, true, true));
		attachChild(wallRight);
	}
	
    @Override
    public void createPlayer(){
    	final FixtureDef fixDef = PhysicsFactory.createFixtureDef(0f,0f, 1.0f);
		
		BitmapTextureAtlas mTexturePlayer = new BitmapTextureAtlas(engine.getTextureManager(), 90, 110);
		mPlayerTextureRegion = BitmapTextureAtlasTextureRegionFactory.createFromAsset(mTexturePlayer, this.activity, "player.png", 0, 0);
		mTexturePlayer.load();
		
    	player = new Sprite(300,350, mPlayerTextureRegion, engine.getVertexBufferObjectManager());
		Body bodyPlayer = PhysicsFactory.createBoxBody(physicsWorld, player, BodyType.StaticBody, fixDef);
		physicsWorld.registerPhysicsConnector(new PhysicsConnector(player, bodyPlayer, true, true));
		player.setScale(0.5f);
		attachChild(player);
    }

    @Override
    public void onBackKeyPressed(){
    	SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType(){
        return SceneType.SCENE_GAME;
    }
    
    @Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
		float touchFromRight = pSceneTouchEvent.getX() - (player.getX() + player.getWidth());
		float touchFromLeft = pSceneTouchEvent.getX() - player.getX();
		float touchFromBottom = pSceneTouchEvent.getY() - (player.getY() + player.getHeight());
		float touchFromTop = pSceneTouchEvent.getY() - player.getY();
		Body bodyPlayer = (Body) player.getUserData();
		//Touch to the right of the player
		if ((touchFromRight > 0) && (touchFromRight < 800)){
			if (touchFromRight > 30){
				bodyPlayer.setLinearVelocity(5, bodyPlayer.getLinearVelocity().y);
			}
			else{
				bodyPlayer.setLinearVelocity(1, bodyPlayer.getLinearVelocity().y);
			}
		}
		//Touch to the left of the player
		else if ((touchFromLeft < 0) && (touchFromLeft > -800)){
			
			if (touchFromLeft < -30){
				bodyPlayer.setLinearVelocity(-5, bodyPlayer.getLinearVelocity().y);
			}
			else{
				bodyPlayer.setLinearVelocity(-1, bodyPlayer.getLinearVelocity().y);
			}
		}
		return false;
	}

    @Override
    public void disposeScene(){
    	camera.setHUD(null);
        camera.setCenter(400, 240);
    }
}