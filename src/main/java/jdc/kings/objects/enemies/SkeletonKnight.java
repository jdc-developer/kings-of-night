package jdc.kings.objects.enemies;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import jdc.kings.objects.Enemy;
import jdc.kings.objects.interactions.Attack;
import jdc.kings.utils.Constants;
import jdc.kings.utils.SpriteLoader;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class SkeletonKnight extends Enemy {
	
	private boolean cutting;
	private boolean slicing;
	private long randomTimer;
	
	private List<BufferedImage[]> sprites = new ArrayList<>();
	
	private static final int IDLE = 0;
	private static final int WALKING = 1;
	private static final int CUTTING = 2;
	private static final int SLICING = 3;
	private static final int DYING = 4;

	public SkeletonKnight(TileMap tm) {
		super(tm);
		facingRight = false;
		
		moveSpeed = 0.35f;
		maxSpeed = 0.4f;
		fallSpeed = 0.2f;
		maxFallSpeed = 5f;
		jumpStart = -0.8f;
		maxJumpSpeed = 3.5f;
		stopJumpSpeed = 1.3f;
		flinchYSpeed = 5.5f;
		flinchXSpeed = 1f;
		maxFlinchXSpeed = 1.5f;
		
		width = 70;
		height = 110;
		cwidth = 35;
		cheight = 105;
		
		attacks.add(new Attack(8, 2, 4, 66, 4, 250, 500));
		attacks.add(new Attack(10, 3, 5, 100, 6, 250, 500));
		
		health = maxHealth = 20;
		stamina = maxStamina = 15;
		damage = 7;
		
		shieldDamage = 1;
		shieldCost = 4;
		
		sightXDistance = 650;
		sightYDistance = 150;
		
		SpriteLoader loader = SpriteLoader.getInstance();
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/idle.png", this, 0, 5, 0, 2, 0, 0, 200, 322, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/walking.png", this, 0, 5, 0, 2, 0, 0, 200, 325, 0, 0));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/cutting.png", this, 0, 5, 0, 1, 0, 0, 246, 265, 50, 10));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/slicing.png", this, 0, 5, 0, 1, 0, 0, 246, 265, 50, 10));
		sprites.add(loader.loadAction("/sprites/enemies/skeleton-knight/dying.png", this, 0, 7, 0, 1, 0, 0, 200, 186, 0, 0));
	
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
		
		if (currentAction == CUTTING &&
				!(jumping || falling)) {
			velX = 0;
		}
		
		if (jumping && !falling) {
			velY = jumpStart;
			falling = true;
		}
		
		if (stamina < maxStamina) {
			stamina += 0.04f;
		}
		
		if (stamina < 0) {
			stamina = 0;
		}
		
		if (falling) {
			velY += fallSpeed;
			
			if (velY > 0) jumping = false;
			if (velY < 0 && !jumping) velY += stopJumpSpeed;
			
			if (velY > maxFallSpeed) velY = maxFallSpeed;
		}
	}
	
	@Override
	public void tick() {
		if (!dying) {
			getNextPosition();
			playerPosition();
			checkPlayerDamage();
			super.tick();
		}
		
		if (flinching) {
			long elapsed = (System.nanoTime() - flinchTimer) / 1000000;
			if (elapsed > 400) {
				flinching = false;
			}
			
			if (elapsed > 100) {
				flinchDirection = 0;
			} else {
				right = false;
				left = false;
			}
			
			if (cutting) {
				animator.setFrames(sprites.get(IDLE));
				cutting = false;
				height = 110;
				width = 70;
			}
		}
		
		if (currentAction == DYING) {
			long elapsed = (System.nanoTime() - holdTimer) / 1000000;
			if (elapsed > 500) {
				animator.holdLastFrame();
				dead = true;
			}
			height = 110;
			width = 70;
		}
		
		if (currentAction == CUTTING) {
			if (animator.hasPlayedOnce()) {
				cutting = false;
				height = 110;
				width = 70;
				
				maxSpeed = 0.4f;
			}
		}
		
		if (currentAction == SLICING) {
			if (animator.hasPlayedOnce()) {
				slicing = false;
				height = 110;
				width = 70;
				
				maxSpeed = 0.4f;
			}
		}
		
		if (dying) {
			if (currentAction != DYING) {
				currentAction = DYING;
				animator.setFrames(sprites.get(DYING));
				animator.setSpeed(120);
				holdTimer = System.nanoTime();
				width = 105;
				height = 130;
			}
		} else if (cutting && !slicing) {
			if (currentAction != CUTTING) {
				attack = attacks.get(0);
				long elapsed = (System.nanoTime() - attack.getTimer()) / 1000000;
				if (stamina >= attack.getCost() && elapsed > 1500) {
					currentAction = CUTTING;
					attack.setTimer(System.nanoTime());
					
					stamina -= attack.getCost();
					animator.setFrames(sprites.get(CUTTING));
					animator.setSpeed(100);
					width = 105;
					height = 130;
				}
			}
		} else if (slicing && !cutting) {
			if (currentAction != SLICING) {
				attack = attacks.get(1);
				long elapsed = (System.nanoTime() - attack.getTimer()) / 1000000;
				if (stamina >= attack.getCost() && elapsed > 1500) {
					currentAction = SLICING;
					attack.setTimer(System.nanoTime());
					
					stamina -= attack.getCost();
					animator.setFrames(sprites.get(SLICING));
					animator.setSpeed(150);
					width = 105;
					height = 130;
					
					maxSpeed = 2f;
					jumping = true;
					
					if (facingRight) right = true;
					else left = true;
				}
			}
		} else if (left || right) {
			if (currentAction != WALKING) {
				currentAction = WALKING;
				animator.setFrames(sprites.get(WALKING));
				animator.setSpeed(80);
				width = 70;
			}
		} else {
			if (currentAction != IDLE) {
				currentAction = IDLE;
				animator.setFrames(sprites.get(IDLE));
				animator.setSpeed(120);
				width = 70;
			}
		}
		
		if (right) facingRight = true;
		if (left) facingRight = false;
	}
	
	public void checkPlayerDamage() {
		super.checkPlayerDamage();
		if (cutting && attack != null) {
			attack.checkAttack(this, player, false);
		}
		
		if (slicing && attack != null) {
			attack.checkAttack(this, player, false);
		}
	}
	
	public void playerPosition() {
		super.playerPosition();
		long elapsed = (System.nanoTime() - randomTimer) / 1000000;
		if (elapsed > 500) {
			Random random = Constants.random;
			int r = random.nextInt(2);
			randomTimer = System.nanoTime();
			
			if (playerXDistance <= 100 && playerXDistance > 0) {
				if (r == 1) {
					cutting = true;
					slicing = false;
				} else {
					slicing = true;
					cutting = false;
				}
			} else if (playerXDistance >= -100 && playerXDistance < 0) {
				if (r == 1) {
					cutting = true;
					slicing = false;
				} else {
					slicing = true;
					cutting = false;
				}
			}
		}
	}

}
