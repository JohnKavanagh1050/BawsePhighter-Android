package com.bawsephighter;

public class Projectile {
	private float x, y, speedX, speedY;
	private boolean visible;

	public Projectile(float startX, float startY) {
		x = startX;
		y = startY;
		speedY = -7;
		visible = true;
	}
	
	public void update(){
		y += speedY;
		if (y < 0) {
		   visible = false;
		}
	}
	
	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}

	public float getSpeedX() {
		return speedX;
	}

	public boolean isVisible() {
		return visible;
	}

	public void setX(int x) {
		this.x = x;
	}

	public void setY(int y) {
		this.y = y;
	}

	public void setSpeedX(int speedX) {
		this.speedX = speedX;
	}

	public void setVisible(boolean visible) {
		this.visible = visible;
	}
}