import java.util.Scanner;
import java.io.File;
import java.io.FileNotFoundException;
import java.util.Arrays;
import java.util.Locale;

class Estado{
	String[] hijas;
	String[] objetivos;
	String[] aciertos;
	
	String palabraObj;
	
	int lenHijas;
	int numAciertos;
	int numObjetivos;
	int objetivosAcertados;
	
	Lector leer;

	public Estado(int numObjetivos, String nombreArchivo) {
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

	private void setLenHijas() {
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
		setLenHijas();
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

	public int setObjetivos() {
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
			System.out.println("Puntos: " + puntos);
			while(respuesta!=true) {
				
				System.out.println("¿Otra Partida?");  
				
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
		

	}
	

	public static int jugar(Scanner in) {

		int puntos = 0;
		String nombreArchivo = "bin\\Diccionario.txt";
		int numObjetivos=6;
		Estado estado = new Estado(numObjetivos, nombreArchivo);
		estado.setObjetivos();

		
		String palabraDesordenada=new String(desordenar(estado.palabraObj));
		
		System.out.println("Introduce todas las palabras objetivo ("+estado.numObjetivos+") o introduce \"/salir\" para terminar.");
	
		boolean salir=false;
		while (estado.objetivosAcertados < estado.numObjetivos&&salir==false){
			System.out.println();
			System.out.println(palabraDesordenada);
			System.out.println();
			for (int i = 0; i < estado.numObjetivos; i++) {
				
				if(contiene(estado.aciertos, estado.objetivos[i], estado.numAciertos)) {
					System.out.println(estado.objetivos[i]);
				}else {
					for(int cont=0;cont<estado.objetivos[i].length();cont++) {
						System.out.print("_ ");
					}
					System.out.println();
				}
				
			}
			String entrada=in.next();
			if(entrada.toLowerCase().equals("/salir")){
				salir=true;
			}else {
			puntos += comprobarPalabra(estado,entrada);
			}
		}
		if(estado.objetivosAcertados == estado.numObjetivos) {
		System.out.println("¡¡Has acertado todas las palabras objetivo!!");
		puntos+=25;
		}
		System.out.println("Pulsa enter para continuar");
		in.nextLine();
		in.nextLine();

		return puntos;
	}
	
	

	public static int comprobarPalabra(Estado estado, String entrada) {
		int puntos = 0;

		if (estado.leer.estaEnArchivo(entrada)) {
			if (contiene(estado.hijas, entrada, estado.lenHijas)) {
				if (contiene(estado.aciertos, entrada, estado.numAciertos)) {
					System.out.println("Ya has introducido esa palabra");
				} else if (contiene(estado.objetivos, entrada, estado.numObjetivos)) {
					System.out.println("Palabra objetivo encontrada");
					estado.AñadirAciertosObj(entrada);
					
				} else {
					System.out.println("Palabra bonus encontrada");
					estado.añadirAciertosBonus(entrada);
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
