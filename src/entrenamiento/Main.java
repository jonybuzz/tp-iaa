package entrenamiento;

import org.opencv.core.Core;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if (args.length == 0) {
			mostraMensajeParametros();
		}

		//String action = "entrenamiento";
		switch (args[0]) {
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
		default:
			mostraMensajeParametros();
		}
	}

	private static void mostraMensajeParametros() {
		System.out.println("Indicar accion como parametro: [entrenamiento, detectar, convertirapng, convertirapgm]");
	}
}
