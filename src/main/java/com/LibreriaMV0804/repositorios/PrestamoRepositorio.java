
package com.LibreriaMV0804.repositorios;

import com.LibreriaMV0804.entidades.Prestamo;
import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PrestamoRepositorio extends JpaRepository<Prestamo, Integer> {
    
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.id = :idCliente AND p.fechaDevol IS NULL")
    public List<Prestamo> listarPrestamosActivosXClienteR(@Param("idCliente") Integer idCliente);
    
    @Query("SELECT p FROM Prestamo p WHERE p.cliente.id = :idCliente AND p.fechaDevol IS NOT NULL")
    public List<Prestamo> listarPrestamosCanceladosXClienteR(@Param("idCliente") Integer idCliente);
    
    @Query("SELECT p FROM Prestamo p WHERE p.libro.isbn = :isbn AND p.fechaDevol IS NULL")
    public List<Prestamo> listarPrestamosActivosXLibroR(@Param("isbn") Long isbn);
    
    @Query("SELECT p FROM Prestamo p WHERE p.libro.isbn = :isbn AND p.fechaDevol IS NOT NULL")
    public List<Prestamo> listarPrestamosCanceladosXLibroR(@Param("isbn") Long isbn);
    
    @Query("SELECT COUNT(p) > 0 FROM Prestamo p WHERE p.libro.isbn = :isbn")
    public boolean libroAlgunaVezPrestadoR(@Param("isbn") Long isbn);
    
    @Query("SELECT COUNT(p) > 0 FROM Prestamo p WHERE p.cliente.id = :idCliente AND p.fechaDevol IS NULL")
    public boolean clienteConAlgunPrestamoActivoR(@Param("idCliente") Integer idCliente);
    
}
