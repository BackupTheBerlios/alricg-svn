/*
 * Created on 01.05.2005 / 02:13:38
 *
 * This file is part of the project ALRICG. The file is copyright
 * protected and under the GNU General Public License.
 * For more information see "http://alricg.die3sphaere.de/".
 *
 */
package org.d3s.alricg.charKomponenten.spezial;

import org.d3s.alricg.charKomponenten.CharElement;
import org.d3s.alricg.charKomponenten.Profession;
import org.d3s.alricg.charKomponenten.charZusatz.SimpelGegenstand;
import org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl;
import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.AuswahlAusruestung;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.IdLinkList;
import org.d3s.alricg.charKomponenten.links.AuswahlAusruestung.HilfsAuswahl;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;

/**
 * <u>Beschreibung:</u><br> 
 * Diese Klasse hat die Aufgabe aus zwei Professionen eine neue Profession zu erstellen 
 * und diese zu Repräsentieren. Die Erstellung der neuen Profession geschieht nach den
 * Regeln des Vorteils "Veteran" (siehe AH S. 111 und das Errata von S&H).
 * Nach Außen kann eine Objekt der Klasse ganz wie eine normale Profession benutz werden.
 * 
 * @author V. Strelow
 */
public class VeteranProf extends Profession {
	
	private Profession profEins;
	private Profession profZwei;

	/**
	 * Erzeugt aus zwei uebergebenen Professionen eine Veteranen-Profession.
	 * Es werden zwei Professionen uebergeben da Gardisten, Soldaten und 
	 * Söldner, wenn sie den Vorteil Veteran waehlen,  eine andere Variante
	 * ihrer Profession als zweite Dienstzeit waehlen koennen. Die Boni
	 * (LE, AU, AE, SO, Talente) der zweiten Profession werden zur Haelfte
	 * angerechnet. Fuer die normale Variante des Veteran uebergibt man einfach 
	 * zweimal die selbe Profession.
	 * <p>
	 * <strong>ACHTUNG: Die Herkunft von nicht veränderten Auswahlen zeigt noch
	 * auf die Original Profession.</strong> Bitte im Quellcode nachpruefen. 
	 *  
	 * @param prof1 Die "original" Profession, die eigentlich gewählt wurde
	 * @param prof2 Die Veteran-Profession die gewählt wurde
	 */
	public VeteranProf( Profession prof1, Profession prof2 ) {
		super( prof1.getId() + " / " + prof2.getId() );

		this.profEins = prof1;
		this.profZwei = prof2;
		
		if ( prof1 == prof2 ) {
			// Hier wird der normale Fall abgehandelt dass der Held die Boni
			// auf SO, LE, AE, AU und Talente 1,5-fach (aufgerundet) erhaelt.
			
			setAktivierbareZauber( prof1.getAktivierbareZauber() );
			setAnzeigen( prof1.isAnzeigen() );
			setAnzeigenText( prof1.getAnzeigenText() );
			setArt( prof1.getArt() );
			setAufwand( prof1.getAufwand() );
			setAusruestung( prof1.getAusruestung() );
			setBeschreibung( prof1.getBeschreibung() + ", " + prof2.getBeschreibung() );
			setBesondererBesitz( prof1.getBesondererBesitz() );
			setEmpfNachteile( prof1.getEmpfNachteile() );
			setEmpfVorteile( prof1.getEmpfVorteile() );
			setGeschlecht( prof1.getGeschlecht() );
			setGeweihtGottheit( prof1.getGeweihtGottheit() );
			setGpKosten( prof1.getGpKosten() );
			setHauszauber( prof1.getHauszauber() );
			setKannGewaehltWerden( prof1.isKannGewaehltWerden() );
			setLiturgienAuswahl( prof1.getLiturgienAuswahl() );
			setMagierAkademie( prof1.getMagierAkademie() );
			setNachteileAuswahl( prof1.getNachteileAuswahl() );
			setName( prof1.getName() + " (Veteran)" ); // TODO Ok?
			setRegelAnmerkung( prof1.getRegelAnmerkung() );
			setRepraesentation( prof1.getRepraesentation() );
			setRitualeAuswahl( prof1.getRitualeAuswahl() );
			setRitusModis( prof1.getRitusModis() );
			setSammelberiff( prof1.getSammelBegriff() );
			setSfAuswahl( prof1.getSfAuswahl() );
			setSoMax( prof1.getSoMax() );
			setSoMin( prof1.getSoMin() );
			setSonderregel( (SonderregelAdapter) prof1.getSonderregel() );
			setUngeNachteile( prof1.getUngeNachteile() );
			setUngeVorteile( prof1.getUngeVorteile() );
			setVarianten( prof1.getVarianten() ); // TODO macht das Sinn?
			setVerbilligteLiturgien( prof1.getVerbilligteLiturgien() );
			setVerbilligteNacht( prof1.getVerbilligteNacht() );
			setVerbilligteRituale( prof1.getVerbilligteRituale() );
			setVerbilligteSonderf( prof1.getVerbilligteSonderf() );
			setVerbilligteVort( prof1.getVerbilligteVort() );
			setZauber( prof1.getZauber() );
			setZauberNichtBeginn( prof1.getZauberNichtBeginn() );

			// Hier werden die Boni mit dem 1,5-fachen Wert erzeugt.
			// TODO Bei den Eigenschaften werden die Variablen Auswahlen noch 
			// nicht richtig kopiert. Eigentlich muesste aus "LE oder AU +1"
			// ein "LE oder AU +2" werden. Das ist aber noch nicht der Fall.
			// Da das in den Regeln noch nicht vor kommt lass ich es, fuers 
			// erste, einfach mal weg.
			setEigenschaftModis( kopieren( prof1.getEigenschaftModis(), eigenschaften ) );
			setTalente( kopieren( prof1.getTalente(), talente ) );
			setSchriften( kopieren( prof1.getSchriften(), talente ) );
			setSprachen( kopieren( prof1.getSprachen(), talente ) );
			
		} else {
			// Hier wird der Sonderfall fuer Gardisten, Soldaten und Soeldner
			// abgehandelt, die eine Variante ihrer Profession als zweite
			// Dienstzeit waehlen koennen. Siehe dazu die S&H-Errata vom 
			// 08.07.02

			setAnzeigen( prof1.isAnzeigen() );
			setAnzeigenText( prof1.getAnzeigenText() );
			setArt( prof1.getArt() );
			setAufwand( prof1.getAufwand() );
			setAusruestung( prof1.getAusruestung() );
			setBeschreibung( prof1.getBeschreibung() );
			setBesondererBesitz( prof1.getBesondererBesitz() );
			setEmpfNachteile( prof1.getEmpfNachteile() );
			setEmpfVorteile( prof1.getEmpfVorteile() );
			setGeschlecht( prof1.getGeschlecht() );
			setGpKosten( prof1.getGpKosten() );
			setKannGewaehltWerden( prof1.isKannGewaehltWerden() );
			setName( prof1.getName() + ", 2. Dienstzeit " + prof2.getName() ); // TODO Ok?
			setRegelAnmerkung( prof1.getRegelAnmerkung() ); // Muss ich das auch zusammenlegen?
			setSammelberiff( prof1.getSammelBegriff() );
			setSoMax( prof1.getSoMax() );
			setSoMin( prof1.getSoMin() );
			setSonderregel( (SonderregelAdapter) prof1.getSonderregel() );
			setUngeNachteile( prof1.getUngeNachteile() );
			setUngeVorteile( prof1.getUngeVorteile() );
			setVarianten( prof1.getVarianten() ); // TODO macht das Sinn?
			setVerbilligteLiturgien( prof1.getVerbilligteLiturgien() );
			setVerbilligteNacht( prof1.getVerbilligteNacht() );
			setVerbilligteSonderf( prof1.getVerbilligteSonderf() );
			setVerbilligteVort( prof1.getVerbilligteVort() );
			
			setSfAuswahl( vereinen( prof1.getSfAuswahl(), prof2.getSfAuswahl() ) );
			setNachteileAuswahl( vereinen( prof1.getNachteileAuswahl(), prof2.getNachteileAuswahl() ) );
			setVorteileAuswahl( vereinen( prof1.getVorteileAuswahl(), prof2.getNachteileAuswahl() ) );
		}
	}
	
	/**
	 * @param auswahl1
	 * @param auswahl2
	 * @return Eine Auswahl die sich aus {@code auswahl1} und {@code auswahl2}
	 * 		zusammensetzt.
	 */
	private Auswahl vereinen( Auswahl auswahl1, Auswahl auswahl2 ) {
		
		Auswahl neueAuswahl = new Auswahl( this );
		
		neueAuswahl.setFesteAuswahl( vereinen( auswahl1.getFesteAuswahl(), auswahl2.getFesteAuswahl() ) );
		
		neueAuswahl.setVariableAuswahl( vereinen( auswahl1.getVariableAuswahl(), auswahl2.getVariableAuswahl() ) );
		
		return neueAuswahl;
	}
	
	/**
	 * Macht aus zwei Arrays ein einziges.
	 * 
	 * @param auswahl1 ist der erste Teil des neuen Arrays.
	 * @param auswahl2 ist der zweite Teil des neuen Arrays.
	 * @return Ein Array das sich aus dem Inhalt von {@code auswahl1} und 
	 * 		{@code auswahl2} zusammensetzt.
	 */
	private AbstractVariableAuswahl[] vereinen( AbstractVariableAuswahl[] auswahl1, AbstractVariableAuswahl[] auswahl2 ) {
		
		AbstractVariableAuswahl[] neueAuswahl = new AbstractVariableAuswahl[ auswahl1.length + auswahl2.length ];
		
		System.arraycopy( auswahl1, 0, neueAuswahl, 0, auswahl1.length );
		
		System.arraycopy( auswahl2, 0, neueAuswahl, auswahl1.length, auswahl2.length );
		
		return neueAuswahl;
	}
	
	
	/**
	 * Macht aus zwei Arrays ein einziges.
	 * 
	 * @param links1 ist der erste Teil des neuen Arrays.
	 * @param links2 ist der zweite Teil des neuen Arrays.
	 * @return Ein Array das sich aus dem Inhalt von {@code link1} und 
	 * 		{@code link2} zusammensetzt.
	 */
	private IdLink[] vereinen( IdLink[] links1, IdLink[] links2 ) {
		
		IdLink[] neueLinks = new IdLink[ links1.length + links2.length ];
		
		System.arraycopy( links1, 0, neueLinks, 0, links1.length );
		
		System.arraycopy( links2, 0, neueLinks, links1.length, links2.length );
		
		return neueLinks;
	}
		
	/**
	 * Erzeugt eine Kopie einer Auswahl.
	 * 
	 * @param auswahl
	 * 		die kopiert werden soll.
	 * @param kopierer 
	 * 		der verwendet wird um die IdLinks zu kopieren.
	 * @return
	 * 		eine deep-copy der uebergebenen Auswahl.
	 */
	private Auswahl kopieren( Auswahl auswahl, LinkKopierer kopierer ) {
		
		Auswahl neueAuswahl = new Auswahl( this );
		
		if ( auswahl.hasFesteAuswahl() ) {
			
			IdLink[] links = kopieren( auswahl.getFesteAuswahl(), neueAuswahl, kopierer );
			
			neueAuswahl.setFesteAuswahl( links );
		}
		
		if ( auswahl.hasVarianteAuswahl() ) {
			
			AbstractVariableAuswahl[] variableAuswahlen = auswahl.getVariableAuswahl();
			
			AbstractVariableAuswahl[] neueVariableAuswahlen = kopieren( variableAuswahlen, auswahl, kopierer );
			
			neueAuswahl.setVariableAuswahl( neueVariableAuswahlen );
		}
				
		return neueAuswahl;
	}
	
	/**
	 * Legt eine Kopie eines Arrays mit variablen Auswahlen an.
	 * 
	 * @param variableAuswahlen
	 * 			die kopiert werden sollen.
	 * @param auswahl
	 * 			zu der die neu erzeugte Variable Auswahl gehoeren wird.
	 * @param kopierer
	 * 			der zum Kopieren der IdLinks verwendet wird.
	 * @return die Kopien der Variablen auswahlen.
	 */
	private AbstractVariableAuswahl[] kopieren( 
			AbstractVariableAuswahl[] variableAuswahlen, 
			Auswahl auswahl,
			LinkKopierer kopierer ) {
		
		AbstractVariableAuswahl[] neueVariableAuswahlen = new AbstractVariableAuswahl[ variableAuswahlen.length ];
		
		for ( int i = 0 ; i < variableAuswahlen.length ; i++ ) {
			
			AbstractVariableAuswahl.Modus modus = variableAuswahlen[ i ].getModus();
			neueVariableAuswahlen[ i ] = AbstractVariableAuswahl.createAuswahl( auswahl, modus );
			
			if ( variableAuswahlen[ i ].hasOptionen() ) {
				
				IdLink[] optionen = variableAuswahlen[ i ].getOptionen();
				
				IdLink[] neueOptionen = kopieren( optionen, auswahl, kopierer );
				       
				neueVariableAuswahlen[ i ].setOptionen( neueOptionen );
			}
			
			if ( variableAuswahlen[ i ].hasOptionsgruppen() ) {
				
				IdLink[][] optionen = variableAuswahlen[ i ].getOptionsGruppe();
				IdLink[][] neueOptionen = new IdLink[ optionen.length ][];
				
				for (int j = 0; j < neueOptionen.length; j++) {
					
					neueOptionen[ j ] = kopieren( optionen[ j ], auswahl, kopierer );
				}
			}
			
			switch ( modus ) {
			case ANZAHL:
				neueVariableAuswahlen[ i ].setAnzahl( variableAuswahlen[ i ].getAnzahl() );
				break;
				
			case LISTE:
				int[] werte = variableAuswahlen[ i ].getWerteListe();
				int[] neueWerte = new int[ werte.length ];
				for (int j = 0; j < neueWerte.length; j++) {
					neueWerte[ j ] = kopierer.kopieren( werte[ j ] );
				}
				neueVariableAuswahlen[ i ].setWerteListe( neueWerte );
				break;
				
			case VERTEILUNG:
				neueVariableAuswahlen[ i ].setAnzahl( variableAuswahlen[ i ].getAnzahl() );
				neueVariableAuswahlen[ i ].setMax( variableAuswahlen[ i ].getMax() );
				neueVariableAuswahlen[ i ].setWert( kopierer.kopieren( variableAuswahlen[ i ].getWert() ) );
				break;
			}
		}
		
		return neueVariableAuswahlen;
	}
	
	/**
	 * Kopiert ein Array mit IdLinks.
	 * 
	 * @param links
	 * 			die kopiert werden sollen.
	 * @param auswahl
	 * 			zu der die kopierten IdLinks gehoeren.
	 * @param kopierer
	 * 			der zum Kopieren der IdLinks verwendet wird.
	 * @return die kopierten IdLinks.
	 */
	private IdLink[] kopieren( IdLink[] links, Auswahl auswahl, LinkKopierer kopierer ) {
		
		IdLink[] neueLinks = new IdLink[ links.length ];
		
		for ( int i = 0 ; i < links.length ; i++ ) {
		
			neueLinks[ i ] = kopierer.kopieren( links[ i ], auswahl );
		
		}
		
		return neueLinks;
	}
	
	/*
	 * 	TODO implement !
	 * 
	 * - Das meißte sollte über die Setter erledigt werden können, ohne Methoden 
	 * überschreiben zu müssen. Man ließt einfach die Werte beider Professionen aus,
	 * bearbeitet sie entsprechend und schreibt sie per Setter in die Instanz von 
	 * "VeteranProf".
	 * - Es gibt viele gemeinsamkeiten mit "Breitgefächerter Bildung". Evtl. kann Code von
	 * beiden Klassen verwendet werden.
	 * - Nach dem Errata von S&H ist es auch Möglich unterschiedliche Professionen
	 * mittels "Veteran" zu verbinden, daher werden auch zwei Professionen angegeben 
	 * (diese können aber natürlich auch gleich sein, was dem klassischen Veteran
	 * entspricht)
	 */
	
	/**
	 * Legt eine Kopie eines IdLinks an.
	 */
	private class LinkKopierer {
		protected IdLink kopieren( IdLink original, Auswahl auswahl ) {
			
			IdLink kopie = new IdLink( VeteranProf.this, auswahl );
			
			kopie.setLeitwert( original.isLeitwert() );
			kopie.setText( original.getText() );
			kopie.setZiel( original.getZiel() );
			kopie.setWert( original.getWert() );
			kopie.setZweitZiel( original.getZweitZiel() );
			
			return kopie;
		}

		protected int kopieren( int wert ) {
			return wert;
		}
	}
	
	/**
	 * Legt eine Kopie eines Links an. Wenn der Link das Ziel LE, AU, AE oder
	 * SO hat, so wird das Attribut wert des IdLinks um 0.5 (aufgerundet) 
	 * erhoeht. 
	 */
	private LinkKopierer eigenschaften = new LinkKopierer() {
		@Override
		protected IdLink kopieren( IdLink original, Auswahl auswahl ) {
			
			IdLink kopie = super.kopieren( original, auswahl );
			
			if ( hatZielLeAuAeOderSo( original ) ) {
				int wert = original.getWert();
				wert += ( ( wert + 1 ) / 2 );  // += wert/2 (aufgerundet)
				kopie.setWert( wert );
			}
			
			return kopie;
		}
		
		private boolean hatZielLeAuAeOderSo( IdLink link ) {
			CharElement ziel = link.getZiel();
			return ziel.getId().equals( "EIG-LE" )
				|| ziel.getId().equals( "EIG-AU" )
				|| ziel.getId().equals( "EIG-AE" )
				|| ziel.getId().equals( "EIG-SO" );
		}
	};
	
	/**
	 * Legt eine Kopie eines IdLinks an und erhoeht dabei das Attribut wert um
	 * 0.5 (aufgerundet).
	 */
	private LinkKopierer talente = new LinkKopierer() {
		@Override
		protected IdLink kopieren( IdLink original, Auswahl auswahl ) {
			IdLink kopie = super.kopieren( original, auswahl );
			
			int wert = original.getWert();
			wert += ( ( wert + 1 ) / 2 );  // += wert/2 (aufgerundet)
			kopie.setWert( wert );
			
			return kopie;
		}
		
		@Override
		protected int kopieren( int wert ) {
			return ( ( 3 * wert ) + 1 ) / 2;
		}
	};
}
