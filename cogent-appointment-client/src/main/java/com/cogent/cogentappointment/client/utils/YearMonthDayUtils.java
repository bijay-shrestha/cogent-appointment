package com.cogent.cogentappointment.client.utils;


import com.cogent.cogentappointment.client.dto.request.date.DateConverterRequestDTO;
import com.cogent.cogentappointment.client.dto.request.date.MonthAndWeekRequestDTO;
import com.cogent.cogentappointment.client.dto.request.date.YearMonthDayRequestDTO;
import com.cogent.cogentappointment.client.dto.response.date.DateConverterResponeDTO;
import com.cogent.cogentappointment.client.dto.response.date.YearMonthDayResponseDTO;
import com.cogent.cogentappointment.client.dto.response.date.YearMonthDaysResponseDTO;
import com.cogent.cogentappointment.client.dto.response.date.YearMonthDetailResponseDTO;
import com.cogent.cogentappointment.client.service.YearMonthDayService;
import com.cogent.cogentappointment.persistence.model.YearMonthDay;
import org.joda.time.Days;
import org.joda.time.LocalDate;

import java.time.YearMonth;
import java.util.*;

import static com.cogent.cogentappointment.client.utils.commons.DateUtils.convertToString;
import static com.cogent.cogentappointment.client.utils.commons.DateUtils.getDateFromString;

/**
 * @author nikesh
 */
public class YearMonthDayUtils {


    private static final int STARTING_NEP_YEAR_FOR_BS = 1970;
    private static final int STARTING_NEP_MONTH_FOR_BS = 1;
    private static final int STARTING_NEP_DAY_FOR_BS = 1;

    private static final int STARTING_ENG_YEAR_FOR_BS = 1913;
    private static final int STARTING_ENG_MONTH_FOR_BS = 4;
    private static final int STARTING_ENG_DAY_FOR_BS = 13;

    private static final int STARTING_ENG_YEAR_FOR_AD = 1914;
    private static final int STARTING_ENG_MONTH_FOR_AD = 1;
    private static final int STARTING_ENG_DAY_FOR_AD = 1;

    private static final int STARTING_NEP_YEAR_FOR_AD = 1970;
    private static final int STARTING_NEP_MONTH_FOR_AD = 9;
    private static final int STARTING_NEP_DAY_FOR_AD = 18;

    public static YearMonthDay convertToEntity(YearMonthDayRequestDTO req) {
        YearMonthDay entity = new YearMonthDay();

        entity.setAshad(req.getAshar());
        entity.setAshwin(req.getAshwin());
        entity.setBasihak(req.getBaisakh());
        entity.setBhadra(req.getBhadra());
        entity.setChaitra(req.getChaitra());
        entity.setFalgun(req.getFalgun());
        entity.setId(req.getId());
        entity.setJestha(req.getJestha());
        entity.setKartik(req.getKartik());
        entity.setMagh(req.getMagh());
        entity.setManghsir(req.getMangsir());
        entity.setPoush(req.getPoush());

        entity.setShrawan(req.getShrawan());
        entity.setYear(req.getYear());
        entity.setZero(req.getZero());

        return entity;
    }

    public static YearMonthDayResponseDTO convertToResponse(YearMonthDay entity) {
        YearMonthDayResponseDTO response = new YearMonthDayResponseDTO();

        response.setAshar(entity.getAshad());
        response.setAshwin(entity.getAshwin());
        response.setBaisakh(entity.getBasihak());
        response.setBhadra(entity.getBhadra());
        response.setChaitra(entity.getChaitra());
        response.setFalgun(entity.getFalgun());
        response.setId(entity.getId());
        response.setJestha(entity.getJestha());
        response.setKartik(entity.getKartik());
        response.setMagh(entity.getMagh());
        response.setMangsir(entity.getManghsir());
        response.setPoush(entity.getPoush());

        response.setShrawan(entity.getShrawan());
        response.setYear(entity.getYear());
        response.setZero(entity.getZero());

        return response;
    }


    public static List<YearMonthDayResponseDTO> convertToResponseList(List<YearMonthDay> entities) {
        List<YearMonthDayResponseDTO> responses = new ArrayList<>();

        for (YearMonthDay entity : entities) {
            responses.add(convertToResponse(entity));
        }

        return responses;
    }

    public static DateConverterResponeDTO convertFromBSToAD(Map<Integer,
            Object[]> nepaliMap, DateConverterRequestDTO request) {
        int year = request.getYear();
        int month = request.getMonth();
        int day = request.getDay();

        long totalNepDaysCount = 0;

// count total days in-terms of year
        for (int i = STARTING_NEP_YEAR_FOR_BS; i < year; i++) {
            for (int j = 1; j <= 12; j++) {
                totalNepDaysCount += (Integer) nepaliMap.get(i)[j];
            }
        }

// count total days in-terms of month
        for (int j = STARTING_NEP_MONTH_FOR_BS; j < month; j++) {
            totalNepDaysCount += (Integer) nepaliMap.get(year)[j];
        }

// count total days in-terms of date
        totalNepDaysCount += day - STARTING_NEP_DAY_FOR_BS;

        int[] daysInMonth = new int[]{0, 31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        int[] daysInMonthOfLeapYear = new int[]{0, 31, 29, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31};
        // calculation of equivalent english date
        //...
        int engYear = STARTING_ENG_YEAR_FOR_BS;
        int engMonth = STARTING_ENG_MONTH_FOR_BS;
        int engDay = STARTING_ENG_DAY_FOR_BS;

        int endDayOfMonth = 0;

        while (totalNepDaysCount != 0) {
            if (isLeapYear(engYear)) {
                endDayOfMonth = daysInMonthOfLeapYear[engMonth];
            } else {
                endDayOfMonth = daysInMonth[engMonth];
            }
            engDay++;
//            dayOfWeek++;
            if (engDay > endDayOfMonth) {
                engMonth++;
                engDay = 1;
                if (engMonth > 12) {
                    engYear++;
                    engMonth = 1;
                }
            }
            /*if (dayOfWeek > 7) {
                dayOfWeek = 1;
            }*/
            totalNepDaysCount--;
        }

        return convertToResponseDTO(engYear, engMonth, engDay);
    }

    public static DateConverterResponeDTO convertFromADToBS(Map<Integer, Object[]> nepaliMap, DateConverterRequestDTO request) {

        int year = request.getYear();
        int month = request.getMonth() - 1;
        int day = request.getDay();

        Calendar currentEngDate = new GregorianCalendar();
        currentEngDate.set(year, month, day);

        Calendar baseEngDate = new GregorianCalendar();
        baseEngDate.set(STARTING_ENG_YEAR_FOR_AD, STARTING_ENG_MONTH_FOR_AD, STARTING_ENG_DAY_FOR_AD);

        long totalEngDaysCount = daysBetween(baseEngDate, currentEngDate);

        int nepYear = STARTING_NEP_YEAR_FOR_AD;
        int nepMonth = STARTING_NEP_MONTH_FOR_AD;
        int nepDay = STARTING_NEP_DAY_FOR_AD;

        // decrement totalEngDaysCount until its value becomes 0
        while (totalEngDaysCount != 0) {

            // getting the total number of days in month nepMonth in year nepYear
            int daysInIthMonth = (Integer) nepaliMap.get(nepYear)[nepMonth];

            nepDay++; // incrementing nepali day

            if (nepDay > daysInIthMonth) {
                nepMonth++;
                nepDay = 1;
            }
            if (nepMonth > 12) {
                nepYear++;
                nepMonth = 1;
            }

            totalEngDaysCount--;
        }

        return convertToResponseDTO(nepYear, nepMonth, nepDay);
    }

    public static boolean isLeapYear(int year) {
        if (year % 100 == 0) {
            return year % 400 == 0;
        } else {
            return year % 4 == 0;
        }
    }

    public static long daysBetween(Calendar startDate, Calendar endDate) {
//        Calendar date = (Calendar) startDate.clone();
        long daysBetween = 0;

        int startYear = startDate.get(Calendar.YEAR);
        int endYear = endDate.get(Calendar.YEAR);

        int endMonth = (endDate.get(Calendar.MONTH)) + 1;

        int endDay = endDate.get(Calendar.DATE);

        for (int i = startYear; i <= endYear; i++) {
            if (i == endYear) {
                for (int j = 1; j <= endMonth; j++) {
                    if (j == endMonth) {
                        //LAST DAY IS NOT ADDED
                        daysBetween += (endDay - 1);
                    } else {
                        YearMonth ym = YearMonth.of(i, j);
                        daysBetween += ym.lengthOfMonth();
                    }
                }
            } else {
                for (int j = 1; j <= 12; j++) {
                    YearMonth ym = YearMonth.of(i, j);
                    daysBetween += ym.lengthOfMonth();
                }
            }
        }

//        while (date.before(endDate)) {
//            date.add(Calendar.DAY_OF_MONTH, 1);
//            daysBetween++;
//        }
        return daysBetween;
    }

    public static DateConverterResponeDTO convertToResponseDTO(int convertedYear, int convertedMonth, int convertedDay) {

        DateConverterResponeDTO responseDTO = new DateConverterResponeDTO();

        responseDTO.setDay(convertedDay);
        responseDTO.setMonth(convertedMonth);
        responseDTO.setYear(convertedYear);

        return responseDTO;
    }

    public static List<YearMonthDaysResponseDTO> fetchYearMonthDaysForAD() {
        List<YearMonthDaysResponseDTO> responseList = new ArrayList<>();

        for (int i = STARTING_ENG_YEAR_FOR_AD; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {

            for (int j = 1; j <= 12; j++) {
                YearMonthDaysResponseDTO responseDTO = new YearMonthDaysResponseDTO();
                responseDTO.setYear(i);
                responseDTO.setMonth(j);
                YearMonth ym = YearMonth.of(i, j);
                responseDTO.setNumberOfDays(ym.lengthOfMonth());

                responseList.add(responseDTO);
            }
        }

        return responseList;
    }

    public static List<YearMonthDaysResponseDTO> fetchYearMonthDaysForBS(Map<Integer, Object[]> nepaliDateMap, Integer currentNepaliYear) {
        List<YearMonthDaysResponseDTO> responseList = new ArrayList<>();

        for (int i = STARTING_NEP_YEAR_FOR_BS; i <= currentNepaliYear; i++) {
            for (int j = 1; j <= 12; j++) {
                YearMonthDaysResponseDTO responseDTO = new YearMonthDaysResponseDTO();

                responseDTO.setYear(i);
                responseDTO.setMonth(j);
                responseDTO.setNumberOfDays((Integer) nepaliDateMap.get(i)[j]);

                responseList.add(responseDTO);
            }

        }

        return responseList;
    }

    public static List<Integer> fetchYearsForAD() {
        List<Integer> yearList = new ArrayList<>();

        for (int i = STARTING_ENG_YEAR_FOR_AD; i <= Calendar.getInstance().get(Calendar.YEAR); i++) {
            yearList.add(i);
        }

        return yearList;
    }

    public static List<Integer> fetchYearsForBS(Integer currentNepaliYear) {
        List<Integer> yearList = new ArrayList<>();

        for (int i = STARTING_NEP_YEAR_FOR_BS; i <= currentNepaliYear + 1; i++) {
            yearList.add(i);
        }

        return yearList;
    }

    public static Map<Integer, Object[]> getNepaliDateMap(List<YearMonthDay> allNepaliDates) {
        Map<Integer, Object[]> nepaliDateMap = new HashMap<Integer, Object[]>();

        for (YearMonthDay nepDate : allNepaliDates) {
            List<Integer> monthDays = new ArrayList<>();

            monthDays.add(nepDate.getZero());
            monthDays.add(nepDate.getBasihak());
            monthDays.add(nepDate.getJestha());
            monthDays.add(nepDate.getAshad());
            monthDays.add(nepDate.getShrawan());
            monthDays.add(nepDate.getBhadra());
            monthDays.add(nepDate.getAshwin());
            monthDays.add(nepDate.getKartik());
            monthDays.add(nepDate.getManghsir());
            monthDays.add(nepDate.getPoush());
            monthDays.add(nepDate.getMagh());
            monthDays.add(nepDate.getFalgun());
            monthDays.add(nepDate.getChaitra());

            nepaliDateMap.put(nepDate.getYear(), monthDays.toArray());
        }
        return nepaliDateMap;
    }

    public static DateConverterResponeDTO getCurrentNepaliYearAndMonth(Map<Integer, Object[]> nepaliDateMap, YearMonthDayService yearMonthDayService) {
        DateConverterRequestDTO requestDTO = new DateConverterRequestDTO();
        requestDTO.setYear(Calendar.getInstance().get(Calendar.YEAR));
        requestDTO.setMonth(Calendar.getInstance().get(Calendar.MONTH) + 1);
        requestDTO.setDay(Calendar.getInstance().get(Calendar.DATE));

        DateConverterResponeDTO responseDTO = convertFromADToBS(nepaliDateMap, requestDTO);
        responseDTO.setDayNumber(getDayNumberByMonthAndDate(yearMonthDayService.findByYear(responseDTO.getYear()),
                responseDTO.getMonth(), responseDTO.getDay()));

        return responseDTO;
    }

    public static int getDayNumberByMonth(YearMonthDay yearMonthDay, Integer month) {
        int startDay = yearMonthDay.getStartDay();
        int startMonth = 1;

        while (startMonth <= month) {
            if (startMonth == month) {
                return startDay;
            }
            int totalNumberOfDaysInCurrentMonth = getNumberOfDaysInMonth(startMonth, yearMonthDay);
            int dayNumber = 1;
            while (dayNumber <= totalNumberOfDaysInCurrentMonth) {

                dayNumber++;
                startDay++;

                if (startDay == 8) {
                    startDay = 1;
                }

                if (dayNumber == totalNumberOfDaysInCurrentMonth) {
                    startMonth++;
                }
            }
        }
        return startDay;
    }

    public static int getDayNumberByMonthAndDate(YearMonthDay yearMonthDay, Integer month, Integer date) {
        int startDayNumberForTheMonth = getDayNumberByMonth(yearMonthDay, month);

        int dayNumber = startDayNumberForTheMonth;
        for (int i = 2; i <= date; i++) {
            dayNumber++;
            if (dayNumber == 8) {
                dayNumber = 1;
            }
        }

        return dayNumber;
    }

    public static int getWeekNumberByNepaliDate(YearMonthDay yearMonthDay, Integer currentMonth, Integer currentDay) {
        int startDayNumber = 1;
        int startDay = getDayNumberByMonth(yearMonthDay, currentMonth);
        int week = 1;
        while (startDayNumber <= currentDay) {
            if (startDayNumber == currentDay) {
                return week;
            }
            startDayNumber++;
            startDay++;

            if (startDay == 8) {
                week++;
                startDay = 1;
            }
        }
        return week;
    }

    public static Integer getNumberOfDaysInMonth(int month, YearMonthDay yearMonthDay) {
        switch (month) {
            case 1:
                return yearMonthDay.getBasihak();
            case 2:
                return yearMonthDay.getJestha();
            case 3:
                return yearMonthDay.getAshad();
            case 4:
                return yearMonthDay.getShrawan();
            case 5:
                return yearMonthDay.getBhadra();
            case 6:
                return yearMonthDay.getAshwin();
            case 7:
                return yearMonthDay.getKartik();
            case 8:
                return yearMonthDay.getManghsir();
            case 9:
                return yearMonthDay.getPoush();
            case 10:
                return yearMonthDay.getMagh();
            case 11:
                return yearMonthDay.getFalgun();
            case 12:
                return yearMonthDay.getChaitra();
            default:
                return 0;
        }
    }

    public static DateConverterResponeDTO getFirstNepaliDateForMonthAndWeekNumber(YearMonthDay yearMonthDay, Integer month, Integer weekNumber) {
        int startDay = yearMonthDay.getStartDay();
        int startMonth = 1;
        int week = 1;

        while (startMonth <= month) {
            int totalNumberOfDaysInCurrentMonth = getNumberOfDaysInMonth(startMonth, yearMonthDay);
            int dayNumber = 1;
            while (dayNumber <= totalNumberOfDaysInCurrentMonth) {
                if (startMonth == month && week == weekNumber) {
                    DateConverterResponeDTO responeDTO = new DateConverterResponeDTO(yearMonthDay.getYear(), month, dayNumber);
                    return responeDTO;
                }

                dayNumber++;
                startDay++;

                if (startDay == 8) {
                    startDay = 1;
                    week++;
                }

                if (dayNumber > totalNumberOfDaysInCurrentMonth) {
                    startMonth++;
                    week = 1;
                }
            }
        }
        return null;
    }

    public static DateConverterResponeDTO getLastNepaliDateForMonthAndWeekNumber(YearMonthDay yearMonthDay, Integer month, Integer weekNumber) {
        int startDay = yearMonthDay.getStartDay();
        int startMonth = 1;
        int week = 1;

        while (startMonth <= month) {
            int totalNumberOfDaysInCurrentMonth = getNumberOfDaysInMonth(startMonth, yearMonthDay);
            int dayNumber = 1;
            while (dayNumber <= totalNumberOfDaysInCurrentMonth) {
                if (startMonth == month && week == weekNumber && (startDay == 7 || dayNumber == totalNumberOfDaysInCurrentMonth)) {
                    DateConverterResponeDTO responeDTO = new DateConverterResponeDTO(yearMonthDay.getYear(), month, dayNumber);
                    return responeDTO;
                }

                dayNumber++;
                startDay++;

                if (startDay == 8) {
                    startDay = 1;
                    week++;
                }

                if (dayNumber > totalNumberOfDaysInCurrentMonth) {
                    startMonth++;
                    week = 1;
                }
            }
        }
        return null;
    }

    public static String getMonthNameByNumber(Integer month) {
        switch (month) {
            case 1:
                return "Baishakh";
            case 2:
                return "Jestha";
            case 3:
                return "Ashad";
            case 4:
                return "Shrawan";
            case 5:
                return "Bhadra";
            case 6:
                return "Ashwin";
            case 7:
                return "Kartik";
            case 8:
                return "Manghsir";
            case 9:
                return "Poush";
            case 10:
                return "Magh";
            case 11:
                return "Falgun";
            case 12:
                return "Chaitra";
            default:
                return "";
        }
    }

//    /**
//     * Method that check weather workPlannerTaskInfo's yearMonthWeek list is valid by comparing it with current nepali date.
//     * The method extract first element from the yearMonthWeekList and converts its year, month and week to current
//     * nepali year, month and week.
//     *
//     * @param workPlannerTaskInfo
//     * @param currentNepaliYearAndMonth
//     * @param yearMonthDayService
//     * @return
//     */
//    public static boolean areYearMonthWeeksValidForCurrentNepaliDate(
//            WorkPlannerTaskInfo workPlannerTaskInfo,
//            DateConverterResponeDTO currentNepaliYearAndMonth,
//            YearMonthDayService yearMonthDayService) {
//        List<WorkPlannerMonthAndWeekRequestDTO> formattedWorkPlannerMonthWeekData = WorkPlannerUtils.getFormattedWorkPlannerMonthWeekData(workPlannerTaskInfo);
//
//        // check first WorkPlannerMonthAndWeekRequestDTO element
////        WorkPlannerMonthAndWeekRequestDTO firstElement = formattedWorkPlannerMonthWeekData.get(0);
//
//        // check last WorkPlannerMonthAndWeekRequestDTO element
//        WorkPlannerMonthAndWeekRequestDTO lastElement = formattedWorkPlannerMonthWeekData.get(formattedWorkPlannerMonthWeekData.size() - 1);
//
//        // If current year is greater than request, its invalid
//        if (lastElement.getYear().intValue() < currentNepaliYearAndMonth.getYear().intValue()) {
//            return false;
//        } else if (lastElement.getYear().equals(currentNepaliYearAndMonth.getYear())) { // else if years are equal, then check months
//            if (lastElement.getMonth().intValue() < currentNepaliYearAndMonth.getMonth().intValue()) {
//                return false;
//            } else if (lastElement.getMonth().equals(currentNepaliYearAndMonth.getMonth())) {  // if months are equal, then check weeks
//
////                Integer firstElementWeekNo = lastElement.getWeeks().get(0);
//                Integer lastElementWeekNo = lastElement.getWeeks().get(lastElement.getWeeks().size() - 1);
//                Integer currentWeekNo = getWeekNumberByNepaliDate(yearMonthDayService.findByYear(
//                        currentNepaliYearAndMonth.getYear()),
//                        currentNepaliYearAndMonth.getMonth(),
//                        currentNepaliYearAndMonth.getDay());
//
//                if (lastElementWeekNo < currentWeekNo) {
//                    return false;
//                }
//
//            }
//        }
//        return true;
//    }
//
    public static Integer getDifferenceBetweenNepaliDates(DateConverterResponeDTO startDate, DateConverterResponeDTO endDate, YearMonthDayService yearMonthDayService) {
        DateConverterResponeDTO equivalentStartDateInEnglish = convertFromBSToAD(getNepaliDateMap(yearMonthDayService.findAll()), new DateConverterRequestDTO('B', startDate.getYear(), startDate.getMonth(), startDate.getDay()));
        DateConverterResponeDTO equivalentEndDateInEnglish = convertFromBSToAD(getNepaliDateMap(yearMonthDayService.findAll()), new DateConverterRequestDTO('B', endDate.getYear(), endDate.getMonth(), endDate.getDay()));

        Date equivalentStartDate = getDateFromString(equivalentStartDateInEnglish.getFormattedDate());
        Date equivalentEndDate = getDateFromString(equivalentEndDateInEnglish.getFormattedDate());

        return Days.daysBetween(new LocalDate(equivalentStartDate), new LocalDate(equivalentEndDate)).getDays();

    }

    public static List<DateConverterResponeDTO> getDateAndDayNumberForEveryDayInANepaliMonthAndYear(YearMonthDay yearMonthDay, Integer month) {
        List<DateConverterResponeDTO> responseList = new ArrayList<>();

        Integer totalDaysInCurrentMonth = getNumberOfDaysInMonth(month, yearMonthDay);
        Integer currentDate = 1;

        while (currentDate <= totalDaysInCurrentMonth) {
            Integer dayNumber = getDayNumberByMonthAndDate(yearMonthDay, month, currentDate);
            responseList.add(new DateConverterResponeDTO(yearMonthDay.getYear(), month, currentDate, dayNumber));
            currentDate++;
        }

        return responseList;
    }

    public static Integer getNumberOfWeeksInAMonth(YearMonthDay yearMonthDay, Integer month) {
        Integer totalNumberOfWeeks = 1;

        List<DateConverterResponeDTO> daysOfCurrentMonth = getDateAndDayNumberForEveryDayInANepaliMonthAndYear(yearMonthDay, month);
        for (int i = 1; i < daysOfCurrentMonth.size(); i++) {
            if (daysOfCurrentMonth.get(i).getDayNumber() == 1 && daysOfCurrentMonth.get(i - 1).getDayNumber() == 7) {
                totalNumberOfWeeks++;
            }
        }

        return totalNumberOfWeeks;
    }

    public static List<DateConverterResponeDTO> getListOfAllDaysForNepaliWeekAndMonth(YearMonthDay yearMonthDay, Integer month, Integer weekNumber, YearMonthDayService yearMonthDayService) {
        List<DateConverterResponeDTO> daysListForAWeek = new ArrayList<>();
        DateConverterResponeDTO firstDate = getFirstNepaliDateForMonthAndWeekNumber(yearMonthDay, month, weekNumber);
        DateConverterResponeDTO lastDate = getLastNepaliDateForMonthAndWeekNumber(yearMonthDay, month, weekNumber);

        //sabu
        DateConverterResponeDTO currentDate = getNepaliDateForToday(yearMonthDayService);

        Integer currentDay = firstDate.getDay();

        if (currentDate.getDay() > currentDay &&
                currentDate.getMonth().equals(firstDate.getMonth()) &&
                currentDate.getYear().equals(firstDate.getYear())) {
          currentDay=currentDay+(currentDate.getDay()-currentDay);

        }

        while (currentDay <= lastDate.getDay()) {

            //add day only if it is greater or equal to current day
            daysListForAWeek.add(new DateConverterResponeDTO(firstDate.getYear(), firstDate.getMonth(), currentDay));

            currentDay++;
        }

        return daysListForAWeek;
    }

    //sabu
    public static DateConverterResponeDTO getNepaliDateForToday(YearMonthDayService yearMonthDayService) {
        String englishDateString = convertToString(new Date(), "yyyy-MM-dd");

        Integer year = Integer.parseInt(englishDateString.split("-")[0].trim());
        Integer month = Integer.parseInt(englishDateString.split("-")[1].trim());
        Integer day = Integer.parseInt(englishDateString.split("-")[2].trim());

        DateConverterResponeDTO nepaliDateResponseDTO = YearMonthDayUtils.convertFromADToBS(YearMonthDayUtils.getNepaliDateMap(yearMonthDayService.findAll()), new DateConverterRequestDTO('A', year, month, day));

        return nepaliDateResponseDTO;
    }

    public static YearMonthDetailResponseDTO getYearMonthDetailForYearAndMonth(YearMonthDay ymd, Integer month) {
        YearMonthDetailResponseDTO response = new YearMonthDetailResponseDTO();

        response.setNumberOfDays(getNumberOfDaysInMonth(month, ymd));
        response.setMonth(month);
        response.setDayNumber(getDayNumberByMonthAndDate(ymd, month, 1));

        return response;
    }

    public static int getDayCountForWeekNumbers(MonthAndWeekRequestDTO request, YearMonthDay yearMonthDay) {
        List<Integer> weeks = request.getWeeks();

        int totalDays = 0;
        for (Integer week : weeks) {
            Integer firstDate = getFirstNepaliDateForMonthAndWeekNumber(yearMonthDay, request.getMonth(), week).getDay();
            Integer lastDate = getLastNepaliDateForMonthAndWeekNumber(yearMonthDay, request.getMonth(), week).getDay();
            int noOfDays = (lastDate.intValue() - firstDate.intValue()) + 1;

            totalDays += noOfDays;
        }

        return totalDays;
    }
    }
