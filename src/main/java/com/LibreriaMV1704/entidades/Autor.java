
package com.LibreriaMV1704.entidades;

import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
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
