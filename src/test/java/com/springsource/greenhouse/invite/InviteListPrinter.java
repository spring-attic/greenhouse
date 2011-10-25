package com.springsource.greenhouse.invite;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;

import org.springframework.util.StringUtils;

import au.com.bytecode.opencsv.CSVReader;

public class InviteListPrinter {

	public static void main(String[] args) throws IOException {
		CSVReader reader = new CSVReader(new FileReader(new File("src/test/java/com/springsource/greenhouse/invite/attendees.csv")));
		for (String[] record = reader.readNext(); record != null; record = reader.readNext()) {
			String inviteeString = name(record[0]) + " " + name(record[1]) + " <" + record[2] + ">";
			System.out.println(Invitee.valueOf(inviteeString));
		}
	}
	
	private static String name(String string) {
		return StringUtils.capitalize(string.replaceAll(" ", ""));
	}

}
