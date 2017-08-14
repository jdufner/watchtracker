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

package de.jdufner.watch.tracker.services;

import de.jdufner.watch.tracker.businessobjects.Abweichung;
import de.jdufner.watch.tracker.businessobjects.AbweichungTest;
import de.jdufner.watch.tracker.repositories.AbweichungRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.PageRequest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.anyLong;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Jürgen Dufner
 * @since 0.0
 */
@RunWith(MockitoJUnitRunner.class)
public class AbweichungServiceTest {

  @InjectMocks
  private AbweichungService abweichungService;

  @Mock
  private AbweichungRepository abweichungRepository;

  @Test
  public void testSave_whenErfassungszeitpunktIsNull_expectErfassungszeitpunktIsSet() {
    // arrange
    final Abweichung abweichung = AbweichungTest.AbweichungBuilder.defaultTestObjectBuilder().withErfassungszeitpunkt(null).build();
    when(abweichungRepository.save(any(Abweichung.class))).then(invocationOnMock -> {
      Abweichung savedAbweichung = (Abweichung) invocationOnMock.getArguments()[0];
      savedAbweichung.setId(1);
      return savedAbweichung;
    });

    // act
    final Abweichung savedAbweichung = abweichungService.saveAbweichung(abweichung);

    // assert
    verify(abweichungRepository).save(abweichung);
    assertThat(savedAbweichung.getId()).isEqualTo(1);
    assertThat(savedAbweichung.getErfassungszeitpunkt()).isNotNull();
  }

  @Test
  public void testSave_whenErfassungszeitpunktIsSet_expectErfassungszeitpunktIsUnchanged() {
    // arrange
    final Abweichung abweichung = AbweichungTest.AbweichungBuilder.defaultTestObjectBuilder().build();
    when(abweichungRepository.save(any(Abweichung.class))).then(invocationOnMock -> {
      Abweichung savedAbweichung = (Abweichung) invocationOnMock.getArguments()[0];
      savedAbweichung.setId(1);
      return savedAbweichung;
    });

    // act
    final Abweichung savedAbweichung = abweichungService.saveAbweichung(abweichung);

    // assert
    verify(abweichungRepository).save(abweichung);
    assertThat(savedAbweichung.getId()).isEqualTo(1);
    assertThat(savedAbweichung.getErfassungszeitpunkt()).isEqualTo(abweichung.getErfassungszeitpunkt());
  }

  @Test
  public void testFindAbweichungen_whenCalled_expectRepositoryCalled() {
    // arrange

    // act
    abweichungService.findAbweichungen(0);

    // assert
    verify(abweichungRepository).findAll(any(PageRequest.class));
  }

  @Test
  public void testDelete_whenCalled_expectRepositoryCalled() {
    // arrange

    // act
    abweichungService.deleteAbweichung(anyLong());

    // assert
    verify(abweichungRepository).delete(anyLong());
  }

}
