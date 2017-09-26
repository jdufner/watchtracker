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

package de.jdufner.watch.tracker.config;

import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.config.ConfigurableListableBeanFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;

import java.io.File;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.springframework.core.annotation.AnnotationUtils.findAnnotation;

/**
 * @author Jürgen Dufner
 * @since 0.3
 */
public class SeleniumTestExecutionListener extends AbstractTestExecutionListener {

  private WebDriver webDriver;

  public int getOrder() {
    return Ordered.HIGHEST_PRECEDENCE;
  }

  @Override
  public void prepareTestInstance(TestContext testContext) throws Exception {
    if (webDriver != null) {
      return;
    }
    ApplicationContext context = testContext.getApplicationContext();
    if (context instanceof ConfigurableApplicationContext) {

      SeleniumTest annotation = findAnnotation(
          testContext.getTestClass(), SeleniumTest.class);
      webDriver = BeanUtils.instantiate(annotation.driver());

      ConfigurableApplicationContext configurableApplicationContext = (ConfigurableApplicationContext) context;
      ConfigurableListableBeanFactory bf = configurableApplicationContext.getBeanFactory();
      bf.registerResolvableDependency(WebDriver.class, webDriver);
    }
  }

  @Override
  public void beforeTestMethod(TestContext testContext) throws Exception {
    if (webDriver != null) {
      SeleniumTest annotation = findAnnotation(
          testContext.getTestClass(), SeleniumTest.class);
      webDriver.get(annotation.baseUrl());
    }
  }

  @Override
  public void afterTestMethod(TestContext testContext) throws Exception {
    if (testContext.getTestException() == null) {
      return;
    }
    File screenshot = ((TakesScreenshot) webDriver).getScreenshotAs(OutputType.FILE);
    String testName = toLowerUnderscore(testContext.getTestClass().getSimpleName());
    String methodName = toLowerUnderscore(testContext.getTestMethod().getName());
    Files.copy(screenshot.toPath(),
        Paths.get("screenshots", testName + "_" + methodName + "_" + screenshot.getName()));
  }

  @Override
  public void afterTestClass(TestContext testContext) throws Exception {
    if (webDriver != null) {
      webDriver.quit();
    }
  }

  private String toLowerUnderscore(String upperCamel) {
    return Stream
        .of(upperCamel.split("(?=[A-Z])"))
        .map(String::toLowerCase)
        .collect(Collectors.joining("_"));
  }

}
