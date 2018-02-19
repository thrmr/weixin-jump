package com.jump.icon;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.RenderingHints;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

/**
 * 缩放Icon
 * <p>Title: ScaleIcon</p>
 * <p>Descreption: </p>
 * <p>Company: </p>
 * @author thrmr
 * @date   Feb 4, 2018 8:00:49 PM
 */
public class ScaleIcon implements Icon {
 
    private Icon icon = null;
    
    //缩放比例
    private int width = 0,hight = 0;
    
    //得到一个显示充满父容器的Icon，但宽高为参数icon的宽高
    public ScaleIcon(Icon icon) {  
        this.icon = icon;
    }
    //若xScale和yScale都不为0,则缩放为指定的宽高,宽高为指定宽高
    //若xScale或yScale为0,则得到显示充满父容器的Icon，但宽高为参数icon的宽高
    public ScaleIcon(Icon icon, int width, int hight) {  
        this.icon = icon; 
        this.width = width;
        this.hight = hight;
    }  
  
    @Override  
    public int getIconHeight() {
    	if (hight == 0) {
    		return icon.getIconHeight();
    	}
        return hight;  
    }  
  
    @Override  
    public int getIconWidth() {  
    	if (width == 0) {
    		return icon.getIconWidth();
    	}
        return width;  
    }  
    
    @Override
    public void paintIcon(Component c, Graphics g, int x, int y) {  
        int iconWid = icon.getIconWidth();  
        int iconHei = icon.getIconHeight();  
        int ch = c.getHeight();
        int cw = c.getWidth();
        float xScale = 0, yScale = 0;
        Graphics2D g2d = (Graphics2D) g;  
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        if (this.width == 0  || this.hight == 0) {
        	xScale = (float)cw / iconWid;
        	yScale = (float)ch / iconHei;
        	System.out.println(xScale + ":" + yScale);
        } else {
        	xScale = (float)this.width / iconWid;
        	yScale = (float)this.hight / iconHei;
        }
        g2d.scale(xScale, yScale);  
        icon.paintIcon(c, g2d, x, y);
    }  
  
    public static void main(String[] args) throws IOException {  
       // ScaleIcon icon = new ScaleIcon(new ImageIcon(ClassLoader.getSystemResource("img/main.jpg")), 1, 1);  
        ScaleIcon icon = new ScaleIcon(new ImageIcon(ImageIO.read(new File("/home/thrmr/aaa.png"))), 480, 800);  
		JLabel label = new JLabel(icon);  
		JFrame frame = new JFrame();  
		frame.getContentPane().add(label, BorderLayout.CENTER);  
		//	                frame.getContentPane().add(new JButton("click"),BorderLayout.NORTH);  
	    frame.setSize(480, 800);  
	    frame.setLocationRelativeTo(null);  
	    frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
	    frame.setVisible(true);  
	}  
}