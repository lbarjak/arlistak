package arlistak;

import java.io.IOException;
import java.util.ArrayList;
import java.util.LinkedHashMap;

public class Arlistak {

	public static final LinkedHashMap<String, ArrayList<String>> HANGZAVAR_NETSOFT_EXPORT = new LinkedHashMap<>();
	private static String hangzavarNetsoftFile;

	public static void main(String[] args) throws IOException {

		hangzavarNetsoftFile = args[0];
		System.out.println("ver.: 0.0.1");
		System.out.println("Kapott paraméter: " + hangzavarNetsoftFile);
		System.out.println("-------------------------------------------\n");
		new FromCSV().read(hangzavarNetsoftFile);

		int nettoBeszerzesiEgysegarIndex = HANGZAVAR_NETSOFT_EXPORT.get("Termék kód").indexOf("Beszerzési ár (Nettó)");
		int nettoEladasiEgysegarIndex = HANGZAVAR_NETSOFT_EXPORT.get("Termék kód").indexOf("Nettó eladási egységár");
		int termekTipusIndex = HANGZAVAR_NETSOFT_EXPORT.get("Termék kód").indexOf("Termék típus");

		double nettoBeszerzesiEgysegar = 0;
		double nettoEladasiEgysegar = 0;
		String termekTipus = "";

		for (String key : HANGZAVAR_NETSOFT_EXPORT.keySet()) {
			if (!HANGZAVAR_NETSOFT_EXPORT.get(key).get(nettoBeszerzesiEgysegarIndex).equals("Beszerzési ár (Nettó)")) {
				nettoBeszerzesiEgysegar = Double.parseDouble(HANGZAVAR_NETSOFT_EXPORT.get(key)
						.get(nettoBeszerzesiEgysegarIndex).replace("\"", "").replace(",", "."));
				nettoEladasiEgysegar = Double.parseDouble(HANGZAVAR_NETSOFT_EXPORT.get(key)
						.get(nettoEladasiEgysegarIndex).replace("\"", "").replace(",", "."));
				termekTipus = HANGZAVAR_NETSOFT_EXPORT.get(key).get(termekTipusIndex);
				System.out.println(nettoBeszerzesiEgysegar);
				System.out.println(nettoEladasiEgysegar);
				System.out.println(termekTipus);
			}
		}

	}
	// return bekerules / minimum_arszorzo - net_eladasi) * torzsvasarlo2_arany +
	// bekerules * min_arszorzo;
	//
	//minimum_arszorzo=1,1099
	//Schetl László, [11.10.19 11:33]
	//torzsvasarlo_2_arany=0,6
	//torzsvasarlo_5-nagyker_arany=0,225
}
