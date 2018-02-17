package com.niceprogrammer.flappeebee.gameobjects;

import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;

public class Bee {

	private static final float COLLISION_RADIUS = 18f;
	private static final float DIVE_ACCEL = 0.3f;
	private static final float FLY_ACCEL = 5f;
	private static final int TILE_WIDTH = 118, TILE_HEIGHT = 118;
	private static final float FRAME_DURATION = 0.25f;

	private final Circle collisionCircle;
	private final Animation<TextureRegion> animation;
	private float animationTimer = 0;
	private float x = 0, y = 0;
	private float ySpeed = 0;

	public Bee(TextureRegion beeTexture) {
		collisionCircle = new Circle(x, y, COLLISION_RADIUS);
		TextureRegion[][] beeTextures = beeTexture.split(TILE_WIDTH, TILE_HEIGHT);

		animation = new Animation<TextureRegion>(FRAME_DURATION, beeTextures[0][0], beeTextures[0][1]);
		animation.setPlayMode(Animation.PlayMode.LOOP);
	}

	public void draw(SpriteBatch batch) {
		TextureRegion region = animation.getKeyFrame(animationTimer);
		float regX = collisionCircle.x - region.getRegionWidth() / 2;
		float regY = collisionCircle.y - region.getRegionHeight() / 2;
		batch.draw(region, regX, regY);
	}

	public void drawDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.circle(collisionCircle.x, collisionCircle.y, COLLISION_RADIUS);
	}

	public void setPosition(float x, float y) {
		this.x = x;
		this.y = y;
		updateCollisionCircle();
	}

	private void updateCollisionCircle() {
		collisionCircle.x = x;
		collisionCircle.y = y;
	}

	public void update(float delta) {
		animationTimer += delta;
		ySpeed -= DIVE_ACCEL;
		setPosition(x, y + ySpeed);
	}

	public void flyUp() {
		ySpeed = FLY_ACCEL;
		setPosition(x, y + ySpeed);
	}

	public Circle getCollisionCircle() {
		return collisionCircle;
	}

	public float getX() {
		return x;
	}

	public float getY() {
		return y;
	}
}