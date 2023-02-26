package progetto_ingegneriaSW;

import java.util.ArrayList;
import java.util.List;

import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgproc.Imgproc;
import org.opencv.video.BackgroundSubtractorMOG2;

public class HandDetection {
	Mat src;
	Mat grayscale = new Mat();
	Mat fgMask = new Mat();
	Mat binary;
	MatOfPoint biggestContours = new MatOfPoint();
	
	
	public HandDetection(Mat input) {
		src = input;
		binary = new Mat(src.rows(), src.cols(), CvType.CV_8UC1, new Scalar(0, 0, 0));
	}
	
	
	public void grayscaleImg() {		
		//medianBlur(src, src, 7);
        Imgproc.cvtColor(src, grayscale, Imgproc.COLOR_BGR2GRAY); //RGB to grayscale
        //HighGui.imshow("grayscale", grayscale);
	}
	
	
	
	public void imgProcessing() {
        filterImage(fgMask); //do filtering operations
        //HighGui.imshow("img filtrata", fgMask);
        
        Imgproc.GaussianBlur(fgMask, fgMask, new Size(3,3), 0);
        //HighGui.imshow("img filtrata", fgMask);
        
        getBinaryImage(fgMask, binary); //binary image
        //HighGui.imshow("binary", binary);
        
        //detect all contours
        List<MatOfPoint> contours = new ArrayList<>();
        Mat hierarchy = new Mat(); //not used
        Imgproc.findContours(binary, contours, hierarchy, Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_SIMPLE);
        
        //find biggest contours
        int biggestContourIndex = 0;
        int maxContourArea = -1000;
        MatOfPoint2f tmp;
        //MatOfPoint biggestContours = new MatOfPoint();
        for (int i = 0; i < contours.size(); i++) {       
            if (Imgproc.contourArea(contours.get(i)) > maxContourArea) {
                maxContourArea = (int) Imgproc.contourArea(contours.get(i));
                biggestContourIndex = i;
                biggestContours = contours.get(i); //extract from all contour list only biggest contours (hand)
            }
        }
        // show biggest contour
        Mat imgBiggestContour = new Mat(binary.rows(), binary.cols(), CvType.CV_8UC1, new Scalar(0, 0, 0));
        Imgproc.drawContours(imgBiggestContour, contours, biggestContourIndex, new Scalar(255, 255, 255), 2, 8, new Mat(), 0, new Point());
        HighGui.imshow("maxContour", imgBiggestContour);
	}
	
	
	
	static void filterImage(Mat img) {
		Mat erodeElement  = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(3, 3));
		Mat dilateElement = Imgproc.getStructuringElement(Imgproc.MORPH_RECT, new Size(10, 10));
		
		//delete noise points from image to better identify the hand
		Imgproc.erode(img, img, erodeElement);
		Imgproc.dilate(img, img, dilateElement);
		Imgproc.dilate(img, img, dilateElement);
	}
	
	
	
	static final int binaryImageSectionCount = 8;
	static void getBinaryImage(Mat grayImage, Mat binaryImage) {
		// get every partial gray image's height
		int partImageHeight = grayImage.rows() / binaryImageSectionCount;
		for (int i = 0; i < binaryImageSectionCount; i++) {
			Mat partialGrayImage = new Mat();            
	        Mat partialBinaryImage = new Mat();
	        Rect partialRect;
	        if (i != binaryImageSectionCount - 1) {
	        	// if it's not last piece, Rect's height should be partImageHeight
	            partialRect = new Rect(0, i * partImageHeight, grayImage.cols(), partImageHeight);
	        } else {
	        	// if it's last piece, Rect's height should be (grayImage.rows - i  * partImageHeight)
	            partialRect = new Rect(0, i * partImageHeight, grayImage.cols(), grayImage.rows() - i  * partImageHeight);
	        }
	        
	        Mat partialResource = grayImage.submat(partialRect);
	        partialResource.copyTo(partialGrayImage);
	        Imgproc.threshold(partialGrayImage, partialBinaryImage, 0, 255, Imgproc.THRESH_OTSU);
	        // combine partial binary image to one piece
	        partialBinaryImage.copyTo(binaryImage.submat(partialRect));
		}
		
	    return;
	}
	/*
	public boolean prova(Ball ball) {
		prova = new MatOfPoint2f(biggestContours.toArray());
		if (Imgproc.pointPolygonTest(prova, ball.position, false) >= 0) {
			return true;
		} else {
			return false;
		}
	}*/
}
