
package com.LibreriaMV0804.controladores;

import com.LibreriaMV0804.entidades.Autor;
import com.LibreriaMV0804.entidades.Libro;
import com.LibreriaMV0804.errores.ErrorServicio;
import com.LibreriaMV0804.servicios.AutorService;
import com.LibreriaMV0804.servicios.LibroService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/foto")
public class FotoController {
    
    @Autowired
    private AutorService autorService;
    
    @Autowired
    private LibroService libroService;
    
    @GetMapping("/foto_libro/{isbnLibro}")
    public ResponseEntity<byte[]> fotoLibro(@PathVariable Long isbnLibro) throws Exception {
        try {
            Libro libro = libroService.buscarXIsbn(isbnLibro);
            if (libro.getFoto() == null) {
                throw new ErrorServicio("El libro no tiene foto asignada.");
            }
            byte[] foto = libro.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
    @GetMapping("/foto_autor/{idAutor}")
    public ResponseEntity<byte[]> fotoAutor(@PathVariable Long idAutor) throws Exception {
        try {
            Autor autor = autorService.buscarXId(idAutor);
            if (autor.getFoto() == null) {
                throw new ErrorServicio("El autor no tiene foto asignada.");
            }
            byte[] foto = autor.getFoto().getContenido();
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.IMAGE_JPEG);
            return new ResponseEntity<>(foto, headers, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(HttpStatus.NOT_FOUND);
        }
    }
    
}
