package me.Micheal.Main;

import java.awt.List;
import java.util.ArrayList;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.osgi.OpenCVInterface;
import org.opencv.osgi.OpenCVNativeLoader;
import org.opencv.video.Video;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class Main {
	

	//Rect objectBoundingRectangle = Rect(0,0,0,0);
	
	// Initial Setup
	public static void main(String[] args){
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		//opencamera();
		//Mat mat = Mat.eye(3, 3, CvType.CV_8UC1);
		//System.out.println(mat);
		findobjects();
	}
	

	public static void findobjects(){
		VideoCapture camera = new VideoCapture(0);
		
		HighGui.namedWindow("Named");
		
		
		while(true){
			Mat Frame1 = new Mat();
			Mat Gry1 = new Mat();
			Mat Frame2 = new Mat();
			Mat Gry2 = new Mat();
			Mat Dif = new Mat();
			camera.read(Frame1);
			Imgproc.cvtColor(Frame1, Gry1, Imgproc.COLOR_BGR2GRAY);
			camera.read(Frame2);
			Imgproc.cvtColor(Frame2, Gry2, Imgproc.COLOR_BGR2GRAY);
			Core.absdiff(Gry1, Gry2, Dif);
			
			
			//threshold functions
			Mat ThreshH = new Mat();
			Imgproc.threshold(Dif,ThreshH,20,255,Imgproc.THRESH_BINARY);
			Size t = new Size(10, 10);
			Imgproc.blur(ThreshH, ThreshH, t);
			
			ArrayList<MatOfPoint> contours = new ArrayList<>();
			Mat hierarchy = new Mat();

			// find contours
			Imgproc.findContours(ThreshH, contours, hierarchy, Imgproc.RETR_CCOMP, Imgproc.CHAIN_APPROX_SIMPLE);

			// if any contour exist...
			if (hierarchy.size().height > 0 && hierarchy.size().width > 0)
			{
			        // for each contour, display it in blue
				
			        for (int idx = 0; idx >= 0; idx = (int) hierarchy.get(0, idx)[0])
			        {
			        	
			        	Scalar a = new Scalar(255,0,0);
			                Imgproc.drawContours(Frame1, contours, idx, new Scalar(250, 0, 0));
			                Point ok = new Point(1,10);
			                	Imgproc.putText(Frame1, "Movement", ok,1,1,a,2);

			        }
			}
			
			HighGui.imshow("Named", Frame1);
			HighGui.waitKey(1);
		}
	}
	public static void opencamera(){
		int CameraI = 0;
		VideoCapture camera = new VideoCapture(CameraI); // Gets first camera.
	//	Mat frame = Imgcodecs.imread(".\\Dep\\bouncingBall.avi");
		
		//camera.open(0);
		//if(!camera.isOpened()){
			
		//}else{
			//Imgproc.resize(frame, frame, new Size(400, frame.size().height * 400 / frame.size().width));
			
			
			//ThreadCapture capThread = new OpencvFirstStep().new ThreadCapture(camera);
		//}
		HighGui.namedWindow("CamWin", HighGui.WINDOW_AUTOSIZE);
		while (true){
			Mat frame = new Mat();// Creates a new mat to store camera image
			camera.read(frame); // Stores camera image to Mat named Frame
			//Mat gryimg = null;
			
			Mat gryimg = new Mat(); //creates a new mate to store a gray image of the camera
			
			// converts the frame image from the camera to grayscale and puts it in a new mat.
			Imgproc.cvtColor(frame, gryimg, Imgproc.COLOR_BGR2GRAY); 
			// Creates a blur extending the gray image...
			Imgproc.blur(gryimg, frame, new Size(3,3));
			//Does canny for the image..
			Imgproc.Canny(frame, frame, 30, 30 * 3, 3, false);
			Mat dest = new Mat();
			Core.add(dest, Scalar.all(0), dest);
			frame.copyTo(dest, frame);
			
			// Shows new window with image...
			HighGui.imshow("CamWin", frame);
			// Waits 1 milasecond before repeating to show the next frame of the video.
			HighGui.waitKey(1);
			
		}
	}
	
}
