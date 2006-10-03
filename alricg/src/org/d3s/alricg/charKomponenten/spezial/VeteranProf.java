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
import org.d3s.alricg.charKomponenten.Eigenschaft;
import org.d3s.alricg.charKomponenten.EigenschaftEnum;
import org.d3s.alricg.charKomponenten.Profession;
import org.d3s.alricg.charKomponenten.links.AbstractVariableAuswahl;
import org.d3s.alricg.charKomponenten.links.Auswahl;
import org.d3s.alricg.charKomponenten.links.IdLink;
import org.d3s.alricg.charKomponenten.links.VariableAuswahlAnzahl;
import org.d3s.alricg.charKomponenten.sonderregeln.principle.SonderregelAdapter;
import org.d3s.alricg.controller.CharKomponente;
import org.d3s.alricg.store.DataStore;
import org.d3s.alricg.store.FactoryFinder;

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
	 *  
	 * @param profEins Die "original" Profession, die eigentlich gewählt wurde
	 * @param profZwei Die Veteran-Profession die gewählt wurde
	 */
	public VeteranProf(Profession profEins, Profession profZwei) {
		super(profEins.getId() + " / " + profZwei.getId());

		this.profEins = profEins;
		this.profZwei = profZwei;
		
		if ( profEins == profZwei ) {
			// Hier wird der normale Fall abgehandelt dass der Held die Boni
			// auf SO, LE, AE, AU und Talente 1,5-fach (aufgerundet) erhaelt.
			
			setAktivierbareZauber( profEins.getAktivierbareZauber() );
			setAnzeigen( profEins.isAnzeigen() );
			setAnzeigenText( profEins.getAnzeigenText() );
			setArt( profEins.getArt() );
			setAufwand( profEins.getAufwand() );
			setAusruestung( profEins.getAusruestung() );
			setBeschreibung( profEins.getBeschreibung() );
			setBesondererBesitz( profEins.getBesondererBesitz() );
			setEmpfNachteile( profEins.getEmpfNachteile() );
			setEmpfVorteile( profEins.getEmpfVorteile() );
			setGeschlecht( profEins.getGeschlecht() );
			setGeweihtGottheit( profEins.getGeweihtGottheit() );
			setGpKosten( profEins.getGpKosten() );
			setHauszauber( profEins.getHauszauber() );
			setKannGewaehltWerden( profEins.isKannGewaehltWerden() );
			setLiturgienAuswahl( profEins.getLiturgienAuswahl() );
			setMagierAkademie( profEins.getMagierAkademie() );
			setNachteileAuswahl( profEins.getNachteileAuswahl() );
			setName( profEins.getName() + " (Veteran)" ); // TODO Ok?
			setRegelAnmerkung( profEins.getRegelAnmerkung() );
			setRepraesentation( profEins.getRepraesentation() );
			setRitualeAuswahl( profEins.getRitualeAuswahl() );
			setRitusModis( profEins.getRitusModis() );
			setSammelberiff( profEins.getSammelBegriff() );
			setSfAuswahl( profEins.getSfAuswahl() );
			setSoMax( profEins.getSoMax() );
			setSoMin( profEins.getSoMin() );
			setSonderregel( (SonderregelAdapter) profEins.getSonderregel() );
			setUngeNachteile( profEins.getUngeNachteile() );
			setUngeVorteile( profEins.getUngeVorteile() );
			setVarianten( profEins.getVarianten() ); // TODO macht das Sinn?
			setVerbilligteLiturgien( profEins.getVerbilligteLiturgien() );
			setVerbilligteNacht( profEins.getVerbilligteNacht() );
			setVerbilligteRituale( profEins.getVerbilligteRituale() );
			setVerbilligteSonderf( profEins.getVerbilligteSonderf() );
			setVerbilligteVort( profEins.getVerbilligteVort() );
			setZauber( profEins.getZauber() );
			setZauberNichtBeginn( profEins.getZauberNichtBeginn() );

			// Hier werden die Boni mit dem 1,5-fachen Wert erzeugt.
			// TODO Bei den Eigenschaften werden die Variablen Auswahlen noch 
			// nicht richtig kopiert. Eigentlich muesste aus "LE oder AU +1"
			// ein "LE oder AU +2" werden. Das ist aber noch nicht der Fall.
			// Da das in den Regeln noch nicht vor kommt lass ich es, fuers 
			// erste, einfach mal weg.
			setEigenschaftModis( kopieren( profEins.getEigenschaftModis(), eigenschaften ) );
			setTalente( kopieren( profEins.getTalente(), talente ) );
			setSchriften( kopieren( profEins.getSchriften(), talente ) );
			setSprachen( kopieren( profEins.getSprachen(), talente ) );
		}
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
	
	/**
	 * Legt eine Kopie eines IdLinks an ohne dabei etwas zu veraendern.
	 */
	private LinkKopierer standard = new LinkKopierer();
}
