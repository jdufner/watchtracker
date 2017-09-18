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

package de.jdufner.watchtracker.metadataextractor.service;

import de.jdufner.watchtracker.metadataextractor.configuration.MetadataextractorConfig;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import static org.junit.Assert.assertEquals;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MetadataextractorConfig.class})
public class AufnahmedatumLeserCT {

  @Autowired
  private AufnahmedatumLeser aufnahmedatumLeser;

  @Test
  public void testLeseZeitstempel_whenImageShotByFujiX10_expectDigitizedTimestamp() throws Exception {
    // act
    String zeitstempel = aufnahmedatumLeser.leseZeitstempel("Sonnenblume.jpg");

    // assert
    assertEquals("2017:07:29 13:08:54", zeitstempel);
  }

  @Test
  public void testLeseZeitstempel_whenImageShotBySamsungS4_expectDigitizedTimestamp() throws Exception {
    // act
    String zeitstempel = aufnahmedatumLeser.leseZeitstempel("Tempic_20170819_090757.jpg");

    // assert
    assertEquals("2017:08:19 09:07:56", zeitstempel);
  }

}