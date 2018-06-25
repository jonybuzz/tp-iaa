package entrenamiento;

import java.io.File;
import java.util.Arrays;
import org.opencv.core.Mat;
import org.opencv.core.MatOfRect;
import org.opencv.core.Rect;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.objdetect.CascadeClassifier;

public class Deteccion {

	private static int CANT_FILAS_OFERTAS_POR_CATALOGO = 6;
	private static int CANT_COLUMNAS_OFERTAS_POR_CATALOGO = 4;
	private static Config config = Config.getInstance();

	public static void run() {

		String directorioImagenes = config.getProperty("imgInputPathPng");
		System.out.println("Detectando ofertas en las imagenes del directorio " + directorioImagenes);

		CascadeClassifier clasificadorDeOfertas = new CascadeClassifier(
				config.getProperty("data") + File.separator + "cascade.xml");

		for (File archivoImagen : new File(directorioImagenes).listFiles()) {

			Mat imagenOriginal = Imgcodecs.imread(archivoImagen.getAbsolutePath());
			Mat imagenProcesada = prepararImagen(imagenOriginal);

			Rect[] areasConOfertasDetectadas = detectarObjetos(clasificadorDeOfertas, imagenProcesada);
			System.out.println(String.format("Ofertas detectadas en %s: %s", archivoImagen.getName(),
					areasConOfertasDetectadas.length));

			Rect[] areasConOfertasImagenOriginal = extrapolarAreas(imagenOriginal, imagenProcesada,
					areasConOfertasDetectadas);

			recortarAreas(archivoImagen.getName(), imagenOriginal, areasConOfertasImagenOriginal,
					config.getProperty("imgOutputPathPng"));
		}
	}

	private static Mat prepararImagen(Mat imagenOriginal) {

		Mat imagenProcesada = ProcesamientoImagenes.convertirAEscalaGrises(imagenOriginal);
		int pxWidthOferta = config.getIntProperty("imgWidth");
		int pxHeightOferta = config.getIntProperty("imgHeight");
		imagenProcesada = ProcesamientoImagenes.resize(imagenProcesada,
				pxWidthOferta * CANT_COLUMNAS_OFERTAS_POR_CATALOGO, pxHeightOferta * CANT_FILAS_OFERTAS_POR_CATALOGO);
		return imagenProcesada;
	}

	private static Rect[] detectarObjetos(CascadeClassifier clasificador, Mat imagen) {

		MatOfRect matrizDeAreasConObjetosDetectados = new MatOfRect();
		clasificador.detectMultiScale(imagen, matrizDeAreasConObjetosDetectados);
		return matrizDeAreasConObjetosDetectados.toArray();
	}

	private static Rect[] extrapolarAreas(Mat imagenOriginal, Mat imagenProcesada, Rect[] areas) {

		double widthRatio = imagenOriginal.size().width / imagenProcesada.size().width;
		double heightRatio = imagenOriginal.size().height / imagenProcesada.size().height;
		return Arrays.stream(areas).map(rect -> new Rect((int) (rect.x * widthRatio), (int) (rect.y * heightRatio),
				(int) (rect.width * widthRatio), (int) (rect.height * heightRatio))).toArray(Rect[]::new);
	}

	private static void recortarAreas(String nombreImagenOriginal, Mat imagenOriginal, Rect[] areas,
			String pathSalida) {

		File directorioSalida = new File(pathSalida);
		if (!directorioSalida.exists()) {
			directorioSalida.mkdir();
		}
		for (int i = 0; i < areas.length; i++) {
			Mat imagenArea = new Mat(imagenOriginal, areas[i]);
			// Reemplaza extension del original por sufijo con numero de oferta y nueva
			// extension
			String filename = nombreImagenOriginal.replaceAll("(\\..{3}$)", String.format("_oferta%03d.png", i + 1));
			System.out.println(String.format("Guardando %s de %s", (i + 1), areas.length));
			Imgcodecs.imwrite(directorioSalida + File.separator + filename, imagenArea);
		}
	}

}