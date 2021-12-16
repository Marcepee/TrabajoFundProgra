import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;

class Lista {
	String[] lista;
	int len;
	int objetivos;
	public Lista(int len) {
	this.lista= new String[len];
	this.len=0;
	this.objetivos=0;
	}
	public void aumentarObj() {
		this.objetivos++;
		this.len++;
	}
	
	public void aumentarLen() {
		this.len++;
	}


}

public class Juego {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		int opc = 1;
		int puntos = 0;
		while (opc == 1) {
			puntos = jugar(in);
			System.out.println("Quieres seguir jugando?\n 1 = Si\n 0 = No");
			opc = in.nextInt();
		}
		in.close();
		System.out.println("Programa terminado con exito");
		System.out.println("Puntos: " + puntos);

	}
	
	public static int jugar(Scanner in) {
		int pos = 0;
		int puntos= 0;
		
		File diccionario = new File("bin\\Diccionario.txt");
		Scanner dc;
		int num_Palabras=0;
		try {
			dc = new Scanner(diccionario);
			String[] lista_Palabras = new String[106];
			while (dc.hasNextLine()) {
				lista_Palabras[pos] = dc.nextLine();
				pos++;
				num_Palabras++;

				}
			int palabraObj= (int) (num_Palabras * Math.random());
			while(lista_Palabras[palabraObj].length()<6) {
				
			palabraObj = (int) (num_Palabras  * Math.random());		
			}
		
			
			System.out.println(lista_Palabras[palabraObj]);
			desordenar(lista_Palabras[palabraObj]);
			
			
			
			String[] hijas= new String[200];
			String[] objetivos= new String[6];
			Lista aciertos= new Lista(200);
			
			
			System.out.println("\n\n\nPalabras hijas:");
			
			int lenHijas=palabrasHijas(lista_Palabras,hijas,lista_Palabras[palabraObj], pos);
			System.out.println("\n" +lenHijas);
			for(int i= 0; i<lenHijas;i++) {
				System.out.println(hijas[i]);
			}
			
			objetivos[0]=lista_Palabras[palabraObj];
			int lenObj=selObj(hijas, objetivos, lenHijas, 6);
			
			System.out.println("\n\n\nPalabras objetivo:");
			
			for(int i= 0; i<6;i++) {
				System.out.println(objetivos[i]);
			}
			while(aciertos.objetivos<lenObj) {
			puntos+=plc_hldr(objetivos,hijas,aciertos,in.next(),lenObj,lenHijas);
				
			
			}
			System.out.println("¡¡Has acertado todas las palabras objetivo!!");
			
			

			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		System.out.println("Pulsa enter para continuar");
		in.nextLine();
		in.nextLine();
		

		
		
		return puntos;
	}
	
	
	

	public static int plc_hldr(String[] objetivos, String[] hijas,Lista aciertos, String entrada, int lenObj, int lenHijas) {
		int puntos=0;
		
		if(contiene(aciertos.lista,entrada,aciertos.len)) {
			System.out.println("Ya has introducido esa palabra");
			
		}else {
			if(contiene(objetivos,entrada,lenObj)) {
				aciertos.lista[aciertos.len]=entrada;
				aciertos.aumentarObj();
				puntos+=3;
				System.out.println("Has encontrado la palabra objetivo: "+entrada);
			}else if(contiene(hijas,entrada,lenHijas)) {
				aciertos.lista[aciertos.len]=entrada;
				aciertos.aumentarLen();
				puntos++;
				System.out.println("Has encontrado la palabra bonus: "+entrada);
			}else {
				System.out.println(entrada+" es incorrecto");
			}
			
		}
		
		return puntos;
	}
	
	public static boolean contiene(String[] objetivos, String entrada, int length) {
		
		boolean acierto=false;
			for(int cont= 0; cont<length;cont++) {
				if (entrada.equals(objetivos[cont])) {
					acierto=true;
					cont=length;
				}
			}
		return acierto;
	}
	
	public static boolean esHija(String palabraAncestro, String palabraHija) { // comprueba que todas las letras de palabra2 //
																		// estan en palabra1, para eso es necesario que las
																		// dos palabras esten ordenadas
		
		char palabraAncestroChar[] = palabraAncestro.toCharArray();
		Arrays.sort(palabraAncestroChar);
		String palabraAncestroOrd = new String(palabraAncestroChar);
		
		
		char palabraHijaChar[] = palabraHija.toCharArray();
		Arrays.sort(palabraHijaChar);
		String palabraHijaOrd = new String(palabraHijaChar);
		
		
		int ind =0;
		for (int cont =0;cont <palabraHijaOrd.length(); cont++) {

			
			if (palabraAncestroOrd.indexOf(palabraHijaOrd.charAt(cont), ind) != -1) { 
				ind = palabraAncestroOrd.indexOf(palabraHijaOrd.charAt(cont), ind);
				ind++;

			} else {
				return false;
			}

			
		}

		return true;
	}
	
	public static int palabrasHijas(String[] lista,String[] hijas, String palabra,int length) {
		int pos= 0;
		for(int cont=0; cont<length; cont++) {
			if(esHija(palabra,lista[cont])) {
				hijas[pos]=lista[cont];
				pos++;
			}
			
		}
		
		return pos;
	}
	
	public static int selObj(String[] hijas, String[] objetivos, int lenhijas,int numObj) {
		int[] dado;
		int random = 0;
		dado = new int[lenhijas];
		for (int cont = 0; cont < lenhijas; cont++) {
			dado[cont] = cont;
		}
		int cont = 1;
		while (cont < numObj) {
			random = (int) (Math.random() * lenhijas);
			if (dado[random] != -1) {
				objetivos[cont]=hijas[random];
				dado[random] = -1;
				cont++;
			}
		}
		return cont;
	}

	public static void desordenar(String palabraObj) {
		int[] dado;
		int random = 0;
		dado = new int[palabraObj.length()];
		for (int cont = 0; cont < palabraObj.length(); cont++) {
			dado[cont] = cont;
		}
		int cont = 0;
		while (cont < palabraObj.length()) {
			random = (int) (Math.random() * palabraObj.length());
			if (dado[random] != -1) {
				System.out.print(palabraObj.charAt(random));
				dado[random] = -1;
				cont++;
			}
		}
	}

}	

	


