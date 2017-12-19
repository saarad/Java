/**
* Ord.java
* @author Ilia Rad Saadat
* Oppgave 2
*
* Denne klassen er oppgave 2 i eksamen H2017.
* Den tar imot et ord og dens definisjon gjennom konstruktøren, og klienten kan senere velge å legge til flere definisjoner gjennom boolean-metode.
* Klassen inneholder i tillegg en egen equals-metode, privat utvidelses-metode, toString-metode samt tilgangsmetoder.
*/

import java.io.*; //Pga oppgave 3

class Ord implements Serializable{
	private String navn;
	private String[] def;
	private int antall = 1; //Pga av at vi tar imot en definisjon gjennom konstruktøren

	//Oppgaven spesiferer ikke om konstruktøren skal ta inn en definisjon, men jeg velger å gjøre det slik,
	//da det er mer naturlig at alle ord hvertfall har én definisjon.
	public Ord(String navn, String def){
		this.navn = navn;
		this.def = new String[antall];
		this.def[0] = def;
	}//end constructor

	//Lager en konstruktør som bare tar inn ordet. Dette er for søkemetoden i klassen Ordbok.java
	public Ord(String navn){
		this.navn = navn;
		antall = 0;
	}//end constructor

	//Tilgangsmetoder
	public String getNavn(){return navn;}
	public String[] getDef(){return def;}
	public int getAntall(){return antall;}

	//Equals-metode. Er veldig lik APIets equals-metode, men den gjør alt til småbokstaver før den sammenligner dem.
	//Legg merke til at dette ikke påvirker dem utenfor metoden.
	@Override
	public boolean equals(Object b){
		if(b == null)return false; //Sjekk om argumentet er tomt
		if(this == b)return true; //Sjekk om objektet sender seg selv som argument
		if(!(b instanceof Ord)) return false; //Sjekk om argumentet er et tilfelle av Ord.java før vi caster

		Ord a = (Ord)b; //casting

		if(navn.toLowerCase().equals(a.getNavn().toLowerCase())) return true;
		else return false;
	}//end method


	//Metode for å legge til nye definisjoner. Bruker den private metoden utvid() og klassens egen equals-metode.
	//Om de er helt like vil den returnere false. Delvis like går greit.
	public boolean leggTilNyDef(String ny){
		if(ny == null) return false; //check if the argument is empty
		for(int i = 0; i<antall; i++){
			if(def[i].toLowerCase().equals(ny.toLowerCase())) return false;
		}//end loop
		utvid();
		def[antall] = ny;
		antall++;
		return true;
	}//end method

	//Utvidelsesmetode. Den lager en ny tabell med 1 ekstra plass, kopierer def over, og setter referansen til def til den nye tabellen.
	private void utvid(){
		String[] nyTab = new String[antall+1];
		for(int i = 0; i<antall; i++){ //Dyp kopierer
			nyTab[i] = def[i];
		}//end loop
		def = nyTab; //Skifter referanse
	}//end method

	public String toString(){
		String s = navn + " | Definisjoner |"+ "\n" + "---------";
		for(int i = 0; i<antall; i++){
			s+= "\n" + def[i] + "\n" + "---------";
		}//end loop
		return s;
	}//end method

}//end class
