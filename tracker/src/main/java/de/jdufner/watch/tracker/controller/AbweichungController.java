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

package de.jdufner.watch.tracker.controller;

import de.jdufner.watch.tracker.businessobjects.Abweichung;
import de.jdufner.watch.tracker.services.AbweichungService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

/**
 * @author Jürgen Dufner
 * @since 0.0
 */
@Controller
public class AbweichungController {

  private static final Logger LOG = LoggerFactory.getLogger(AbweichungController.class);

  private final AbweichungService abweichungService;

  public AbweichungController(final AbweichungService abweichungService) {
    this.abweichungService = abweichungService;
  }

  @GetMapping("/tracker")
  public String trackerForm(final Model model) {
    model.addAttribute("abweichung", new Abweichung());
    return "tracker";
  }

  @PostMapping("/tracker")
  public String trackerSubmit(@ModelAttribute final Abweichung abweichung) {
    LOG.debug("{}", abweichung);
    abweichungService.saveAbweichung(abweichung);
    return "redirect:overview";
  }

  @GetMapping("/overview")
  public String overview(@RequestParam(value = "seite", required = false, defaultValue = "0") final int seite, final Model model) {
    final Page<Abweichung> abweichungen = abweichungService.findAbweichungen(seite);
    model.addAttribute("abweichungen", abweichungen);
    LOG.debug("totalElement={}, totalPages={}, hasPrevious={}, hasNext={}",
        abweichungen.getTotalElements(), abweichungen.getTotalPages(), abweichungen.hasPrevious(), abweichungen.hasNext());
    return "overview";
  }

  @DeleteMapping("/overview")
  public String deleteAbweichung(@RequestParam(value = "abweichungId", required = true) final String abweichungId) {
    LOG.debug("abweichungId={}", abweichungId);
    abweichungService.deleteAbweichung(Long.parseLong(abweichungId));
    return "redirect:overview";
  }

  @GetMapping("/upload")
  public String upload() {
    return "upload";
  }

  @PostMapping("/upload")
  public String uploadForm(@RequestParam("file") final MultipartFile file) {
    LOG.debug("name={}", file.getName());
    LOG.debug("empty={}", file.isEmpty());
    LOG.debug("contentType={}", file.getContentType());
    LOG.debug("originalFilename={}", file.getOriginalFilename());
    LOG.debug("size={}", file.getSize());
    return "redirect:upload";
  }

}
