package com.azserve.composizionealimenti;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ElencoAlimenti {

	private final static String FONTE = "C:/documentsennio/battistuzzi/Alphabetical.html";
	private final static String FILE_PATH = "C:/documentsennio/battistuzzi/alimenti/";

	public static void main(String[] args) throws IOException {
		ElencoAlimenti x = new ElencoAlimenti();
		x.faSchede();
		x.faContenuti();
	}

	private void faSchede() throws IOException {
		elenco().forEach(s -> {
			System.out.println(s);
			ComposizioneAlimento ca = new ComposizioneAlimento(s, FILE_PATH);
			ca.run();
		});
	}

	public void faContenuti() throws IOException {
		elenco().forEach(s -> {
			ComposizioneAlimento ca = new ComposizioneAlimento(s, FILE_PATH);
			System.out.println(ca.datoContenutiIn());
		});
	}

	public List<String> elenco() throws IOException {
		List<String> ris = new ArrayList<String>();
		File input = new File(FONTE);
		Document doc = Jsoup.parse(input, "UTF-8", "");
		Element esTable = doc.select("table[id=tblResult]").get(0);
		Elements alims = esTable.select("tr");
		alims.remove(0); // titles
		for (Element p : alims) {
			Elements fs = p.select("a[href]");
			Element fs0 = fs.get(0);
			String link = fs0.attr("href");
			ris.add(link);
		}
		return ris;
	}
}
