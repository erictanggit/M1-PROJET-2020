package io.github.erictanggit.main;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import com.univocity.parsers.common.processor.RowListProcessor;

import io.github.erictanggit.main.ActionPerform;
import io.github.erictanggit.model.DescriptionAnonymizationRules;
import io.github.erictanggit.model.DescriptionCSV;
import io.github.erictanggit.model.DescriptionRules;
import io.github.erictanggit.model.Type;

public class ActionPerformTest {

	private DescriptionCSV[] typeCsvData = new DescriptionCSV[1];
	private DescriptionRules[] ruleCsvData = new DescriptionRules[1];
	private DescriptionAnonymizationRules[] anonymizationrule = new DescriptionAnonymizationRules[1];
	private String[] header = new String[1];
	private String[] row = new String[1];
	private List<String[]> myCsv = new ArrayList<>();
	private static final Path FOLDER_TEST = Paths.get("src", "main", "resources");

	public ActionPerformTest() {
		typeCsvData[0] = new DescriptionCSV("email", Type.STRING);
		ruleCsvData[0] = new DescriptionRules("email", Arrays.asList("BE_AN_EMAIL"));
		anonymizationrule[0] = new DescriptionAnonymizationRules("email", "RANDOM_LETTER_FOR_LOCAL_PART");
		header[0] = "email";
		row[0] = "java@gmail.com";
		myCsv.add(header);
		myCsv.add(row);
	}

	@Test
	public void test_isInt() {
		assertTrue(ActionPerform.isInt("5"));
		assertTrue(ActionPerform.isInt("-5"));
		assertFalse(ActionPerform.isInt("5.0"));
		assertFalse(ActionPerform.isInt("java"));
	}

	@Test
	public void test_isDouble() {
		assertTrue(ActionPerform.isDouble("5.0"));
		assertFalse(ActionPerform.isDouble("java"));
	}

	@Test
	public void test_wantToVerifDataFile() {
		try {
			ActionPerform.wantToVerifDataFile(myCsv, typeCsvData, ruleCsvData, "test_wantToVerifDataFile.csv");
			assertTrue(new File(FOLDER_TEST + "/test_wantToVerifDataFile.csv").exists());
		} catch (URISyntaxException | IOException e) {
			fail("error occured :" + e.toString());
		}
	}

	@Test
	public void test_getClassInstance() {
		Class[] paramString = ActionPerform.getClassInstance();
		assertTrue(paramString[0] == String.class);
	}

	@Test
	public void test_getRulesColumn() {
		List<String> rules = ActionPerform.getRulesColumn(header[0], ruleCsvData);
		assertTrue(rules.contains("BE_AN_EMAIL"));
	}

	@Test
	public void checkColumnRules() {
		List<String> rules = ActionPerform.getRulesColumn(header[0], ruleCsvData);
		try {
			assertTrue(ActionPerform.checkColumnRules(row[0], rules));
		} catch (NoSuchMethodException | IllegalAccessException | InvocationTargetException e) {
			fail("error occured :" + e.toString());
		}
	}

	@Test
	public void test_dataAnonymized()
			throws NoSuchMethodException, SecurityException, IllegalAccessException, InvocationTargetException {
		assertNotNull(ActionPerform.dataAnonymized(row[0], anonymizationrule[0].getAnonymazation()));
	}

	@Test
	public void test_checkColumnType() {
		assertTrue(ActionPerform.checkColumnType("-5", Type.INT));
		assertTrue(ActionPerform.checkColumnType("-5.0", Type.DOUBLE));
		assertTrue(ActionPerform.checkColumnType("java", Type.STRING));
		assertFalse(ActionPerform.checkColumnType("java", Type.INT));
	}

	@Test
	public void test_writeIntoCSVFile() {
		try {
			ActionPerform.wantToVerifDataFile(myCsv, typeCsvData, ruleCsvData, "test_wantToVerifDataFile.csv");
			ActionPerform.writeIntoCSVFile(new File(FOLDER_TEST + "test_writeIntoCSVFile.csv"), myCsv);
		} catch (URISyntaxException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		assertTrue(new File(FOLDER_TEST + "/test_wantToVerifDataFile.csv").exists());
	}

	@Test
	public void test_getParserSetting() {
		assertNotNull(ActionPerform.getParserSetting(new RowListProcessor()));
	}

	@Test
	public void test_readFile() {
		assertNotNull(ActionPerform.readFile(new File("src/test/resources/test.csv")));
	}

	@Test
	public void test_wantToAnanonymizeDataFile() {
		ActionPerform.wantToAnanonymizeDataFile(myCsv, typeCsvData, anonymizationrule, "wantToAnanonymizeDataFile");
		assertTrue(new File(FOLDER_TEST + "/test_wantToVerifDataFile.csv").exists());
	}

	@Test
	public void test_convertListMapToArray() {
		assertTrue(ActionPerform.convertListMapToArray(new ArrayList<>()).getClass().isArray());
	}

	@Test
	public void test_wantToBeAnonimized() {
		assertEquals(anonymizationrule[0].getAnonymazation(),
				ActionPerform.wantToBeAnonimized(anonymizationrule[0].getName(), anonymizationrule));
	}
}
