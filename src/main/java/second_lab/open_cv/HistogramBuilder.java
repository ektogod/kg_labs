package second_lab.open_cv;

import org.opencv.core.*;
import org.opencv.imgproc.Imgproc;
import org.springframework.stereotype.Component;

@Component
public class HistogramBuilder {
    public Mat buildHistogram(Mat origMat) {
        Mat hist = new Mat();
        MatOfInt histSize = new MatOfInt(256);
        MatOfFloat histRange = new MatOfFloat(0, 256);

        Imgproc.calcHist(java.util.Collections.singletonList(origMat),
                new MatOfInt(0),
                new Mat(),
                hist,
                histSize,
                histRange);

        Core.normalize(hist, hist, 0, 255, Core.NORM_MINMAX);

        int histWidth = 512;
        int histHeight = 400;
        Mat histImage = new Mat(histHeight, histWidth, org.opencv.core.CvType.CV_8UC3, new Scalar(0, 0, 0));

        long binWidth = Math.round((float) histWidth / histSize.get(0, 0)[0]);

        for (int i = 0; i < histSize.get(0, 0)[0]; i++) {
            double binValue = hist.get(i, 0)[0];
            int y = (int) Math.round(binValue);

            Imgproc.rectangle(histImage, new Point(i * binWidth, histHeight),
                    new Point((i + 1) * binWidth - 1, histHeight - y),
                    new Scalar(0, 0, 255), -1);
        }

        return histImage;
    }

    public Mat equalizeHistogram(Mat origMat){
        PhotoHandler handler = new PhotoHandler();
        return buildHistogram(handler.equalizeImage(origMat));
    }

    public Mat linBuildHistogram(Mat mat, double alpha, double beta){
        ContrastEnhancer enhancer = new ContrastEnhancer();
        return buildHistogram(enhancer.enhanceContrast(mat, alpha, beta));
    }
}
