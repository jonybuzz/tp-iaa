package entrenamiento;

import org.opencv.core.Core;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
		String action;
		if (args.length == 0) {
			mostraMensajeParametros();
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
			ProcesamientoImagenes.pngToPgm();// convierte los png positivos y negativos a pgm
			Entrenamiento.run();// genera los files necesarios para opencv y corre entrenamiento
			Deteccion.run();// toma las imagenes de input y genera una salida con un recuadro con el
							// resultado
		default:
			mostraMensajeParametros();
		}
	}

	private static void mostraMensajeParametros() {
		System.out.println("Ejecutando acciones: [convertirapgm, entrenamiento, detectar]");
	}
}
