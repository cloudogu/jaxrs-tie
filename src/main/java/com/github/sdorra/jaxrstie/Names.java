/*
 * MIT License
 *
 * Copyright (c) 2021, Sebastian Sdorra
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package com.github.sdorra.jaxrstie;

import javax.lang.model.element.Element;
import java.util.Locale;

public final class Names {

  private Names() {
  }

  public static String removePrefixAndSuffixes(String name) {
    String newName = removePrefix(name, "get");
    newName = removeSuffix(newName, "Resource");
    return newName;
  }

  private static String removePrefix(String name, String prefix) {
    if (name.startsWith(prefix) && name.length() > prefix.length()) {
      return name.substring(prefix.length());
    }
    return name;
  }

  private static String removeSuffix(String name, String suffix) {
    if (name.endsWith(suffix) && name.length() > suffix.length()) {
      return name.substring(0, name.length() - suffix.length());
    }
    return name;
  }

  public static String capitalizeFirstLetter(String name) {
    if (name.length() == 1) {
      return name.toUpperCase(Locale.ENGLISH);
    }
    return name.substring(0, 1).toUpperCase(Locale.ENGLISH) + name.substring(1);
  }

  public static String downcaseFirstLetter(String name) {
    if (name.length() == 1) {
      return name.toLowerCase(Locale.ENGLISH);
    }
    return name.substring(0, 1).toLowerCase(Locale.ENGLISH) + name.substring(1);
  }

  public static String methodName(String name) {
    return downcaseFirstLetter(removePrefixAndSuffixes(name));
  }

  public static String className(String name) {
    return capitalizeFirstLetter(removePrefixAndSuffixes(name));
  }

  public static String of(Element element) {
    return element.getSimpleName().toString();
  }
}
