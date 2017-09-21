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

package de.jdufner.watch.tracker.services;

import de.jdufner.watch.tracker.businessobjects.Abweichung;
import de.jdufner.watch.tracker.repositories.AbweichungRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;
import org.springframework.data.domain.Sort;
import org.springframework.util.ReflectionUtils;

import java.lang.reflect.Field;
import java.util.Collections;

import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

/**
 * @author Jürgen Dufner
 * @since 0.2
 */
@RunWith(MockitoJUnitRunner.class)
public class AktualisierungsServiceTest {

  @InjectMocks
  private AktualisierungsService cut;

  @Mock
  private AbweichungService abweichungService;
  @Mock
  private AbweichungRepository abweichungRepository;

  @Test
  public void testUpdateAll_whenUpdateData_expectAbweichungServiceCalled() {
    // arrange
    Field updateData = ReflectionUtils.findField(AktualisierungsService.class, "updateData");
    ReflectionUtils.makeAccessible(updateData);
    ReflectionUtils.setField(updateData, cut, true);
    when(abweichungRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(mock(Abweichung.class)));

    // act
    cut.updateAll();

    // assert
    verify(abweichungService).saveAbweichung(any(Abweichung.class));
  }

  @Test
  public void testUpdateAll_whenNotUpdateData_expectAbweichungServiceNotCalled() {
    // arrange
    when(abweichungRepository.findAll(any(Sort.class))).thenReturn(Collections.singletonList(mock(Abweichung.class)));

    // act
    cut.updateAll();

    // assert
    verify(abweichungService, times(0)).saveAbweichung(any(Abweichung.class));
  }

}
