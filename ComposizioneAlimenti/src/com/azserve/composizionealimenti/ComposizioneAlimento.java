package com.azserve.composizionealimenti;

import java.io.BufferedWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ComposizioneAlimento {
	private static final String CAPITOLO_PREFIX = "***";
	private final static String PRIMO_CAPITOLO = "COMPONENTI PRINCIPALI";
	private final String linkAlimento;
	private final String filePathRoot;

	public ComposizioneAlimento(final String linkAlimento,
			final String filePathRoot) {
		this.linkAlimento = linkAlimento;
		this.filePathRoot = filePathRoot;
	}

	public void run() {
		try {
			List<String> ll = new ArrayList<String>();

			Document doc = Jsoup.connect(linkAlimento).get();

			Element titolo = doc.select(".titolo").first();
			String titoloCompleto = titolo.text();
			System.out.println(titoloCompleto);
			String codice = extractCodice(titoloCompleto);
			String descrizione = titoloCompleto.substring(codice.length() + 1)
					.trim().replaceAll("[^\\w\\s-]", "");

			Element tabellaComponenti = doc.select("table[id=tblComponenti]")
					.first();
			Elements trs = tabellaComponenti.select(">tbody>tr");

			trs.forEach(tr -> {
				StringBuffer riga = new StringBuffer();

				Elements tds = tr.select(">td");
				int i = 0;
				boolean isCapitolo=false;
				for (Element td : tds) {
					if (++i > 2)
						break;
					String valoreColonna = td.text();
					if(td.className().equals("testobold")){
						valoreColonna=CAPITOLO_PREFIX+valoreColonna;
						isCapitolo = true;
					}
					valoreColonna.replaceAll("\"", "").replaceAll("\\|", "");
					riga.append(valoreColonna);
					riga.append("|");
				}
				String line = riga.toString();
				line = line.substring(0, line.length() - 1);
				if(!isCapitolo && (line.indexOf("|")<0))
					line +="|";
				ll.add(line);
			});

			outputToFile(codice, descrizione, ll);
		} catch (IOException e) {
			e.printStackTrace();
		}

	}

	private String extractCodice(String titoloCompleto) {
		Pattern p = Pattern.compile("[0-9]+");
		Matcher m = p.matcher(titoloCompleto);
		return m.find() ? m.group() : "";
	}

	private void outputToFile(String codice, String descrizione, List<String> ll) {
		// TODO Auto-generated method stub
		String s = "";
		int i = -1;
		while (!s.startsWith(CAPITOLO_PREFIX)) {
			++i;
			s = ll.get(i);
		}
		String capitolo = PRIMO_CAPITOLO;
		Path path = Paths.get(filePathRoot + codice + ".txt");
		try (BufferedWriter writer = Files.newBufferedWriter(path)) {
			for (int j = i; j < ll.size(); j++) {
				String line = ll.get(j);
				if (line.startsWith(CAPITOLO_PREFIX))
					capitolo = line.substring(CAPITOLO_PREFIX.length());
				else
					writer.write(codice + "|" + descrizione + "|" + capitolo
							+ "|" + line + System.lineSeparator());
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}
