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
import de.jdufner.watch.tracker.repositories.AbweichungRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

/**
 * @author Jürgen Dufner
 * @since 0.0
 */
@Service
public class AbweichungService {

  @Value("${overview.page.size}")
  private int seitengroesse;

  private final AbweichungRepository abweichungRepository;

  public AbweichungService(final AbweichungRepository abweichungRepository) {
    this.abweichungRepository = abweichungRepository;
  }

  @Transactional
  public Abweichung saveAbweichung(final Abweichung abweichung) {
    vervollstaendigeDaten(abweichung);
    return abweichungRepository.save(abweichung);
  }

  private void vervollstaendigeDaten(final Abweichung abweichung) {
    vervollstaendigeErfassungszeitpunkt(abweichung);
    vervollstaendigeAbweichungenProTag(abweichung);
    vervollstaendigeAbweichungenProWoche(abweichung);
  }

  private void vervollstaendigeAbweichungenProTag(final Abweichung abweichung) {
    final Abweichung vorigeAbweichung = abweichungRepository.findFirstByErfassungszeitpunktBeforeOrderByErfassungszeitpunktDesc(
        abweichung.getErfassungszeitpunkt());
    final Double abweichungProTagSeitLetzterMessung = abweichung.berechneAbweichungProTag(vorigeAbweichung);
    abweichung.setAbweichungProTagSeitLetzterMessung(abweichungProTagSeitLetzterMessung);
  }

  private void vervollstaendigeAbweichungenProWoche(final Abweichung abweichung) {
    final List<Abweichung> abweichungen = abweichungRepository.findByErfassungszeitpunktAfterAndErfassungszeitpunktBeforeOrderByErfassungszeitpunktAsc(
        abweichung.berechneErfassungszeitpunktVorEinerWoche(), abweichung.getErfassungszeitpunkt());
    final Double abweichungProTagInLetzterWoche = abweichung.berechneAbweichungProTag(abweichungen);
    abweichung.setAbweichungProTagInLetzterWoche(abweichungProTagInLetzterWoche);
  }

  private void vervollstaendigeErfassungszeitpunkt(final Abweichung abweichung) {
    if (abweichung.getErfassungszeitpunkt() == null) {
      abweichung.setErfassungszeitpunkt(new Date());
    }
  }

  public Page<Abweichung> findAbweichungen(final int seite) {
    final PageRequest pageRequest = new PageRequest(seite, seitengroesse, new Sort(Sort.Direction.DESC, "erfassungszeitpunkt"));
    return abweichungRepository.findAll(pageRequest);
  }

  public Long count() {
    return abweichungRepository.count();
  }

  @Transactional
  public void deleteAbweichung(final long abweichungId) {
    abweichungRepository.delete(abweichungId);
  }

}
