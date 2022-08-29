package com.Bumstudios.Main;

import java.awt.Canvas;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferStrategy;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import javax.swing.JFrame;

import Entities.BulletShoot;
import Entities.Enemy;
import Entities.Entity;
import Entities.Player;
import Graficos.Spritesheet;
import Graficos.UI;
import world.World;


public class Game extends Canvas implements Runnable, KeyListener {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	public static JFrame frame;
	private Thread thread;
	private boolean isRunning = true;
	public static final int WIDTH = 320;
	public static final int HEIGHT = 240;
	public static final int SCALE = 3;

	private int CUR_LEVEL = 1,MAX_LEVEL = 3;
	private BufferedImage image;
    public static List<Entity> entities;
    public static List<Enemy> enemies;
    public static List<BulletShoot>bullets;
    public static Spritesheet spritesheet;
    
    public static World world;
    public static Player player;
    
    public static Random rand;
    public UI ui;
    
    public static String gameState = "MENU";
    private boolean showMessageGameOver = true;
	private int framesGameOver =0;
	private boolean restartGame = false;
	public static Menu menu;
	public boolean saveGame = false;
	
	private boolean ShowMensageEnd = true;
	public Game() {
		rand = new Random();
		 addKeyListener(this);
		this.setPreferredSize(new Dimension(WIDTH*SCALE,HEIGHT*SCALE));
		initFrame();
		//iniciando objetos\\
	Sound.music.loop();
		ui = new UI();
		image = new BufferedImage(WIDTH,HEIGHT,BufferedImage.TYPE_INT_RGB);
	    entities = new ArrayList<Entity>();
	    enemies = new ArrayList<Enemy>();
	    bullets = new ArrayList<BulletShoot>();
	    
	    spritesheet = new Spritesheet("/spritesheet.png");
	    player = new Player(0,0,16,16,spritesheet.getSprite(32,0,16,16));
	    entities.add(player);
	    world = new World("/level1.png");
	   
	   
	    menu = new Menu();
	}
	public void initFrame() {
		frame = new JFrame("Lost path");
		frame.add(this);
		frame.setResizable(false);
		frame.pack();
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
	} 
	
	public synchronized void start() {
		thread = new Thread(this);
		isRunning = true;
		thread.start();
		
	}
	
	public synchronized void stop() {
		
	}
 public static void main(String args[]) {
	 Game game = new Game();
	 game.start();
 }
	public void tick() {
		if(gameState == "normal") {
			if(this.saveGame) {
			 this.saveGame = false;
			 String [] opt1 = {"level","vida"};
			 int[] opt2 = {this.CUR_LEVEL,(int)player.life};
			 Menu.saveGame(opt1,opt2,10);
			 System.out.println("Salvando o jogo !");
			}
			this.restartGame = false;
		for(int i = 0; i<entities.size(); i++) {
			Entity e= entities.get(i);
			e.tick();
		}
		
		for(int i = 0; i < bullets.size();i++) {
			bullets.get(i).tick();
		}
		if(enemies.size() == 0) {
		// level up
			CUR_LEVEL ++;
			if(CUR_LEVEL > MAX_LEVEL) {
			this.ShowMensageEnd = true;
			 CUR_LEVEL = 1;
			}
			String newWorld = "level"+CUR_LEVEL+".png";
			System.out.println(newWorld);
			World.restartGame(newWorld);
		}
		}else if(gameState == "GAME_OVER") {
			this.framesGameOver++;
		 if(this.framesGameOver == 22) {
			 this.framesGameOver = 0;
			 if(this.showMessageGameOver)
				 this.showMessageGameOver = false;
			 else
				 this.showMessageGameOver = true;
			 
		 }
		        
		 if(restartGame) {
			 this.restartGame = false;
		     Game.gameState = "normal";
			 CUR_LEVEL = 1;
			 String newWorld = "level"+CUR_LEVEL+".png";
				World.restartGame(newWorld);
		 }
		}else if(gameState == "MENU") {
			// MENU Principal
			
			menu.tick();
		}
	}
	public void render() {
		BufferStrategy bs = this.getBufferStrategy();
		if(bs == null) {
			this.createBufferStrategy(3);
			return;
			
		}
		Graphics g = image.getGraphics();
		g.setColor(new Color(0,0,0));
		g.fillRect(0, 0,WIDTH,HEIGHT);
		/* Renderização do jogo*/
		world.render(g);
		for(int i = 0; i<entities.size(); i++) {
			Entity e= entities.get(i);
			e.render(g);
		}
		for(int i = 0; i < bullets.size();i++) {
			bullets.get(i).render(g);
		}
		ui.render(g);
		/***/
		g.dispose();
		g = bs.getDrawGraphics();
		g.drawImage(image, 0, 0,WIDTH*SCALE,HEIGHT*SCALE,null);
		if(gameState == "GAME_OVER") {
			Graphics2D g2 = (Graphics2D) g;
			g2.setColor(new Color (0,0,0,100));
			g2.fillRect(0,0, WIDTH*SCALE, HEIGHT*SCALE);
			g.setFont(new Font("Arial",Font.BOLD,40));
			g.setColor(Color.black);
			g.drawString("GAME OVER",609/2,540/2);
			g.setFont(new Font("Arial",Font.BOLD,40));
			g.setColor(Color.RED);
			g.drawString("GAME OVER",620/2,540/2);
			g.setFont(new Font("Arial",Font.BOLD,20));
			g.setColor(Color.WHITE);
			if(showMessageGameOver)
			 g.drawString("PRECIONE <ENTER> PARA CONTINUAR",509/2,640/2);
			
			 if(this.ShowMensageEnd) {
				 g.setFont(new Font("Arial",Font.BOLD,40));
					g.setColor(Color.LIGHT_GRAY);
					g.drawString("Obrigado por jogar meu jogo",509/2,644/2);
			}
				

		}else if (gameState  == "MENU") {
			menu.render(g); 
		}
		bs.show();
		
	}
	public void run() {
	 
		  long lastime = System.nanoTime();
		  double amountOfTicks = 60.0;
		  double ns = 1000000000 / amountOfTicks;
		  double delta = 0;
		  int frames = 0;
		  double timer = System.currentTimeMillis();
		  requestFocus();
		  while(isRunning) {
		  long now = System.nanoTime();
		  delta+= (now - lastime) / ns;
		  lastime = now;
		
		 
		if(delta >= 1) {
			  tick();
			  render();
			frames++;
			delta--;
		}
		  
		if(System.currentTimeMillis() - timer >= 1000) {
			 System.out.println("FPS: "+ frames);
			  frames = 0;
			  timer+=1000;
		 } 
		  
	   }

	}
	
	public void keyTyped(KeyEvent arg0) {

	}
	@Override
	public void keyPressed(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = true;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = true;
		}if(e.getKeyCode()== KeyEvent.VK_UP||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = true;
			if(gameState == "MENU") {
				menu.up = true;
			}
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = true;
			if(gameState == "MENU") {
				menu.down = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_F) {
			player.shoot = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_ENTER) {
			this.restartGame = true;
			if(Game.gameState == "MENU") {
				menu.enter = true;
			}
		}
		if(e.getKeyCode() == KeyEvent.VK_ESCAPE) {
			gameState = "MENU";
			Menu.pause = true;
		}
		if(e.getKeyCode() == KeyEvent.VK_SPACE) {
			if(gameState == "normal") 
			this.saveGame = true;
			
		}
			
		
	}
	
	public void keyReleased(KeyEvent e) {
		if(e.getKeyCode() == KeyEvent.VK_RIGHT||
				e.getKeyCode() == KeyEvent.VK_D) {
			player.right = false;
		}else if(e.getKeyCode() == KeyEvent.VK_LEFT||
				e.getKeyCode() == KeyEvent.VK_A) {
			player.left = false;
		}if(e.getKeyCode()== KeyEvent.VK_UP||
				e.getKeyCode() == KeyEvent.VK_W) {
			player.up = false;
		}else if(e.getKeyCode() == KeyEvent.VK_DOWN||
				e.getKeyCode() == KeyEvent.VK_S) {
			player.down = false;
		}
			
		
	}
	
}