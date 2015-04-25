package br.cos.ufrj;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Scanner;

/**
 * 
 */

/**
 * @author fabricio
 *
 */
public class SplitTweets {

	private static final String FORMAT_NAME_OUTFILE = "%08d";
	private static final String FORMAT_FILE_TIME = "%s.time";
	private static final String FORMAT_FILE_TEXT = "%s.text";
	private static final String FORMAT_TIMESTAMP = "yyyy-MM-dd HH:mm:ss.S";

	private static final String DATASET_FOLDER = "dataset";
	private static final String INPUT_FOLDER = "input";

	public static void split(final int timeSplit_InMinutes, String... files) {
		for (String inputName : files) {
			File fileInput = new File(DATASET_FOLDER, inputName);

			if (!fileInput.exists()) {
				System.err.println("Aquivo de entrada inexistente!");
				System.exit(0);
			}

			try {
				splitFile(fileInput, timeSplit_InMinutes);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
	}

	private static void splitFile(File fileInput, final int timeSplit_InMinutes) throws Exception {
		Scanner scanner = new Scanner(fileInput);

		FileWriter fileWriterText = null;
		FileWriter fileWriterTime = null;

		int splitCount = 0;
		int lineCount = 0;
		boolean splitNewFile = true;
		Date firstTimestamp = null;

		Exception eout = null;
		try {
			while (scanner.hasNextLine()) {
				String line = scanner.nextLine();
				lineCount++;
				if (Util.isNullOrEmpty(line)) {
					System.err.println(String.format("Entrada totalmente vazia na linha %d!", lineCount));
					continue;
				}
				line = line.trim();
				String[] splitLine = line.split("\",\"", 2);
				String text = splitLine[1].replaceFirst("\"", "");
				if (text.endsWith("\"")) {
					text.substring(0, text.length() - 1);
				}
				String time = splitLine[0].replaceAll("\"", "");
				if (Util.isNullOrEmpty(text) || Util.isNullOrEmpty(time)) {
					System.err.println(String.format("Entrada vazia na linha %d: \"%s\"\t\"%s\"!", lineCount, time, text));
					continue;
				}

				String newLine = "";
				Date timestamp = (new SimpleDateFormat(FORMAT_TIMESTAMP)).parse(time.trim());
				if (firstTimestamp == null) {
					firstTimestamp = loadFirstTimestamp(timestamp);
				} else if (Util.diffDate(firstTimestamp, timestamp, Calendar.MINUTE) >= timeSplit_InMinutes) {
					firstTimestamp = Util.incrementMinute(firstTimestamp, timeSplit_InMinutes);
					splitCount++;
					splitNewFile = true;
				} else {
					newLine = System.lineSeparator();
				}

				if (splitNewFile) {
					newLine = "";
					closeFilesWriter(fileWriterText, fileWriterTime);
					String nameFileOut = String.format(FORMAT_NAME_OUTFILE, splitCount);
					fileWriterText = createNewFileWriter(String.format(FORMAT_FILE_TEXT, nameFileOut));
					fileWriterTime = createNewFileWriter(String.format(FORMAT_FILE_TIME, nameFileOut));
					splitNewFile = false;
				}

				fileWriterText.append(newLine + text.trim());
				fileWriterTime.append(newLine + time.trim());

			}

		} catch (Exception e) {
			eout = e;
		} finally {
			try {
				closeScanners(scanner);
			} catch (Exception e) {
				if (eout == null) {
					eout = e;
				} else {
					e.printStackTrace();
				}
			}
			try {
				closeFilesWriter(fileWriterText, fileWriterTime);
			} catch (Exception e) {
				if (eout == null) {
					eout = e;
				} else {
					e.printStackTrace();
				}
			}
		}

		if (eout != null) {
			throw eout;
		}
	}

	private static FileWriter createNewFileWriter(String fileName) throws IOException {
		File file = new File(INPUT_FOLDER, fileName);
		if (file.exists()) {
			file.delete();
		}
		file.createNewFile();
		return new FileWriter(file);
	}

	private static void closeScanners(Scanner... scanners) throws Exception {
		Exception eout = null;
		for (Scanner scanner : scanners) {
			if (scanner != null) {
				try {
					scanner.close();
				} catch (Exception e) {
					if (eout == null) {
						eout = e;
					} else {
						e.printStackTrace();
					}
				}
			}
		}
		if (eout != null) {
			throw eout;
		}
	}

	private static void closeFilesWriter(FileWriter... filesWriter) throws IOException {
		IOException eout = null;
		for (FileWriter fileWriter : filesWriter) {
			if (fileWriter != null) {
				try {
					fileWriter.flush();
				} catch (IOException e) {
					if (eout == null) {
						eout = e;
					} else {
						e.printStackTrace();
					}
				} finally {
					try {
						fileWriter.close();
					} catch (IOException e) {
						if (eout == null) {
							eout = e;
						} else {
							e.printStackTrace();
						}
					}
				}
			}
		}
		if (eout != null) {
			throw eout;
		}
	}

	private static Date loadFirstTimestamp(Date timestamp) {
		// Calendar calendar = new GregorianCalendar();
		// calendar.setTime(timestamp);
		// return calendar.getTime();
		return (Date) timestamp.clone();
	}

}
