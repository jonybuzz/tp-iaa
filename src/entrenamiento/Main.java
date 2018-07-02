package entrenamiento;

import org.opencv.core.Core;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String action ;
		if (args.length == 0) {
			action = "all";
		} else {
			action = args[0];
		}

		switch (action) {
		case "entrenamiento":
			Entrenamiento.run();
			break;
		case "detectar":
			Deteccion.run();
			break;
		case "convertirapng":
			ProcesamientoImagenes.pgmToPng();
			break;
		case "convertirapgm":
			ProcesamientoImagenes.pngToPgm();
			break;
		case "all":
			System.out.println("Ejecutando acciones: [convertirapgm, entrenamiento, detectar]");

			long inicioProcImg = System.currentTimeMillis();
			ProcesamientoImagenes.pngToPgm();// convierte los png positivos y negativos a pgm
			long finProcImg = System.currentTimeMillis();
			Entrenamiento.run();// genera los files necesarios para opencv y corre entrenamiento
			long finEntrenamiento = System.currentTimeMillis();
			Deteccion.run();// toma las imagenes de input y genera una salida con un recuadro con el
							// resultado
			long finDeteccion = System.currentTimeMillis();

			System.out.println("#######################");
			System.out.println("# Procesamiento: " + (finProcImg - inicioProcImg) / 1000 + "\"");
			System.out.println("# Entrenamiento: "
					+ ((finEntrenamiento - finProcImg) / 1000 > 60 ? (finEntrenamiento - finProcImg) / 60000 + "min"
							: (finEntrenamiento - finProcImg) / 1000 + "\""));
			System.out.println("# Detección:     " + (finDeteccion - finEntrenamiento) / 1000 + "\"");
			System.out.println("#######################");

			break;
		}

	}

}
