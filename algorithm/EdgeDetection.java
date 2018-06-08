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

}