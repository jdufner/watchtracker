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

package de.jdufner.watchtracker.metadataextractor.service;

import de.jdufner.watchtracker.metadataextractor.configuration.MetadataextractorProperties;
import de.jdufner.watchtracker.metadataextractor.exception.MetadataextractorException;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.ExpectedException;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import java.io.File;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

/**
 * @author Jürgen Dufner
 * @since 0.3
 */
@RunWith(MockitoJUnitRunner.class)
public class AufnahmedatumLeserTest {

  @Rule
  public ExpectedException expectedException = ExpectedException.none();

  @InjectMocks
  private AufnahmedatumLeser aufnahmedatumLeser;

  @Mock
  private MetadataextractorProperties metadataextractorProperties;

  @Test
  public void testLeseOriginalZeitstempel() throws Exception {
    // arrange
    final File file = new File("src/test/resources/Sonnenblume.jpg");
    when(metadataextractorProperties.getTimestamp()).thenReturn("ORIGINALZEITSTEMPEL");

    // act
    String aufnahmedatum = aufnahmedatumLeser.leseZeitstempel(file);

    // assert
    assertEquals("2017:07:29 13:08:54", aufnahmedatum);
  }

  @Test
  public void testLeseDigitalisierungsZeitstempel() throws Exception {
    // arrange
    final File file = new File("src/test/resources/Sonnenblume.jpg");
    when(metadataextractorProperties.getTimestamp()).thenReturn("DIGITALISIERUNGSZEITSTEMPEL");

    // act
    String aufnahmedatum = aufnahmedatumLeser.leseZeitstempel(file);

    // assert
    assertEquals("2017:07:29 13:08:54", aufnahmedatum);
  }

  @Test
  public void testLeseZeitstempel() throws Exception {
    // arrange
    final File file = new File("src/test/resources/Sonnenblume.jpg");
    when(metadataextractorProperties.getTimestamp()).thenReturn("ZEITSTEMPEL");

    // act
    String aufnahmedatum = aufnahmedatumLeser.leseZeitstempel(file);

    // assert
    assertEquals("2017:09:16 22:37:02", aufnahmedatum);
  }

  @Test
  public void testLeseZeitstempel_whenFileNotExists_expectException() throws Exception {
    // arrange
    expectedException.expect(MetadataextractorException.class);
    final File file = new File("not_existing_file.jpg");
    when(metadataextractorProperties.getTimestamp()).thenReturn("DIGITALISIERUNGSZEITSTEMPEL");

    // act
    String aufnahmedatum = aufnahmedatumLeser.leseZeitstempel(file);
  }

  @Test
  public void testLeseZeitstempel_whenFileNameNotExists_expectException() throws Exception {
    // arrange
    expectedException.expect(MetadataextractorException.class);
    final String fileName = "not_existing_file.jpg";

    // act
    String aufnahmedatum = aufnahmedatumLeser.leseZeitstempel(fileName);
  }

  @Test
  public void testLeseZeitstempel_whenFileIsNoPicture_expectException() throws Exception {
    // arrange
    expectedException.expect(MetadataextractorException.class);
    final String fileName = "metadataextractor.properties";
    when(metadataextractorProperties.getTimestamp()).thenReturn("DIGITALISIERUNGSZEITSTEMPEL");

    // act
    String aufnahmedatum = aufnahmedatumLeser.leseZeitstempel(fileName);
  }

}
