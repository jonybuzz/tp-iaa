package entrenamiento;

import java.io.File;
import org.opencv.core.Mat;
import org.opencv.core.Size;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

public class ProcesamientoImagenes {

	public static void pgmToPng() {
				
		String directorioImagenes = Config.getInstance().getConfig("imgPath");

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
		String directorioImagenesPositivasPgm = Config.getInstance().getConfig("imgPosPathgm");
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
			
			Mat imagenBN = Imgcodecs.imread(pathImagenOriginal, 0);

			Mat resize = new Mat();

			Config config = Config.getInstance();
			Double length = Double.parseDouble(config.getConfig("imgWidth"));
			Double width = Double.parseDouble(config.getConfig("imgLength")) ;
			Size sz = new Size(length , width );
			Imgproc.resize(imagenBN, resize, sz);

			//CONVIERTE TANTO los files .png como los .PNG a .pgm
			String filename = directorioSalida.getAbsolutePath() + "\\" + imagenes[i].getName().replaceAll("\\.png", ".pgm").replaceAll("\\.PNG", ".pgm");

			//---------------------------------------------------//
			
			//Guarda los archivos .pgm en la carpeta pgm			
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, resize);
			
		}
	}

}
