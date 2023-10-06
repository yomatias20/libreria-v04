
package com.LibreriaMV1704.repositorios;

import com.LibreriaMV1704.entidades.Autor;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface AutorRepositorio extends JpaRepository<Autor, Long> {
    
    @Query("SELECT a FROM Autor a ORDER BY a.nombre")
    public List<Autor> listarAutoresAlfR();
    
    @Query("SELECT a FROM Autor a WHERE a.nombre = :nombreAutor")
    public List<Autor> buscarAutorXNombreCompletoR(@Param("nombreAutor") String nombreAutor);
    
}
