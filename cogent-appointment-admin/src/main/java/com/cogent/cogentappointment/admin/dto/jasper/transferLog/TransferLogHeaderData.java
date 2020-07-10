package com.cogent.cogentappointment.admin.dto.jasper.transferLog;

import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rupak ON 2020/07/10-9:03 AM
 */
@Getter
@Setter
public class TransferLogHeaderData implements Serializable {

    private String fromDate;
    private String toDate;
    private String booked;
    private String checkedIn;
    private String cancelled;
    private String refundedToClient;
}

