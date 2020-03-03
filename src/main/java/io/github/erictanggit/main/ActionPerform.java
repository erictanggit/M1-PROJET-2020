package io.github.erictanggit.main;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;

import com.univocity.parsers.common.processor.RowListProcessor;
import com.univocity.parsers.csv.CsvParser;
import com.univocity.parsers.csv.CsvParserSettings;
import com.univocity.parsers.csv.CsvWriter;
import com.univocity.parsers.csv.CsvWriterSettings;

import io.github.erictanggit.logger.Logger;
import io.github.erictanggit.model.DescriptionAnonymizationRules;
import io.github.erictanggit.model.DescriptionCSV;
import io.github.erictanggit.model.DescriptionRules;
import io.github.erictanggit.model.Type;
import io.github.erictanggit.rules.Rules;

public class ActionPerform {

	/**
	 * 
	 * @param myCsv    the content of the file
	 * @param c        list of field type
	 * @param r        list of rule for each type
	 * @param fileName the csv with the data verified
	 * @throws URISyntaxException
	 * @throws IOException
	 */
	public static void wantToVerifDataFile(List<String[]> myCsv, DescriptionCSV[] c, DescriptionRules[] r,
			String fileName) throws URISyntaxException, IOException {
		ArrayList<String[]> rowToWrite = new ArrayList<>();
		List<String> rules;
		rowToWrite.add(myCsv.remove(0));
		for (String[] row : myCsv) {
			boolean canAddRowInCSV = true;
			for (int i = 0; i < row.length; i++) {
				if (!checkColumnType(row[i], c[i].getType())) {
					Logger.warn(ActionPerform.class, "wrong type for " + row[i] + " it must be: " + c[i].getType());
					canAddRowInCSV = false;
					break;
				}
				// rowToWrite.get(0)[i] nom de la colonne a tester.
				if (!(rules = getRulesColumn(rowToWrite.get(0)[i], r)).isEmpty()) {

					try {
						if (!checkColumnRules(row[i], rules)) {
							canAddRowInCSV = false;
							Logger.warn(ActionPerform.class, "Rule " + row[i] + "must respect the following rules : ");
							for (String string : rules) {
								Logger.warn(ActionPerform.class, Arrays.asList(rules));
							}
							break;
						}
					} catch (NoSuchMethodException | SecurityException | IllegalAccessException
							| IllegalArgumentException | InvocationTargetException e) {
						Logger.error(ActionPerform.class, "an error occured " + e.toString());
					}

				}
			}
			if (canAddRowInCSV) {
				rowToWrite.add(row);
			} else {
				Logger.warn(ActionPerform.class, "Default value is applied");
				rowToWrite.add(new String[] { "" });
			}
		}
		writeIntoCSVFile(new File("src/main/resources/results/" + fileName), rowToWrite);
	}

	/**
	 * 
	 * @return a string instance used for reflection process
	 */
	public static Class[] getClassInstance() {
		Class[] paramString = new Class[1];
		paramString[0] = String.class;

		return paramString;
	}

	/**
	 * 
	 * @param columnName the field needed
	 * @param r          all rules parsed i JSON file
	 * @return all rules associated to @param columnName
	 */
	public static List<String> getRulesColumn(String columnName, DescriptionRules[] r) {

		List<String> rulesToVerify = new ArrayList<String>();
		for (int i = 0; i < r.length; i++) {
			if (r[i].getName().equalsIgnoreCase(columnName)) {
				rulesToVerify.addAll(r[i].getRules());
			}
		}

		return rulesToVerify;
	}

	/**
	 * 
	 * @param val   is the field value to test with each rule
	 * @param rules all rules to verify for a data
	 * @return true if @param val respect all rules
	 * @throws NoSuchMethodException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static boolean checkColumnRules(String val, List<String> rules)
			throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {

		Class[] paramString = getClassInstance();
		for (String rule : rules) {
			Method method = Rules.getMethod(rule, paramString);
			boolean res = (boolean) method.invoke(null, val);
			if (!res)
				return res;
		}
		return true;

	}

	/**
	 * 
	 * @param val  is the val to anonymize
	 * @param rule is the rule to apply for anonymize val
	 * @return the new val obtained
	 * @throws NoSuchMethodException
	 * @throws SecurityException
	 * @throws IllegalAccessException
	 * @throws InvocationTargetException
	 */
	public static String dataAnonymized(String val, String rule)
			throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {

		Class[] paramString = getClassInstance();
		Method method = Rules.getMethod(rule, paramString);
		return (String) method.invoke(null, val);

	}

	/**
	 * 
	 * @param s the string to check
	 * @return true if s could be parse to int
	 */
	public static boolean isInt(String s) {
		try {
			int i = Integer.parseInt(s);
			return true;
		} catch (NumberFormatException er) {
			return false;
		}
	}

	/**
	 * 
	 * @param s the string to check
	 * @return true if s could be parse to double
	 */
	public static boolean isDouble(String s) {
		try {
			double i = Double.parseDouble(s);
			return true;
		} catch (NumberFormatException er) {
			return false;
		}
	}

	/**
	 * 
	 * @param val  is the val of one column
	 * @param type is the type of val
	 * @return true if val could be parsed in type
	 */
	public static boolean checkColumnType(String val, Type type) {

		switch (type) {
		case INT:
			return isInt(val);
		case DOUBLE:
			return isDouble(val);
		case STRING:
			return !isInt(val) && !isDouble(val);
		default:
			return false;
		}
	}

	/**
	 * 
	 * @param fileName is the output file
	 * @param rows     the content to write
	 */
	public static void writeIntoCSVFile(File fileName, List<String[]> rows) {

		CsvWriterSettings settings = new CsvWriterSettings();
		settings.setEmptyValue("error wrong format");
		settings.setHeaders(rows.remove(0));
		CsvWriter writer = new CsvWriter(fileName, settings);
		writer.writeHeaders();
		rows.forEach(row -> writer.writeRow(row));
		writer.close();
	}

	/**
	 * @return csv parser settings
	 */

	public static CsvParserSettings getParserSetting(RowListProcessor rowProcessor) {

		CsvParserSettings parserSettings = new CsvParserSettings();
		parserSettings.setLineSeparatorDetectionEnabled(true);
		parserSettings.setHeaderExtractionEnabled(true);
		parserSettings.setProcessor(rowProcessor);
		return parserSettings;

	}

	/**
	 * 
	 * @param fileName is the input file
	 * @return all lines in a list<String[]>
	 */
	public static <T> List<String[]> readFile(File fileName) {

		RowListProcessor rowProcessor = new RowListProcessor();
		CsvParserSettings parserSettings = getParserSetting(rowProcessor);
		CsvParser parser = new CsvParser(parserSettings);
		parser.parse(fileName);
		List<String[]> rows = new ArrayList<String[]>();
		rows.add(rowProcessor.getHeaders());
		rows.addAll(rowProcessor.getRows());
		for (String[] strings : rows) {
			Logger.info(ActionPerform.class, "CSV Content:" + Arrays.asList(strings));
		}
		return rows;

	}

	/**
	 * 
	 * @param myCsv    the content of the file
	 * @param list     of field type
	 * @param a        list of anonynimization rule
	 * @param fileName output file
	 */
	public static void wantToAnanonymizeDataFile(List<String[]> myCsv, DescriptionCSV[] c,
			DescriptionAnonymizationRules[] a, String fileName) {
		ArrayList<String[]> rowToWrite = new ArrayList<>();
		rowToWrite.add(myCsv.remove(0));
		String rule = "";
		LinkedHashSet<String> headerColumnAnonymised = new LinkedHashSet<String>();
		for (String[] row : myCsv) {
			ArrayList<String> rowValueAnonymized = new ArrayList<>();
			for (int i = 0; i < row.length; i++) {
				if (!(rule = wantToBeAnonimized(c[i].getName(), a)).isEmpty()) {

					headerColumnAnonymised.add(c[i].getName());

					if (checkColumnType(row[i], c[i].getType())) {
						try {
							Logger.info(ActionPerform.class,
									"anonymization field : " + row[i] + ", applying the rule :" + rule);
							rowValueAnonymized.add(dataAnonymized(row[i], rule));
						} catch (NoSuchMethodException | SecurityException | IllegalAccessException
								| IllegalArgumentException | InvocationTargetException e) {
							Logger.error(ActionPerform.class, "an error occured " + e.toString());
						}
					} else {
						Logger.info(ActionPerform.class, "default value will be applied");
						rowValueAnonymized.add("");
					}
				}
			}

			rowToWrite.add(convertListMapToArray(rowValueAnonymized));
		}

		rowToWrite.set(0, convertListMapToArray(headerColumnAnonymised)); // reset only header of column that will be
																			// anonymized
		writeIntoCSVFile(new File("src/main/resources/results/" + fileName), rowToWrite);
	}

	/**
	 * 
	 * @param myList is the collection to convert in array
	 * @return array after convertion
	 */
	public static String[] convertListMapToArray(Collection<?> myList) {
		return myList.toArray(new String[0]);
	}

	/**
	 * 
	 * @param columnName is the name of the column and we want to know if an
	 *                   anonymization rule is set for her.
	 * @param a          all anonymization rules obtained after JSON file has been
	 *                   parsed
	 * @return the anonymization rule find if it exist
	 */
	public static String wantToBeAnonimized(String columnName, DescriptionAnonymizationRules[] a) {

		for (DescriptionAnonymizationRules anonymizeColumn : a) {
			if (anonymizeColumn.getName().equalsIgnoreCase(columnName)) {
				return anonymizeColumn.getAnonymazation();
			}
		}

		return "";

	}

}
