package second_lab.open_cv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;

@Component
public class PhotoHandler {
    public Mat light(Mat mat){
        Mat gray = toGray(mat);

        Mat gradX = new Mat();
        Mat gradY = new Mat();
        Imgproc.Sobel(gray, gradX, CvType.CV_16S, 1, 0);
        Imgproc.Sobel(gray, gradY, CvType.CV_16S, 0, 1);
        Mat absGradX = new Mat();
        Mat absGradY = new Mat();
        Core.convertScaleAbs(gradX, absGradX);
        Core.convertScaleAbs(gradY, absGradY);
        Mat grad = new Mat();
        Core.addWeighted(absGradX, 0.5, absGradY, 0.5, 0, grad);

        return grad;
    }

    public Mat findPoints(Mat src){
        Mat gray = toGray(src);
        MatOfPoint corners = new MatOfPoint();

        Imgproc.goodFeaturesToTrack(gray, corners, 100, 0.01, 10);

        Point[] cornerPoints = corners.toArray();
        for (Point corner : cornerPoints) {
            Imgproc.circle(gray, corner, 5, new Scalar(255, 0, 0), 2);
        }

        return gray;
    }

    public Mat findLines(Mat src){
        Mat gray = toGray(src);

        Mat edges = new Mat();
        Imgproc.Canny(gray, edges, 50, 150);
        Mat lines = new Mat();
        Imgproc.HoughLinesP(edges, lines, 1, Math.PI / 180, 50, 50, 10);
        for (int i = 0; i < lines.rows(); i++) {
            double[] line = lines.get(i, 0);
            Imgproc.line(src, new Point(line[0], line[1]), new Point(line[2], line[3]), new Scalar(0, 255, 0), 2);
        }

        return edges;
    }

    public Mat equalizeImage(Mat origMat){
        Mat yCrCb = new Mat();
        Imgproc.cvtColor(origMat, yCrCb, Imgproc.COLOR_BGR2YCrCb);

        List<Mat> channels = new ArrayList<>();
        Core.split(yCrCb, channels);

        Imgproc.equalizeHist(channels.get(0), channels.get(0));

        Core.merge(channels, yCrCb);

        Mat equalizedImage = new Mat();
        Imgproc.cvtColor(yCrCb, equalizedImage, Imgproc.COLOR_YCrCb2BGR);

        return equalizedImage;
    }

    private Mat toGray(Mat src){
        Mat gray = new Mat();
        Imgproc.cvtColor(src, gray, Imgproc.COLOR_BGR2GRAY);
        return gray;
    }
}
