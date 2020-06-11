package com.cogent.cogentappointment.persistence.history;

import com.cogent.cogentappointment.persistence.config.Action;
import com.cogent.cogentappointment.persistence.model.IntegrationChannel;
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
 * @author rupak on 2020-05-27
 */
@Entity
@Getter
@Setter
@NoArgsConstructor
@EntityListeners(AuditingEntityListener.class)
@Table(name = "integration_channel_history")
public class IntegrationChannelHistory implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "integration_channel_id",
            foreignKey = @ForeignKey(name = "FK_integration_channel_history_integration_channel"))
    private IntegrationChannel integrationChannel;

    @Column(name = "integration_channel_content")
    @Lob
    private String integrationChannelContent;

    @CreatedBy
    private String modifiedBy;

    @CreatedDate
    @Temporal(TIMESTAMP)
    private Date modifiedDate;

    @Enumerated(STRING)
    private Action action;

    public IntegrationChannelHistory(IntegrationChannel integrationChannel, Action action) {
        this.integrationChannel = integrationChannel;
        this.integrationChannelContent = integrationChannel.toString();
        this.action = action;
    }
}
