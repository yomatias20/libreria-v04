
package com.LibreriaMV0802.repositorios;

import com.LibreriaMV0802.entidades.Cliente;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface ClienteRepositorio extends JpaRepository<Cliente, Integer> {
    
    @Query("SELECT c FROM Cliente c ORDER BY c.apellidos, c.nombre")
    public List<Cliente> listarTodosClientesAlfabeticamenteR();
    
    @Query("SELECT c FROM Cliente c WHERE c.alta = true ORDER BY c.apellidos, c.nombre")
    public List<Cliente> listarClientesActivosAlfabeticamenteR();
    
    @Query("SELECT c FROM Cliente c WHERE c.nombre LIKE :cadena2 OR c.apellidos LIKE :cadena2 ORDER BY c.apellidos, c.nombre")
    public List<Cliente> listarClientesXCadenaNombreR(@Param("cadena2") String cadena2);
    
    @Query("SELECT c FROM Cliente c WHERE c.dni LIKE :cadena2 ORDER BY c.apellidos, c.nombre")
    public List<Cliente> listarClientesXCadenaDniR(@Param("cadena2") String cadena2);
    
    @Query("SELECT c FROM Cliente c WHERE c.telefono LIKE :cadena2 ORDER BY c.apellidos, c.nombre")
    public List<Cliente> listarClientesXCadenaTelefonoR(@Param("cadena2") String cadena2);
    
    @Query("SELECT c FROM Cliente c WHERE c.email LIKE :cadena2 ORDER BY c.apellidos, c.nombre")
    public List<Cliente> listarClientesXCadenaEmailR(@Param("cadena2") String cadena2);
    
    @Query("SELECT c FROM Cliente c WHERE c.email = :email")
    public Cliente buscarClienteXEmailR(@Param("email") String email);
    
}
