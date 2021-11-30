import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;	
public class Juego {
public static void main(String[] args){
	
	
	while (run()==true) {
	}
	

}
public static boolean run(){
	int cont = 0;
	Scanner in= new Scanner(System.in);
	File diccionario = new File("C:\\Users\\marce\\eclipse-workspace\\JuegoPalabrasPrueba\\src\\Diccionario.txt");
	Scanner dc;
	try {
		dc = new Scanner(diccionario);	
		String[] palabra = new String[106];
		while (dc.hasNextLine()) {
		palabra[cont] = dc.nextLine();
		cont++;
		
		}
		int rand = 13;
		rand = (int)(rand*Math.random());
		String palabra2= in.next();
		System.out.println(palabra[rand]);
		if(palabra[rand].equals(palabra2)) {
			System.out.println("has ganado");
		}else {
			System.out.println("has fallado");
		}
		
		dc.close();
	} catch (FileNotFoundException e) {	
		e.printStackTrace();
	}
	System.out.println("Quieres seguir jugando?");
	boolean opc = in.nextBoolean();
	in.close();
	return opc;
}
}
