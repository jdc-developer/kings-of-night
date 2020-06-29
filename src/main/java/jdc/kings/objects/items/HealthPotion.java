package jdc.kings.objects.items;

import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.Locale;

import javax.imageio.ImageIO;

import jdc.kings.Game;
import jdc.kings.objects.Item;
import jdc.kings.utils.BundleUtil;
import jdc.kings.view.Animator;
import jdc.kings.view.TileMap;

public class HealthPotion extends Item {

	public HealthPotion(TileMap tm) {
		super(tm);
		try {
			Locale locale = Game.getInstance().getPreferences().getLocale();
			description = BundleUtil.getMessageResourceString("healthPotionDescription", locale);
			
			id = 1;
			fallSpeed = 0.2f;
			maxFallSpeed = 10.0f;
			
			width = 32;
			height = 32;
			cwidth = 25;
			cheight = 22;
			
			if (spriteLoader.getSprites("health-potion") == null) {
				BufferedImage[][] sprites = new BufferedImage[1][1];
				sprites[0][0] = ImageIO.read(getClass().getResourceAsStream("/sprites/items/health-potion.png"));
				spriteLoader.loadSprites("health-potion", sprites);
			}
			
			image = spriteLoader.getAction("health-potion", 0)[0];
			animator = new Animator(spriteLoader.getAction("health-potion", 0));
			animator.setSpeed(120);
			animator.start();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	private void getNextPosition() {
		if (jumping && !falling) {
			velY = jumpStart;
			falling = true;
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
		getNextPosition();
		super.tick();
	}

}
