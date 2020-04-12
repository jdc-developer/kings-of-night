package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import jdc.kings.objects.Enemy;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class HellHound extends Enemy {
	
	private boolean running;
	private long turnAroundTimer;
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int JUMPING = 2;
	private static final int RUNNING = 3;

	public HellHound(TileMap tm) {
		super(tm);
		facingRight = false;
		
		moveSpeed = 5f;
		maxSpeed = 6f;
		fallSpeed = 0.2f;
		maxFallSpeed = 10.0f;
		
		width = 60;
		height = 60;
		cwidth = 35;
		cheight = 35;
		
		health = maxHealth = 15;
		damage = 5;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/idle.png", this, 0, 6, 11, 22, 40, 24, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/walking.png", this, 0, 12, 9, 19, 45, 26, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/jumping.png", this, 0, 6, 11, 14, 47, 39, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/hellhound/running.png", this, 0, 5, 9, 22, 45, 29, 0, 0));
	
		animator = new Animator(sprites.get(0));
		currentAction = IDLE;
		animator.setSpeed(120);
		animator.start();
	}
	
	private void getNextPosition() {
		if (left) {
			velX -= moveSpeed;
			if (velX < -maxSpeed) {
				velX = -maxSpeed;
			}
		} else if (right) {
			velX += moveSpeed;
			if (velX > maxSpeed) {
				velX = maxSpeed;
			}
		}
		
		if (falling) {
			velY += fallSpeed;
		}
	}
	
	@Override
	public void tick() {
		getNextPosition();
		checkTileMapCollision();
		setPosition(xtemp, ytemp);
		playerPosition();
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
		}
		
		if (left || right) {
			if (currentAction != WALKING && !running) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(100);
				width = 60;
			}
			
			if (currentAction != RUNNING && running) {
				currentAction = RUNNING;
				animator.setFrames(sprites.get(RUNNING));
				animator.setSpeed(100);
				width = 60;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(120);
				width = 60;
			}
		}
		if (right) facingRight = true;
		if (left) facingRight = false;
		
		animator.update(System.currentTimeMillis());
	}
	
	private void playerPosition() {
		float distance = this.x - player.getX();
		if (distance <= 650  && distance > 0) {
			long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
			if (elapsed > 1000) {
				running = true;
				left = true;
				right = false;
				turnAroundTimer = System.nanoTime();
			}
		} else if (distance >= -650 && distance < 0) {
			long elapsed = (System.nanoTime() - turnAroundTimer) / 1000000;
			if (elapsed > 1000) {
				running = true;
				right = true;
				left = false;
				turnAroundTimer = System.nanoTime();
			}
		}
	}

}
