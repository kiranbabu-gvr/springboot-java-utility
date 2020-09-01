package com.kiran.test;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class StringToLocalDate {
	public static void main(String[] args) {
		DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/d/yyyy");
		String date = "08/16/2016";

		// convert String to LocalDate
		LocalDate localDate = LocalDate.parse(date, formatter);
		System.out.println(localDate);
		System.out.println(localDate.toString());
		
		SimpleDateFormat formatter1 = new SimpleDateFormat("MM/dd/yyyy");
        String dateInString = "08/16/2016";

        try {

            Date date1 = formatter1.parse(dateInString);
            System.out.println(date1);

        } catch (ParseException e) {
            e.printStackTrace();
        }
        
        DateTimeFormatter formatter2 = DateTimeFormatter.ofPattern("M/d/yyyy");
        LocalDate testDate = LocalDate.parse("12/18/2020", formatter2);
        System.out.println(testDate);
        
        try {
			Date startDate = new SimpleDateFormat("MM/dd/yyyy").parse("2/8/2020");
			System.out.println(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
        try {
			Date startDate = new SimpleDateFormat("yyyy-MM-dd").parse("2020-12-24");
			System.out.println(startDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
        
       System.out.println(LocalDate.parse( "2020-12-24" ));
       System.out.println(DateTimeFormatter.ISO_LOCAL_DATE.format(null));
        
	}
}
