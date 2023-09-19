
package com.LibreriaMV0804.entidades;

import com.LibreriaMV0804.enumeraciones.Categoria;
import com.LibreriaMV0804.enumeraciones.Sexo;
import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import lombok.Data;

@Entity
@Data
@Table(name = "cliente")
public class Cliente implements Serializable {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ID")
    private Integer id;
    
    @Column(name = "DNI")
    private String dni;
    
    @Column(name = "NOMBRE")
    private String nombre;
    
    @Column(name = "APELLIDOS")
    private String apellidos;
    
    @Column(name = "TELEFONO")
    private String telefono;
    
    @Column(name = "EMAIL")
    private String email;
    
    @Column(name = "CLAVE")
    private String clave;
    
    @Column(name = "SEXO")
    @Enumerated(EnumType.STRING)
    private Sexo sexo;
    
    @Column(name = "CATEGORIA")
    @Enumerated(EnumType.STRING)
    private Categoria categoria;
    
    @Column(name = "ALTA")
    private Boolean alta;
    
}
