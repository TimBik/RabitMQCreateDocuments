package ru.itis.jlab.model;//package ru.itis.ru.itis.jlab.model;
//
//import com.beust.jcommander.Parameter;
//
//import java.lang.reflect.Field;
//import java.util.Date;
//import java.util.HashMap;
//import java.util.Map;
//
////@Parameters(separators = "=")
//public class User {
//    @Parameter(names = {"name", "Name"})
//    private String name;
//
//    @Parameter(names = {"surname", "Surname"})
//    private String surname;
//
//    @Parameter(names = {"passport", "passportNumber"})
//    private String passportNumber;
//
//    @Parameter(names = {"number", "phoneNumber"})
//    private String phoneNumber;
//
//    @Parameter(names = {"polis"})
//    private String polis;
//
//
//    @Parameter(names = {"age"})
//    private int age;
//
//    @Parameter(names = {"ITN"})
//    private String ITN;
//
//    @Parameter(names = {"IEC"})
//    private String IEC;
//
//    @Parameter(names = {"PSRN"})
//    private String PSRN;
//
//    @Parameter(names = {"dateOfIssue", "issue"}, converter = TimeConverter.class)
//    private Date dateOfIssue;
//
//    public Map<String, Object> getParameters() {
//        Map<String, Object> map = new HashMap();
//        Field[] fields = User.class.getDeclaredFields();
//        try {
//            for (Field f :
//                    fields) {
//                Object v = f.get(this);
//                map.put(f.getName(), v);
//            }
//        } catch (IllegalAccessException e) {
//            throw new IllegalArgumentException();
//        }
//        return map;
//    }
//}
//
