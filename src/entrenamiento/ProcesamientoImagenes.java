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
			
			Mat resize = transformarColorYMedidas(pathImagenOriginal);

			//CONVIERTE TANTO los files .png como los .PNG a .pgm
			String filename = directorioSalida.getAbsolutePath() + File.pathSeparator + imagenes[i].getName().replaceAll("(\\.PNG|\\.png)", ".pgm");

			//---------------------------------------------------//
			
			//Guarda los archivos .pgm en la carpeta pgm			
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, resize);
			
		}
	}

	private static Mat transformarColorYMedidas(String pathImagenOriginal) {
		
		Mat imagenBN = Imgcodecs.imread(pathImagenOriginal, Imgcodecs.CV_LOAD_IMAGE_GRAYSCALE);
		Config config = Config.getInstance();
		Integer width = Integer.parseInt(config.getConfig("imgWidth"));
		Integer height = Integer.parseInt(config.getConfig("imgHeight")) ;
		Size size = new Size(width , height);
		Mat resize = new Mat();
		Imgproc.resize(imagenBN, resize, size);
		return resize;
	}

}
