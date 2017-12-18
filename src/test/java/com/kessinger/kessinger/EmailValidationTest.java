package com.kessinger.kessinger;

import junit.framework.TestCase;
import org.junit.Assert;
import org.junit.Test;

public class EmailValidationTest extends TestCase{
    @Test
    public void testShouldPassIfValidEmail()
    {
        Assert.assertTrue(Validacao.isEmailValid("teste@gmail.com"));
    }

    public void testShouldFailIfInvalidEmail()
    {
        Assert.assertTrue(Validacao.isEmailValid("@gmail"));
    }

    public void testShouldFailIfSimpleString()
    {
        Assert.assertTrue(Validacao.isEmailValid("teste"));
    }

    public void testShouldFailIfOnlyAt()
    {
        Assert.assertTrue(Validacao.isEmailValid("@"));
    }

    public void testShouldFailIfItLacksDotCom()
    {
        Assert.assertTrue(Validacao.isEmailValid("teste@gmail"));
    }

    public void testShouldFailWhenEmptyString()
    {
        Assert.assertTrue(Validacao.isEmailValid(""));
    }

    public void testShouldFailWhenSpecialCharacters()
    {
        Assert.assertTrue(Validacao.isEmailValid("$%^@@#$%%%"));
    }
}
