package com.cogent.cogentappointment.persistence.model;

import com.cogent.cogentappointment.persistence.audit.Auditable;
import com.cogent.cogentappointment.persistence.listener.FavouriteEntityListener;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.io.Serializable;

/**
 * @author rupak ON 2020/06/16-11:50 AM
 */
@Entity
@Table(name = "favourite")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@EntityListeners(FavouriteEntityListener.class)
public class Favourite extends Auditable<String> implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "code")
    private String code;

    @Column(name = "path")
    private String path;

    @Column(name = "description")
    private String description;

    @Column(name = "remarks")
    private String remarks;

    @Column(name = "status")
    private Character status;

    @Override
    public String toString() {
        return "Favourite{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", code='" + code + '\'' +
                ", path='" + path + '\'' +
                ", description='" + description + '\'' +
                ", remarks='" + remarks + '\'' +
                ", status=" + status +
                '}';
    }

    }