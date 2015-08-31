package com.bawsephighter;

import org.andengine.entity.sprite.Sprite;
import org.andengine.extension.physics.box2d.PhysicsConnector;
import org.andengine.extension.physics.box2d.PhysicsFactory;
import org.andengine.extension.physics.box2d.PhysicsWorld;
import org.andengine.opengl.vbo.VertexBufferObjectManager;

import com.badlogic.gdx.physics.box2d.Body;
import com.badlogic.gdx.physics.box2d.BodyDef.BodyType;

public class Boss extends Sprite {
	private Body body;
	private int health;
	
	public Boss(float pX, float pY, VertexBufferObjectManager vbo, PhysicsWorld physicsWorld){
	    super(pX, pY, ResourcesManager.getInstance().boss_region, vbo);
	    health = 100;
	    createPhysics(physicsWorld);
	}
    
    private void createPhysics(PhysicsWorld physicsWorld){        
        body = PhysicsFactory.createBoxBody(physicsWorld, this, BodyType.DynamicBody, PhysicsFactory.createFixtureDef(0, 0, 0));
        body.setUserData("boss");
        body.setLinearDamping(5);
        body.setFixedRotation(true);
        
        physicsWorld.registerPhysicsConnector(new PhysicsConnector(this, body, true, false));
    }
    
    public void setX(float pX){
    	body.setLinearVelocity(pX, body.getLinearVelocity().y);
    }
    
    public void setY(float pY){
    	body.setLinearVelocity(body.getLinearVelocity().x, pY);
    }
    
    public int getLives(){
    	return health;
    }
    
    public void loseLife(){
    	health += -1;
    }
}