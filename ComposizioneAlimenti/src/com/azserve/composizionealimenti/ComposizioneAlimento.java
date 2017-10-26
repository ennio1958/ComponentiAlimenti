package com.azserve.composizionealimenti;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class ComposizioneAlimento {
	private final String linkAlimento;
	private final String filePathRoot;

	public ComposizioneAlimento(final String linkAlimento, final String filePathRoot) {
		// TODO Auto-generated constructor stub
		this.linkAlimento = linkAlimento;
		this.filePathRoot = filePathRoot;
	}

	public void run() {
		try {
			Document doc = Jsoup.connect(linkAlimento).get();
			Element titolo = doc.select(".titolo").first();
			System.out.println(titolo.text());
			String titoloCompleto = titolo.text();
			Pattern p = Pattern.compile("[0-9]+");
			Matcher m = p.matcher(titoloCompleto);
			String codice = "";
			while (m.find()) {
				codice = m.group();
			}

			String descrizione = titoloCompleto.substring(codice.length() + 1).trim().replaceAll("[^\\w\\s-]", "");;
			System.out.println(codice + "::" + descrizione + ".");
			
			Element tabellaComponenti = doc.select("table[id=tblComponenti]").first();
			Elements trs = tabellaComponenti.select(">tbody>tr");
			
			trs.forEach(tr->{
				StringBuffer riga = new StringBuffer();
				Elements tds = tr.select(">td");
				int i=0;
				for(Element td:tds){
					if(++i>2)
						break;
					String valoreColonna= td.text();
					riga.append(valoreColonna);riga.append("|");
				}
//				tds.forEach(td->{
//					String valoreColonna= td.text();
//					riga.append(valoreColonna);riga.append("|");
//				});
				System.out.println(riga.toString());
			});

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
}
