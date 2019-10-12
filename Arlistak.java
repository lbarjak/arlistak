package arlistak;

import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.Locale;

public class Arlistak {

	public static final LinkedHashMap<String, ArrayList<String>> HANGZAVAR_NETSOFT_EXPORT = new LinkedHashMap<>();
	public static final LinkedHashMap<String, ArrayList<String>> NETSOFT_ARLISTAK = new LinkedHashMap<>();
	private static String hangzavarNetsoftFile;
	private static ArrayList<String> toFile = new ArrayList<>();

	public static void main(String[] args) throws IOException {
		toFile.add("Termék kód" + ";" + "Árlista" + ";" + "Egységár" + ";" + "Alapár (Nettó)" + ";" + "Árrés %" + ";"
				+ "Kedvezmény %");

		hangzavarNetsoftFile = args[0];
		System.out.println("ver.: 0.0.3");
		System.out.println("Kapott paraméter: " + hangzavarNetsoftFile);
		System.out.println("-------------------------------------------\n");
		new FromCSV().read(hangzavarNetsoftFile);

		int nettoBeszerzesiEgysegarIndex = HANGZAVAR_NETSOFT_EXPORT.get("Termék kód").indexOf("Beszerzési ár (Nettó)");
		int nettoEladasiEgysegarIndex = HANGZAVAR_NETSOFT_EXPORT.get("Termék kód").indexOf("Nettó eladási egységár");
		int termekTipusIndex = HANGZAVAR_NETSOFT_EXPORT.get("Termék kód").indexOf("Termék típus");

		double nettoBeszerzesiEgysegar = 0;
		double nettoEladasiEgysegar = 0;
		String termekTipus = "";
		double torzsvasarlo_2_arany = 0.6;
		double torzsvasarlo_5_nagyker_arany = 0.225;
		double minimumArszorzo = 1.1099;
		double torzsvasarlo_2 = 0;
		double torzsvasarlo_5_nagyker = 0;
		String kedvezmenyTorzsvasarlo_2_Percent = "";
		String kedvezmenyTorzsvasarlo_5_nagykerPercent = "";

		for (String key : HANGZAVAR_NETSOFT_EXPORT.keySet()) {

			if (!HANGZAVAR_NETSOFT_EXPORT.get(key).get(nettoBeszerzesiEgysegarIndex).equals("Beszerzési ár (Nettó)")) {

				nettoBeszerzesiEgysegar = Double.parseDouble(HANGZAVAR_NETSOFT_EXPORT.get(key)
						.get(nettoBeszerzesiEgysegarIndex).replace("\"", "").replace(",", "."));
				nettoEladasiEgysegar = Double.parseDouble(HANGZAVAR_NETSOFT_EXPORT.get(key)
						.get(nettoEladasiEgysegarIndex).replace("\"", "").replace(",", "."));
				termekTipus = HANGZAVAR_NETSOFT_EXPORT.get(key).get(termekTipusIndex);

				if (nettoBeszerzesiEgysegar == 0 && nettoEladasiEgysegar == 0) {
					toFile.add(key + ";" + "Törzsvásárló 2" + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0);
					toFile.add(key + ";" + "Törzsvásárló 5-nagyker" + ";" + 0 + ";" + 0 + ";" + 0 + ";" + 0);
					continue;
				}

				String arresPercent = round((1 - nettoBeszerzesiEgysegar / nettoEladasiEgysegar) * 100);

				if (termekTipus.equals("Termék")) {

					torzsvasarlo_2 = (nettoEladasiEgysegar / minimumArszorzo - nettoBeszerzesiEgysegar)
							* torzsvasarlo_2_arany + nettoBeszerzesiEgysegar * minimumArszorzo;
					torzsvasarlo_5_nagyker = (nettoEladasiEgysegar / minimumArszorzo - nettoBeszerzesiEgysegar)
							* torzsvasarlo_5_nagyker_arany + nettoBeszerzesiEgysegar * minimumArszorzo;
					
					kedvezmenyTorzsvasarlo_2_Percent = round((1 - torzsvasarlo_2 / nettoEladasiEgysegar) * 100);
					kedvezmenyTorzsvasarlo_5_nagykerPercent = round(
							(1 - torzsvasarlo_5_nagyker / nettoEladasiEgysegar) * 100);

					toFile.add(key + ";" + "Törzsvásárló 2" + ";" + round(torzsvasarlo_2) + ";"
							+ round(nettoEladasiEgysegar) + ";" + arresPercent + ";"
							+ kedvezmenyTorzsvasarlo_2_Percent);
					toFile.add(key + ";" + "Törzsvásárló 5-nagyker" + ";" + round(torzsvasarlo_5_nagyker) + ";"
							+ round(nettoEladasiEgysegar) + ";" + arresPercent + ";"
							+ kedvezmenyTorzsvasarlo_5_nagykerPercent);
				} else if (termekTipus.equals("Szolgáltatás")) {
					toFile.add(key + ";" + "Törzsvásárló 2" + ";" + round(nettoEladasiEgysegar) + ";"
							+ round(nettoEladasiEgysegar) + ";" + arresPercent + ";" + 0);
					toFile.add(key + ";" + "Törzsvásárló 5-nagyker" + ";" + round(nettoBeszerzesiEgysegar) + ";"
							+ round(nettoEladasiEgysegar) + ";" + arresPercent + ";" + 0);
				}
			}
		}
		writeToFileCSV();
	}

	private static void writeToFileCSV() {

		String time = new Dates().now();
		FileWriter fw;
		try {
			fw = new FileWriter("arlistak_" + time + ".csv");
			for (String row : toFile) {
				fw.write(row + "\n");
			}
			fw.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static String round(double number) {

		Locale locale = new Locale("hu", "HU");
		String pattern = ".##";
		DecimalFormat decimalFormat = (DecimalFormat) NumberFormat.getNumberInstance(locale);
		decimalFormat.applyPattern(pattern);
		String formatted = decimalFormat.format(number);
		return formatted;
	}
}
