package com.bawsephighter;

import java.util.ArrayList;

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
	private Text score;
	private int bossHealthNumber = 100;
	private int playerHealthNumber = 100;
	private int scoreNumber = 0;
	private PhysicsWorld physicsWorld;
	
	private ITextureRegion mPlayerTextureRegion;
	private ITextureRegion mBoundryTextureRegion;
	
	private Player player;
	private Boss boss;
	
	private ArrayList<Projectile> projectiles = new ArrayList<Projectile>();

	private void createHUD(){
	    gameHUD = new HUD();
	    
	    // CREATE boss health
	    bossHealth = new Text(0, 0, resourcesManager.font, "Health: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    bossHealth.setSkewCenter(0, 0);
	    bossHealth.setScale(0.5f);
	    bossHealth.setText("Bawse Health: 100");
	    bossHealth.setColor(255,0,255);
	    gameHUD.attachChild(bossHealth);
	    
	    // CREATE player health
	    playerHealth = new Text(0, 400, resourcesManager.font, "Health: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    playerHealth.setSkewCenter(0, 0);
	    playerHealth.setScale(0.5f);
	    playerHealth.setText("Player Health: 100");
	    playerHealth.setColor(0,0,0);
	    gameHUD.attachChild(playerHealth);
	    
	    // CREATE score
	    score = new Text(500, 400, resourcesManager.font, "Score: 0123456789", new TextOptions(HorizontalAlign.LEFT), vbom);
	    score.setSkewCenter(0, 0);
	    score.setScale(0.5f);
	    score.setText("Score: 0");
	    score.setColor(0,0,0);
	    gameHUD.attachChild(score);
	    
	    camera.setHUD(gameHUD);
	}
	
	private void AddScore(int i){
	    scoreNumber -= i;
	    score.setText("Score: " + scoreNumber );
	}

	private void createPhysics(){
	    physicsWorld = new FixedStepPhysicsWorld(60, new Vector2(0, 0), false){
			@Override
			public void onUpdate(float pSecondsElapsed){
				super.onUpdate(pSecondsElapsed);
				shoot();
				BossAI();
				
				ArrayList projectiles = getProjectiles();
				for (int i = 0; i < projectiles.size(); i++) {
					Projectile p = (Projectile) projectiles.get(i);
					if (p.isVisible() == true) {
						p.update();
					} else {
						projectiles.remove(i);
					}
				}
			}
		}; 
	    registerUpdateHandler(physicsWorld);
	}
	
	public void shoot() {
		Projectile p = new Projectile(player.getX() + 30, player.getY());
		projectiles.add(p);
	}
	
	public ArrayList getProjectiles() {
		return projectiles;
	}
    
    private void createBackground(){
        setBackground(new Background(255, 255, 255));
    }
    
    @Override
    public void createScene(){
    	createBackground();
        createHUD();
        createPhysics();  
        createObjects();
        createBoundry();
        this.engine.registerUpdateHandler(this);
        setOnSceneTouchListener(this);
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
    public void createObjects(){
    	player = new Player(300, 400, vbom, physicsWorld);
		attachChild(player);
		
		boss = new Boss(300, 0, vbom, physicsWorld);
		attachChild(boss);
    }

    @Override
    public void onBackKeyPressed(){
    	SceneManager.getInstance().loadMenuScene(engine);
    }

    @Override
    public SceneType getSceneType(){
        return SceneType.SCENE_GAME;
    }
    
    public void BossAI(){
  
    }
    
    @Override
	public boolean onSceneTouchEvent(Scene pScene, TouchEvent pSceneTouchEvent) {
    	float touchFromRight = pSceneTouchEvent.getX() - (player.getX() + player.getWidth());
		float touchFromLeft = pSceneTouchEvent.getX() - player.getX();
		float touchFromBottom = pSceneTouchEvent.getY() - (player.getY() + player.getHeight());
		float touchFromTop = pSceneTouchEvent.getY() - player.getY();
		
		//Touch to the right of the player
		if ((touchFromRight > 0) && (touchFromRight < 800)){
			player.setX(touchFromRight/10);
		}
		
		//Touch to the left of the player
		else if ((touchFromLeft < 0) && (touchFromLeft > -800)){
			player.setX(touchFromLeft/10);
		}
		
		//Touch to the top of the player
		if ((touchFromTop > 0) && (touchFromTop < 480)){
			player.setY(touchFromTop/10);
		}
		
		//Touch to the bottom of the player
		else if ((touchFromBottom < 0) && (touchFromBottom > -480)){
			player.setY(touchFromBottom/10);
		}

		return false;
	}

    @Override
    public void disposeScene(){
    	camera.setHUD(null);
        camera.setCenter(400, 240);
    }
}