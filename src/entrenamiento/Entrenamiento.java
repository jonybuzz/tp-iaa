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
		String directorioData = Config.getInstance().getConfig("data");
		File[] files = new File(directorioData).listFiles();
		for (File file : files) {
			if (!file.isDirectory()) {
				file.delete();
			}
		}
		System.out.println("Datos de entrenamientos anteriores borrados");
	}

	private static void crearInfoFile() {

		String infoFileName = Config.getInstance().getConfig("infoFilename");
		System.out.println("Generando " + infoFileName);

		String directorioPgmPositivas = Config.getInstance().getConfig("imgPosPathPgm");
		File[] imagenesPositivas = new File(directorioPgmPositivas).listFiles();

		String ejemplosPorImagen = "1";
		String imgWidthStart = "0";
		String imgHeightStart = "0";
		// String imgWidth = Config.getInstance().getConfig("imgWidth");
		// String imgHeight = Config.getInstance().getConfig("imgLength");

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(infoFileName))) {

			for (File imagenPositiva : imagenesPositivas) {

				Mat matrizImagen = Imgcodecs.imread(imagenPositiva.getAbsolutePath());
				String imgWidth = Integer.toString(matrizImagen.width());
				String imgHeight = Integer.toString(matrizImagen.height());
				String registroInfo = String.join(" ", imagenPositiva.toString(), ejemplosPorImagen, imgWidthStart,
						imgHeightStart, imgWidth, imgHeight);
				writer.write(registroInfo + "\n");
			}

			System.out.println(infoFileName + " generado con éxito");

		} catch (IOException e) {
			System.out.println(infoFileName + " no pudo ser generado");
			e.printStackTrace();
		}
	}

	private static void crearBGFile() {

		String bgFileName = Config.getInstance().getConfig("bgFilename");
		System.out.println("Generando " + bgFileName);
		String directorioPgmPositivas = Config.getInstance().getConfig("imgNegPathPgm");
		File[] imagenes = new File(directorioPgmPositivas).listFiles();

		try (BufferedWriter writer = new BufferedWriter(new FileWriter(bgFileName, false))) {

			for (int i = 0; i < imagenes.length; i++) {
				writer.write(imagenes[i] + "\n");
			}
			System.out.println(bgFileName + " Generado con éxito");

		} catch (IOException e) {
			System.out.println(bgFileName + " no pudo ser generado");
			e.printStackTrace();
		}
	}

	private static void runOpenCVcreateSamples() {

		System.out.println("Preparando comando createSamples");
		Config config = Config.getInstance();
		String createSamplesPath = config.getConfig("exeCreateSamplesPath");
		String infoPath = config.getConfig("infoFilename");
		String vecFile = config.getConfig("vecFilename");
		String width = config.getConfig("imgWidth");
		String height = config.getConfig("imgHeight");
		String directorioPgmPositivas = config.getConfig("imgPosPathPgm");
		File[] imagenesPos = new File(directorioPgmPositivas).listFiles();
		String cantImagenesPos = String.valueOf(imagenesPos.length);

		String command = String.join(" ", createSamplesPath, "-info", infoPath, "-num", cantImagenesPos, "-w", width,
				"-h", height, "-vec", vecFile);
		executeCommand(command);
		System.out.println("Finaliza script createSamples");
	}

	private static void runOpenCVtraincascade() {

		System.out.println("Preparando comando trainCascade");
		Config config = Config.getInstance();
		String exeTrainCascadePath = config.getConfig("exeTrainCascadePath");
		String bgFile = config.getConfig("bgFilename");
		String vecFile = config.getConfig("vecFilename");
		String nsplits = config.getConfig("nsplits");
		String numStages = config.getConfig("numStages");
		String directorioPgmPositivas = config.getConfig("imgPosPathPgm");
		String directorioPgmNegativas = config.getConfig("imgNegPathPgm");
		String minhitrate = config.getConfig("minhitrate");
		String maxfalsealarm = config.getConfig("maxfalsealarm");
		String width = config.getConfig("imgWidth");
		String height = config.getConfig("imgHeight");
		String data = config.getConfig("data");
		File[] negImagenes = new File(directorioPgmNegativas).listFiles();
		File[] imagenes = new File(directorioPgmPositivas).listFiles();
		int numPos = imagenes.length;
		int numNeg = negImagenes.length;

		String command = exeTrainCascadePath + " -data " + data + " -vec " + vecFile + " -bg " + bgFile + " -numStages "
				+ numStages + " -nsplits " + nsplits + " -numStages " + numStages + " -minhitrate " + minhitrate
				+ " -maxfalsealarm " + maxfalsealarm + " -numPos " + numPos + " -numNeg " + numNeg + " -w " + width
				+ " -h " + height + " -numPos " + numPos;

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

		} catch (Exception e) {
			System.out.println("Error al ejecutar [" + command + "]");
			e.printStackTrace();
		}
		System.out.println(output);
		return output.toString();
	}

}
