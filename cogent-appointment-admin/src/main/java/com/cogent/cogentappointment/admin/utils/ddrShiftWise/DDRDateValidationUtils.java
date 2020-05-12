package com.cogent.cogentappointment.admin.utils.ddrShiftWise;

import java.time.LocalTime;
import java.util.Date;

import static com.cogent.cogentappointment.admin.utils.commons.DateUtils.getTimeFromDate;

/**
 * @author smriti on 12/05/20
 */
public class DDRDateValidationUtils {

    public static boolean validateTimeOverlap(Date initialStartTime, Date initialEndTime,
                                              Date nextStartTime, Date nextEndTime) {

        LocalTime initialStart = LocalTime.parse(getTimeFromDate(initialStartTime));
        LocalTime initialEnd = LocalTime.parse(getTimeFromDate(initialEndTime));

        LocalTime nextStartTarget = LocalTime.parse(getTimeFromDate(nextStartTime));
        LocalTime nextEndTarget = LocalTime.parse(getTimeFromDate(nextEndTime));

        Boolean isNextStartTimeInclusive =
                ((!nextStartTarget.isBefore(initialStart) && nextStartTarget.isBefore(initialEnd)));

        Boolean isNextEndTimeInclusive =
                ((!nextEndTarget.isBefore(initialStart) && nextStartTarget.isBefore(initialEnd)));

        return (isNextStartTimeInclusive || isNextEndTimeInclusive);
    }

    public static boolean validateBreakTimeOverlap(Date initialStartTime, Date initialEndTime,
                                                   Date nextStartTime, Date nextEndTime) {

        LocalTime initialStart = LocalTime.parse(getTimeFromDate(initialStartTime));
        LocalTime initialEnd = LocalTime.parse(getTimeFromDate(initialEndTime));

        LocalTime nextStartTarget = LocalTime.parse(getTimeFromDate(nextStartTime));
        LocalTime nextEndTarget = LocalTime.parse(getTimeFromDate(nextEndTime));

        Boolean isNextStartTimeInclusive =
                ((!nextStartTarget.isBefore(initialStart) && nextStartTarget.isBefore(initialEnd)));

        Boolean isNextEndTimeInclusive =
                ((!nextEndTarget.isBefore(initialStart) && nextStartTarget.isBefore(initialEnd)));

        return (isNextStartTimeInclusive && isNextEndTimeInclusive);
    }
}
