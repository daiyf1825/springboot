package com.example.demo.util;

import com.google.common.base.Throwables;
import com.ixiye.common.exception.JsonResponseException;
import lombok.extern.slf4j.Slf4j;
import net.coobird.thumbnailator.Thumbnails;
import org.bytedeco.javacv.FFmpegFrameGrabber;
import org.bytedeco.javacv.Java2DFrameConverter;
import org.icepdf.core.pobjects.Document;
import org.icepdf.core.pobjects.Page;
import org.icepdf.core.util.GraphicsRenderingHints;

import javax.imageio.IIOImage;
import javax.imageio.ImageIO;
import javax.imageio.ImageWriter;
import javax.imageio.stream.ImageOutputStream;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.RenderedImage;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Iterator;

@Slf4j
public class ThumbnailUtil {

    /**
     * pdf生成缩略图
     *
     * @param filepath  pdf
     * @param imagepath 生成缩略图保存的路径
     * @param zoom      缩放比例
     */
    public static void pdfTransfer(String filepath, String imagepath, float zoom) {
        Document document = new Document();
        float rotation = 0f;
        try {
            File saveDir = new File(imagepath.substring(0, imagepath.lastIndexOf("/")));
            log.debug("filePath: {}", saveDir);
            if (!saveDir.exists()) {
                boolean ret = saveDir.mkdirs();
                log.debug("ret: {}", ret);
            }
            document.setFile(filepath);
            Image img = document.getPageImage(0, GraphicsRenderingHints.SCREEN, Page.BOUNDARY_CROPBOX, rotation, zoom);
            Iterator iterator = ImageIO.getImageWritersBySuffix("jpg");
            ImageWriter writer = (ImageWriter) iterator.next();
            File outFile = new File(imagepath);
            FileOutputStream out = new FileOutputStream(outFile);
            ImageOutputStream outImage = ImageIO.createImageOutputStream(out);
            writer.setOutput(outImage);
            writer.write(new IIOImage((RenderedImage) img, null, null));
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonResponseException("生成缩略图异常" + Throwables.getStackTraceAsString(e));
        }
    }

    /**
     * 抓取视频第一帧生成图片
     *
     * @param videoFile 视频路径
     * @param frameFile 生成图片的地址
     */
    public static void fetchFrame(String videoFile, String frameFile) {
//        String videoFile = "/Users/ixiye/Documents/1.mp4";
//        String frameFile = "/Users/ixiye/Documents/5.jpg";
        File saveDir = new File(frameFile.substring(0, frameFile.lastIndexOf("/")));
        if (!saveDir.exists()) {
            saveDir.mkdirs();
        }
        File targetFile = new File(frameFile);
        FFmpegFrameGrabber ff = new FFmpegFrameGrabber(videoFile);
        try {
            ff.start();
            int length = ff.getLengthInFrames();
            int i = 0;
            Frame f = null;
            while (i < length) {
                f = ff.grabFrame();
                if (i > 48 && f.image != null) {
                    break;
                }
                i++;
            }
            int owidth = f.imageWidth;
            int oheight = f.imageHeight;
            // 对截取的帧进行等比例缩放
            int width = 300;
            int height = (int) (((double) width / owidth) * oheight);
            Java2DFrameConverter converter = new Java2DFrameConverter();
            BufferedImage fecthedImage = converter.getBufferedImage(f);
            BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_3BYTE_BGR);
            bi.getGraphics().drawImage(fecthedImage.getScaledInstance(width, height, Image.SCALE_SMOOTH), 0, 0, null);
            ImageIO.write(bi, "jpg", targetFile);
            ff.stop();
        } catch (Exception e) {
            e.printStackTrace();
            throw new JsonResponseException("生成缩略图异常" + Throwables.getStackTraceAsString(e));
        }

    }

    /**
     * 图片生成缩略图
     *
     * @param imgPath   图片地址
     * @param targetPth 生成缩略图的保存的地址
     */
    public static void imageTransfer(String imgPath, String targetPth) {
//        String imgPath = "/Users/ixiye/Pictures/2.png";
        try {
            File saveDir = new File(targetPth.substring(0, targetPth.lastIndexOf("/")));
            if (!saveDir.exists()) {
                saveDir.mkdirs();
            }
            Thumbnails.of(imgPath)
                    .scale(0.45f)
                    .toFile(targetPth);
        } catch (IOException e) {
            e.printStackTrace();
            throw new JsonResponseException("生成缩略图异常" + Throwables.getStackTraceAsString(e));
        }
    }
}
