package entrenamiento;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStreamReader;

import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Entrenamiento {

	private static Config config = Config.getInstance();

	public static void run() {
		System.out.println("Comienza entrenamiento");
		removeOldDataFiles();
		crearInfoFile();
		crearBGFile();
		runOpenCVcreateSamples();
		runOpenCVtraincascade();
		System.out.println("Entrenamiento Finalizado con éxito");
	}

	private static void removeOldDataFiles() {
		System.out.println("Borrando datos de entrenamientos anteriores");
		String directorioData = config.getProperty("data");
		File[] files = new File(directorioData).listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				file.delete();
			}
		}
		System.out.println("Datos de entrenamientos anteriores borrados");
	}

	private static void crearInfoFile() {

		String infoFileName = config.getProperty("infoFilename");
		System.out.println("Generando " + infoFileName);

		String directorioPgmPositivas = config.getProperty("imgPosPathPgm");
		File[] imagenesPositivas = new File(directorioPgmPositivas).listFiles();

		String ejemplosPorImagen = "1";
		String imgWidthStart = "0";
		String imgHeightStart = "0";

		try (BufferedWriter infoWriter = new BufferedWriter(new FileWriter(infoFileName))) {

			for (File imagenPositiva : imagenesPositivas) {

				Mat matrizImagen = Imgcodecs.imread(imagenPositiva.getAbsolutePath());
				String imgWidth = Integer.toString(matrizImagen.width());
				String imgHeight = Integer.toString(matrizImagen.height());
				String registroInfo = String.join(" ", imagenPositiva.toString(), ejemplosPorImagen, imgWidthStart,
						imgHeightStart, imgWidth, imgHeight);
				infoWriter.write(registroInfo + "\n");
			}

			System.out.println(infoFileName + " generado con éxito");

		} catch (IOException e) {
			System.out.println(infoFileName + " no pudo ser generado");
			e.printStackTrace();
		}
	}

	private static void crearBGFile() {

		String bgFileName = config.getProperty("bgFilename");
		System.out.println("Generando " + bgFileName);
		String directorioPgmPositivas = config.getProperty("imgNegPathPgm");
		File[] imagenes = new File(directorioPgmPositivas).listFiles();

		try (BufferedWriter bgWriter = new BufferedWriter(new FileWriter(bgFileName, false))) {

			for (int i = 0; i < imagenes.length; i++) {
				bgWriter.write(imagenes[i] + "\n");
			}
			System.out.println(bgFileName + " Generado con éxito");

		} catch (IOException e) {
			System.out.println(bgFileName + " no pudo ser generado");
			e.printStackTrace();
		}
	}

	private static void runOpenCVcreateSamples() {

		System.out.println("Preparando comando createSamples");
		String createSamplesPath = config.getProperty("exeCreateSamplesPath");
		String infoPath = config.getProperty("infoFilename");
		String vecFile = config.getProperty("vecFilename");
		String width = config.getProperty("imgWidth");
		String height = config.getProperty("imgHeight");
		String directorioPgmPositivas = config.getProperty("imgPosPathPgm");
		File[] imagenesPos = new File(directorioPgmPositivas).listFiles();
		String cantImagenesPos = String.valueOf(imagenesPos.length);

		String command = String.join(" ", createSamplesPath, "-info", infoPath, "-num", cantImagenesPos, "-w", width,
				"-h", height, "-vec", vecFile);
		executeCommand(command);
		System.out.println("Finaliza script createSamples");
	}

	private static void runOpenCVtraincascade() {

		System.out.println("Preparando comando trainCascade");
		String exeTrainCascadePath = config.getProperty("exeTrainCascadePath");
		String data = config.getProperty("data");
		String vecFile = config.getProperty("vecFilename");
		String bgFile = config.getProperty("bgFilename");
		String numStages = config.getProperty("numStages");
		String nsplits = config.getProperty("nsplits");
		String minHitRate = config.getProperty("minhitrate");
		String maxFalseAlarmRate = config.getProperty("maxfalsealarm");
		File[] imagenesPositivas = new File(config.getProperty("imgPosPathPgm")).listFiles();
		File[] imagenesNegativas = new File(config.getProperty("imgNegPathPgm")).listFiles();
		String numPos = String.valueOf(imagenesPositivas.length);
		String numNeg = String.valueOf(imagenesNegativas.length);
		String width = config.getProperty("imgWidth");
		String height = config.getProperty("imgHeight");

		String command = String.join(" ", exeTrainCascadePath, "-data", data, "-vec", vecFile, "-bg", bgFile,
				"-numStages", numStages, "-nsplits", nsplits, "-minHitRate", minHitRate, "-maxFalseAlarmRate",
				maxFalseAlarmRate, "-numPos", numPos, "-numNeg", numNeg, "-w", width, "-h", height);

		executeCommand(command);
		System.out.println("Finaliza script trainCascade");
	}

	private static String executeCommand(String command) {

		System.out.println("Ejecutando: [" + command + "]");
		StringBuffer output = new StringBuffer();
		Process process;

		try {
			process = Runtime.getRuntime().exec(command);
			BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String line = "";
			while ((line = reader.readLine()) != null) {
				output.append(line + "\n");
			}
			process.waitFor();
			reader.close();

		} catch (Exception e) {
			System.out.println("Error al ejecutar [" + command + "]");
			e.printStackTrace();
		}
		System.out.println(output);
		return output.toString();
	}

}
