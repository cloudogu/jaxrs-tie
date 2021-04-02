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

package com.github.sdorra.jaxrstie.internal;

import com.google.common.base.Joiner;

import javax.annotation.Nonnull;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;

public class MethodParameters implements Iterable<MethodParameter> {

    private final List<MethodParameter> parameters;

    public MethodParameters(List<MethodParameter> parameters) {
        this.parameters = parameters;
    }

    public ParameterNames getNames() {
        return new ParameterNames(parameters.stream().map(MethodParameter::getName).collect(Collectors.toList()));
    }

    public boolean isEmpty() {
        return parameters.isEmpty();
    }

    public static MethodParameters createEmpty() {
        return new MethodParameters(Collections.emptyList());
    }

    @Nonnull
    @Override
    public Iterator<MethodParameter> iterator() {
        return parameters.iterator();
    }

    @Override
    public String toString() {
        return Joiner.on(", ").join(parameters);
    }

    public static class ParameterNames implements Iterable<String> {

        private final List<String> names;

        public ParameterNames(List<String> names) {
            this.names = names;
        }

        @Nonnull
        @Override
        public Iterator<String> iterator() {
            return names.iterator();
        }

        @Override
        public String toString() {
            return Joiner.on(", ").join(names);
        }
    }
}
