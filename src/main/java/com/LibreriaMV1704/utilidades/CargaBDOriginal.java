package com.LibreriaMV1704.utilidades;

import com.LibreriaMV1704.entidades.Foto;
import com.LibreriaMV1704.repositorios.FotoRepositorio;
import jakarta.transaction.Transactional;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.Optional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class CargaBDOriginal {

    @Autowired
    private FotoRepositorio fotoRepositorio;

    @Transactional
    public void cargarFoto(Long idFoto, String url) throws IOException {
        File file = new File(url);
        byte[] bytes = Files.readAllBytes(file.toPath());
        Foto foto = new Foto();
        Optional<Foto> fotoOptional = fotoRepositorio.findById(idFoto);
        if (fotoOptional.isPresent()) {
            foto = fotoOptional.get();
        }
        foto.setContenido(bytes);
        fotoRepositorio.save(foto);
    }

}
