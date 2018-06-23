package entrenamiento;

import org.opencv.core.Core;
import java.io.IOException;

public class Main {

	public static void main(String[] args) throws IOException {

		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

		if (args.length == 0) {
			mostraMensajeParametros();
		}

		
		switch (args[0]) {
		case "entrenamiento":
			Entrenamiento.run();
			// Entrenamiento.run; La idea es que al ejecutarse esto se genere el
			// positive.info negative.bg
			// y se corra los .exe con los files en pos y neg
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
