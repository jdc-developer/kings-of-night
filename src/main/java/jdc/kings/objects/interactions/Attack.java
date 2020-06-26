package jdc.kings.objects.interactions;

import jdc.kings.objects.GameObject;
import jdc.kings.utils.AudioPlayer;

public class Attack {
	
	private int damage;
	private int shieldDamage;
	private int shieldCost;
	private int range;
	private int cost;
	private int startTime;
	private int endTime;
	private long timer;
	
	public int getDamage() {
		return damage;
	}

	public void setDamage(int damage) {
		this.damage = damage;
	}

	public int getShieldDamage() {
		return shieldDamage;
	}
	
	public void setShieldDamage(int shieldDamage) {
		this.shieldDamage = shieldDamage;
	}
	
	public int getShieldCost() {
		return shieldCost;
	}
	
	public void setShieldCost(int shieldCost) {
		this.shieldCost = shieldCost;
	}
	
	public int getRange() {
		return range;
	}
	
	public void setRange(int range) {
		this.range = range;
	}
	
	public int getCost() {
		return cost;
	}
	
	public void setCost(int cost) {
		this.cost = cost;
	}

	public int getStartTime() {
		return startTime;
	}

	public void setStartTime(int startTime) {
		this.startTime = startTime;
	}

	public int getEndTime() {
		return endTime;
	}

	public void setEndTime(int endTime) {
		this.endTime = endTime;
	}

	public long getTimer() {
		return timer;
	}

	public void setTimer(long timer) {
		this.timer = timer;
	}

	public Attack(int damage, int shieldDamage, int shieldCost, int range, int cost, int startTime, int endTime) {
		super();
		this.damage = damage;
		this.shieldDamage = shieldDamage;
		this.shieldCost = shieldCost;
		this.range = range;
		this.cost = cost;
		this.startTime = startTime;
		this.endTime = endTime;
	}
	
	public void checkAttack(GameObject attacking, GameObject attacked, boolean ranged, String hitClip) {
		long elapsed = (System.nanoTime() - timer) / 1000000;
		if ((elapsed > startTime && elapsed < endTime && !attacked.isDead()) || ranged) {
			
			if (attacked.isShield()) {
				
				long shieldElapsed = (System.nanoTime() - attacked.getHoldTimer()) / 1000000;
				if ((!attacking.isFacingRight() && attacked.isFacingRight()) || (attacking.isFacingRight() && !attacked.isFacingRight()) &&
						(shieldElapsed > 200)) {
					attacked.shieldDamage(shieldDamage, damage, shieldCost, !attacking.isFacingRight());
				} else {
					consumateAttack(attacking, attacked, ranged, hitClip);
				}
			} else if (attacked.isRolling()) {
				
				long rollElapsed = (System.nanoTime() - attacked.getRollTimer()) / 1000000;
				if (rollElapsed < 100) {
					consumateAttack(attacking, attacked, ranged, hitClip);
				}
			} else {
				consumateAttack(attacking, attacked, ranged, hitClip);
			}
		}
	}
	
	private void consumateAttack(GameObject attacking, GameObject attacked, boolean ranged, String hitClip) {
		AudioPlayer audioPlayer = AudioPlayer.getInstance();
		if (attacking.isFacingRight()) {
			 if (
					 attacked.getX() > attacking.getX() &&
					 attacked.getX() < attacking.getX() + range &&
					 attacked.getY() > attacking.getY() - attacking.getHeight() / 2 &&
					 attacked.getY() < attacking.getY() + attacking.getHeight() / 2) {
				 
				 attacked.hit(damage, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 } else if (ranged) {
				 attacked.hit(damage, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 }
		 } else {
			 if (
					 attacked.getX() < attacking.getX() &&
					 attacked.getX() > attacking.getX() - range &&
					 attacked.getY() > attacking.getY() - attacking.getHeight() / 2 &&
					 attacked.getY() < attacking.getY() + attacking.getHeight() / 2) {
				 
				 attacked.hit(damage, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 } else if (ranged) {
				 attacked.hit(damage, !attacking.isFacingRight(), false);
				 if (hitClip != null) audioPlayer.play(hitClip);
			 }
		 }
	}

}
