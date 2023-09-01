
package com.LibreriaMV0802.repositorios;

import com.LibreriaMV0802.entidades.Libro;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface LibroRepositorio extends JpaRepository<Libro, Long> {
    
    @Query("SELECT l FROM Libro l ORDER BY l.titulo")
    public List<Libro> listarTodosLibrosAlfabeticamenteR();
    
    @Query("SELECT l FROM Libro l WHERE l.ejemDisp > 0 AND l.alta = true ORDER BY l.titulo")
    public List<Libro> listarLibrosDisponiblesAlfabeticamenteR();
    
    @Query("SELECT l FROM Libro l WHERE l.autor.id = :idAutor AND l.ejemDisp > 0 AND l.alta = true")
    public List<Libro> listarLibrosDispXAutorR(@Param("idAutor") Long idAutor);
    
    @Query("SELECT l FROM Libro l WHERE l.autor.id = :idAutor AND l.ejemDisp = 0 AND l.alta = true")
    public List<Libro> listarLibrosNoDispXAutorR(@Param("idAutor") Long idAutor);
    
    @Query("SELECT l FROM Libro l WHERE l.titulo LIKE :cadena2 AND l.alta = true ORDER BY l.titulo")
    public List<Libro> listarLibrosXCadenaTituloR(@Param("cadena2") String cadena2);
    
    @Query("SELECT l FROM Libro l WHERE l.autor.nombre LIKE :cadena2 AND l.alta = true ORDER BY l.titulo")
    public List<Libro> listarLibrosXCadenaAutorR(@Param("cadena2") String cadena2);
    
    @Query("SELECT l FROM Libro l WHERE l.editorial.nombre LIKE :cadena2 AND l.alta = true ORDER BY l.titulo")
    public List<Libro> listarLibrosXCadenaEditorialR(@Param("cadena2") String cadena2);
    
    @Query("SELECT l FROM Libro l WHERE l.editorial.id = :idEditorial AND l.alta = true")
    public List<Libro> listarLibrosXEditorialR(@Param("idEditorial") Long idEditorial);
    
}
