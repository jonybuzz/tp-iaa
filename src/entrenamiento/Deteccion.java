package entrenamiento;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Point;
import org.opencv.core.Rect;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;
import org.opencv.imgproc.Imgproc;

public class Deteccion {

	private static final Scalar COLOR_VERDE = new Scalar(0, 255, 0);

	public static void run() {
		
		String directorioImagenes = Config.getInstance().getConfig("imgPath");

		System.out.println("Detectando ofertas en las imagenes del directorio " + directorioImagenes);

		File[] imagenes = new File(directorioImagenes).listFiles();
				
		CascadeClassifier clasificadorDeOfertas = new CascadeClassifier(Config.getInstance().getConfig("cascadePath"));

		for (int i = 0; i < imagenes.length; i++) {
			Mat imagen = Imgcodecs.imread(imagenes[i].getAbsolutePath());

			MatOfRect rectangulosOfertasDetectadas = new MatOfRect();
			clasificadorDeOfertas.detectMultiScale(imagen, rectangulosOfertasDetectadas);

			System.out.println(String.format("Ofertas detectadas en %s: %s", imagenes[i].getName(), rectangulosOfertasDetectadas.toArray().length));

			for (Rect rectangulo : rectangulosOfertasDetectadas.toArray()) {
				Imgproc.rectangle(imagen, new Point(rectangulo.x, rectangulo.y),
						new Point(rectangulo.x + rectangulo.width, rectangulo.y + rectangulo.height), COLOR_VERDE);
			}

			String filename = "oferta-" + i + ".png";
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, imagen);
		}

	}

}