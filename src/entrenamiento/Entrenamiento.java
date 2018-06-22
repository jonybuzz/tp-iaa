package entrenamiento;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Entrenamiento {
	
	public static void run() {

		System.out.println("Generando ofertas.info");
		String directorioPgmPositivas = Config.getInstance().getConfig("imgPosPath") + "/pgm";
		
		File[] imagenes = new File(directorioPgmPositivas).listFiles();
		
		for (int i = 0; i < imagenes.length; i++) {
			Mat imagen = Imgcodecs.imread(imagenes[i].getAbsolutePath());
			
			int ancho = imagen.width();
			int alto = imagen.height();

			String filename = "/recursos/ofertas.info";
			Imgcodecs.imwrite(filename, imagen);
		}
	}


}
