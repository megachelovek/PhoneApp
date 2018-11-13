package com.example.danilius.phoneapp;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

@RunWith(JUnit4.class)
public class PhoneBookUnitTest {
    private PhoneBook phoneBook;

    @Before
    public void setUp() throws Exception {
        phoneBook = new PhoneBook("Test",new Long("999999999999"),"Test@test");
    }

    @Test
    public void testSum() throws Exception {
        assertTrue(phoneBook.getName()!=null && phoneBook.getPhone()!=null && phoneBook.getEmail()!=null);
    }

    @After
    public void tearDown() throws Exception {
        phoneBook = null;
    }
}
