package com.zdm.test.img;

import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.font.TextAttribute;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.AttributedCharacterIterator;
import java.text.AttributedString;

import javax.imageio.ImageIO;
import javax.swing.ImageIcon;

public class ImgAddText {

	public static void main(String[] a) {

		// ImgAddText.createMark("/home/bill/blank_green_marker.png",
		// "/home/bill/blank_green_marker1.png", "djms179234", null, 1, "", 30);
		// System.out.println(d.createMark("e8.jpg","e81.jpg","",null,
		// 1,"",16));

		try {
			// generate("我喜欢麦当劳",TEXT_FONT,new File("/home/bill/image.png"));
			ImgAddText.createMark("/home/bill/blank_green_marker.png",
					"../../blank_green_marker1.png", "0234", Color.black, "",
					12, "png");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 
	 * @param souchFilePath
	 *            ：源图片路径
	 * @param targetFilePath
	 *            ：生成后的目标图片路径
	 * @param markContent
	 *            :要加的文字
	 * @param markContentColor
	 *            :文字颜色
	 * @param qualNum
	 *            :质量数字
	 * @param fontType
	 *            :字体类型
	 * @param fontSize
	 *            :字体大小
	 * @return
	 * @throws IOException
	 */
	public static void createMark(String souchFilePath, String targetFilePath,
			String markContent, Color markContentColor, String fontType,
			int fontSize, String type) throws IOException {

		ImageIcon imgIcon = new ImageIcon(souchFilePath);
		Image theImg = imgIcon.getImage();
		// Image可以获得 输入图片的信息
		int width = theImg.getWidth(null);
		int height = theImg.getHeight(null);

		BufferedImage bimage = null;
		int mLen = markContent.length();
		if (mLen > 2) {
			bimage = new BufferedImage(23 + (mLen - 2) * 6, height,
					BufferedImage.TYPE_4BYTE_ABGR);

		} else {
			bimage = new BufferedImage(width, height,
					BufferedImage.TYPE_4BYTE_ABGR);
		}
		// 为画出图片的大小

		// 2d 画笔
		Graphics2D g = bimage.createGraphics();
		g.setColor(markContentColor);
		g.setBackground(Color.red);

		// 画出图片-----------------------------------
		if (mLen > 2) {
			g.drawImage(theImg, (mLen - 2) * 3, 0, null);
		} else {
			g.drawImage(theImg, 0, 0, null);
		}

		// --------对要显示的文字进行处理--------------
		AttributedString ats = new AttributedString(markContent);
		Font f = new Font(fontType, Font.BOLD, fontSize);
		ats.addAttribute(TextAttribute.FONT, f, 0, markContent.length());
		AttributedCharacterIterator iter = ats.getIterator();
		// ----------------------

		if (mLen > 2) {
			g.drawString(iter, 0, 15);
		} else {
			g.drawString(iter, 10 - 4 * mLen, 15);
		}

		// 添加水印的文字和设置水印文字出现的内容 ----位置
		g.dispose();// 画笔结束
		try {
			ImageIO.write(bimage, type, new File(targetFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}