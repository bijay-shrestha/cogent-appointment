package com.cogent.cogentappointment.esewa.repository;

import com.cogent.cogentappointment.persistence.model.YearMonthDay;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

/**
 * @author nikesh
 */
public interface YearMonthDayRepository extends JpaRepository<YearMonthDay, Long> {

    @Query("SELECT ymd FROM YearMonthDay ymd WHERE ymd.year=:year")
    YearMonthDay findByYear(@Param("year") Integer year);

}
