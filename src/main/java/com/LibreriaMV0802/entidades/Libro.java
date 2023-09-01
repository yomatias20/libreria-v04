
package com.LibreriaMV0802.entidades;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToOne;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "libro")
public class Libro implements Serializable {

    @Id
    @Column(name = "ISBN")
    private Long isbn;
    
    @Column(name = "ALTA")
    private boolean alta;
    
    @Column(name = "ANIO")
    private Integer anio;
    
    @Column(name = "EJEMDISP")
    private Integer ejemDisp;
    
    @Column(name = "EJEMPREST")
    private Integer ejemPrest;
    
    @Column(name = "TITULO")
    private String titulo;
    
    @ManyToOne
    @JoinColumn(name = "AUTOR_ID")
    private Autor autor;
    
    @ManyToOne
    @JoinColumn(name = "EDITORIAL_ID")
    private Editorial editorial;

    @Column(name = "RESUMEN")
    private String resumen;
    
    @OneToOne
    @JoinColumn(name = "FOTO_ID")
    private Foto foto;
    
    public void ejemplarPrestado() {
        ejemPrest = ejemPrest + 1;
        ejemDisp = ejemDisp - 1;
    }

    public void ejemplarDevuelto() {
        ejemPrest = ejemPrest - 1;
        ejemDisp = ejemDisp + 1;
    }
    
}
