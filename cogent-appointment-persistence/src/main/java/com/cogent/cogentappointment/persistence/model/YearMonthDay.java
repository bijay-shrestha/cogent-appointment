package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author nikesh
 */
@Entity
@Table(name = "year_month_day")
@Getter
@Setter
public class YearMonthDay implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "year")
    private Integer year;

    @Column(name = "zero")
    private Integer zero;

    @Column(name = "basihak")
    private Integer basihak;

    @Column(name = "jestha")
    private Integer jestha;

    @Column(name = "ashad")
    private Integer ashad;

    @Column(name = "shrawan")
    private Integer shrawan;

    @Column(name = "bhadra")
    private Integer bhadra;

    @Column(name = "ashwin")
    private Integer ashwin;

    @Column(name = "kartik")
    private Integer kartik;

    @Column(name = "manghsir")
    private Integer manghsir;

    @Column(name = "poush")
    private Integer poush;

    @Column(name = "magh")
    private Integer magh;

    @Column(name = "falgun")
    private Integer falgun;

    @Column(name = "chaitra")
    private Integer chaitra;

    /*
    *
    * Represents which day is the starting day of the year
    *
    */
    @Column(name = "start_day")
    private Integer startDay;

    @Override
    public String toString() {
        return "YearMonthDay{" +
                "id=" + id +
                ", year=" + year +
                ", zero=" + zero +
                ", basihak=" + basihak +
                ", jestha=" + jestha +
                ", ashad=" + ashad +
                ", shrawan=" + shrawan +
                ", bhadra=" + bhadra +
                ", ashwin=" + ashwin +
                ", kartik=" + kartik +
                ", manghsir=" + manghsir +
                ", poush=" + poush +
                ", magh=" + magh +
                ", falgun=" + falgun +
                ", chaitra=" + chaitra +
                ", startDay=" + startDay +
                '}';
    }
}
