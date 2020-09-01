package com.kiran.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class TestDates {
	public static void main(String[] args) {
		String outputDate = "";
		try {
			Date parsed = new SimpleDateFormat("dd-MMM-yyyy", java.util.Locale.getDefault()).parse("29-Jul-2020");
			outputDate = new SimpleDateFormat("dd/MM/yyyy", java.util.Locale.getDefault()).format(parsed);
		} catch (ParseException e) {
		}

		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
                .withLocale(Locale.FRENCH);
		LocalDate date = LocalDate.parse(outputDate, formatter);
		
		System.out.println(date);
		
		String[] dateStrArray = outputDate.split("/");

	}
}
