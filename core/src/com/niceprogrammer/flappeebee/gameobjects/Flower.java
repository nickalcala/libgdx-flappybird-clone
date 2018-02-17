package com.niceprogrammer.flappeebee.gameobjects;

import com.badlogic.gdx.graphics.g2d.SpriteBatch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Circle;
import com.badlogic.gdx.math.Intersector;
import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Rectangle;

public class Flower {

	private static final float COLLISSION_RECT_WIDTH = 13f;
	private static final float COLLISSION_RECT_HEIGHT = 447f;
	private static final float COLLISSION_CIRCLE_RADIUS = 33f;
	private static final float MAX_SPEED_PER_SECOND = 100f;
	public static final float WIDTH = COLLISSION_CIRCLE_RADIUS * 2;
	private static final float HEIGHT_OFFSET = -400f;
	private static final float DISTANCE_BETWEEN_FLOOR_AND_CEILING = 225f;

	private final Circle floorCollisionCircle;
	private final Rectangle floorCollisionRect;
	private final Circle ceilingCollisionCircle;
	private final Rectangle ceilingCollisionRect;
	private float x = 0;
	private float y = 0;
	private boolean pointClaimed = false;
	private final TextureRegion floorTexture, ceilingTexture;

	public Flower(TextureRegion floorTexture, TextureRegion ceilingTexture) {

		this.floorTexture = floorTexture;
		this.ceilingTexture = ceilingTexture;

		y = MathUtils.random(HEIGHT_OFFSET);
		floorCollisionRect = new Rectangle(x, y, COLLISSION_RECT_WIDTH, COLLISSION_RECT_HEIGHT);
		floorCollisionCircle = new Circle(x + floorCollisionRect.width / 2, y + floorCollisionRect.height,
				COLLISSION_CIRCLE_RADIUS);
		ceilingCollisionRect = new Rectangle(x, floorCollisionCircle.y + DISTANCE_BETWEEN_FLOOR_AND_CEILING,
				COLLISSION_RECT_WIDTH, COLLISSION_RECT_HEIGHT);
		ceilingCollisionCircle = new Circle(x + floorCollisionRect.width / 2, ceilingCollisionRect.y,
				COLLISSION_CIRCLE_RADIUS);
	}

	public void update(float delta) {
		setPosition(x - (MAX_SPEED_PER_SECOND * delta));
	}

	public void draw(SpriteBatch batch) {
		drawFloorFlower(batch);
		drawCeilingFlower(batch);
	}

	private void drawFloorFlower(SpriteBatch batch) {
		float x = floorCollisionCircle.x - floorTexture.getRegionWidth() / 2;
		float y = floorCollisionRect.getY() + COLLISSION_CIRCLE_RADIUS;
		batch.draw(floorTexture, x, y);
	}

	private void drawCeilingFlower(SpriteBatch batch) {
		float x = ceilingCollisionCircle.x - ceilingTexture.getRegionWidth() / 2;
		float y = ceilingCollisionRect.getY() - COLLISSION_CIRCLE_RADIUS;
		batch.draw(ceilingTexture, x, y);
	}

	public void setPosition(float x) {
		this.x = x;
		updateCollisionCircle();
		updateCollisionRect();
	}

	private void updateCollisionCircle() {
		floorCollisionCircle.setX(x + floorCollisionRect.width / 2);
		ceilingCollisionCircle.setX(x + floorCollisionRect.width / 2);
	}

	private void updateCollisionRect() {
		floorCollisionRect.setX(x);
		ceilingCollisionRect.setX(x);
	}

	public void drawDebug(ShapeRenderer shapeRenderer) {
		shapeRenderer.circle(floorCollisionCircle.x, floorCollisionCircle.y, floorCollisionCircle.radius);
		shapeRenderer.rect(floorCollisionRect.x, floorCollisionRect.y, floorCollisionRect.width,
				floorCollisionRect.height);
		shapeRenderer.circle(ceilingCollisionCircle.x, ceilingCollisionCircle.y, ceilingCollisionCircle.radius);
		shapeRenderer.rect(ceilingCollisionRect.x, ceilingCollisionRect.y, ceilingCollisionRect.width,
				ceilingCollisionRect.height);
	}

	public boolean isBeeColliding(Bee bee) {
		Circle beeCircle = bee.getCollisionCircle();
		return Intersector.overlaps(beeCircle, ceilingCollisionCircle)
				|| Intersector.overlaps(beeCircle, floorCollisionCircle)
				|| Intersector.overlaps(beeCircle, ceilingCollisionRect)
				|| Intersector.overlaps(beeCircle, floorCollisionRect);
	}

	public boolean isPointClaimed() {
		return pointClaimed;
	}

	public void markPointClaimed() {
		pointClaimed = true;
	}

	public float getX() {
		return x;
	}
}