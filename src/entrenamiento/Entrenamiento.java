package entrenamiento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;


public class Entrenamiento {
	
	public static void run() {
		System.out.println("Comienza entrenamiento");
		removeOlddataFiles();
		crearInfoFile();
		crearBGFile();
		runOpenCVcreateSamples();
		runOpenCVtraincascade();
		System.out.println("Entrenamiento Finalizado con éxito");
	}

private static void removeOlddataFiles(){
	System.out.println("Borrando datos de entrenamientos anteriores");
	String directorioData = Config.getInstance().getConfig("data");
	File[] files = new File(directorioData).listFiles();
	for(File file: files) {
	    if (!file.isDirectory()){ 
	        file.delete();}
	}
	System.out.println("Datos de entrenamientos anteriores borrados");
}	
	
private static void crearInfoFile(){

	System.out.println("Generando ofertas.info");
	String directorioPgmPositivas = Config.getInstance().getConfig("imgPosPathPgm");
	File[] imagenes = new File(directorioPgmPositivas).listFiles();
	BufferedWriter writer;
	try {
		writer = new BufferedWriter(new FileWriter(Config.getInstance().getConfig("infoFilename")));
		for (int i = 0; i < imagenes.length; i++) {
			String str =  imagenes[i] +" 1 0 0 "+Config.getInstance().getConfig("imgWidth")+" "+Config.getInstance().getConfig("imgLength");
			writer.write(str+"\n");
		}
		  writer.close();
		  System.out.println("ofertas.info Generado con éxito");
	
	} catch (IOException e) {
		  System.out.println("ofertas.info no pudo ser generado");		
	}	
}

private static void crearBGFile(){

	System.out.println("Generando bg.txt");
	String directorioPgmPositivas = Config.getInstance().getConfig("imgNegPathPgm");
	File[] imagenes = new File(directorioPgmPositivas).listFiles();
	BufferedWriter writer;
	try {
		writer = new BufferedWriter(new FileWriter(Config.getInstance().getConfig("bgFilename")));
		for (int i = 0; i < imagenes.length; i++) {
			writer.write(imagenes[i]+"\n");
		}
		  writer.close();
		  System.out.println("bg.txt Generado con éxito");
	
	} catch (IOException e) {
		  System.out.println("bg.txt no pudo ser generado");		
	}
}

private static void runOpenCVcreateSamples(){
	 
	System.out.println("preparando Comando createSamples");
	Config config = Config.getInstance();
	String createSamplesPath = config.getConfig("exeCreateSamplesPath");
	String infoPath = config.getConfig("infoFilename");
	String vecFile = config.getConfig("vecFilename");
	String width = config.getConfig("imgWidth");
	String height =config.getConfig("imgLength");
	String directorioPgmPositivas = config.getConfig("imgPosPathPgm");
	File[] imagenes = new File(directorioPgmPositivas).listFiles();
	int posFiles = imagenes.length;
	 
	String command = createSamplesPath +  " -info "+ infoPath + " -num "+posFiles+" -w "+width+" -h "+height+" -vec "+vecFile;
	System.out.println("ejecutando " + command );		
	String output = executeCommand(command);
	System.out.println(output);
	System.out.println("Finaliza script createSamples");
	
}

private static void runOpenCVtraincascade(){
	 
	System.out.println("preparando Comando trainCascade");	
	Config config = Config.getInstance();
	String trainCascadePath = config.getConfig("exeTrainCascadePath");
	String bgFile = config.getConfig("bgFilename");
	String vecFile = config.getConfig("vecFilename");
	String nsplits = config.getConfig("nsplits");
	String numStages =config.getConfig("numStages");
	String directorioPgmPositivas = config.getConfig("imgPosPathPgm");
	String directorioPgmNegativas = config.getConfig("imgNegPathPgm");
	String minhitrate =config.getConfig("minhitrate");
	String maxfalsealarm = config.getConfig("maxfalsealarm");
	String width = config.getConfig("imgWidth");
	String height =config.getConfig("imgLength");
	String data = config.getConfig("data");
	File[] negImagenes = new File(directorioPgmNegativas).listFiles();
	File[] imagenes = new File(directorioPgmPositivas).listFiles();
	int numPos  = imagenes.length;
	int numNeg  = negImagenes.length;
	 
	String command = trainCascadePath +  " -data "+ data 
			+" -vec "+vecFile
			+" -bg "+bgFile
			+" -numStages "+numStages
			+" -nsplits "+nsplits
			+" -numStages "+numStages
			+" -minhitrate "+minhitrate 
			+" -maxfalsealarm "+maxfalsealarm
			+" -numPos "+numPos
			+" -numNeg "+numNeg
			+" -w "+width
			+" -h "+height
			+" -numPos "+numPos;
	
	System.out.println("ejecutando " + command );		
	String output = executeCommand(command);
	System.out.println(output);

	System.out.println("Finaliza script trainCascade");
	
}


private static String executeCommand(String command) {

	StringBuffer output = new StringBuffer();
	Process p;
	try {
		p = Runtime.getRuntime().exec(command);
	
		System.out.println("termino de ejecutar comando");
		BufferedReader reader = 
                        new BufferedReader(new InputStreamReader(p.getInputStream()));

                    String line = "";			
		while ((line = reader.readLine())!= null) {
			output.append(line + "\n");	
		}
		
		p.waitFor();
		

	} catch (Exception e) {
		System.out.println("no la ibamos a tener");
		e.printStackTrace();
	}

	return output.toString();

}

}



