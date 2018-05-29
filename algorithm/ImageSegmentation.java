/**
 * @author Titanlbr520
 * @function 图像分割
 */
package algorithm;

import java.awt.image.BufferedImage;

public class ImageSegmentation {

    /*
     * 函数名称：简单阈值分割
     */
    public static BufferedImage threshold(BufferedImage srcImage, int t) {
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        int srcRGBs[] = ImageEnhancement.grayScale(srcImage).getRGB(0, 0, width, height, null, 0, width);
        BufferedImage destImage = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);

        for (int i = 0; i < srcRGBs.length; i++) {
            if ((srcRGBs[i] & 0x000000ff) <= t) {
                srcRGBs[i] = 0xff000000;
            } else {
                srcRGBs[i] = 0xffffffff;
            }

        }

        destImage.setRGB(0, 0, width, height, srcRGBs, 0, width);
        return destImage;

    }


    /*
     * 函数名称：Ostu法
     */
    public static BufferedImage otsu(BufferedImage srcImage) {
        // A:拿到所有需要的数据,首先定义所有数据
        int histInfo[] = null;// 直方图数组
        int UT = 0; // 全图平均灰度值μT
        int Uj1 = 0; // 第一个区间的平均灰度值μ1
        int Uj2 = 0; // 第二个区间的平均灰度值μ2
        double Wj1 = 0.0; // 第一个区间的概率ω1
        double Wj2 = 0.0; // 第二个区间的概率ω2
        int threshold = 0; // 最终确定的阈值

        int temp = 0; // 中间变量
        int temp1 = 0; // 中间变量
        double temp2 = 0; // 中间变量
        double temp3 = 0; // 中间变量
        int width = srcImage.getWidth();
        int height = srcImage.getHeight();
        // ①：利用以前写过的函数对直方图数组其赋值
        histInfo = ImageEnhancement.getHistInfo(srcImage, histInfo);
        // ②：计算平均灰度值
        for (int i = 0; i < histInfo.length; i++) {
            temp = temp + histInfo[i] * i;
        }
        UT = temp / (width * height);
        // B:计算最合适阈值
        temp = 0;
        for (int i = 0; i < histInfo.length; i++) {
            for (int j = 0; j < i; j++) {
                temp = temp + histInfo[j] * j;
                temp1 = temp1 + histInfo[j];
            }
            Uj1 = temp1 == 0 ? 0 : temp / temp1;
            Wj1 = temp1 / width * height;
            temp2 = Wj1 * (Uj1 - UT) * (Uj1 - UT);
            temp = temp1 = 0;
            for (int j = i; j < histInfo.length; j++) {
                temp = temp + histInfo[j] * j;
                temp1 = temp1 + histInfo[j];
            }
            Uj2 = temp1 == 0 ? 0 : temp / temp1;
            Wj2 = temp1 / width * height;
            temp2 = temp2 + Wj2 * (Uj2 - UT) * (Uj2 - UT);
            if (temp3 < temp2) {
                temp3 = temp2;
                threshold = i;
            }
        }
        // C:进行阈值分割
        return threshold(srcImage, threshold);

    }

}
