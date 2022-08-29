package Graficos;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics;

import com.Bumstudios.Main.Game;

import Entities.Player;

public class UI {

	
	public void render(Graphics g) {
		g.setColor(Color.red);
		g.fillRect(5, 8, 50, 9);
		g.setColor(Color.green);
		g.fillRect(5, 8,(int)((Game.player.life/Game.player.MaxLife)*50), 9);
		g.setColor(Color.white);
		g.setFont(new Font("Arial",Font.BOLD,8));
		g.drawString((int)Game.player.life+"/"+(int) Game.player.MaxLife,8,8);
		g.drawString("Ammo:"+Player.ammo, 260,8);
	}
}
