package io.github.erictanggit.rules;

import java.lang.reflect.Method;
import java.util.regex.Pattern;

public class Rules {
	public static Method getMethod(String name, Class<?>... parameterTypes)
			throws NoSuchMethodException, SecurityException {
		return Rules.class.getMethod(name, parameterTypes);

	}

	/**
	 * 
	 * @param email the string to test
	 * @return true if respect the pattern of email
	 */
	public static boolean BE_AN_EMAIL(String email) {

		if (email == null) {
			return false;
		}

		String str_pattern = "^(.+)@(.+)\\.(.+)$";
		Pattern pattern = Pattern.compile(str_pattern);

		return pattern.matcher(email).matches();
	}

	/**
	 * 
	 * @param email the string to test
	 * @return true if respect the pattern of email dauphine
	 */

	public static boolean BE_AN_DAUPHINE_EMAIL(String email) {

		if (email == null) {
			return false;
		}
		String str_pattern = "^(.+)@(dauphine(\\.|\\.psl\\.)eu|lamsade(\\.)dauphine(\\.)fr)$";
		Pattern pattern = Pattern.compile(str_pattern);
		return (pattern.matcher(email).matches());
	}

	/**
	 * 
	 * @param age the string to test
	 * @return true if age >= 0 and <= 120
	 */
	public static boolean BE_AN_AGE(String age) {
		if (age == null) {
			return false;
		}
		try {
			int i = Integer.parseInt(age);
			if (i >= 0 && i <= 120)
				return true;
			return false;
		} catch (NumberFormatException er) {
			return false;
		}
	}

	/**
	 * 
	 * @param email the string to modify
	 * @return new email after anonymization of local part
	 */
	public static String RANDOM_LETTER_FOR_LOCAL_PART(String email) {

		String[] parts_email = email.split("@");
		String beforeAt = parts_email[0];
		String afterAt = parts_email[1];

		beforeAt = RANDOM_LETTER(beforeAt);
		return beforeAt + "@" + afterAt;
	}

	/**
	 * 
	 * @param s the string to modify
	 * @return the new string obtained
	 */
	public static String RANDOM_LETTER(String s) {
		if (s == null) {
			throw new IllegalStateException();
		}

		String str_anonymize = "";
		int c = 0;
		for (int i = 0; i < s.length(); i++) {
			c = s.charAt(i) ^ 48;
			str_anonymize = str_anonymize + (char) c;
		}
		return str_anonymize;
	}
}
