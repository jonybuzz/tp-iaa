package entrenamiento;

import java.io.File;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ProcesamientoImagenes {

	private static Config config = Config.getInstance();

	public static void pgmToPng() {

		System.out.println("Convirtiendo imagenes PGM a PNG");
		File[] imagenes = new File(config.getProperty("imgNegPathPgm"))
				.listFiles((archivo, nombre) -> nombre.endsWith("pgm"));

		for (File imagen : imagenes) {
			String pathImagenOriginal = imagen.getAbsolutePath();
			String nuevoNombre = pathImagenOriginal.replaceAll("pgm", "png");
			System.out.println(String.format("Guardando Imagen %s", nuevoNombre));
			Imgcodecs.imwrite(nuevoNombre, Imgcodecs.imread(pathImagenOriginal));
		}
	}

	public static void pngToPgm() {
		System.out.println("Convirtiendo imagenes PNG a PGM en BN y menor tamaño");
		procesarImagenesPng(config.getProperty("imgPosPathPng"), config.getProperty("imgPosPathPgm"));
		procesarImagenesPng(config.getProperty("imgNegPathPng"), config.getProperty("imgNegPathPgm"));
	}

	private static void procesarImagenesPng(String directorioImagenesPng, String directorioImagenesPgm) {
		File[] imagenes = new File(directorioImagenesPng)
				.listFiles((archivo, nombre) -> nombre.toUpperCase().endsWith("PNG"));

		File directorioSalida = new File(directorioImagenesPgm);
		if (!directorioSalida.exists()) {
			directorioSalida.mkdir();
		}

		for (File imagen : imagenes) {
			Mat imagenOriginal = Imgcodecs.imread(imagen.getAbsolutePath());

			Mat imagenBN = convertirAEscalaGrises(imagenOriginal);
			Mat resize = resize(imagenBN, config.getIntProperty("imgWidth"), config.getIntProperty("imgHeight"));

			// CONVIERTE TANTO los files .png como los .PNG a .pgm
			String filename = directorioSalida.getAbsolutePath() + File.separator
					+ imagen.getName().replaceAll("(\\.PNG|\\.png)", ".pgm");

			// ---------------------------------------------------//

			// Guarda los archivos .pgm en la carpeta pgm
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, resize);
		}
	}

	public static Mat resize(Mat imagenOriginal, int pxWidth, int pxHeight) {
		Mat resize = new Mat();
		Imgproc.resize(imagenOriginal, resize, new Size(pxWidth, pxHeight));
		return resize;
	}

	public static Mat convertirAEscalaGrises(Mat matrizImagen) {
		Mat imagenBN = new Mat();
		Imgproc.cvtColor(matrizImagen, imagenBN, Imgproc.COLOR_RGB2GRAY);
		return imagenBN;
	}

}
