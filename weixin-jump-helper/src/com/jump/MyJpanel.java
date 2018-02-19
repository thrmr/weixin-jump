package com.jump;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Image;
import java.awt.image.BufferedImage;

import javax.swing.JPanel;

class MyJpanel extends JPanel {

	private BufferedImage image;

	public MyJpanel() {
		
	}
	
	public MyJpanel(BufferedImage image) {
		this.image = image;
	}
	
	@Override
	public void paint(Graphics g) {
		super.paint(g);
		g.setColor(Color.RED);
		g.drawImage(this.image, 0, 0, this);
	}
	

	public BufferedImage getBufferedImage() {
		return image;
	}


	public void setBufferedImage(BufferedImage image) {
		this.image = image;
	}
	
	
}
