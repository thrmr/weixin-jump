package com.jump;

import java.awt.Color;
import java.awt.Container;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JFrame;

import com.jump.utils.ImageUtil;

public class WeixinJumpMain {
	
	private static boolean flag = true;
	
	//速度
	private static double speech = 3.15f;
	//色差最小值
	private static int redDiff = 10;
	private static int greenDiff = 10;
	private static int buleDiff = 10;
	
	//扫描起始位置
	public static int xStart = 10;
	public static int yStart = 120;
	//扫描终止位置
	public static int xStop = 470;
	public static int yStop = 720;
	
	//左侧点的检测精度
	//y跳跃长度
	private static int precision = 6;
	//背景采样相对top点位置 
	private static int xOffset = 0;
	private static int yOffset = -20;
	
	private static BufferedImage bufferedImage;
	public static String imageUrl = "/tmp/weixinjump.png";
	//截图脚本位置
	public static String adbScreencapCommand = "/tmp/weixinjump.sh";
	//adb触屏指令
	private static String adbCommand = "adb shell input swipe";

	//人偶的相对颜色差值允许最小值
	private static int manR = 2;
	private static int manG = 2;
	private static int manB = 2;
	
	//人偶坐标
	private static int manX;
	//跳过人偶参数
	public static int skipWidth = 35;
	
	public static void main(String[] args) {
	
		//设置窗口
		JFrame frame = new JFrame("微信跳一跳辅助");
		frame.setSize(485, 825);
		frame.setLocation(1000, 200);
		Container contentPane = frame.getContentPane();
		
		screenCap();
		bufferedImage = ImageUtil.getScaleImage(imageUrl, 480, 800);
		final MyJpanel myJpanel = new MyJpanel(bufferedImage);
		contentPane.add(myJpanel);
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setVisible(true);
		
		//鼠标监听
		myJpanel.addMouseListener(new MouseListenerAdapter() {
			
			@Override
			public void mouseReleased(MouseEvent e) {
				//加载游戏图片
				if(e.getButton() == e.BUTTON3) {
					flag = false;
					screenCap();
					bufferedImage = ImageUtil.getScaleImage(imageUrl, 480, 800);
					myJpanel.setBufferedImage(bufferedImage);
					myJpanel.repaint();
					System.out.println("更新截图成功");
				}
				//确定
				if(e.getButton() == e.BUTTON1) {
					int beginX = e.getX();
					int beginY = e.getY();
					flag = true;
					System.out.println("当前坐标为：" + "X:" + beginX + "    Y:" + beginY);
					int rgb = bufferedImage.getRGB(beginX, beginY);
					Color color = new Color(rgb);
					System.out.printf("颜色为:%x ,", rgb);
					System.out.printf("r颜色为:%x ,", color.getRed());
					System.out.printf("g颜色为:%x ,", color.getGreen());
					System.out.printf("b颜色为:%x %n", rgb & 0x000000ff);
					
					
				}
				
				if(e.getButton() == e.BUTTON2) {
					
					flag = true;
					new Thread(new Runnable() {
						
						@Override
						public void run() {
							int targetX = 0, targetY = 0, manX = 0, manY = 0, time = 0;
							double	distance = 0;
							while (flag) {
								
								//检测人偶的位置
								Postion pos = getManPos();
								if(pos == null) {
									System.out.println("未检测到人偶");
									return;
								}
								manX = pos.getX();
								manY= pos.getY();
								System.out.println("检测到人偶的位置为：" + manX + "," + manY);
								WeixinJumpMain.manX = pos.getX();
								
								//检测目标位置
								Map<String, Postion> posMap = getTargetPos(precision);
								Postion posTop = posMap.get("top");
								Postion posLeft = posMap.get("left");
								if(posTop == null || posLeft == null) {
									System.out.println("未检测到目标位置");
									return;
								} 									
								System.out.println("检测到目标方块的最上坐标为：" + posTop.getX() + "," + posTop.getY());
								System.out.println("检测到目标方块的最左侧坐标为：" + posLeft.getX() + "," + posLeft.getY());
								targetX = posTop.getX();
								targetY = posLeft.getY();
								System.out.println("目标实际位置为：" + targetX + "," + targetY);
								//计算距离
								distance = getDistance(manX, manY, targetX, targetY);
								//计算时间
								time = (int) (distance * speech);
								//调用adb命令起跳
								double touchX =530 + Math.random() * 150,
										touchY =1600 + Math.random() * 200;
								try {
									Process process = Runtime.getRuntime().exec(adbCommand + 
											" " + touchX + " " + touchY  +
											" " + (touchX + Math.random() * 10) + " " + (touchY + Math.random() * 13) +
											" " + time);
									process.waitFor();
									process.destroy();
									//延迟1.5-3s后重新加载截图
									Thread.sleep((int)(1500 + Math.random() * 1500));
									screenCap();
									bufferedImage = ImageUtil.getScaleImage(imageUrl, 480, 800);
									myJpanel.setBufferedImage(bufferedImage);
									myJpanel.repaint();
								} catch (Exception e) {
									System.out.println("起跳失败");
									e.printStackTrace();
								}
							}
						}
					}).start();
					
				}
			}
		});
		
	}
	
	
	//得到人偶的位置
	public static Postion getManPos() {
		for(int y=yStart; y<yStop ; y++) {
			for(int x=xStart; x<xStop; x++ ) {
				int c = bufferedImage.getRGB(x, y);
				int[] colorDif = getColorDif(c, 0xff383862);
				if (colorDif[0] < manR && colorDif[1] < manG && colorDif[2] < manB) {
					return new Postion(x, y);
				}
			}
		}
		return null;
	}
	
	//得到目标位置坐标
	public static Map<String, Postion> getTargetPos(int precision) {
		HashMap<String, Postion> map = new HashMap<String, Postion>();
		for(int y=yStart; y<yStop ; y++) {
			for(int x=xStart; x<xStop; x++ ) {
				int c1 = bufferedImage.getRGB(x, y);
				int c2 = bufferedImage.getRGB(x+1, y);
				//System.out.println("色差为");
				if (!compareColor(c1, c2)) {
					//跳过人偶
					if (Math.abs(x - manX) < skipWidth) {
						continue;
					}
					//得到目标方块最上坐标
					map.put("top", new Postion(x+1, y));
					//检测最左侧坐标
					//背景采样
					int bgColor = bufferedImage.getRGB(x + xOffset, y + yOffset);
					System.out.printf("采集到的背景色为：%x%n", bgColor);
					int k = 1;
					int x1 = x;
					int y1 = y;
					
					while (k <= precision) {
						//保证x1,y1与背景色一致
						while(compareColor(bufferedImage.getRGB(x1, y1), bgColor) == false) {
							x1++;
							if (x1 >= bufferedImage.getWidth()) {
								map.put("left", new Postion(x1, y1));
								return map;
							}
						}
						//重置背景色
						//bgColor = bufferedImage.getRGB(x1, y1);
						//与背景色比较，若不同，重置坐标
						boolean dif = compareColor(bgColor, bufferedImage.getRGB(x1, y1 + k));
						if (dif == false) {
							y1 = y1 + k;
							k = 1;
							//x1++;
						} else {
							k++;
						}
					}
					map.put("left", new Postion(x1, y1));
					return map;
				}
			}
		}
		return null;
	}
	//计算色差
	static int[] getColorDif(int c1, int c2) {
		Color color1 = new Color(c1);
		int r1 = color1.getRed();
		int g1 = color1.getGreen();
		int b1 = color1.getBlue();
		
		Color color2 = new Color(c2);
		int r2 = color2.getRed();
		int g2 = color2.getGreen();
		int b2 = color2.getBlue();
		//计算色差
		int r = Math.abs(r1 - r2);
		int g = Math.abs(g1 - g2);
		int b = Math.abs(b1 - b2);
		int[] array = {r, g, b};
		return array;
	}
	//根据色差计算颜色是否一致
	static boolean compareColor(int c1, int c2) {
		int[] a = getColorDif(c1, c2);
		int r = a[0];
		int g = a[1];
		int b = a[2];
		//System.out.println("色差为");
		if (r >= redDiff || g >= greenDiff || b >= buleDiff) {
			return false;
		}
		return true;
	}
	
	
	
	//跳一跳游戏截图到电脑
	public static void screenCap() {
		try {
			Process process = Runtime.getRuntime().exec(adbScreencapCommand + " " + imageUrl);
			process.waitFor();
			if(process.exitValue() == 0) {
				
				System.out.println("adb截图成功");
			}
			process.destroy();
		} catch (Exception e1) {
			System.out.println("adb截图出错");
			e1.printStackTrace();
		}
		
	
	}
	
	//计算两坐标的距离
	static double getDistance(int beginX, int beginY, int endX, int endY) {
		int xDistance = beginX - endX;
		int yDistance = beginY - endY;
		double distance = Math.sqrt( yDistance*yDistance + xDistance*xDistance);
		
		return distance;
	}
}


