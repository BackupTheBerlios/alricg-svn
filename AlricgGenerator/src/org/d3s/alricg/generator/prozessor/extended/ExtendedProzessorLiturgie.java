package org.d3s.alricg.generator.prozessor.extended;

import org.d3s.alricg.generator.prozessor.GeneratorLink;
import org.d3s.alricg.store.charElemente.Liturgie;

public interface ExtendedProzessorLiturgie {
	
	/**
	 * Diese Methode sollte benutzt werden, um W�hlbare Werte einer Liturgie zu
	 * bestimmen.
	 * @param link Link mit einer Liturgie
	 * @return Ein sortiertes Array mit allen Werten, die f�r diese Liturgie g�ltig sind. Sind
	 * 	KEINE Werte g�ltig, wird "null" zur�ckgeliefert.
	 */
	public Integer[] getPossibleLiturgieWerte(GeneratorLink link);
	
	public Integer[] getPossibleLiturgieWerte(Liturgie liturgie);
}
