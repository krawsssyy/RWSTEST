package rwsinterview;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.PrintStream;
import java.util.HashMap;
import java.util.Scanner;
import java.util.Collections;
import java.util.LinkedHashMap;
import static java.util.stream.Collectors.*;
import static java.util.Map.Entry.*;

public class Main {
	
	private static HashMap<String, Integer> countWordAppearances(String fileName) throws Exception {
		HashMap<String, Integer> map = new HashMap<String, Integer>();
		File file = new File(fileName);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(file);
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			String[] words = line.split(" ");
			for(String word : words) {
				word = word.toLowerCase();
				if (map.containsKey(word))
					map.replace(word, map.get(word) + 1);
				else
					map.put(word,  1);
			}
		}
		return map;
	}
	
	private static HashMap<String, Integer> descendingSortHashMap(HashMap<String, Integer> map) {
		HashMap<String, Integer> sorted = map.entrySet().stream().sorted(Collections.reverseOrder(comparingByValue())).collect(toMap(e -> e.getKey(), e -> e.getValue(), (e1, e2) -> e2,LinkedHashMap::new));
		return sorted;
	}
	
	private static void jaccardDistanceBetweenSentences(String fileName) throws Exception {
		File file = new File(fileName);
		@SuppressWarnings("resource")
		Scanner sc = new Scanner(file);
		String text = "";
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			text = text + line;
		} 
		String[] sentences = text.split("\\.");
		for(int i = 0; i < sentences.length - 1; i++) {
			for(int j = i + 1; j < sentences.length; j++) {
				int intersectionSize = 0;
				String[] wordsSentence1 = sentences[i].split(" ");
				String[] wordsSentence2 = sentences[j].split(" ");
				for(String word : wordsSentence1)
					for(String word2 : wordsSentence2)
						if(word.toLowerCase().equals(word2.toLowerCase()))
								intersectionSize++;
				float jaccardCoefficient = (float)intersectionSize / (wordsSentence1.length + wordsSentence2.length - intersectionSize);
				float jaccardDistance = 1 -  jaccardCoefficient;
				System.out.println(String.format("Propozitia %d : %s \n Propozitia %d : %s \n Distanta Jaccard dintre ele : %f \n ", i, sentences[i], j, sentences[j], jaccardDistance));
			}
		}
	}
	
	public static void main(String[] args) throws Exception {
		Scanner sc = new Scanner(System.in);
		// fisierele sunt in directorul src
		// fisierul folosit pt punctele A si B = test.txt
		// fisierul folosit pt distanta jaccard si punctul 3 Bonus = jaccard.txt
		// fisierul folosit pt punctul 4 Bonus = big.txt
		System.out.print("Introduceti calea absoluta catre fisier: ");
		try {
			String fileName = sc.next();
			HashMap<String, Integer> map = countWordAppearances(fileName);
			File outputFile = new File("output.txt");
			FileWriter fw = new FileWriter("output.txt");
			fw.write("------------------Punctul A------------------\n");
			for(String key : map.keySet())
				fw.write(String.format("%s=%d\n", key, map.get(key)));
			fw.write("------------------Punctul B------------------\n");
			HashMap<String, Integer> descendingMap = descendingSortHashMap(map);
			for(String key : descendingMap.keySet())
				fw.write(String.format("%s=%d\n", key, descendingMap.get(key)));
			System.out.print("Introduceti calea absoluta catre fisierul necesar calcularii distantei Jaccard: ");
			String fileNameJaccard = sc.next();
			System.out.print("Introduceti calea absoluta catre fisierul mare: ");
			String fileNameBig = sc.next();
			fw.write("------------------Punctul C------------------\n");
			PrintStream consolePrintStream = System.out;
			FileOutputStream outputFileStream = new FileOutputStream(outputFile);
			PrintStream filePrintStream = new PrintStream(outputFileStream);
			System.setOut(filePrintStream);
			jaccardDistanceBetweenSentences(fileNameJaccard);
			System.out.println("------------------Punctul 3(BONUS)------------------");
			System.out.println(String.format("his=%d", countWordAppearances(fileNameJaccard).get("his")));
			System.out.println("------------------Punctul 4(BONUS)------------------");
			System.out.println(String.format("was=%d", countWordAppearances(fileNameBig).get("was")));
			System.setOut(consolePrintStream);
			System.out.println("Program incheiat cu succes!");
			fw.close();
			sc.close();
		}
		catch(Exception e) {
			System.out.println("S-a produs o eroare. Va rugam revizuiti datele introduse si reincercati!");
		}
		
		
	}

}
