package com.github.sdorra.jaxrstie;

import java.io.IOException;
import java.io.Writer;

public interface SourceCodeGenerator {

  void generate(Writer writer, Model model) throws IOException;

}
