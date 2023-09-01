
package com.LibreriaMV0802.repositorios;

import com.LibreriaMV0802.entidades.Editorial;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface EditorialRepositorio extends JpaRepository<Editorial, Long> {
    
    @Query("SELECT e FROM Editorial e ORDER BY e.nombre")
    public List<Editorial> listarEditorialesAlfR();
    
    @Query("SELECT e FROM Editorial e WHERE e.nombre = :nombreEditorial")
    public List<Editorial> buscarEditorialXNombreCompletoR(@Param("nombreEditorial") String nombreEditorial);
    
}
