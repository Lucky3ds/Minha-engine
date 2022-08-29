package com.Bumstudios.Main;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;

import world.World;




public class Menu  {
	
	public String[] options = {"Novo jogo","carregar jogo", "Sair"};
	
	public int currentOption = 0;
	public int maxOption = options.length -1;

	public  boolean down,up,enter;
    public static boolean pause = false;
	
    public static boolean saveExists = false;
    public static boolean saveGame = false;

	public void tick() {
		File file = new File("save.txt");
		if(file.exists()) {
			saveExists =true;
		}else {
			saveExists = false;
		}
		if(up) {
			up = false;
			currentOption --;
			if(currentOption < 0) 
				currentOption = maxOption;
		}
		if(down) {
			down = false;
			currentOption ++;
			if(currentOption > maxOption ) {
				currentOption = 0;
			}
				
		}
		if(enter) {
		enter = false;
		if(options [currentOption] == "Novo jogo"|| options [currentOption] == "continuar") {
			Game.gameState = "normal";
			pause = false;
		    file = new File("save.txt");
			//file.delete();
		}else if(options [currentOption] == "carregar jogo") {
			    file = new File("save.txt");
			    if(file.exists()) {
			    	String saver = loadGame(10);
			    	applySave(saver);
			    }

		}else if(options [currentOption] == "Sair") {
			System.exit(1);
	     	     }
		  }
	}
	public static void applySave(String str) {
		String[] spl = str.split("/");
		for(int i = 0; i < spl.length; i++) {
			String[] spl2 = spl[i].split(":");
			switch(spl2[0]) 
			{
			
			case "level ":
				World.restartGame("level"+spl2[1]+"png");
				Game.gameState = "normal";
				pause = false;
				break;
			
			}
		}
	}
	public static String loadGame(int encode) {
		String line = "";
		File file = new File("save.txt");
		if(file.exists()) {
			try {
				String singleLine = null;
				BufferedReader reader = new BufferedReader(new FileReader("save.txt"));
				try {
					while((singleLine = reader.readLine())!= null) {
						String[] trans = singleLine.split(":");
						char [] val = trans [1].toCharArray();
						trans[1] = "";
						for(int i = 0; i < val.length; i++) {
							val[i] -= encode;
							trans [1]+=val[i];
						}
						line+=trans[0];
						line+="";
						line+=trans[1];
						line+="/";
					}
				}catch(IOException e) {}
			}catch(FileNotFoundException e) {}
		}
		return line;
	}
	
	public static void saveGame(String[] val1,int[]val2,int encode) {
		
		BufferedWriter write =null;
		try {
			write = new BufferedWriter(new FileWriter("save.txt"));
			
			
		}catch(IOException e){}
		
		for(int i = 0; i < val1.length; i++) {
			String current = val1[i];
			current +=":";
			char[] value = Integer.toString(val2[i]).toCharArray();
			for(int n = 0; n < value.length; n++) {
				value [n]+= encode;
				current += value[n];
			}
			try {
				write.write(current);
				if(i < val1.length -1 ) 
					write.newLine();
			}catch(IOException e) {}
		}
		try {
			write.flush();
			write.close();
		}catch(IOException e){}
		
	}
	public void render (Graphics g) {
        g.setColor(Color.black);
		g.fillRect(0, 0, Game.WIDTH*Game.SCALE,Game.HEIGHT*Game.SCALE);
		g.setColor(Color.yellow);
		g.setFont(new Font ("arial",Font.BOLD,100));
        g.drawString("Lost", 230, 120);
		g.setFont(new Font ("arial",Font.BOLD,80));
        g.drawString("Path", 330, 200);
	       
          //Opçoes do menu
        g.setFont(new Font ("arial",Font.BOLD,24));
        if(pause == false)
       g.drawString("Novo jogo",(Game.WIDTH*Game.SCALE)/2 -400,(Game.HEIGHT*Game.SCALE)/2-50 );
        else
        g.drawString("Continuar",(Game.WIDTH*Game.SCALE)/2 -400,(Game.HEIGHT*Game.SCALE)/2-50 );
        
       g.drawString("Carregar",(Game.WIDTH*Game.SCALE)/2 -390,(Game.HEIGHT*Game.SCALE)/2-5 );
       
       g.drawString("sair",(Game.WIDTH*Game.SCALE)/2 -370,(Game.HEIGHT*Game.SCALE)-1/2-320 );
       
      if(options[currentOption] == "Novo jogo") {
    	  g.drawString(">", (Game.WIDTH*Game.SCALE)/2 -430,(Game.HEIGHT*Game.SCALE)/2-50 );
		}else if(options [currentOption] == "carregar jogo") {
			 g.drawString(">", (Game.WIDTH*Game.SCALE)/2 -410,(Game.HEIGHT*Game.SCALE)/2-5);
		}else if(options [currentOption] == "Sair") {
			 g.drawString(">", (Game.WIDTH*Game.SCALE)/2 -390,403);
		}
       
	}
	


}