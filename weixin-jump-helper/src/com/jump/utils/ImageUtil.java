package com.jump.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;

import javax.imageio.ImageIO;
import javax.swing.Icon;
import javax.swing.ImageIcon;

import com.jump.icon.ScaleIcon;

/**
 * 图片处理Util
 * <p>Title: ImageUtil</p>
 * <p>Descreption: </p>
 * <p>Company: </p>
 * @author thrmr
 * @date   Feb 4, 2018 1:24:02 PM
 */

public class ImageUtil {
	
	//从指定路径加载图片，并转化成指定大小的Icon对象，并返回
	public static ScaleIcon getScaleIcon(String imageUrl, int width, int height) {
		
		//加载截图
		Icon icon = null;
		try {
			icon = new ImageIcon(ImageIO.read(new File(imageUrl)));			
		} catch (IOException e) {
			System.out.println("截图加载失败");
			e.printStackTrace();
		}
		ScaleIcon scaleIcon = new ScaleIcon(icon, width, height);
		return scaleIcon;
	}
	
	public static BufferedImage getBufferedImage(String imageUrl) {
		
		try {
			BufferedImage bufferedImage = ImageIO.read(new File(imageUrl));
			return bufferedImage;
		} catch (IOException e) {
			System.out.println("图片加载失败");
			e.printStackTrace();
		}
		return null;
	}
	
	//从指定路径加载png图片，并转化成指定大小的Image对象，并返回
	public static BufferedImage getScaleImage(String imageUrl, int width, int height) {
		
		//加载图片
		try {
			BufferedImage image = getBufferedImage(imageUrl);
			BufferedImage bImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB); 
			Graphics2D g2d = bImage.createGraphics();  
			g2d.drawImage(image.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
			return bImage;
		} catch (Exception e) {
			System.out.println("截图加载失败");
			e.printStackTrace();
		}
		return null;
	}
	
	//根据坐标得到图形的染色数据，保存在参数r，g， b中
	public static int getImageColor(BufferedImage image, int x, int y, int r, int g, int b) {
		int rgb = image.getRGB(x, y);
		return rgb;
	}
}