
package com.LibreriaMV0802.servicios;

import com.LibreriaMV0802.entidades.Foto;
import com.LibreriaMV0802.repositorios.FotoRepositorio;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class FotoService {
    
    @Autowired
    private FotoRepositorio fotoRepositorio;
    
    public Foto guardarFoto(MultipartFile archivo) throws Exception {
        if (archivo != null) {
            Foto foto = new Foto();
            foto.setNombre(archivo.getName());
            foto.setMime(archivo.getContentType());
            foto.setContenido(archivo.getBytes());
            return fotoRepositorio.save(foto);
        } else {
            return null;
        }
    }
    
    public Foto actualizarFoto(Long idFoto, MultipartFile archivo) throws Exception {
        if (archivo != null) {
            Foto foto = new Foto();
            if (idFoto != null) {
                Optional<Foto> fotoOptional = fotoRepositorio.findById(idFoto);
                if (fotoOptional.isPresent()) {
                    foto = fotoOptional.get();
                }
            }
            foto.setNombre(archivo.getName());
            foto.setMime(archivo.getContentType());
            foto.setContenido(archivo.getBytes());
            return fotoRepositorio.save(foto);
        } else {
            return null;
        }
    }
    
}
