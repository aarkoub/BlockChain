package test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;

public class TestUtils {
	public static byte[] readFile(String path) {
		byte[] content = null;
		try{
			File file = new File(path);
			InputStream insputStream = new FileInputStream(file);
			content = new byte[(int) file.length()];
			insputStream.read(content);
			insputStream.close();
    	}
    	catch(FileNotFoundException e){
    		System.out.println("Fichier introuvable");
    	}
		catch(IOException e){
			System.out.println("Probleme d'entree sortie : "+e.getMessage());
		}
		return content;
	}
}
