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

import com.drew.imaging.ImageMetadataReader;
import com.drew.imaging.ImageProcessingException;
import com.drew.metadata.Directory;
import com.drew.metadata.Metadata;
import com.drew.metadata.exif.ExifIFD0Directory;
import com.drew.metadata.exif.ExifSubIFDDirectory;
import de.jdufner.watchtracker.metadataextractor.configuration.MetadataextractorProperties;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;

/**
 * @author Jürgen Dufner
 * @since 0.3
 */
@Component
public class AufnahmedatumLeser {

  private MetadataextractorProperties metadataextractorProperties;

  public AufnahmedatumLeser(final MetadataextractorProperties metadataextractorProperties) {
    this.metadataextractorProperties = metadataextractorProperties;
  }

  public String leseZeitstempel(final String filename) throws ImageProcessingException, IOException {
    Resource imageResource = new ClassPathResource(filename);
    Metadata imageMetadata = ImageMetadataReader.readMetadata(imageResource.getFile());
    return getDateStringFromMetadata(imageMetadata, Datum.valueOf(metadataextractorProperties.getTimestamp()));
  }

  public String leseZeitstempel(final File file) throws ImageProcessingException, IOException {
    return getDateStringFromMetadata(file, Datum.valueOf(metadataextractorProperties.getTimestamp()));
  }

  private String getDateStringFromMetadata(final File file, final Datum datum) throws ImageProcessingException, IOException {
    final Metadata metadata = ImageMetadataReader.readMetadata(file);
    return getDateStringFromMetadata(metadata, datum);
  }

  private String getDateStringFromMetadata(final Metadata metadata, final Datum datum) {
    final Directory directory = metadata.getFirstDirectoryOfType(datum.getDirectory());
    return directory.getString(datum.getKey());
  }

  private enum Datum {

    ZEITSTEMPEL(ExifIFD0Directory.class, ExifIFD0Directory.TAG_DATETIME),
    DIGITALISIERUNGSZEITSTEMPEL(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_DIGITIZED),
    ORIGINALZEITSTEMPEL(ExifSubIFDDirectory.class, ExifSubIFDDirectory.TAG_DATETIME_ORIGINAL);

    private final Class<? extends Directory> directory;
    private final int key;

    Datum(final Class<? extends Directory> directory, final int key) {
      this.directory = directory;
      this.key = key;
    }

    private Class<? extends Directory> getDirectory() {
      return this.directory;
    }

    private int getKey() {
      return this.key;
    }

  }

}
