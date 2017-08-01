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

package de.jdufner.watch.tracker.repositories;

import de.jdufner.watch.tracker.businessobjects.Abweichung;
import de.jdufner.watch.tracker.businessobjects.AbweichungTest;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.junit4.SpringRunner;

import java.util.Date;

import static org.assertj.core.api.Assertions.assertThat;

/**
 * @author Jürgen Dufner
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@DataJpaTest
public class AbweichungRepositoryIT {

  private static final Logger log = LoggerFactory.getLogger(AbweichungRepositoryIT.class);

  @Autowired
  private TestEntityManager entityManager;

  @Autowired
  private AbweichungRepository abweichungRepository;

  @Test
  public void testFindAll() {
    // arrange
    entityManager.merge(AbweichungTest.AbweichungBuilder.defaultTestObjectBuilder().build());

    // act
    Iterable<Abweichung> abweichungen = abweichungRepository.findAll();

    // assert
    assertThat(abweichungen).isNotNull().isNotEmpty();
  }

  @Test
  public void testFindFirstByErfassungszeitpunktBeforeOrderByErfassungszeitpunktDesc() {
    // arrange
    entityManager.merge(AbweichungTest.AbweichungBuilder.defaultTestObjectBuilder().withKorrektur(1).withErfassungszeitpunkt(new Date(1000000L)).build());
    entityManager.merge(AbweichungTest.AbweichungBuilder.defaultTestObjectBuilder().withKorrektur(2).withErfassungszeitpunkt(new Date(2000000L)).build());
    entityManager.merge(AbweichungTest.AbweichungBuilder.defaultTestObjectBuilder().withKorrektur(3).withErfassungszeitpunkt(new Date(3000000L)).build());


    // act
    Abweichung abweichung = abweichungRepository.findFirstByErfassungszeitpunktBeforeOrderByErfassungszeitpunktDesc(new Date(3000000L));

    // assert
    log.debug("{}", abweichung);
    assertThat(abweichung.getKorrektur()).isEqualTo(2);
  }

}
