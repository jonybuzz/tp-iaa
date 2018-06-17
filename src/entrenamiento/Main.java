package entrenamiento;

import org.opencv.core.Core;
import java.io.IOException;


public class Main {

	public static void main(String[] args) throws IOException {
	
		//CREE UN SINGLETON CON UN ARCHIVO DE CONFIGURACION
		//SUGIERO AGREGAR PARAMETROS Y PATH EN EL CONFIG FILE 
		Config config = Config.getInstance();
		
		
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	
	//	String action =args[0];	
	//	String directorioImagenes = args[1];

		String current = new java.io.File( "." ).getCanonicalPath();		
		System.out.println(current);
		String directorioImagenes = current+ "/src/recursos/posPnG";
		String action = "convertirapgm";
		
		
		switch (action) {
		case "entrenamiento":
		//	Entrenamiento.run; La idea es que al ejecutarse esto se genere el positive.info negative.bg 
		//	y se corra los .exe con los files en pos y neg
			
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
