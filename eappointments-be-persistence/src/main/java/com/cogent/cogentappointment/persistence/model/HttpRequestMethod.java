package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.HttpRequestMethodEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak on 2020-05-18
 */
@Entity
@Table(name = "http_request_method")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(HttpRequestMethodEntityListener.class)
public class HttpRequestMethod extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "HttpRequestMethod{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", status=" + status +
                '}';

    }


}
