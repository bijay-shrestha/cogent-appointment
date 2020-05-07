package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.CountryEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;


/**
 * @author smriti on 2019-11-08
 */
@Entity
@Table(name = "country")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(CountryEntityListener.class)
public class Country extends Auditable<String> implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "nice_name")
    private String niceName;

    @Column(name = "iso")
    private String iso;

    @Column(name = "iso_3")
    private String iso3;

    /*NOTE :The primitive data type 'int' isn't nullable.
     You need to use the Wrapper class 'Integer' in this case.
     */
    @Column(name = "num_code")
    private Integer numCode;

    @Column(name = "phone_code")
    private Integer phoneCode;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "Country{" +
                "id=" + id +
                ", name=" + name +
                ", niceName=" + niceName +
                ", iso=" + iso +
                ", iso3=" + iso3 +
                ", numCode=" + numCode +
                ", phoneCode=" + phoneCode +
                ", status=" + status +
                '}';
    }
}
