package com.jump.utils;

import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

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
	/**
	 * 从指定路径加载图片，并转化成指定大小的Icon对象，并返回
	 *<p>Title: getScaleIcon </p>
	 *<p>Description: </p>
	 *@param imageUrl
	 *@param width
	 *@param height
	 *@return
	 *@see ImageUtil
	 */
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
	
	/**
	 * 从指定路径中加载BufferedImage
	 *<p>Title: getBufferedImage </p>
	 *<p>Description: </p>
	 *@param imageUrl
	 *@return
	 *@see ImageUtil
	 */
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
	
	/**
	 * 从流中加载图片
	 *<p>Title: getBufferedImage </p>
	 *<p>Description: </p>
	 *@param inputStream
	 *@return
	 *@see ImageUtil
	 */
	public static BufferedImage getBufferedImage(InputStream inputStream) {
		BufferedImage bufferedImage = null;
		try {
			//必须关闭流，方法才能执行完整
			bufferedImage = ImageIO.read(inputStream);
			inputStream.close();
			return bufferedImage;
		} catch (IOException e) {
			System.out.println("图片加载失败");
			e.printStackTrace();
		}
		try {
			inputStream.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}
	/**
	 * 将指定BufferedImage转换为指定大小的BufferedImage对象
	 *<p>Title: getBufferedImage </p>
	 *<p>Description: </p>
	 *@param bImage
	 *@return
	 *@see ImageUtil
	 */
	public static BufferedImage getBufferedImage(BufferedImage bImage, int width, int height) {
		BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);
		Graphics2D g2d = bufferedImage.createGraphics();
		g2d.drawImage(bImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
		return bufferedImage;
	}
	
	/**
	 * 从指定路径加载png格式图片，并转化成指定大小的BufferedImage对象，并返回
	 *<p>Title: getScaleImage </p>
	 *<p>Description: </p>
	 *@param imageUrl
	 *@param width
	 *@param height
	 *@return
	 *@see ImageUtil
	 */
	public static BufferedImage getScaleImage(String imageUrl, int width, int height) {
		
		//加载图片
		BufferedImage image = getBufferedImage(imageUrl);
		BufferedImage bufferedImage = getBufferedImage(image, width, height);
		
		return bufferedImage;
	}
	
	/**
	 * 从流中加载png图片，并转化成指定大小的BufferedImage对象，并返回
	 *<p>Title: getScaleImage </p>
	 *<p>Description: </p>
	 *@param inputStream
	 *@param width
	 *@param height
	 *@return
	 *@see ImageUtil
	 */
	public static BufferedImage getScaleImage(InputStream inputStream, int width, int height) {
		
		//加载图片
		BufferedImage image = getBufferedImage(inputStream);
		BufferedImage bufferedImage = getBufferedImage(image, width, height);
		
		return bufferedImage;
	}
	
	/**
	 * 根据坐标得到图形的染色数据，保存在参数r，g， b中
	 *<p>Title: getImageColor </p>
	 *<p>Description: </p>
	 *@param image
	 *@param x
	 *@param y
	 *@param r
	 *@param g
	 *@param b
	 *@return
	 *@see ImageUtil
	 */
	public static int getImageColor(BufferedImage image, int x, int y, int r, int g, int b) {
		int rgb = image.getRGB(x, y);
		return rgb;
	}
}