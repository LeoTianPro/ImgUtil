/**
 * @author Titanlbr520
 * @function 边缘检测
 */
package algorithm;

import util.ProcessMath;

import java.awt.image.BufferedImage;

public class EdgeDetection {

    public static BufferedImage horGradient(BufferedImage srcImage) {
        float kernel[] = new float[]{-1, 0, 1, -2, 0, 2, -1, -0, -1};
        return ProcessMath.convolve(srcImage, kernel);
    }

    public static BufferedImage verGradient(BufferedImage srcImage) {
        float kernel[] = new float[]{-1, -2, -1, 0, 0, 0, 1, 2, 1};
        return ProcessMath.convolve(srcImage, kernel);
    }


    public static BufferedImage canny(BufferedImage srcImage) {
        Canny canny = new Canny();
        canny.setSourceImage(srcImage);
        canny.setThreshold(128);
        canny.setWidGaussianKernel(5);
        canny.process();
        BufferedImage destImage = new BufferedImage(srcImage.getWidth(), srcImage.getHeight(),
                BufferedImage.TYPE_INT_RGB);
        destImage = (BufferedImage) canny.getEdgeImage();
        return destImage;

    }
}