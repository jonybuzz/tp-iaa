package entrenamiento;

import java.io.File;

import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

public class Deteccion {

	public static void run() {

		String directorioImagenes = Config.getInstance().getConfig("imgInputPathPng");
		System.out.println("Detectando ofertas en las imagenes del directorio " + directorioImagenes);
		File[] imagenes = new File(directorioImagenes).listFiles();
		CascadeClassifier clasificadorDeOfertas = new CascadeClassifier(Config.getInstance().getConfig("cascadePath"));

		for (File imagen : imagenes) {
			extraerOfertas(clasificadorDeOfertas, imagen);
		}
	}

	private static void extraerOfertas(CascadeClassifier clasificadorDeOfertas, File imagen) {
		Mat matrizImagen = Imgcodecs.imread(imagen.getAbsolutePath());

		MatOfRect rectangulosOfertasDetectadas = new MatOfRect();
		clasificadorDeOfertas.detectMultiScale(matrizImagen, rectangulosOfertasDetectadas);
		Rect[] arrayRectangulosOfertasDetectadas = rectangulosOfertasDetectadas.toArray();

		System.out.println(String.format("Ofertas detectadas en %s: %s", imagen.getName(),
				arrayRectangulosOfertasDetectadas.length));

		File directorioOfertasDetectadas = new File(Config.getInstance().getConfig("imgOutputPathPng"));
		if (!directorioOfertasDetectadas.exists()) {
			directorioOfertasDetectadas.mkdir();
		}

		for (int i = 0; i < arrayRectangulosOfertasDetectadas.length; i++) {
			Mat matrizOferta = new Mat(matrizImagen, arrayRectangulosOfertasDetectadas[i]);
			String filename = imagen.getName() + "-oferta" + (i + 1) + ".png";
			System.out.println(String.format("Guardando %s de %s", (i + 1), arrayRectangulosOfertasDetectadas.length));
			Imgcodecs.imwrite(directorioOfertasDetectadas + File.pathSeparator + filename, matrizOferta);
		}

	}

}