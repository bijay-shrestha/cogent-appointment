package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.enums.GeographyType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.io.Serializable;
import java.sql.Date;

/**
 * @author Sauravi Thapa ON 6/16/20
 *
 * isNew:
 *       0 = old Structure
 *       1 = new Structure
 *
 * geographyType :
 *  0 = ZONE
 *  1 = DISTRICT
 *  2 = VDC_MUNICIPALITY
 *  3 = PROVINCE
 */
@Getter
@Entity
@Table(name = "address")
@AllArgsConstructor
@NoArgsConstructor
public class Address implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "value")
    private String value;

    @Column(name = "display_name")
    private String displayName;

    @Column(name = "geography_type")
    private GeographyType geographyType;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "parent_id")
    private Address parentId;

    private Date created;

    @Column(name = "last_modified")
    private Date lastModified;

    private boolean isNew;

    @Override
    public String toString() {
        return "Address{" +
                "id=" + id +
                ", value='" + value + '\'' +
                ", displayName='" + displayName + '\'' +
                ", geographyType=" + geographyType.name() +
                ", parentId=" + parentId +
                ", created=" + created +
                ", lastModified=" + lastModified +
                ", isNew=" + isNew +
                '}';
    }
}
