
package com.LibreriaMV0802.entidades;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import lombok.Data;

@Entity
@Data
@Table(name = "prestamo")
public class Prestamo implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHAPREST")
    private Date fechaPrest;
    
    @Temporal(TemporalType.DATE)
    @Column(name = "FECHADEVOL")
    private Date fechaDevol;
    
    @ManyToOne
    @JoinColumn(name = "LIBRO_ISBN")
    private Libro libro;
    
    @ManyToOne
    @JoinColumn(name = "CLIENTE_ID")
    private Cliente cliente;

}
