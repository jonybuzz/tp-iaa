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
		
		String directorioImagenes = Config.getInstance().getConfig("imgPath");
		
		System.out.println("Convirtiendo imagenes PNG a PGM en blanco y negro");
		
		File[] imagenes = new File(directorioImagenes).listFiles((archivo, nombre ) -> nombre.toUpperCase().endsWith("PNG")  );

		for (int i = 0; i < imagenes.length; i++) {
			
			String pathImagenOriginal = imagenes[i].getAbsolutePath();
			
			Mat imagenBN = Imgcodecs.imread(pathImagenOriginal, 0);

			Mat resize = new Mat();

			Config config = Config.getInstance();
			Double length = Double.parseDouble(config.getConfig("IMGwidth"));
			Double width = Double.parseDouble(config.getConfig("IMGlength")) ;
			Size sz = new Size(length , width );
			Imgproc.resize(imagenBN, resize, sz);

			//CONVIERTE TANTO los files .png como los .PNG a .pgm
			//Guarda los archivos .pgm en la carpeta pos
			
			String filename = pathImagenOriginal.replaceAll("png", "pgm");
			filename = filename.replaceAll("PNG", "pgm");
			filename = filename.replaceAll("posPng", "pos");
			//---------------------------------------------------//
			
			System.out.println(String.format("Guardando Imagen %s", filename));
			Imgcodecs.imwrite(filename, resize);
			
		}

	}

}
