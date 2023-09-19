
package com.LibreriaMV0804.entidades;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "foto")
public class Foto implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Long id;
    
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Column(name = "MIME")
    private String mime;
    
    @Lob
    @Basic(fetch = FetchType.LAZY)
    @Column(name = "CONTENIDO")
    private byte[] contenido;

}
