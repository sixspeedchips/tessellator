package io.libsoft.tessel.view;


import io.libsoft.tessel.util.Props;
import java.io.ByteArrayInputStream;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javax.swing.ImageIcon;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;


public class CannyViewer extends ImageView {


  private static final int RATIO = 10;
  private static final int KERNEL_SIZE = 3;
  private static final Size BLUR_SIZE = new Size(2, 2);
  private final int lowThresh = 20;
  private final int highThresh = 60;
  private final MatOfByte byteMat = new MatOfByte();
  private final MatOfByte resized = new MatOfByte();
  private final MatOfByte cannyFiltered = new MatOfByte();
  private final String imagePath = "E:\\Projects\\java\\tessellator\\src\\main\\resources\\Untitled-1.jpg";
  private final Mat srcBlur = new Mat();
  private final Mat detectedEdges = new Mat();
  private final ScheduledExecutorService executorService = Executors.newSingleThreadScheduledExecutor();
  private Mat dst = new Mat();


  public void drawOriginal() {
    Mat raw = Imgcodecs.imread(imagePath,
        Imgcodecs.CV_LOAD_IMAGE_COLOR);
    Imgproc.resize(raw, resized, new Size(raw.size().width / 2, raw.size().height / 2), 0, 0,
        Imgproc.INTER_AREA);
    update(resized);
  }

  private void update(Mat src) {
    Imgproc.blur(src, srcBlur, BLUR_SIZE);
    Imgproc.Canny(srcBlur, detectedEdges, lowThresh, highThresh, KERNEL_SIZE, false);
    dst = new Mat(src.size(), CvType.CV_8UC3, Scalar.all(0));
    src.copyTo(dst, detectedEdges);
    setImage(getImageFromMat(dst));
  }


  private Image getImageFromMat(Mat mat) {
    Imgcodecs.imencode(".bmp", mat, byteMat);
    return new Image(new ByteArrayInputStream(byteMat.toArray()));
  }
}
