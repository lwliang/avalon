package com.avalon.file.util;

import org.bytedeco.javacpp.BytePointer;
import org.bytedeco.javacpp.IntPointer;
import org.bytedeco.opencv.global.opencv_imgcodecs;
import org.bytedeco.opencv.opencv_core.Mat;

import java.util.Locale;

/**
 * @author lwlianghehe@gmail.com
 * @date 2025/06/07 14:28
 */
public class ImageCompressUtils {
    public static byte[] compressImage(byte[] inputBytes, String format, int quality) {
        Mat imgMat = opencv_imgcodecs.imdecode(new Mat(inputBytes), opencv_imgcodecs.IMREAD_UNCHANGED);
        if (imgMat.empty()) return null;

        String fmt = format.toLowerCase(Locale.ROOT);
        BytePointer ext = new BytePointer(fmt);  // 用 BytePointer
        IntPointer params;
        switch (fmt) {
            case ".jpg":
            case ".jpeg":
                params = new IntPointer(new int[]{opencv_imgcodecs.IMWRITE_JPEG_QUALITY, quality});
                break;
            case ".png":
                params = new IntPointer(new int[]{opencv_imgcodecs.IMWRITE_PNG_COMPRESSION, Math.max(0, Math.min(9, quality))});
                break;
            case ".webp":
                params = new IntPointer(new int[]{opencv_imgcodecs.IMWRITE_WEBP_QUALITY, Math.max(1, Math.min(100, quality))});
                break;
            case ".tiff":
            case ".tif":
                params = new IntPointer(new int[]{opencv_imgcodecs.IMWRITE_TIFF_COMPRESSION, 1});
                break;
            default:
                params = new IntPointer(0); // BMP等无压缩参数
        }

        BytePointer buf = new BytePointer();
        boolean ok = opencv_imgcodecs.imencode(ext, imgMat, buf, params);
        if (!ok) return null;

        byte[] compressed = new byte[(int) buf.limit()];
        buf.get(compressed);

        buf.deallocate();
        imgMat.deallocate();

        return compressed;
    }
}
