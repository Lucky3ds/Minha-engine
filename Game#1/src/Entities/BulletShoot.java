package Entities;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Rectangle;
import java.awt.image.BufferedImage;

import com.Bumstudios.Main.Game;


import world.Camera;
import world.World;



public class BulletShoot extends Entity{
    
  
	private double dx;
	private double dy;
	public double spd = 4;
	private int life = 50, curLife = 0; 
	
	
	public BulletShoot(int x, int y, int width, int height, BufferedImage sprite,double dx,double dy) {
		super(x, y, width, height, sprite);
      
		this.dx = dx;
		this.dy = dy;
	}

	
	
	public void tick() {

		x+=dx*spd;
		y+=dy*spd;
		curLife++;
		if(curLife == life) {
			Game.bullets.remove(this);
			return;
		}
	
	}
	
	public void render(Graphics g){
		g.setColor(Color.black);
		g.fillOval(this.getX() - Camera.x,this.getY() - Camera.y,width,height);
	}
	
 
}
