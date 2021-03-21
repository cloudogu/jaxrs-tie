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
