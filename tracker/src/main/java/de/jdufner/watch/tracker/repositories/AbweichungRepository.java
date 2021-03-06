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

package de.jdufner.watch.tracker.repositories;

import de.jdufner.watch.tracker.businessobjects.Abweichung;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.Date;
import java.util.List;

/**
 * @author Jürgen Dufner
 * @since 0.0
 */
public interface AbweichungRepository extends PagingAndSortingRepository<Abweichung, Long> {

  /**
   * @param erfassungszeitpunkt
   * @return the next older {@link Abweichung} than the given erfassungszeitpunkt
   * @since 0.2
   */
  Abweichung findFirstByErfassungszeitpunktBeforeOrderByErfassungszeitpunktDesc(Date erfassungszeitpunkt);

  /**
   * @param erfassungszeitpunktVon
   * @param erfassungszeitpunktBis
   * @return all {@link Abweichung}s between #erfassungszeitpunktVon and #erfassungszeitpunktBis
   * @since 0.2
   */
  List<Abweichung> findByErfassungszeitpunktAfterAndErfassungszeitpunktBeforeOrderByErfassungszeitpunktAsc(Date erfassungszeitpunktVon, Date erfassungszeitpunktBis);

}
