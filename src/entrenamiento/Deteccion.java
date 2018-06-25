package entrenamiento;

import java.io.File;
import java.util.Arrays;
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
		CascadeClassifier clasificadorDeOfertas = new CascadeClassifier(Config.getInstance().getConfig("data") + File.separator + "cascade.xml");

		for (File archivoImagen : imagenes) {

			Mat imagenOriginal = Imgcodecs.imread(archivoImagen.getAbsolutePath());
			Mat imagenProcesada = ProcesamientoImagenes.convertirAEscalaGrises(imagenOriginal);
			imagenProcesada = ProcesamientoImagenes.resize(imagenProcesada, 300, 500);

			Rect[] areasConOfertasDetectadas = detectarObjetos(clasificadorDeOfertas, imagenProcesada);
			System.out.println(String.format("Ofertas detectadas en %s: %s", archivoImagen.getName(),
					areasConOfertasDetectadas.length));

			Rect[] areasConOfertasImagenOriginal = extrapolarAreas(imagenOriginal, imagenProcesada,
					areasConOfertasDetectadas);

			recortarAreas(archivoImagen.getName(), imagenOriginal, areasConOfertasImagenOriginal,
					Config.getInstance().getConfig("imgOutputPathPng"));
		}
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
			//Reemplaza extension del original por sufijo con numero de oferta y nueva extension
			String filename = nombreImagenOriginal.replaceAll("(\\..{3}$)", "_oferta" + String.format("%03d", i + 1) + ".png");
			System.out.println(String.format("Guardando %s de %s", (i + 1), areas.length));
			Imgcodecs.imwrite(directorioSalida + File.separator + filename, imagenArea);
		}
	}

}