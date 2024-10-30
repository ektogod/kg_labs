package second_lab.utils;

import org.opencv.core.CvType;
import org.opencv.core.Mat;

import javax.imageio.ImageIO;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.concurrent.ThreadLocalRandom;

public class ImageUtils {
    public static Mat imgToMat(Image image){
        BufferedImage bufferedImage;

        if (image instanceof BufferedImage) {
            bufferedImage = (BufferedImage) image;
        } else {
            bufferedImage = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
            Graphics g = bufferedImage.getGraphics();
            g.drawImage(image, 0, 0, null);
            g.dispose();
        }

        Mat mat = new Mat(bufferedImage.getHeight(), bufferedImage.getWidth(), CvType.CV_8UC3);

        for (int y = 0; y < bufferedImage.getHeight(); y++) {
            for (int x = 0; x < bufferedImage.getWidth(); x++) {
                int rgb = bufferedImage.getRGB(x, y);
                byte[] data = new byte[]{
                        (byte) ((rgb >> 16) & 0xFF),
                        (byte) ((rgb >> 8) & 0xFF),
                        (byte) (rgb & 0xFF)
                };
                mat.put(y, x, data);
            }
        }
        return mat;
    }

    public static Image matToImg(Mat mat){
        int type = (mat.channels() > 1) ? BufferedImage.TYPE_3BYTE_BGR : BufferedImage.TYPE_BYTE_GRAY;

        BufferedImage image = new BufferedImage(mat.cols(), mat.rows(), type);
        byte[] data = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
        mat.get(0, 0, data);

        return image;
    }

    public static void save(Image img, Path path) throws IOException {
        BufferedImage bufferedImage = new BufferedImage(img.getWidth(null), img.getHeight(null), BufferedImage.TYPE_INT_ARGB);

        Graphics2D bGr = bufferedImage.createGraphics();
        bGr.drawImage(img, 0, 0, null);
        bGr.dispose();

        path = Paths.get(path.toString() + String.format("\\lab_2_image_%d.png", ThreadLocalRandom.current().nextInt(0, 10000)));
        ImageIO.write(bufferedImage, "png", path.toFile());
    }

    private ImageUtils() {
    }
}
