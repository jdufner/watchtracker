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
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Iterator;

import static org.hamcrest.CoreMatchers.containsString;
import static org.mockito.Matchers.anyInt;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.fileUpload;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Jürgen Dufner
 * @since 0.1
 */
@RunWith(SpringRunner.class)
@WebMvcTest(AbweichungController.class)
public class AbweichungControllerTest {

  @Autowired
  private MockMvc mockMvc;

  @MockBean
  private AbweichungService abweichungService;

  @Test
  public void whenGetTracker_expectTracker() throws Exception {
    mockMvc.perform(get("/tracker"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Erfassung")));
  }

  @Test
  public void whenPostTracker_expectOverview() throws Exception {
    mockMvc.perform(post("/tracker"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", containsString("overview")));
  }

  @Test
  public void whenGetOverview_expectOverview() throws Exception {
    // arrange
    Page<Abweichung> page = mock(Page.class);
    when(page.iterator()).thenReturn((Iterator<Abweichung>) mock(Iterator.class));
    when(abweichungService.findAbweichungen(anyInt())).thenReturn(page);

    // act + assert
    mockMvc.perform(get("/overview"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Übersicht")));
  }

  @Test
  public void whenDeleteOverview_expectOverview() throws Exception {
    // act + assert
    mockMvc.perform(delete("/overview").param("abweichungId", "1"))
        .andDo(print())
        .andExpect(status().is3xxRedirection())
        .andExpect(header().string("Location", containsString("overview")));
  }

  @Test
  public void whenGetUpload_expectPage() throws Exception {
    // act + assert
    mockMvc.perform(get("/upload"))
        .andDo(print())
        .andExpect(status().isOk())
        .andExpect(content().string(containsString("Upload")));
  }

  @Test
  public void whenPostUpload_expectFile() throws Exception {
    // arrange
    MockMultipartFile multipartFile = new MockMultipartFile("file", "image.jpg", "image/jpg", (byte[]) null);

    // act + assert
    mockMvc.perform(fileUpload("/upload").file(multipartFile))
        .andDo(print())
        .andExpect(status().isFound())
        .andExpect(header().string("Location", "upload"));
  }

}
