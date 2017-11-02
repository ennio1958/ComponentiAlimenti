package com.azserve.composizionealimenti;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

public class CategorieMerceologiche {
	private final static String FONTE = "C:/documentsennio/battistuzzi/CategorieFrame.html";
	
	public static void main(String[] args) throws IOException {
		CategorieMerceologiche x = new CategorieMerceologiche();
		x.elenco().forEach(r->{
			System.out.println(r);
		});
	}
	
	public List<String> elenco() throws IOException {
		List<String> ris = new ArrayList<String>();
		String raggruppamentoMerceologico="";
		File input = new File(FONTE);
//		Document doc = Jsoup.parse(input, "UTF-8", "");
//		Document doc = Jsoup.parse(input, "ISO-8859-1", "");
//		Document doc = Jsoup.parse(input, "US-ASCII", "");
		Document doc = Jsoup.parse(input, "Windows-1252", "");
		
		
		Element esTable = doc.select("table[id=tblResult]").get(0);
		Elements categorie = esTable.select("tr");
		categorie.remove(0); // titles
		int riga=0;
		for (Element p : categorie) {
			riga++;
			String codice="";
			String descrizione="";
			Elements tds = p.select("td");
			if(tds.size()==1)
				raggruppamentoMerceologico=tds.get(0).text();
			else{
				codice=tds.get(0).text();
				descrizione = tds.get(1).text();
				ris.add(raggruppamentoMerceologico+"|"+codice+"|"+descrizione);
			}
		}
		return ris;
	}
	
	
}
