package com.cogent.cogentappointment.esewa.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.Serializable;

/**
 * @author rupak ON 2020/07/06-1:09 PM
 */
@Getter
@Setter
@RequestScope
@Component
public class EsewaRequestDTO implements Serializable {

    private Object data;
}
