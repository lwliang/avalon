package com.avalon.core.util;

import lombok.extern.slf4j.Slf4j;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/25 12:10
 */
@Slf4j
public class ImageUtils {
    public static byte[] generateAvatar(String name) {
        try {
            // 获取首字母
            String firstLetter = name.substring(0, 1).toUpperCase();

            // 图像的宽度和高度
            int width = 200;
            int height = 200;

            // 创建 BufferedImage
            BufferedImage bufferedImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

            // 获取图形上下文
            Graphics2D graphics = bufferedImage.createGraphics();

            // 设置背景颜色（随机化或固定颜色）
            Color backgroundColor = getRandomColor();
            graphics.setColor(backgroundColor);
            graphics.fillRect(0, 0, width, height);

            // 设置字体和文字颜色
            graphics.setFont(new Font("Arial", Font.BOLD, 100));
            graphics.setColor(Color.WHITE);

            // 绘制字母
            FontMetrics fontMetrics = graphics.getFontMetrics();
            int textWidth = fontMetrics.stringWidth(firstLetter);
            int textHeight = fontMetrics.getHeight();
            int x = (width - textWidth) / 2;
            int y = (height - textHeight) / 2 + fontMetrics.getAscent();
            graphics.drawString(firstLetter, x, y);

            // 释放资源
            graphics.dispose();

            return convertImageToBytes(bufferedImage, "png");
        } catch (Exception e) {
            log.error("生成头像失败", e);
            return null;
        }
    }

    // 随机生成背景颜色
    private static Color getRandomColor() {
        int r = (int) (Math.random() * 256);
        int g = (int) (Math.random() * 256);
        int b = (int) (Math.random() * 256);
        return new Color(r, g, b);
    }

    public static byte[] convertImageToBytes(BufferedImage image, String format) throws IOException {
        // 创建字节数组输出流
        ByteArrayOutputStream baos = new ByteArrayOutputStream();

        // 将 BufferedImage 写入输出流
        ImageIO.write(image, format, baos);

        // 转换为字节数组
        return baos.toByteArray();
    }
}
