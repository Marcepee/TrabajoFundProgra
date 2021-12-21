import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;

class Lista {
	String[] hijas;
	int lenHijas;
	String[] objetivos;
	String[] aciertos;
	int numAciertos;
	int numObjetivos;
	int objetivosAcertados;
	String palabraObj;
	Lector leer;

	public Lista(int numObjetivos, String nombreArchivo) {
		this.numObjetivos = numObjetivos;
		numAciertos=0;
		objetivosAcertados=0;
		leer = new Lector(nombreArchivo);
		aciertos = new String[0];
	}

	private int numPalabrasAncestro() {
		int cont = 0;
		Scanner dc = leer.leerArchivo();
		while (dc.hasNextLine()) {
			if (dc.nextLine().length() > 5) {
				cont++;
			}
		}
		return cont;
	}

	private void setPalabraObj() {
		Scanner dc = leer.leerArchivo();
		int num = numPalabrasAncestro();
		int seleccion = (int) ((num - 1) * Math.random());
		String[] palabrasAncestro = new String[num];
		for (int i = 0; i < num; i++) {
			palabrasAncestro[i] = dc.nextLine();
		}
		palabraObj = palabrasAncestro[seleccion];

	}

	private boolean esHija(String palabraAncestro, String palabraHija) {

		char palabraAncestroChar[] = palabraAncestro.toCharArray();
		Arrays.sort(palabraAncestroChar);
		String palabraAncestroOrd = new String(palabraAncestroChar);

		char palabraHijaChar[] = palabraHija.toCharArray();
		Arrays.sort(palabraHijaChar);
		String palabraHijaOrd = new String(palabraHijaChar);

		int ind = 0;
		for (int cont = 0; cont < palabraHijaOrd.length(); cont++) {

			if (palabraAncestroOrd.indexOf(palabraHijaOrd.charAt(cont), ind) != -1) {
				ind = palabraAncestroOrd.indexOf(palabraHijaOrd.charAt(cont), ind);
				ind++;

			} else {
				return false;
			}

		}

		return true;
	}

	private void getLenHijas() {
		lenHijas = 0;
		Scanner dc = leer.leerArchivo();
		while (dc.hasNextLine()) {
			if (esHija(palabraObj, dc.nextLine())) {
				lenHijas++;
			}
		}
	}

	private void setHijas() {
		setPalabraObj();
		getLenHijas();
		int cont = 0;
		hijas = new String[lenHijas];
		Scanner dc = leer.leerArchivo();
		while (dc.hasNextLine()) {
			String lineaDic = dc.nextLine();

			if (esHija(palabraObj, lineaDic)) {
				hijas[cont] = lineaDic;
				cont++;
			}
		}
	}

	public int getObjetivos() {
		setHijas();
		objetivos = new String[numObjetivos];
		
		int[] dado;
		int random = 0;
		dado = new int[lenHijas];
		for (int cont = 0; cont < lenHijas; cont++) {
			dado[cont] = cont;
		}
		objetivos[0] = palabraObj;
		dado[0]= -1;
		int cont = 1;
		while (cont < numObjetivos) {
			random = (int) (Math.random() * lenHijas);
			if (dado[random] != -1&& hijas[random].equals(palabraObj)==false) {
				objetivos[cont] = hijas[random];
				dado[random] = -1;
				cont++;
			}
		}
		return cont;
	}

	public void AñadirAciertosObj(String acierto) {
		this.objetivosAcertados++;
		this.numAciertos++;
		setAciertos(acierto, aciertos);
	}

	public void añadirAciertosBonus(String acierto) {
		numAciertos++;
		setAciertos(acierto, aciertos);

	}

	private void setAciertos(String acierto, String[] aciertosIniciales) {
		aciertos = new String[numAciertos];
		for(int i = 0; i<aciertosIniciales.length;i++) {
			aciertos[i]=aciertosIniciales[i];
		}
		aciertos[numAciertos-1] = acierto;

	}

}

class Lector {
	String nombreArchivo;

	public Lector(String nombreArchivo) {
		this.nombreArchivo = nombreArchivo;
	}

	public Scanner leerArchivo() {
		Scanner dc;
		try {
			dc = new Scanner(new File(nombreArchivo));
			return dc;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		}
		return null;
	}

	public boolean estaEnArchivo(String str) {
		Scanner dc = leerArchivo();
		while (dc.hasNextLine()) {
			if (str.equals(dc.nextLine())) {
				return true;
			}
		}
		return false;
	}

}

public class Juego {

	public static void main(String[] args) {
		Scanner in = new Scanner(System.in);
		in.useLocale(new Locale("es","ES"));
		boolean loop=true;
		boolean respuesta;
		int puntos = 0;
		 do{
			respuesta=false;
			puntos = jugar(in);
			
			while(respuesta!=true) {
				
				System.out.println("¿Otra Partida?");  //jugarDeNuevo();	
				
				String opc=in.next().toLowerCase();
				
				if(opc.equals("si")) {
					
					respuesta=true;
					loop=true;
					
				}else if(opc.equals("no")) {
					
					respuesta=true;
					loop=false;
					
				}
				
			}
			
			
		}while (loop);
		in.close();
		System.out.println("Programa terminado con exito");
		System.out.println("Puntos: " + puntos);

	}

	public static int jugar(Scanner in) {

		int puntos = 0;
		String nombreArchivo = "bin\\Diccionario.txt";
		Lista lista = new Lista(6, nombreArchivo);
		lista.getObjetivos();

		System.out.println(lista.palabraObj);
		System.out.println(desordenar(lista.palabraObj));

		System.out.println("\n\n\nPalabras hijas:");

		System.out.println("\n" + lista.lenHijas);
		for (int i = 0; i < lista.lenHijas; i++) {
			System.out.println(lista.hijas[i]);
		}

		System.out.println("\n\n\nPalabras objetivo:");

		for ( int i = 0; i<lista.numObjetivos;i++) {
			System.out.println(lista.objetivos[i]);
		}

		while (lista.objetivosAcertados < lista.numObjetivos) {
			for (int i = 0; i < lista.numObjetivos; i++) {
				
				if(contiene(lista.aciertos, lista.objetivos[i], lista.numAciertos)) {
					System.out.println(lista.objetivos[i]);
				}else {
					for(int cont=0;cont<lista.objetivos[i].length();cont++) {
						System.out.print("_ ");
					}
					System.out.println();
				}
				
			}
			puntos += plc_hldr(lista,in.next());

		}
		System.out.println("¡¡Has acertado todas las palabras objetivo!!");
		puntos+=25;
		System.out.println("Pulsa enter para continuar");
		in.nextLine();
		in.nextLine();

		return puntos;
	}
	
	

	public static int plc_hldr(Lista lista, String entrada) {
		int puntos = 0;

		if (lista.leer.estaEnArchivo(entrada)) {
			if (contiene(lista.hijas, entrada, lista.lenHijas)) {
				if (contiene(lista.aciertos, entrada, lista.numAciertos)) {
					System.out.println("Ya has introducido esa palabra");
				} else if (contiene(lista.objetivos, entrada, lista.numObjetivos)) {
					System.out.println("Palabra objetivo encontrada");
					lista.AñadirAciertosObj(entrada);
					
				} else {
					System.out.println("Palabra bonus encontrada");
					lista.añadirAciertosBonus(entrada);
					puntos = 1;
				}
			} else {
				System.out.println("No es una palabra hija");
			}
		} else {
			System.out.println("La palabra no esta en el diccionario");
		}

		return puntos;
	}
	
	
	

	public static boolean contiene(String[] objetivos, String entrada, int length) {

		boolean acierto = false;
		for (int cont = 0; cont < length; cont++) {
			if (entrada.equals(objetivos[cont])) {
				acierto = true;
				cont = length;
			}
		}
		return acierto;
	}



	

	

	public static String desordenar(String palabraObj) {
		String desordenada = new String();
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
				desordenada += ("" + palabraObj.charAt(random));
				dado[random] = -1;
				cont++;
			}
		}
		return desordenada;
	}

}
