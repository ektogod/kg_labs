package second_lab.open_cv;

import org.opencv.core.Mat;
import org.springframework.stereotype.Component;

@Component
public class ContrastEnhancer {
    public Mat enhanceContrast(Mat origMat, double alpha, double beta){
        Mat contrastMat = new Mat();
        origMat.convertTo(contrastMat, -1, alpha, beta);
        return contrastMat;
    }
}
