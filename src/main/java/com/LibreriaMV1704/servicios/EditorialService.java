
package com.LibreriaMV1704.servicios;

import com.LibreriaMV1704.entidades.Editorial;
import com.LibreriaMV1704.errores.ErrorServicio;
import com.LibreriaMV1704.repositorios.EditorialRepositorio;
import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class EditorialService {
    
    @Autowired
    private EditorialRepositorio editorialRepositorio;
    
    @Transactional
    public void registrarEditorialS(String nombreForm) throws ErrorServicio {
        try {
            if (nombreForm == null || nombreForm.trim().isEmpty()) {
                throw new ErrorServicio("El nombre debe tener al menos una letra.");
            }
            if (!editorialRepositorio.buscarEditorialXNombreCompletoR(nombreForm).isEmpty()) {
                throw new ErrorServicio("Ya existe una editorial con ese nombre.");
            }
            Editorial editorial = new Editorial();
            editorial.setNombre(nombreForm);
            editorial.setAlta(true);
            editorialRepositorio.save(editorial);
        } catch (ErrorServicio e) {
            throw e;
        }
    }
    
    @Transactional
    public void editarEditorialS(Long idForm, String nombreForm) throws ErrorServicio {
        try {
            if (nombreForm == null || nombreForm.trim().isEmpty()) {
                throw new ErrorServicio("El nombre debe tener al menos una letra.");
            }
            Editorial editorial = editorialRepositorio.findById(idForm).get();
            editorial.setNombre(nombreForm);
            editorialRepositorio.save(editorial);
        } catch (ErrorServicio e) {
            throw e;
        }
    }
    
    public List<Editorial> listarEditorialesAlfS() {
        return editorialRepositorio.listarEditorialesAlfR();
    }
    
    public Editorial buscarXId(Long id) throws Exception {
        Optional<Editorial> editorialOptional = editorialRepositorio.findById(id);
        if (!editorialOptional.isPresent()) {
            throw new ErrorServicio("El número ingresado no corresponde a ninguna editorial.");
        }
        return editorialOptional.get();
    }
        
    public void eliminarEditorialS(Long id) throws Exception {
        editorialRepositorio.delete(buscarXId(id));
    }
    
}
