package com.github.sdorra.jaxrstie;

import com.google.common.collect.Lists;
import com.google.common.truth.Truth;
import com.google.testing.compile.JavaFileObjects;
import com.google.testing.compile.JavaSourcesSubjectFactory;
import org.junit.Test;

import javax.annotation.processing.Processor;
import javax.tools.JavaFileObject;
import java.util.List;
import java.util.stream.Collectors;

public class ResourceProcessorTest {
    @Test
    public void testWithoutResources() {
        with("A")
            .processor(new ResourceProcessor())
            .expect("ALinks");
    }

    @Test
    public void testWithSimpleResource() {
        with("B", "SimpleResource")
                .processor(new ResourceProcessor())
                .expect("BLinks");
    }

    @Test
    public void testWithPatternMatching() {
        with("C", "SimpleResource")
                .processor(new ResourceProcessor())
                .expect("CLinks");
    }

    @Test
    public void testWithSubResource() {
        with("D", "SubResource")
                .processor(new ResourceProcessor())
                .expect("DLinks");
    }

    @Test
    public void testWithSubWithParamResource() {
        with("E", "SubResourceWithParam")
                .processor(new ResourceProcessor())
                .expect("ELinks");
    }

    public InputStep with(String... classes) {
        return new InputStep(Lists.newArrayList(classes));
    }

    private static class InputStep {
        private List<JavaFileObject> input;
        private Processor processor;

        public InputStep(List<String> input) {
            this.input = input.stream()
                    .map(this::resource)
                    .collect(Collectors.toList());
        }

        private final JavaFileObject resource(String className) {
            return JavaFileObjects.forResource("com/github/sdorra/jaxrstie/" + className + ".java");
        }

        InputStep processor(Processor processor) {
            this.processor = processor;
            return this;
        }

        void expect(String output) {
            Truth.assert_()
                    .about(JavaSourcesSubjectFactory.javaSources())
                    .that(input)
                    .processedWith(processor)
                    .compilesWithoutError()
                    .and()
                    .generatesSources(resource(output));
        }
    }

}
