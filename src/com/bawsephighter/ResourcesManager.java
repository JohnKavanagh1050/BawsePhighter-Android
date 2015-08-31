package com.bawsephighter;

import org.andengine.engine.Engine;
import org.andengine.engine.camera.Camera;
import org.andengine.opengl.font.Font;
import org.andengine.opengl.font.FontFactory;
import org.andengine.opengl.texture.ITexture;
import org.andengine.opengl.texture.TextureOptions;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.BitmapTextureAtlasTextureRegionFactory;
import org.andengine.opengl.texture.atlas.bitmap.BuildableBitmapTextureAtlas;
import org.andengine.opengl.texture.atlas.bitmap.source.IBitmapTextureAtlasSource;
import org.andengine.opengl.texture.atlas.buildable.builder.BlackPawnTextureAtlasBuilder;
import org.andengine.opengl.texture.atlas.buildable.builder.ITextureAtlasBuilder.TextureAtlasBuilderException;
import org.andengine.opengl.texture.region.ITextureRegion;
import org.andengine.opengl.vbo.VertexBufferObjectManager;
import org.andengine.util.debug.Debug;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;

public class ResourcesManager{
    private static final ResourcesManager INSTANCE = new ResourcesManager();
    
    public Engine engine;
    public GameActivity activity;
    public Camera camera;
    public VertexBufferObjectManager vbom;
    
    //Splash
	private BitmapTextureAtlas splashTextureAtlas;
    public ITextureRegion splash_region;
	
    //Menu
	private BuildableBitmapTextureAtlas menuTextureAtlas;
	public ITextureRegion menu_background_region;
	public ITextureRegion play_region;
	public ITextureRegion options_region;
	public ITextureRegion player_region;
	public ITextureRegion boss_region;
	public Font font;
	
	//Game
	public BuildableBitmapTextureAtlas gameTextureAtlas;
	
	public static SharedPreferences mSharedPref;
    
    public void loadMenuResources(){
        loadMenuGraphics();
        loadMenuAudio();
        loadMenuFonts();
    }
    
    public void unloadMenuTextures(){
        menuTextureAtlas.unload();
    }
        
    public void loadMenuTextures(){
        menuTextureAtlas.load();
    }
    
    public void unloadGameTextures(){
    }
    
    public void loadGameResources(){
    	loadGameTextures();
        loadGameFonts();
        loadGameAudio();
    }
    
    private void loadMenuAudio(){
        
    }
    
    private void loadGameTextures(){
    	gameTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 2048, 2048, TextureOptions.BILINEAR);
    	player_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "player.png");
    	boss_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(gameTextureAtlas, activity, "firstboss.png");
    	try 
        {
            gameTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
            gameTextureAtlas.load();
        } 
        catch (final TextureAtlasBuilderException e)
        {
            Debug.e(e);
        }
    }
    
    private void loadGameFonts(){
        
    }
    
    private void loadGameAudio(){
        
    }
    
    public void loadSplashScreen(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
    	splashTextureAtlas = new BitmapTextureAtlas(activity.getTextureManager(), 400, 400, TextureOptions.BILINEAR);
    	splash_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(splashTextureAtlas, activity, "splash.png", 0, 0);
    	splashTextureAtlas.load();
    }
    
    public void unloadSplashScreen(){
    	splashTextureAtlas.unload();
    	splash_region = null;
    }
    
    private void loadMenuGraphics(){
    	BitmapTextureAtlasTextureRegionFactory.setAssetBasePath("");
    	menuTextureAtlas = new BuildableBitmapTextureAtlas(activity.getTextureManager(), 1000, 1000, TextureOptions.BILINEAR);
    	menu_background_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "menu background.png");
    	play_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "play.png");
    	options_region = BitmapTextureAtlasTextureRegionFactory.createFromAsset(menuTextureAtlas, activity, "instructions.png");
    	       
    	try {
    	    this.menuTextureAtlas.build(new BlackPawnTextureAtlasBuilder<IBitmapTextureAtlasSource, BitmapTextureAtlas>(0, 1, 0));
    	    this.menuTextureAtlas.load();
    	} 
    	catch (final TextureAtlasBuilderException e){
    	        Debug.e(e);
    	}
    }
    
    private void loadMenuFonts(){
        FontFactory.setAssetBasePath("");
        final ITexture mainFontTexture = new BitmapTextureAtlas(activity.getTextureManager(), 256, 256, TextureOptions.BILINEAR_PREMULTIPLYALPHA);

        font = FontFactory.createStrokeFromAsset(activity.getFontManager(), mainFontTexture, activity.getAssets(), "quicksand.otf", 50, true, Color.WHITE, 2, Color.BLACK);
        font.load();
    }
    
    public static void prepareManager(Engine engine, GameActivity activity, Camera camera, VertexBufferObjectManager vbom){
        getInstance().engine = engine;
        getInstance().activity = activity;
        getInstance().camera = camera;
        getInstance().vbom = vbom;
        mSharedPref = activity.getPreferences(Context.MODE_PRIVATE);
    }
    
    
    public void saveHighScore(int highScore) {
		 mSharedPref = activity.getPreferences(Context.MODE_PRIVATE);
		 SharedPreferences.Editor editor = mSharedPref.edit();		
		//editor.putInt(activity.getString(R.string.saved_high_score), highScore);
		 editor.commit();
	 }
    
    public int getHighScore(){
		 int defaultValue = 0;
		// int highScore = ResourcesManager.mSharedPref.getInt(activity.getString(R.string.saved_high_score), defaultValue);
		 return 1;
	 }
    
    public static ResourcesManager getInstance(){
        return INSTANCE;
    }
}