/*
 * Copyright (c) 2017.
 *
 * This file is part of Watchtracker.
 *
 * Watchtracker is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * Watchtracker is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with Watchtracker.  If not, see <http://www.gnu.org/licenses/>.
 *
 * Diese Datei ist Teil von Watchtracker.
 *
 * Watchtracker ist Freie Software: Sie können es unter den Bedingungen
 * der GNU General Public License, wie von der Free Software Foundation,
 * Version 3 der Lizenz oder (nach Ihrer Wahl) jeder späteren
 * veröffentlichten Version, weiterverbreiten und/oder modifizieren.
 *
 * Watchtracker wird in der Hoffnung, dass es nützlich sein wird, aber
 * OHNE JEDE GEWÄHRLEISTUNG, bereitgestellt; sogar ohne die implizite
 * Gewährleistung der MARKTFÄHIGKEIT oder EIGNUNG FÜR EINEN BESTIMMTEN ZWECK.
 * Siehe die GNU General Public License für weitere Details.
 *
 * Sie sollten eine Kopie der GNU General Public License zusammen mit diesem
 * Programm erhalten haben. Wenn nicht, siehe <http://www.gnu.org/licenses/>.
 */

package de.jdufner.watch.tracker.businessobjects;

import lombok.Data;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

/**
 * @author Jürgen Dufner
 * @since 0.0
 */
@Data
@Entity
public class Abweichung {

  private static final long TAG_IN_MILLISEKUNDEN = 24 * 60 * 60 * 1000L;
  private static final long WOCHE_IN_MILLISEKUNDEN = 7 * TAG_IN_MILLISEKUNDEN;
  private static final long MONAT_IN_MILLISEKUNDEN = 4 * WOCHE_IN_MILLISEKUNDEN;

  @Id
  @GeneratedValue(strategy = GenerationType.SEQUENCE)
  private long id;

  @NotNull
  @Column(unique = true)
  private Date erfassungszeitpunkt;

  @NotNull
  private Integer differenz;

  private Integer korrektur;

  private Double abweichungProTagSeitLetzterMessung;

  private Double abweichungProTagInLetzterWoche;

  private Double abweichungProTagInLetztemMonat;

  public Date berechneErfassungszeitpunktVorEinerWoche() {
    return new Date(getErfassungszeitpunkt().getTime() - Abweichung.WOCHE_IN_MILLISEKUNDEN);
  }

  public Double berechneAbweichungProTag(final Abweichung vorigeAbweichung) {
    if (vorigeAbweichung == null) {
      return null;
    }
    return (double) berechneKorrigierteDifferenz(vorigeAbweichung) * TAG_IN_MILLISEKUNDEN / berechneDauerZwischenErfassungen(vorigeAbweichung);
  }

  public Double berechneAbweichungProTag(final List<Abweichung> abweichungen) {
    if (abweichungen == null || abweichungen.isEmpty()) {
      return null;
    }
    return (double) berechneKorrigierteDifferenz(abweichungen) * TAG_IN_MILLISEKUNDEN / berechneDauerZwischenErfassungen(abweichungen);
  }

  private Long berechneDauerZwischenErfassungen(final List<Abweichung> abweichungen) {
    return erfassungszeitpunkt.getTime() - abweichungen.get(0).getErfassungszeitpunkt().getTime();
  }

  private Integer berechneKorrigierteDifferenz(final List<Abweichung> abweichungen) {
    List<Pair> pairs = new LinkedList<>();
    abweichungen.stream().reduce((abweichung1, abweichung2) -> {
      pairs.add(new Pair(abweichung1, abweichung2));
      return abweichung2;
    });
    return pairs.stream()
        .mapToInt(Pair::berechneKorrigierteDifferenz)
        .sum();
  }

  private long berechneDauerZwischenErfassungen(final Abweichung vorigeAbweichung) {
    return erfassungszeitpunkt.getTime() - vorigeAbweichung.getErfassungszeitpunkt().getTime();
  }

  private int berechneKorrigierteDifferenz(final Abweichung vorigeAbweichung) {
    if (vorigeAbweichung.korrektur == null || vorigeAbweichung.korrektur == 0) {
      return differenz - vorigeAbweichung.differenz;
    }
    return differenz - vorigeAbweichung.korrektur;
  }

  private static class Pair {

    private final Abweichung vorigeAbweichung;
    private final Abweichung abweichung;

    private Pair(final Abweichung vorigeAbweichung, final Abweichung abweichung) {
      this.vorigeAbweichung = vorigeAbweichung;
      this.abweichung = abweichung;
    }

    private int berechneKorrigierteDifferenz() {
      return abweichung.berechneKorrigierteDifferenz(vorigeAbweichung);
    }
  }

}
