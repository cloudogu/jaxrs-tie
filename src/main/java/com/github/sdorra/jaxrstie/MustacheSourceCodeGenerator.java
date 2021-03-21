package com.github.sdorra.jaxrstie;

import com.github.mustachejava.DefaultMustacheFactory;
import com.github.mustachejava.Mustache;
import com.google.googlejavaformat.java.Formatter;
import com.google.googlejavaformat.java.FormatterException;

import java.io.IOException;
import java.io.StringWriter;
import java.io.Writer;

public class MustacheSourceCodeGenerator implements SourceCodeGenerator {

  private static final String TEMPLATE = "com/github/sdorra/jaxrstie/template.mustache";

  @Override
  public void generate(Writer writer, Model model) throws IOException {
    writer.write(generateSourceCode(model));
  }

  private String generateSourceCode(Model model) {
    StringWriter writer = new StringWriter();
    Mustache mustache = new DefaultMustacheFactory().compile(TEMPLATE);
    mustache.execute(writer, model);
    return format(writer.toString());
  }

  private String format(String code) {
    Formatter formatter = new Formatter();
    try {
      return formatter.formatSource(code);
    } catch (FormatterException ex) {
      throw new IllegalStateException("failed to format source code", ex);
    }
  }

}
