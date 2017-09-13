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
 *
 */

package de.jdufner.watch.tracker.businessobjects;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.regex.Pattern;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jürgen Dufner
 * @since 0.0
 */
public class AbweichungTest {

  @Test
  public void whenToString_expectString() {
    // arrange
    final Abweichung abweichung = AbweichungBuilder.defaultTestObjectBuilder().build();

    // act
    final String s = abweichung.toString();

    // assert
    assertThat(s).containsPattern(Pattern.compile("Abweichung\\(id=1.*\\)"));
  }

  @Test
  public void testBerechneErfassungszeitpunktVorEinerWoche_whenCall_expectResult() {
    // arrange
    long l = 1000000000000L;
    Abweichung abweichung = new AbweichungBuilder().withErfassungszeitpunkt(new Date(l)).build();

    // act
    Date erfassungszeitpunktVorEinerWoche = abweichung.berechneErfassungszeitpunktVorEinerWoche();

    // assert
    assertThat(erfassungszeitpunktVorEinerWoche).isEqualTo(new Date(l - 7 * 24 * 60 * 60 * 1000));
  }

  @Test
  public void testBerechneAbweichungProTag_whenVorigeAbweichungIsNull_expectNull() {
    // arrange
    Abweichung abweichung = AbweichungBuilder.defaultTestObjectBuilder().build();

    // act
    Double abweichungProTag = abweichung.berechneAbweichungProTag((Abweichung) null);

    // assert
    assertThat(abweichungProTag).isNull();
  }

  @Test
  public void testBerechneAbweichungProTag_whenVorigeAbweichungEineSekundeProStunde_expectVierungzwandzigProTag() {
    // arrange
    Abweichung vorigeAbweichung = new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000)).withDifferenz(1).build();
    Abweichung abweichung = new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000 + 60 * 60 * 1000)).withDifferenz(0).build();

    // act
    Double abweichungProTag = abweichung.berechneAbweichungProTag(vorigeAbweichung);

    // assert
    assertThat(abweichungProTag).isEqualTo(-24);
  }

  @Test
  public void testBerechneAbweichungProTag_whenVorigeAbweichungEineSekundeProStundeMitKorrektur_expectVierungzwanzigProTag() {
    // arrange
    Abweichung vorigeAbweichung = new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000)).withDifferenz(10).withKorrektur(30).build();
    Abweichung abweichung = new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000 + 60 * 60 * 1000)).withDifferenz(29).build();

    // act
    Double abweichungProTag = abweichung.berechneAbweichungProTag(vorigeAbweichung);

    // assert
    assertThat(abweichungProTag).isEqualTo(-24);
  }

  @Test
  public void testBerechneAbweichungProTag_whenVorigeAbweichungJeweilsEineSekundeProStunde_expectVierundzwanzigProTag() {
    // arrange
    List<Abweichung> abweichungen = new ArrayList<>();
    abweichungen.add(new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000)).withDifferenz(10).withKorrektur(30).build());
    abweichungen.add(new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000 + 60 * 60 * 1000)).withDifferenz(29).build());
    abweichungen.add(new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000 + 2 * 60 * 60 * 1000)).withDifferenz(28).build());
    abweichungen.add(new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000 + 3 * 60 * 60 * 1000)).withDifferenz(27).build());
    Abweichung abweichung = new AbweichungBuilder().withErfassungszeitpunkt(new Date(1000000 + 4 * 60 * 60 * 1000)).withDifferenz(26).build();
    abweichungen.add(abweichung);

    // act
    Double abweichungProTag = abweichung.berechneAbweichungProTag(abweichungen);

    // assert
    assertThat(abweichungProTag).isEqualTo(-24);
  }

  public static class AbweichungBuilder {

    public static AbweichungBuilder defaultTestObjectBuilder() {
      return new AbweichungBuilder().withId(1).withErfassungszeitpunkt(new Date(0)).withDifferenz(3).withKorrektur(0);
    }

    private long id;
    private Date erfassungszeitpunkt;
    private int differenz;
    private int korrektur;

    public AbweichungBuilder withId(final long id) {
      this.id = id;
      return this;
    }

    public AbweichungBuilder withErfassungszeitpunkt(final Date erfassungszeitpunkt) {
      this.erfassungszeitpunkt = erfassungszeitpunkt;
      return this;
    }

    public AbweichungBuilder withDifferenz(final int differenz) {
      this.differenz = differenz;
      return this;
    }

    public AbweichungBuilder withKorrektur(final int korrektur) {
      this.korrektur = korrektur;
      return this;
    }

    public Abweichung build() {
      final Abweichung abweichung = new Abweichung();
      abweichung.setId(id);
      abweichung.setErfassungszeitpunkt(erfassungszeitpunkt);
      abweichung.setDifferenz(differenz);
      abweichung.setKorrektur(korrektur);
      return abweichung;
    }

  }

}
