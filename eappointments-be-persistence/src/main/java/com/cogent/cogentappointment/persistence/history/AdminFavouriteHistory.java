package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.AdminFavourite;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

import static javax.persistence.EnumType.STRING;
import static javax.persistence.TemporalType.TIMESTAMP;

/**
 * @author rupak ON 2020/06/16-12:02 PM
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "admin_favourite_history")
public class AdminFavouriteHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "admin_favourite_id", foreignKey = @ForeignKey(name = "FK_admin_favourite_history_admin_favourite"))
    private AdminFavourite adminFavourite;

    @Column(name = "admin_favourite_content")
    @Lob
    private String adminFavouriteContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public AdminFavouriteHistory(AdminFavourite adminFavourite, Action action) {
        this.adminFavourite = adminFavourite;
        this.adminFavouriteContent = adminFavourite.toString();
        this.action = action;
    }
}
