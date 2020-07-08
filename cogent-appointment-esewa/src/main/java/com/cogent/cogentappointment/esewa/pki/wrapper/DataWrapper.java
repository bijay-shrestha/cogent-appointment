package com.cogent.cogentappointment.esewa.pki.wrapper;

import lombok.Getter;
import lombok.Setter;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

@Getter
@Setter
@RequestScope
@Component
public class DataWrapper {

    private Object data;

}
