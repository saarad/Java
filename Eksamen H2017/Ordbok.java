/**
* Ordbok.java
* @author Ilia Rad Saadat
* Oppgave 3
*
* Denne klassen er oppgave 3 i eksamen H2017.
* Den lager en tabell av objekter av klassen Ord.java og fungerer som en ordliste for klient.
* Den har metoder for å lese og skrive til fil (objekter, ikke tekst-strenger), der den sjekker om det finnes en ordliste før den lager en ny.
* Klassen har i tillegg tilgangsmetoder, registreringsmetode for definisjon og ord, sorteringsmetode, sokemetode, samt toString-metode.
*/

import java.io.*;
import static javax.swing.JOptionPane.*;

class Ordbok implements Serializable{
	private String navn;
	private Ord[] ordbok;
	private Ordbok ordliste;
	private int antall;
	private final int MAKS = 10; //Oppgaven spesiferer 10, så jeg tolker det som at alle ordlister skal være maks 10 ord lange. Ellers kunne klient valgt dette
	private String filnavn = "ordbok.txt"; //Igjen kunne klient valgt dette, men oppgaven spesiferer.

	public Ordbok(String navn){
		this.navn = navn;
		if(!lesFraFil(filnavn)){
			antall = 0;
			ordbok = new Ord[MAKS];
		}//end if
		else{
			antall = ordliste.getAntall();
			ordbok = ordliste.getOrdbok();
		}//end else
	}//end constructor

	//Tilgangsmetoder
	public String getNavn(){return navn;}
	public Ord[] getOrdbok(){return ordbok;}
	public int getAntall(){return antall;}
	public int getMaks(){return MAKS;}
	public String getFilnavn(){return filnavn;}

	//Metode for å registrere nytt ord. Metoden tillater dette bare om ordet ikke er allerede registrert og det er plass i tabellen.
	public boolean regNyttOrd(Ord ny){
		if(ny == null)return false;
		if(antall == MAKS) return false;
		for(int i = 0; i<antall; i++){
			if(ny.equals(ordbok[i]))return false;
		}//end for
		ordbok[antall] = ny;
		antall++;
		return true;
	}//end method


	//Metode for å legge til et ny definisjon. Den tar inn ordet det skal legges til og definisjonen som skal legges til det ordet som argument.
	//Bruker registreringsmetoden fra klassen Ord.java i logikken sin.
	public boolean leggTilNyDef(String ord, String def){
		if(ord == null || def == null) return false;
		Ord funnet = finnOrd(ord);
		if(funnet == null) return false; //ordet er ikke i tabellen
		if(funnet.leggTilNyDef(def)){
			return true;
		}//end if
		return false;
	}//end method


	//Metode for å sortere ordboken alfabetisk. Den returnerer en helt ny tabell, endrer ikke på den orginale.
	//Metoden bruker charAt-metoden fra APIet og bruker unicodeverdiene til den første bokstaven i ordene.
	//Dersom to ord har samme førstebokstav, vil den gå i løkke å sjekke neste bokstav helt til den finner bokstaver som ikke er like.
	//Er fullt mulig å bruke compareTo-metoden fra APIet for å slippe å bruke løkken, og samtidig bruke mindre kode.
	public Ord[] sort(){
		if(antall == 0) return null;
		Ord[] nyTab = new Ord[antall];
		for(int i = 0; i<antall; i++){//Dyp kopierer den gamle over
			nyTab[i] = ordbok[i];
		}//end loop
		for(int start = 0; start<antall; start++){
			int hittilMinst = start;
			for(int i = start+1; i<antall; i++){
				//endrer til lowercase så unicodeverdiene ikke blir annerledes
				char forrige = nyTab[hittilMinst].getNavn().toLowerCase().charAt(0);
				char neste = nyTab[i].getNavn().toLowerCase().charAt(0);
				int f = 1;
				//Hvis bokstavene er like
				while(forrige == neste){
					forrige = nyTab[hittilMinst].getNavn().toLowerCase().charAt(f);
					neste = nyTab[i].getNavn().toLowerCase().charAt(f);
					f++;
				}//end loop
				//Unicodeverdiene øker jo lengere ut i alfabetet man kommer.
				if(forrige>neste){
					hittilMinst = i;
				}//end if
			}//end loop
			//Bytter plass på ordene om if-setningen er aktivert, hvis ikke skjer det ingenting
			Ord hjelp = nyTab[hittilMinst];
			nyTab[hittilMinst] = nyTab[start];
			nyTab[start] = hjelp;
		}//end loop
		return nyTab;
	}//end method



	//Metode for å finne et ord. Metoden lager et objekt av klassen Ord med argumentet
	//før den bruker klassen Ord.java sin equals-metode for å søke etter ordet.
	public Ord finnOrd(String sok){
		if(sok == null)return null;
		Ord soker = new Ord(sok);
		Ord funnet = null;
		for(int i = 0; i<antall; i++){
			if(ordbok[i].equals(soker)){
				funnet = ordbok[i];
			}//end if
		}//end loop
		return funnet;
	}//end method


	//Metode for å lese fra fil. Jeg tolker oppgaven som at metoden skal lese inn objektet og ikke tekst.
	public boolean lesFraFil(String filnavn){
		if(filnavn == null) return false;
		try(FileInputStream innstrom = new FileInputStream(filnavn);
			ObjectInputStream les = new ObjectInputStream(innstrom)){
			ordliste = (Ordbok)les.readObject();
			if(ordliste == null)return false;
			return true;
		}//end try
		catch(FileNotFoundException g){ //Om fil ikke funnet
			showMessageDialog(null, "Fil ikke funnet!");
			return false;
		}//end catch
		catch(EOFException f){ //Om filen er tom
			showMessageDialog(null, "Filen er tom!");
			return false;
		}//end catch
		catch(Exception e){ //Om noe annet går galt
			showMessageDialog(null, "Noe gikk galt!" + "\n" + e);
			return false;
		}//end catch
	}//end method

	public String toString(){
		String s = "Ordlistens navn: " + navn.toUpperCase() + "\n------\n";
		for(int i = 0; i<antall; i++){
			if(ordbok[i] == null) s+= "TOM SPALTE " + "\n";
			else s+= "ORD: " + ordbok[i] + "\n";
		}//end loop
		return s;
	}//end method

}//end class