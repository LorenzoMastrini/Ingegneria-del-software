import java.awt.Image;
import java.awt.image.BufferedImage;
import java.awt.image.DataBufferByte;
import java.util.ArrayList;
import java.util.List;

import javax.swing.ImageIcon;

import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;
/*
//only used for drawing purposes
import org.opencv.core.CvType;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
*/



public class ImgProcessor {
	
	public static BufferedImage bufferedImage(Mat m) { //method necessary to display the image in a JFrame
	    int type = BufferedImage.TYPE_BYTE_GRAY;
	    if (m.channels() > 1) {
	        type = BufferedImage.TYPE_3BYTE_BGR;
	    }
	    int bufferSize = m.channels() * m.cols() * m.rows();
		byte [] buffer = new byte[bufferSize];
		m.get(0, 0, buffer); //get all the pixels
		BufferedImage image = new BufferedImage(m.cols(), m.rows(), type);
		final byte[] targetPixels = ((DataBufferByte) image.getRaster().getDataBuffer()).getData();
		System.arraycopy(buffer, 0, targetPixels, 0, buffer.length);
		return image;
	}
	
	
	
	public static Mat filter(Mat input, BackgroundSubtractorMOG2 background_subtractor) {
		Mat mask = new Mat();
		      
		background_subtractor.apply(input, mask); //Get the foreground mask (it is gray scale)
        //Convert the gray scale to black and white with a threshold
        //the 250 threshold can be modified fitting the webcam and needs, setting it lower will make it more sensitive (also to noise)
        Imgproc.threshold(mask, mask, 250, 255, Imgproc.THRESH_BINARY);
        
        return mask;
	}
	
	
	
	public static MatOfPoint contours(Mat mask) {
		//used only for drawing purposes
		//Mat binary = mask;
		//int biggestContourIndex = 0;
		
        //detect all contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat(); //not used
        Imgproc.findContours(mask, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        //find biggest contours
        int maxContourArea = -1000;
        MatOfPoint biggestContours = new MatOfPoint();
        for (int i = 0; i < contours.size(); i++) {       
            if (Imgproc.contourArea(contours.get(i)) > maxContourArea) {
                maxContourArea = (int) Imgproc.contourArea(contours.get(i));
                //biggestContourIndex = i;
                biggestContours = contours.get(i); //extract from all contour list only biggest contours (hand)
            }
        }
/*
        // show biggest contour
        Mat imgBiggestContour = new Mat(binary.rows(), binary.cols(), CvType.CV_8UC1, new Scalar(0, 0, 0));
        Imgproc.drawContours(imgBiggestContour, contours, biggestContourIndex, new Scalar(255, 255, 255), 2, 8, new Mat(), 0, new Point());
        HighGui.imshow("maxContour", imgBiggestContour);
*/
        return biggestContours;
	}
	
	
	
	//used by the ImageIcon loaded from file to correctly resize them
	public static ImageIcon scaleImage(ImageIcon icon, int w, int h) {
        int nw = icon.getIconWidth();
        int nh = icon.getIconHeight();

        if(icon.getIconWidth() > w) {
          nw = w;
          nh = (nw * icon.getIconHeight()) / icon.getIconWidth();
        }

        if(nh > h) {
          nh = h;
          nw = (icon.getIconWidth() * nh) / icon.getIconHeight();
        }

        return new ImageIcon(icon.getImage().getScaledInstance(nw, nh, Image.SCALE_DEFAULT));
    }
}
