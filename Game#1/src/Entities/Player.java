package Entities;

import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.util.ArrayList;

import com.Bumstudios.Main.Game;
import com.Bumstudios.Main.Sound;

import Graficos.Spritesheet;
import world.Camera;
import world.World;

public class Player extends Entity {
public boolean right,left,up,down;

public int right_dir = 0,left_dir = 1, up_dir =0;
public int dir = right_dir;
public double speed = 1;
public static int ammo = 0;
public double life = 100, MaxLife = 100;
private BufferedImage playerDamage;
public boolean isDamaged = false;
private int DamagedFrames = 0;
public boolean shoot = false;

private boolean hasgun = false;
private int frames = 0,maxFrames = 5,index = 0,maxIndex = 3;
private boolean moved = false;
private BufferedImage[] rightPlayer;
private BufferedImage[] leftPlayer;


public Player(int x, int y, int width, int height, BufferedImage sprite) {
		super(x, y, width, height, sprite);
		
		rightPlayer = new BufferedImage[4];
		leftPlayer = new BufferedImage[4];
	 
		
		playerDamage = Game.spritesheet.getSprite(0, 16, 16, 16);
	
		for(int i=0; i < 4; i++) {
			rightPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 0, 16, 16);
		}
		for(int i=0; i < 4; i++) {
			leftPlayer[i] = Game.spritesheet.getSprite(32 + (i*16), 16, 16, 16);
		}
		
	}
 public void tick(){
	 moved = false;
	 if(right && World.isFree((int)(x+speed),this.getY())) {
		 moved = true;
		 dir = right_dir;
		 x+=speed;
	 }
	 else if(left &&  World.isFree((int)(x-speed),this.getY())) {
		 moved = true;
		 dir = left_dir;
		 x-=speed;
	 }
	    if(up && World.isFree(this.getX(),(int)(y-speed))) {
	     moved = true;
		 y-=speed;
	    }
	 else if(down && World.isFree(this.getX(),(int)(y+speed))) {
		 moved = true;
		 y+=speed;
	 }
	    if(moved) {
	    	frames++;
	    	if(frames == maxFrames) {
	    		frames = 0;
	    	    index++;
	    	    if(index > maxIndex)
	    	    	index = 0;
	    	
	       }
	   }
	    checkCollisionammo();
	    checkCollisionLifePack();
	    checkCollisiGun();
	    if(isDamaged) {
	    	this.DamagedFrames++;
	    	if(this.DamagedFrames == 8) {
	    		this.DamagedFrames = 0;
	    		isDamaged = false;
	    	}
	    }
	    	
	    	if(shoot) {
	    		Sound.shoot.play();;
	    		shoot = false;
	    		//Atirar a bala\\
	    		if( hasgun && ammo > 0) {
	    		ammo--;
	    		int dx = 0;
	    		int px = 0;
	    		int py = 5;
	    		if(dir == right_dir) {
	    		 px = 16;
	    		 dx = 1;
	    		}else{
	    	     px = -8;
	    		 dx = -1;
	    		}
	    		BulletShoot bullet = new BulletShoot(this.getX()+px,this.getY()+py,3,3,null,dx,0);
	    		Game.bullets.add(bullet);
	    		}
	    		
	    	}
	    	
	    
	   if(life <= 0) {
	      // Game over!
		   
		   life = 0;
		   Game.gameState = "GAME_OVER";
	    	
	   
	   }
	    Camera.x = Camera.clamp (this.getX() - (Game.WIDTH/2),0,World.WIDTH*16 - Game.WIDTH); 
	    Camera.y = Camera.clamp(this.getY() - (Game.HEIGHT/2),0,World.HEIGHT*16 - Game.HEIGHT);
  }
 
 public void checkCollisiGun(){
		for(int i = 0; i < Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Weapon) {
				if(Entity.isColidding(this, atual)) {
					hasgun = true; 
					//System.out.println("Armado");
					Game.entities.remove(atual);
				}
			}
		}
	}
 public void checkCollisionammo(){
		for(int i = 0; i < Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Bullet) {
				if(Entity.isColidding(this, atual)) {
					ammo+=15;
					Game.entities.remove(atual);
				}
			}
		}
	}
 public void checkCollisionLifePack(){
		for(int i = 0; i < Game.entities.size(); i++){
			Entity atual = Game.entities.get(i);
			if(atual instanceof Lifepack) {
				if(Entity.isColidding(this, atual)) {
					life+=10;
					if(life > 100)
						life = 100;
					Game.entities.remove(atual);
				}
			}
		}
	}
 
 public void render(Graphics g) {
	 if(!isDamaged) {
		if(dir == right_dir) {
		 g.drawImage(rightPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
	      if(hasgun) {
	    	  //Renderizar arma\\
	    	  //direita.
	    	  g.drawImage(Entity.GUN_RIGHT,this.getX()+7 -Camera.x,this.getY()-Camera.y, null);
	    	  
	      }
	 }else if(dir == left_dir) {
		 g.drawImage(leftPlayer[index], this.getX() - Camera.x, this.getY() - Camera.y, null);
            if(hasgun) {
            	//Renderizar arma
            	//Esquerda
            	g.drawImage(Entity.GUN_LEFT,this.getX()-4-Camera.x,this.getY()-Camera.y, null);
            }
	
	 }
	 }else {
		 g.drawImage(playerDamage, this.getX()-Camera.x,this.getY()-Camera.y ,null);
	 }
     }
 }
