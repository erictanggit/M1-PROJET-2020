package io.github.erictanggit.main;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

import io.github.erictanggit.json.JsonParsing;
import io.github.erictanggit.logger.Logger;
import io.github.erictanggit.model.DescriptionAnonymizationRules;
import io.github.erictanggit.model.DescriptionCSV;
import io.github.erictanggit.model.DescriptionRules;

public class App {
	public static void main(String[] args) {
		DescriptionCSV[] TypeCsvData = DescriptionCSV[].class
				.cast(JsonParsing.readFileJson("src/main/resources/description_csv.json", DescriptionCSV[].class));
		DescriptionRules[] ruleCsvData = DescriptionRules[].class
				.cast(JsonParsing.readFileJson("src/main/resources/verification_csv.json", DescriptionRules[].class));
		DescriptionAnonymizationRules[] anonymizationrule = DescriptionAnonymizationRules[].class.cast(JsonParsing
				.readFileJson("src/main/resources/anonymisation_csv.json", DescriptionAnonymizationRules[].class));
		Scanner sc = new Scanner(System.in);
		Logger.trace(App.class, "Please give a file name \n");
		String nameFile = sc.nextLine();
		List<String[]> rows = ActionPerform.readFile(new File("src/main/resources/" + nameFile));
		for (String[] strings : rows) {
			Logger.info(App.class, Arrays.asList(strings));
		}
		try {
			ActionPerform.wantToVerifDataFile(rows, TypeCsvData, ruleCsvData,
					nameFile.substring(0, nameFile.lastIndexOf('.')) + "_verifdata.csv");
			ActionPerform.wantToAnanonymizeDataFile(rows, TypeCsvData, anonymizationrule,
					nameFile.substring(0, nameFile.lastIndexOf('.')) + "_anonimize.csv");
		} catch (URISyntaxException | IOException e) {
			Logger.error(JsonParsing.class, "Error occured " + e.toString());
		}
	}
}
