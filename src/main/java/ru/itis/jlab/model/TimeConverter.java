package ru.itis.jlab.model;//package ru.itis.ru.itis.jlab.model;
//
//
//import sun.management.counter.StringCounter;
//
//import java.text.ParseException;
//import java.text.SimpleDateFormat;
//import java.util.Date;
//import java.util.Locale;
//
//public class TimeConverter implements StringConver<Date> {
//    private String pattern = "dd.mm.yyyy";
//
//    @Override
//    public Date convert(String value) {
//        try {
//            return new SimpleDateFormat(pattern, new Locale("en")).parse(value);
//        } catch (ParseException e) {
//            throw new IllegalArgumentException("невозможно распарсить дату");
//        }
//    }
//}
