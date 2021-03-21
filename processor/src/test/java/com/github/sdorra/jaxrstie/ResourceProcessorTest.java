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

    @Test
    public void testWithValidation() {
        with("F", "ResourceWithValidation")
                .processor(new ResourceProcessor())
                .expect("FLinks");
    }

    public InputStep with(String... classes) {
        return new InputStep(Lists.newArrayList(classes));
    }

    private static class InputStep {
        private final List<JavaFileObject> input;
        private Processor processor;

        public InputStep(List<String> input) {
            this.input = input.stream()
                    .map(this::resource)
                    .collect(Collectors.toList());
        }

        private JavaFileObject resource(String className) {
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
