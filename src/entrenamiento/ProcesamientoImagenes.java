package entrenamiento;

import java.io.File;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ProcesamientoImagenes {

	public static void pgmToPng(String directorioImagenes) {
		System.out.println("Convirtiendo imagenes PGM a PNG");
				
		File[] imagenes = new File(directorioImagenes).listFiles((archivo, nombre) -> nombre.endsWith("pgm"));

		for (int i = 0; i < imagenes.length; i++) {
			
			String pathImagenOriginal = imagenes[i].getAbsolutePath();

			Mat image = Imgcodecs.imread(pathImagenOriginal);

			String filename = pathImagenOriginal.replaceAll("pgm", "png");
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, image);
		}

	}

	public static void pngToPgm(String directorioImagenes) {
		System.out.println("Convirtiendo imagenes PNG a PGM en blanco y negro");
		
		File[] imagenes = new File(directorioImagenes).listFiles((archivo, nombre) -> nombre.endsWith("png"));

		for (int i = 0; i < imagenes.length; i++) {
			
			String pathImagenOriginal = imagenes[i].getAbsolutePath();
			
			Mat imagenBN = Imgcodecs.imread(pathImagenOriginal, 0);

			Mat resize = new Mat();
			Size sz = new Size(75, 75);
			Imgproc.resize(imagenBN, resize, sz);

			String filename = pathImagenOriginal.replaceAll("png", "pgm");
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, resize);
//			filename = i + ".png";
//			Imgcodecs.imwrite(filename, resize);
		}

	}

}
