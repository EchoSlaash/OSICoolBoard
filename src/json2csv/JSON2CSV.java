package json2csv;

import java.io.*;
import java.nio.charset.Charset;
import java.util.Scanner;

import org.json.*;

public class JSON2CSV {
	
/** Lit le fichier entierement et le convertit en String avec le charset donne.*/
	
	public static String fileToString(File file, Charset charset) throws IOException {
		  StringBuilder builder = new StringBuilder();
		  InputStream input = new FileInputStream(file);
		  BufferedReader reader = new BufferedReader(new InputStreamReader(input, charset));
		  for(String line = reader.readLine(); line != null; line = reader.readLine()) {
		    builder.append(line);
		  }
		  reader.close();
		  return builder.toString();
		}

// Donne dans un tableau tout les noms des fichiers contenus dans un dossier donné (celui des logs en théorie)
		
	public static File[] logFinder (String path) {
		
		File directory = new File(path);
		File[] files = new File[0];
		
		// Vérification de la validité du chemin d'accés

		if (!directory.exists()) {
			System.out.println("Le chemin d'acc�s sp�cifi� est introuvable.");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
		}
			else if(!directory.isDirectory()) {
				System.out.println("Le chemin d'acc�s sp�cifi� ne correspond pas � un dossier");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
				
				// liste les fichiers

			} else  {
				files = directory.listFiles();

			  }
		return files;
				
	}

	// Ecrit les données des fichiers spécifié dans le tableau "loglist" dans le fichier "file"
	
	public static void logWriter(String path, File[] loglist, File file) throws IOException {
		
		//Vérification de la validité du chemin d'accés

		if (!file.exists()) {
			System.out.println("Le chemin d'acc�s sp�cifi� est introuvable");
			try {
				Thread.sleep(3000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			System.exit(0);
			
		} else if (!file.isFile()) {
				System.out.println("Le chemin d'acc�s sp�cifi� ne correspond pas � un fichier");
				try {
					Thread.sleep(3000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
				System.exit(0);
				
			} else {

				// Ecriture des fichiers
			
				for (int i = 0; i<loglist.length; i++) {
				
					JSONObject JSONlog = new JSONObject(fileToString(new File(path + "/" + loglist[i].getName()), Charset.forName("utf-8"))); // convertit les données d'un fichier en String avec "fileToString" puis en JSONObject
					int temp = JSONlog.getInt("temp"); // Récupération des données dans une variable à partir du JSONObject
					int humidite = JSONlog.getInt("humidite");
					String logstring = temp + "," + humidite + ",";
					System.out.println("Writing " + loglist[i].getName() + " to " + file);
					// Ecriture des données dans le csv 
					BufferedWriter writer = new BufferedWriter(new FileWriter(file, true));
					writer.write(logstring);
					writer.newLine();
					writer.close();
				
			}
				System.out.println("Done !");
				try {
					Thread.sleep(6000);
				} catch (InterruptedException e) {
					e.printStackTrace();
				}
			}
				
	}
		
	public static void main(String[] args) throws IOException {
		
		Scanner sc = new Scanner(System.in);
		System.out.println("Veuillez saisir le chemin du dossier contenant les logs");
		String directorypath = sc.nextLine();
		File[] loglist = logFinder(directorypath);
		System.out.println("Veuillez saisir le chemin du csv de sortie");
		File csv = new File(sc.nextLine());
		sc.close();
		logWriter(directorypath, loglist, csv);
		}
	}