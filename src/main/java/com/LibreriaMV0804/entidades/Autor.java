
package com.LibreriaMV0804.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "autor")
public class Autor implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "ALTA")
    private Boolean alta;
    
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Column(name = "BIOGRAFIA")
    private String biografia;

    @OneToOne
    @JoinColumn(name = "FOTO_ID")
    private Foto foto;
    
}
