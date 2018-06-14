package entrenamiento;

import org.opencv.core.Core;

public class Main {

	public static void main(String[] args) {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String directorioImagenes = args[1];
		switch (args[0]) {
		case "detectar":
			Deteccion.run(directorioImagenes);
			break;
		case "convertirapng":
			ProcesamientoImagenes.pgmToPng(directorioImagenes);
			break;
		case "convertirapgm":
			ProcesamientoImagenes.pngToPgm(directorioImagenes);
			break;
		}
	}

}
