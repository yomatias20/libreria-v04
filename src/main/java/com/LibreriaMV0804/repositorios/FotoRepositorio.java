
package com.LibreriaMV0804.repositorios;

import com.LibreriaMV0804.entidades.Foto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FotoRepositorio extends JpaRepository<Foto, Long> {
    
}
