package io.github.erictanggit.rules;

import static org.junit.jupiter.api.Assertions.*;
import org.junit.Test;

public class RulesTest {

	@Test
	public void test_BE_AN_EMAIL() {
		assertTrue(Rules.BE_AN_EMAIL("java@gmail.com"));
		assertFalse(Rules.BE_AN_EMAIL("javagmail.com"));
	}

	@Test
	public void test_BE_AN_DAUPHINE_EMAIL() {
		assertFalse(Rules.BE_AN_DAUPHINE_EMAIL("java@gmail.com"));
		assertTrue(Rules.BE_AN_DAUPHINE_EMAIL("java@dauphine.eu"));
	}

	@Test
	public void test_BE_AN_AGE() {
		assertTrue(Rules.BE_AN_AGE("0"));
		assertTrue(Rules.BE_AN_AGE("120"));
		assertFalse(Rules.BE_AN_AGE("-15"));
	}

	@Test
	public void test_RANDOM_LETTER_FOR_LOCAL_PART() {
		assertFalse("test@gmail.com".equalsIgnoreCase(Rules.RANDOM_LETTER_FOR_LOCAL_PART("test@gmail.com")));
	}

	@Test
	public void test_RANDOM_LETTER() {
		assertFalse("test".equalsIgnoreCase(Rules.RANDOM_LETTER("test")));
	}

}
