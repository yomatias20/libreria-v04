
package com.LibreriaMV1704.entidades;

import java.io.Serializable;
import java.util.Date;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.persistence.Temporal;
import jakarta.persistence.TemporalType;
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
