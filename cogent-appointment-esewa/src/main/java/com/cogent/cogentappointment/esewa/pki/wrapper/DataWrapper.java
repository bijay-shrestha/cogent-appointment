package com.cogent.cogentappointment.esewa.pki.wrapper;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.context.annotation.RequestScope;

import java.io.Serializable;

@Component
@RequestScope
@Slf4j
public class DataWrapper implements Serializable {

    private String data;

    public DataWrapper() {
        log.info("DataWrapper Initialized");
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }
}
