package com.github.sdorra.jaxrstie;

import org.junit.Test;

import static org.junit.Assert.*;

public class NamesTest {

    @Test
    public void testCapitalizeFirstLetter() {
        assertEquals("Abc", Names.capitalizeFirstLetter("abc"));
        assertEquals("Abc", Names.capitalizeFirstLetter("Abc"));
        assertEquals("ABC", Names.capitalizeFirstLetter("aBC"));
    }

    @Test
    public void testDowncaseFirstLetter() {
        assertEquals("abc", Names.downcaseFirstLetter("Abc"));
        assertEquals("abc", Names.downcaseFirstLetter("abc"));
        assertEquals("aBC", Names.downcaseFirstLetter("aBC"));
    }

    @Test
    public void testRemovePrefixAndSuffixes() {
        assertEquals("Abc", Names.removePrefixAndSuffixes("getAbc"));
        assertEquals("Abc", Names.removePrefixAndSuffixes("AbcResource"));
        assertEquals("Abc", Names.removePrefixAndSuffixes("getAbcResource"));
        assertEquals("A", Names.removePrefixAndSuffixes("getA"));
        assertEquals("A", Names.removePrefixAndSuffixes("AResource"));
        assertEquals("get", Names.removePrefixAndSuffixes("get"));
        assertEquals("Resource", Names.removePrefixAndSuffixes("Resource"));
    }

}
