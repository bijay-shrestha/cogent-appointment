package com.cogent.cogentappointment.commons.dto.request.file;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;

/**
 * @author rupak ON 2020/06/28-3:45 PM
 */
@Getter
@Setter
@Builder
public class FileURLRequestDTO implements Serializable {

    private String fileName;
}
