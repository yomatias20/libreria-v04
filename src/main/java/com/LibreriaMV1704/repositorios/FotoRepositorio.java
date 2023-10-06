
package com.LibreriaMV1704.repositorios;

import com.LibreriaMV1704.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, Long> {
    
}
