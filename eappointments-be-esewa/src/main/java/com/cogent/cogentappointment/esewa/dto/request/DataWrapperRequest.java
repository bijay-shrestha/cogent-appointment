package com.cogent.cogentappointment.esewa.dto.request;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

/**
 * @author rupak ON 2020/07/07-1:31 PM
 */
@Getter
@Setter
@RequestScope
@Component
public class DataWrapperRequest {

    private Object data;

}
