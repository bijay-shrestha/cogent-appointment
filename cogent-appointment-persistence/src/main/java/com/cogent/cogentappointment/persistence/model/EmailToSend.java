package com.cogent.cogentappointment.persistence.model;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;
import java.util.Date;

/**
 * @author smriti on 2019-08-26
 * DO NOT USE ABSTRACT AUDITABLE ENITY WHEREVER SCHEDULER IS USED
 * SINCE IT USED AUTHENTICATED ADMIN AS CURRENT AUDITOR WHICH IS NOT
 * EVALUATED IN SCHEDULER
 */
@Table(name = "email_to_send")
@Entity
@Getter
@Setter
public class EmailToSend implements Serializable{

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "is_sent")
    private Character isSent;

    @Column(name = "recorded_date")
    @Temporal(TemporalType.DATE)
    private Date recordedDate;

    @Temporal(TemporalType.DATE)
    @Column(name = "sent_date")
    private Date sentDate;

    @Column(name = "subject", length = 100)
    private String subject;

    @Column(name = "receiverEmailAddress", length = 100)
    private String receiverEmailAddress;

    @Column(name = "param_value")
    private String paramValue;

    @Column(name = "template_name")
    private String templateName;

    @Override
    public String toString() {
        return "EmailToSend{" +
                "id=" + id +
                ", isSent='" + isSent + '\'' +
                ", recordedDate=" + recordedDate +
                ", sentDate=" + sentDate +
                ", subject='" + subject + '\'' +
                ", receiverEmailAddress='" + receiverEmailAddress + '\'' +
                ", paramValue='" + paramValue + '\'' +
                ", templateName='" + templateName + '\'' +
                '}';
    }
}
