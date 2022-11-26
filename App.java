import java.awt.Image;
import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

import javax.swing.ImageIcon;
import javax.swing.JFrame;
import javax.swing.JLabel;

import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.core.MatOfInt4;
import org.opencv.core.MatOfPoint;
import org.opencv.core.MatOfPoint2f;
import org.opencv.core.Point;
import org.opencv.core.RotatedRect;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.imgproc.Imgproc;
import org.opencv.utils.Converters;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

public class App {
	static { System.loadLibrary(Core.NATIVE_LIBRARY_NAME); }   //Loads OpenCV Library
	
	private JFrame frame;
	private JLabel imageLabel;
	private JFrame frame2;   //per rappresentare i passi intermedi della codifica dell'immagine
	private JLabel imageLabel2;
	
	
	
	public static void main(String[] args) {
		App app = new App();
		app.initGUI();
		app.runMainLoop(args, app);
	}
	
	
	//rimuove eventuali punti e "disturbi" dall'immagine per delineare coorettamente la forma della mano
	public Mat filtroMorfologico(int kd, int ke, Mat immagine) {   //kd: kernel size (dilate()) ; ke: kernel size (erode())
		Mat modifica = new Mat();
		Imgproc.erode(immagine, modifica, Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(ke, ke)));
		Imgproc.dilate(modifica, modifica,  Imgproc.getStructuringElement(Imgproc.MORPH_ELLIPSE, new Size(kd,kd)));
		return modifica;
	}
	
	
	//usa valori specifici del formato HSV per identificare il colore nero del guanto
	public Mat filtroColoreHSV(int h, int s, int v, int h1, int s1, int v1, Mat immagine) {   //HSV: Hue Saturation Lightness
		Mat modifica = new Mat();
		if(immagine != null) {
			Core.inRange(immagine, new Scalar(h, s, v), new Scalar(h1, s1, v1), modifica);
			//Core.inRange(immagine, new Scalar(0,0,0), new Scalar(0,0,0), modifica); prova
		}
		else{
			System.out.println("Errore immagine");
		}
		return modifica;
	}
	
	
	
	public List<MatOfPoint> cercaContorno(Mat originale, Mat immagine, boolean disegna, boolean disegnaTutto, int filtroPixel){
		List<MatOfPoint> contours = new LinkedList<MatOfPoint>();
		List<MatOfPoint> contoursBig = new LinkedList<MatOfPoint>();
		Mat hierarchy = new Mat(); //opzionale; contiene informazioni sulla topologia dell'immagine
		
		//la matrice 'immagine' è l'immagine modificata col trashold dei valori di formato HSV
		Imgproc.findContours(immagine, contours , hierarchy , Imgproc.RETR_EXTERNAL, Imgproc.CHAIN_APPROX_NONE, new Point(0,0));

		for(int i = 0; i < contours.size(); i++) {
			if(contours.get(i).size().height > filtroPixel) { //filtroPixel evita di evidenziare zone più piccole di una certa quantità (450 pixel di altezza) per evitare di evidenziare erroneamente eventuali punti non voluti
				contoursBig.add(contours.get(i));
				if(disegna && !disegnaTutto)
					Imgproc.drawContours(originale, contours, i, new Scalar(0, 255, 0), 2, 8, hierarchy, 0, new Point());
			}

			if(disegnaTutto && !disegna)
				Imgproc.drawContours(originale, contours, i, new Scalar(0, 255, 255), 2, 8, hierarchy, 0, new Point());
		}
		return contoursBig;
	}
	
	
	//crea l'inviluppo convesso dei punti della mano in base ai contorni trovati con cercaContorno(), per poi ricavarne le dita
	public List<Point> inviluppoDifetti(Mat immagine, List<MatOfPoint> contours, boolean disegna, int sogliaProfondita) {
		List<Point> defects = new LinkedList<Point>();

		for(int i = 0; i < contours.size(); i++) {
			MatOfInt hull_ = new MatOfInt();
			MatOfInt4 convexityDefects = new MatOfInt4();
			
			/*
			@SuppressWarnings("unused")
			List<Point> puntiContorno = new LinkedList<Point>();
			puntiContorno = contours.get(i).toList();
			*/

			Imgproc.convexHull(contours.get(i), hull_); //nota: forse serve il terzo parametro boolean clockwise (false per default)

			if (hull_.size().height >= 4) {

				Imgproc.convexityDefects(contours.get(i), hull_, convexityDefects);

				List<Point> pts = new ArrayList<Point>();
				MatOfPoint2f pr = new MatOfPoint2f();
				Converters.Mat_to_vector_Point(contours.get(i), pts);
				//rettangolo
				pr.create((int) (pts.size()), 1, CvType.CV_32S);
				pr.fromList(pts);
				if(pr.height() > 10) {
					RotatedRect r = Imgproc.minAreaRect(pr);
					Point[] rect = new Point[4];
					r.points(rect);
					
					//disegna il rettangolo esterno (che contiene quello ruotato che risulta più piccolo)
					Imgproc.line(immagine, rect[0], rect[1], new Scalar(0, 100, 0), 2);
					Imgproc.line(immagine, rect[0], rect[3], new Scalar(0, 100, 0), 2);
					Imgproc.line(immagine, rect[1], rect[2], new Scalar(0, 100, 0), 2);
					Imgproc.line(immagine, rect[2], rect[3], new Scalar(0, 100, 0), 2);
					//disegna il rettangolo ruotato dinamicamente al movimento della mano
					Imgproc.rectangle(immagine, r.boundingRect().tl(), r.boundingRect().br(), new Scalar(50, 50, 50));				
				}
				//fine rettangolo

				int[] buff = new int[4]; //contiene i convexityDefects "giusti"
				int[] zx = new int[1];
				int[] zxx = new int[1];
				for(int i1 = 0; i1 < hull_.size().height; i1++) {
					if(i1 < hull_.size().height - 1) {
						hull_.get(i1, 0, zx);
						hull_.get(i1 + 1, 0, zxx);
					} else {
						hull_.get(i1, 0, zx);
						hull_.get(0, 0, zxx);
					}
					
					if(disegna) //disegna l'inviluppo della mano
						Imgproc.line(immagine, pts.get(zx[0]), pts.get(zxx[0]), new Scalar(140, 140, 140), 2);
				}


				for(int i1 = 0; i1 < convexityDefects.size().height; i1++) {
					convexityDefects.get(i1, 0, buff);
					if(buff[3] / 256 > sogliaProfondita) { //fa un controllo della profondità per eliminare i punti che probabilmente non sono le dita
						if(pts.get(buff[2]).x > 0 && pts.get(buff[2]).x < 1024 && pts.get(buff[2]).y > 0 && pts.get(buff[2]).y < 768) {
							defects.add(pts.get(buff[2]));
							//Imgproc.circle(immagine, pts.get(buff[2]), 6, new Scalar(0,255,0));
							if(disegna) //disegna i punti delle pieghe tra le dita
								Imgproc.circle(immagine, pts.get(buff[2]), 6, new Scalar(0,255,0));
						}
					}
				}
				
				if (defects.size() < 3) {
					int dim = pts.size();
					Imgproc.circle(immagine, pts.get(0), 3, new Scalar(0, 255, 0), 2);
					Imgproc.circle(immagine, pts.get(0 + dim / 4), 3, new Scalar(0, 255, 0), 2);
					defects.add(pts.get(0));
					defects.add(pts.get(0 + dim / 4));
				}
			}
		}
		return defects;
	}
	
	
	
	private void initGUI() {
		frame = new JFrame("Camera Input Example");  
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame.setSize(1080,720);
		imageLabel = new JLabel(); //per inserire le immagini nel frame
		frame.add(imageLabel);
		frame.setVisible(true);
		
		//per mostrare i passaggi intermedi della "lettura" delle immagini dal sistema
		frame2 = new JFrame("Intermediate pictures");  
		frame2.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);  
		frame2.setSize(1080,720);
		imageLabel2 = new JLabel();
		frame2.add(imageLabel2);
		frame2.setVisible(true);
	}

	
	
	private void runMainLoop(String[] args, App app) {
		ImageProcessor imageProcessor = new ImageProcessor();
		Mat webcamMatImage = new Mat();  
		Image tempImage;
		Image tempImage2;
		VideoCapture capture = new VideoCapture(0);
		capture.set(Videoio.CAP_PROP_FRAME_WIDTH, 1080);
		capture.set(Videoio.CAP_PROP_FRAME_HEIGHT, 720);
		
		Mat modifica = new Mat();

		if(capture.isOpened()) {  
			while (true) {
				List<Point> difetti = new LinkedList<Point>();
				
				capture.read(webcamMatImage);   //legge un frame dalla videocamera
				
				modifica = app.filtroMorfologico(2, 7, app.filtroColoreHSV(0, 0, 0, 180, 255, 40, webcamMatImage));
				
				difetti = app.inviluppoDifetti(webcamMatImage, app.cercaContorno(webcamMatImage, modifica, false, false, 450), true, 5);

				if(!webcamMatImage.empty()) {
					Core.flip(webcamMatImage, webcamMatImage, 1);   //rende l'immagine specchiata
					tempImage = imageProcessor.toBufferedImage(webcamMatImage);
					ImageIcon imageIcon = new ImageIcon(tempImage.getScaledInstance(frame.getWidth(), frame.getHeight(), Image.SCALE_DEFAULT), "Captured video"); //getScaledInstance() ridimensiona l'immagine dinamicamente
					imageLabel.setIcon(imageIcon);
					//frame.pack();  //this will resize the window to fit the image
										
					//per ottenere l'immagine intermedia
					Core.flip(modifica, modifica, 1); //rende l'immagine specchiata
					tempImage2 = imageProcessor.toBufferedImage(modifica);
					ImageIcon imageIcon2 = new ImageIcon(tempImage2.getScaledInstance(frame2.getWidth(), frame2.getHeight(), Image.SCALE_DEFAULT), "Captured modified video");
					imageLabel2.setIcon(imageIcon2);
					//frame2.pack();  //this will resize the window to fit the image
				}  
				else {  
					System.out.println(" -- Frame not captured -- Break!"); 
					break;  
				}
			}  
		}
		else {
			System.out.println("Couldn't open capture.");
		}
		
	}
}