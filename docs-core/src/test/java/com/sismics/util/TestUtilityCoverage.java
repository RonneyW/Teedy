package com.sismics.util;

import com.sismics.util.mime.MimeType;
import com.sismics.util.mime.MimeTypeUtil;
import org.junit.Assert;
import org.junit.Test;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.IOException;

/**
 * Additional coverage tests for utility classes.
 */
public class TestUtilityCoverage {
    @Test
    public void testImageUtilBranches() throws IOException {
        Assert.assertNull(ImageUtil.computeGravatar(null));

        BufferedImage binaryImage = new BufferedImage(2, 1, BufferedImage.TYPE_BYTE_BINARY);
        binaryImage.setRGB(0, 0, Color.BLACK.getRGB());
        binaryImage.setRGB(1, 0, Color.WHITE.getRGB());
        Assert.assertTrue(ImageUtil.isBlack(binaryImage, 0, 0));
        Assert.assertFalse(ImageUtil.isBlack(binaryImage, 1, 0));

        BufferedImage rgbImage = new BufferedImage(2, 1, BufferedImage.TYPE_INT_RGB);
        rgbImage.setRGB(0, 0, Color.BLACK.getRGB());
        rgbImage.setRGB(1, 0, Color.WHITE.getRGB());
        Assert.assertTrue(ImageUtil.isBlack(rgbImage, 0, 0));
        Assert.assertFalse(ImageUtil.isBlack(rgbImage, 1, 0));
        Assert.assertFalse(ImageUtil.isBlack(rgbImage, -1, 0));

        BufferedImage alphaImage = new BufferedImage(2, 2, BufferedImage.TYPE_INT_ARGB);
        alphaImage.setRGB(0, 0, new Color(255, 0, 0, 128).getRGB());
        ByteArrayOutputStream alphaOutputStream = new ByteArrayOutputStream();
        ImageUtil.writeJpeg(alphaImage, alphaOutputStream);
        Assert.assertTrue(alphaOutputStream.size() > 0);

        ByteArrayOutputStream rgbOutputStream = new ByteArrayOutputStream();
        ImageUtil.writeJpeg(rgbImage, rgbOutputStream);
        Assert.assertTrue(rgbOutputStream.size() > 0);
    }

    @Test
    public void testMimeTypeFileExtensions() {
        Assert.assertEquals("zip", MimeTypeUtil.getFileExtension(MimeType.APPLICATION_ZIP));
        Assert.assertEquals("gif", MimeTypeUtil.getFileExtension(MimeType.IMAGE_GIF));
        Assert.assertEquals("jpg", MimeTypeUtil.getFileExtension(MimeType.IMAGE_JPEG));
        Assert.assertEquals("png", MimeTypeUtil.getFileExtension(MimeType.IMAGE_PNG));
        Assert.assertEquals("pdf", MimeTypeUtil.getFileExtension(MimeType.APPLICATION_PDF));
        Assert.assertEquals("odt", MimeTypeUtil.getFileExtension(MimeType.OPEN_DOCUMENT_TEXT));
        Assert.assertEquals("docx", MimeTypeUtil.getFileExtension(MimeType.OFFICE_DOCUMENT));
        Assert.assertEquals("txt", MimeTypeUtil.getFileExtension(MimeType.TEXT_PLAIN));
        Assert.assertEquals("csv", MimeTypeUtil.getFileExtension(MimeType.TEXT_CSV));
        Assert.assertEquals("mp4", MimeTypeUtil.getFileExtension(MimeType.VIDEO_MP4));
        Assert.assertEquals("webm", MimeTypeUtil.getFileExtension(MimeType.VIDEO_WEBM));
        Assert.assertEquals("bin", MimeTypeUtil.getFileExtension(MimeType.DEFAULT));
    }
}
