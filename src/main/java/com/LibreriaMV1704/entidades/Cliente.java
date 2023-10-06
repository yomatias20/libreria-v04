
package com.LibreriaMV1704.entidades;

import com.LibreriaMV1704.enumeraciones.Categoria;
import com.LibreriaMV1704.enumeraciones.Sexo;
import java.io.Serializable;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
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
