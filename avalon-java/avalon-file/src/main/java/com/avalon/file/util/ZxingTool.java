/**
 * @author lwlianghehe@gmail.com
 * @date 2024/11/22
 */

package com.avalon.file.util;

import com.avalon.core.util.ObjectUtils;
import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.geom.AffineTransform;
import java.awt.image.AffineTransformOp;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;


public class ZxingTool {

    private static final int IMAGE_WIDTH = 80;
    private static final int IMAGE_HEIGHT = 80;
    private static final int IMAGE_HALF_WIDTH = IMAGE_WIDTH / 2;
    private static final int FRAME_WIDTH = 2;

    /**
     * 生成条形码
     *
     * @param contents 条形码内容
     * @param width    条形码宽度
     * @param height   条形码高度
     * @return
     */
    public static BufferedImage encodeBarcode(String contents, int width, int height) {
        int codeWidth = 3 + // start guard
                (7 * 6) + // left bars
                5 + // middle guard
                (7 * 6) + // right bars
                3; // end guard
        codeWidth = Math.max(codeWidth, width);
        BufferedImage barcode = null;
        try {
            BitMatrix bitMatrix = new MultiFormatWriter().encode(contents, BarcodeFormat.EAN_13, codeWidth, height, null);
            barcode = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return barcode;
    }

    /**
     * 解析读取条形码
     *
     * @param barcodePath 条形码文件路径
     * @return
     */
    public static String decodeBarcode(String barcodePath) {
        BufferedImage image;
        Result result = null;
        try {
            image = ImageIO.read(new File(barcodePath));
            if (image != null) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                result = new MultiFormatReader().decode(bitmap, null);
            }
            return result.getText();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 解析读取条形码
     *
     * @param content 条形码文件路径
     * @return
     */
    public static String decodeBarcode(byte[] content) {
        BufferedImage image;
        Result result = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(content);
            image = ImageIO.read(bais);
            if (image != null) {
                LuminanceSource source = new BufferedImageLuminanceSource(image);
                BinaryBitmap bitmap = new BinaryBitmap(new HybridBinarizer(source));
                result = new MultiFormatReader().decode(bitmap, null);
            }
            return ObjectUtils.isNotNull(result) ? result.getText() : null;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 生成二维码
     *
     * @param context 二维码内容
     * @param width   二维码图片宽度
     * @param height  二维码图片高度
     * @return
     */
    public static BufferedImage encodeQRcode(String context, int width, int height) {
        BufferedImage qrCode = null;
        try {
            Hashtable hints = new Hashtable();
            hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(context, BarcodeFormat.QR_CODE, width, height, hints);
            qrCode = MatrixToImageWriter.toBufferedImage(bitMatrix);
        } catch (WriterException ex) {
            Logger.getLogger(ZxingTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return qrCode;
    }

    /**
     * 生成带有logo标志的二维码
     *
     * @param context  二维码存储内容
     * @param width    二维码宽度
     * @param height   二维码高度
     * @param logoPath 二维码logo路径
     * @return
     */
    public static BufferedImage encodeLogoQRcode(String context, int width, int height, String logoPath) {
        BufferedImage logoQRcode = null;
        try {
            // 读取Logo图像
            BufferedImage logoImage = scale(logoPath, IMAGE_WIDTH, IMAGE_HEIGHT, true);
            int[][] srcPixels = new int[IMAGE_WIDTH][IMAGE_HEIGHT];
            for (int i = 0; i < logoImage.getWidth(); i++) {
                for (int j = 0; j < logoImage.getHeight(); j++) {
                    srcPixels[i][j] = logoImage.getRGB(i, j);
                }
            }

            Map<EncodeHintType, Object> hint = new HashMap<EncodeHintType, Object>();
            hint.put(EncodeHintType.CHARACTER_SET, "utf-8");
            hint.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.H);

            BitMatrix bitMatrix = new MultiFormatWriter().encode(context, BarcodeFormat.QR_CODE, width, height, hint);

            // 二维矩阵转为一维像素数组
            int halfW = bitMatrix.getWidth() / 2;
            int halfH = bitMatrix.getHeight() / 2;

            int[] pixels = new int[width * height];

            for (int y = 0; y < bitMatrix.getHeight(); y++) {
                for (int x = 0; x < bitMatrix.getWidth(); x++) {
                    // 读取图片
                    if (x > halfW - IMAGE_HALF_WIDTH
                            && x < halfW + IMAGE_HALF_WIDTH
                            && y > halfH - IMAGE_HALF_WIDTH
                            && y < halfH + IMAGE_HALF_WIDTH) {
                        pixels[y * width + x] = srcPixels[x - halfW
                                + IMAGE_HALF_WIDTH][y - halfH + IMAGE_HALF_WIDTH];
                    }
                    // 在图片四周形成边框
                    else if ((x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                            && x < halfW - IMAGE_HALF_WIDTH + FRAME_WIDTH
                            && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                            + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                            || (x > halfW + IMAGE_HALF_WIDTH - FRAME_WIDTH
                            && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                            && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                            + IMAGE_HALF_WIDTH + FRAME_WIDTH)
                            || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                            && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                            && y > halfH - IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                            - IMAGE_HALF_WIDTH + FRAME_WIDTH)
                            || (x > halfW - IMAGE_HALF_WIDTH - FRAME_WIDTH
                            && x < halfW + IMAGE_HALF_WIDTH + FRAME_WIDTH
                            && y > halfH + IMAGE_HALF_WIDTH - FRAME_WIDTH && y < halfH
                            + IMAGE_HALF_WIDTH + FRAME_WIDTH)) {
                        pixels[y * width + x] = 0xfffffff;
                    } else {
                        // 此处可以修改二维码的颜色，可以分别制定二维码和背景的颜色；
                        pixels[y * width + x] = bitMatrix.get(x, y) ? 0xff000000 : 0xfffffff;
                    }
                }
            }
            logoQRcode = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            logoQRcode.getRaster().setDataElements(0, 0, width, height, pixels);
        } catch (WriterException ex) {
            Logger.getLogger(ZxingTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        return logoQRcode;
    }

    /**
     * 解析读取二维码
     *
     * @param qrCodePath 二维码图片路径
     * @return
     */
    public static String decodeQRcode(String qrCodePath) {
        BufferedImage image;
        String qrCodeText = null;
        try {
            image = ImageIO.read(new File(qrCodePath));
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            // 对图像进行解码
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            qrCodeText = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return qrCodeText;
    }

    /**
     * 解析读取二维码
     *
     * @param content 二维码图片路径
     * @return
     */
    public static String decodeQRcode(byte[] content) {
        BufferedImage image;
        String qrCodeText = null;
        try {
            ByteArrayInputStream bais = new ByteArrayInputStream(content);
            image = ImageIO.read(bais);
            LuminanceSource source = new BufferedImageLuminanceSource(image);
            Binarizer binarizer = new HybridBinarizer(source);
            BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
            Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
            hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
            // 对图像进行解码
            Result result = new MultiFormatReader().decode(binaryBitmap, hints);
            qrCodeText = result.getText();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (NotFoundException e) {
            e.printStackTrace();
        }
        return qrCodeText;
    }

    /**
     * 对图片进行缩放
     *
     * @param imgPath   图片路径
     * @param width     图片缩放后的宽度
     * @param height    图片缩放后的高度
     * @param hasFiller 是否补白
     * @return
     */
    public static BufferedImage scale(String imgPath, int width, int height, boolean hasFiller) {
        double ratio = 0.0; // 缩放比例
        File file = new File(imgPath);
        BufferedImage srcImage = null;
        try {
            srcImage = ImageIO.read(file);
        } catch (IOException ex) {
            Logger.getLogger(ZxingTool.class.getName()).log(Level.SEVERE, null, ex);
        }
        Image finalImage;
        finalImage = srcImage.getScaledInstance(width, height, BufferedImage.SCALE_SMOOTH);
        // 计算比例
        if ((srcImage.getHeight() > height) || (srcImage.getWidth() > width)) {
            if (srcImage.getHeight() > srcImage.getWidth()) {
                ratio = (Integer.valueOf(height)).doubleValue() / srcImage.getHeight();
            } else {
                ratio = (Integer.valueOf(width)).doubleValue() / srcImage.getWidth();
            }
            AffineTransformOp op = new AffineTransformOp(AffineTransform.getScaleInstance(ratio, ratio), null);
            finalImage = op.filter(srcImage, null);
        }
        if (hasFiller) {// 补白
            BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
            Graphics2D graphic = image.createGraphics();
            graphic.setColor(Color.white);
            graphic.fillRect(0, 0, width, height);
            if (width == finalImage.getWidth(null)) {
                graphic.drawImage(finalImage, 0, (height - finalImage.getHeight(null)) / 2, finalImage.getWidth(null), finalImage.getHeight(null), Color.white, null);
            } else {
                graphic.drawImage(finalImage, (width - finalImage.getWidth(null)) / 2, 0, finalImage.getWidth(null), finalImage.getHeight(null), Color.white, null);
                graphic.dispose();
                finalImage = image;
            }
        }
        return (BufferedImage) finalImage;
    }
}

