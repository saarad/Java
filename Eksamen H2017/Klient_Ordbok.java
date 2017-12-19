/**
* Klient_Ordbok.java
* @author Ilia Rad Saadat
* Oppgave 4
*
* Dette er et klientprogram for klassen Ordbok.java. Den lar brukeren lage en ordliste. Brukeren kan bare opprette en ordliste
* med plass til 10 ord, og kan ikke overskrive denne. Den lagrer seg på fil oppgitt i Ordbok.java hver gang bruker legger
* til et ord eller ny definisjon til et ord. Navnet kan endres ved oppstart.
*
* SIDENOTE:
* Legg merke til at det ikke er nødvendig å lage hele klientprogrammet, men jeg velger å gjøre det i dette løsningsforslaget.
*/
import static javax.swing.JOptionPane.*;
import java.io.*;

public class Klient_Ordbok implements Serializable{
	private final static String[] MULIGHETER = {"Legg til et nytt ord", "Legg til ny definisjon", "Vis ordlisten", "AVSLUTT"};
	private static boolean igjen = true;
	private static Ordbok ordbok;
	private static Ord[] ordet;
	private static int antall;

	public static void main(String[] args){
		String navn = showInputDialog("Skriv navnet på ordboken din. Det er plass til 10 ord totalt. OBS! Dette kan ikke endres senere");
		ordbok = new Ordbok(navn);
		while(igjen){
			antall = ordbok.getAntall();
			ordet = ordbok.getOrdbok();
			try{
				int valg = showOptionDialog(null, "Velg operasjon", "Simpel ordbok", YES_OPTION, 0, null, MULIGHETER, MULIGHETER[0]);

				switch(valg){
					case 0:
					String ord = showInputDialog("Skriv ordet");
					String def = showInputDialog("Skriv definisjonen på ordet");
					ordet[antall] = new Ord(ord,def);
					if(ordbok.regNyttOrd(ordet[antall])){
						showMessageDialog(null, "Registrering velykket!");
					}//end if
					else{
						showMessageDialog(null, "Noe gikk galt med registreringen. Kan være at ordboken er full eller at ordet allerede er registrert");
					}//end else
					igjen = true;
					break;

					case 1:
					String finn = showInputDialog("Skriv ordet du vil legge definisjonen til");
					String nyDef = showInputDialog("Skriv definisjonen du vil legge til");
					if(ordbok.leggTilNyDef(finn,nyDef)){
						showMessageDialog(null, "Definisjonen er registrert!");
					}//end if
					else{
						showMessageDialog(null, "Definisjon ikke lagt til. Det kan hende ordet du skrev inn ikke befinner seg i tabellen");
					}//end else
					igjen = true;
					break;

					case 2:
					System.out.println(ordbok);
					System.out.println("\n\nSortert: ");
					for(int i = 0; i<ordbok.getAntall(); i++){
						System.out.println(ordbok.sort()[i]);
					}//end for
					igjen = true;
					System.out.println("\n\n\n");
					break;

					case 3:
					showMessageDialog(null, "Velkommen tilbake!");
					igjen = false;
					System.exit(0);
					break;
				}//end switch
			}//end try
			catch(Exception e){
				showMessageDialog(null, "Oisann! Noe gikk galt! Prøv igjen" + "\n" + e);
				igjen = true;
			}//end catch
			skrivTilFil(ordbok,ordbok.getFilnavn());
		}//end loop
	}//end main

		//Metode for å skrive til fil
		public static boolean skrivTilFil(Ordbok o, String filnavn){
			if(o == null || filnavn == null) return false;
			try(FileOutputStream innstrom = new FileOutputStream(filnavn);
				ObjectOutputStream skriv = new ObjectOutputStream(innstrom)){ //Append = true, den skriver videre på dokumentet.
				skriv.writeObject(o);
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
}//end class