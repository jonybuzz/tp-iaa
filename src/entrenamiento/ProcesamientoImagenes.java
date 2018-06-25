package entrenamiento;

import java.io.File;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ProcesamientoImagenes {

	public static void pgmToPng() {
				
		String directorioImagenes = Config.getInstance().getConfig("imgNegPathPgm");

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

	public static void pngToPgm() {
		System.out.println("Convirtiendo imagenes PNG a PGM en blanco y negro");
		String directorioImagenesPositivasPng = Config.getInstance().getConfig("imgPosPathPng");
		String directorioImagenesPositivasPgm = Config.getInstance().getConfig("imgPosPathPgm");
		procesarImagenesPng(directorioImagenesPositivasPng, directorioImagenesPositivasPgm);
		String directorioImagenesNegativasPng = Config.getInstance().getConfig("imgNegPathPng");
		String directorioImagenesNegativasPgm = Config.getInstance().getConfig("imgNegPathPgm");
		procesarImagenesPng(directorioImagenesNegativasPng,directorioImagenesNegativasPgm );
	}

	private static void procesarImagenesPng(String directorioImagenesPng, String directorioImagenesPgm) {
		File[] imagenes = new File(directorioImagenesPng)
				.listFiles((archivo, nombre ) -> nombre.toUpperCase().endsWith("PNG"));
		
		File directorioSalida = new File(directorioImagenesPgm);
		if(!directorioSalida.exists()) {
			directorioSalida.mkdir();
		}
		
		for (int i = 0; i < imagenes.length; i++) {
			
			String pathImagenOriginal = imagenes[i].getAbsolutePath();
			Mat imagenOriginal = Imgcodecs.imread(pathImagenOriginal);

			Mat imagenBN = convertirAEscalaGrises(imagenOriginal);
			Config config = Config.getInstance();
			Mat resize = resize(imagenBN, Integer.parseInt(config.getConfig("imgWidth")), Integer.parseInt(config.getConfig("imgHeight")));

			//CONVIERTE TANTO los files .png como los .PNG a .pgm
			String filename = directorioSalida.getAbsolutePath() + File.separator + imagenes[i].getName().replaceAll("(\\.PNG|\\.png)", ".pgm");

			//---------------------------------------------------//
			
			//Guarda los archivos .pgm en la carpeta pgm			
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, resize);
			
		}
	}

	public static Mat resize(Mat imagenOriginal, int pxWidth, int pxHeight) {
		Size size = new Size(pxWidth , pxHeight);
		Mat resize = new Mat();
		Imgproc.resize(imagenOriginal, resize, size);
		return resize;
	}

	public static Mat convertirAEscalaGrises(Mat matrizImagen) {
		Mat imagenBN = new Mat();
		Imgproc.cvtColor(matrizImagen, imagenBN, Imgproc.COLOR_RGB2GRAY);
		return imagenBN;
	}

}
