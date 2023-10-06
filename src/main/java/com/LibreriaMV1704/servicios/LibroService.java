package com.LibreriaMV1704.servicios;

import com.LibreriaMV1704.entidades.Foto;
import com.LibreriaMV1704.entidades.Libro;
import com.LibreriaMV1704.errores.ErrorSeguridad;
import com.LibreriaMV1704.errores.ErrorServicio;
import com.LibreriaMV1704.repositorios.LibroRepositorio;
import com.LibreriaMV1704.repositorios.PrestamoRepositorio;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class LibroService {

    @Autowired
    private LibroRepositorio libroRepositorio;
    
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;

    @Autowired
    private AutorService autorService;
    
    @Autowired
    private EditorialService editorialService;
    
    @Autowired
    private FotoService fotoService;

    public Libro buscarLibroXIsbnS(String isbnStrIng) throws Exception {
        Long isbnLongIng;
        try {
            isbnLongIng = Long.parseLong(isbnStrIng);
            if (isbnLongIng <= 0 || isbnLongIng > 9999) {
                throw new ErrorServicio("El ISBN debe ser un número entre 1 y 9999.");
            }
            Optional<Libro> libroOptional = libroRepositorio.findById(isbnLongIng);
            if (!libroOptional.isPresent()) {
                throw new ErrorServicio("El ISBN ingresado no está en la lista.");
            }
            return libroOptional.get();
        } catch (NumberFormatException e) {
            throw new ErrorServicio("Debes ingresar un número.");
        } catch (ErrorServicio e) {
            throw e;
        }
    }

    @Transactional
    public Libro cargarLibroS(String isbnStrForm, String tituloForm, Long idAutorForm, Long idEditorialForm, Integer anioForm,
            Integer ejemDispForm, String resumenForm, MultipartFile archivo) throws Exception {
        Long isbnLongForm;
        try {
            isbnLongForm = Long.parseLong(isbnStrForm);
            if (isbnLongForm <= 0 || isbnLongForm > 9999) {
                throw new ErrorServicio("El ISBN debe ser un número entre 1 y 9999.");
            }
            if (libroRepositorio.findById(isbnLongForm).isPresent()) {
                throw new ErrorServicio("Ya existe un libro con ese ISBN.");
            }
            validarDatos(tituloForm, anioForm, ejemDispForm);
            Libro libro = new Libro();
            libro.setIsbn(isbnLongForm);
            libro.setTitulo(tituloForm);
            libro.setAutor(autorService.buscarXId(idAutorForm));
            libro.setEditorial(editorialService.buscarXId(idEditorialForm));
            libro.setAnio(anioForm);
            libro.setEjemDisp(ejemDispForm);
            libro.setEjemPrest(0);
            libro.setResumen(resumenForm);
            libro.setAlta(true);
            if (!archivo.isEmpty()) {
                Foto foto = fotoService.guardarFoto(archivo);
                libro.setFoto(foto);
            }
            return libroRepositorio.save(libro);
        } catch (NumberFormatException e) {
            throw new ErrorServicio("El ISBN debe ser un número.");
        } catch (ErrorServicio e) {
            throw e;
        }
    }

    @Transactional
    public void editarLibroS(Long isbnForm, String tituloForm, Long idAutorForm, Long idEditorialForm, Integer anioForm,
            Integer ejemDispForm, String resumenForm, MultipartFile archivo) throws Exception {
        try {
            validarDatos(tituloForm, anioForm, ejemDispForm);
            Libro libro = libroRepositorio.findById(isbnForm).get();
            libro.setTitulo(tituloForm);
            libro.setAutor(autorService.buscarXId(idAutorForm));
            libro.setEditorial(editorialService.buscarXId(idEditorialForm));
            libro.setAnio(anioForm);
            libro.setEjemDisp(ejemDispForm);
            libro.setResumen(resumenForm);
            if (!archivo.isEmpty()) {
                Foto foto = fotoService.guardarFoto(archivo);
                libro.setFoto(foto);
            }
            libroRepositorio.save(libro);
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Libro> listarLibrosXCadenaTituloS(String cadena) throws Exception {
        try {
            if (cadena.trim().isEmpty()) {
                throw new ErrorServicio("El texto a buscar debe tener al menos un carácter no vacío.");
            }
            List<Libro> listLibros = libroRepositorio.listarLibrosXCadenaTituloR("%" + cadena + "%");
            if (listLibros.isEmpty()) {
                throw new ErrorServicio("No se encontraron libros con esos datos.");
            }
            return listLibros;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Libro> listarLibrosXCadenaAutorS(String cadena) throws Exception {
        try {
            if (cadena.trim().isEmpty()) {
                throw new ErrorServicio("El nombre a buscar debe tener al menos un carácter no vacío.");
            }
            List<Libro> listLibros = libroRepositorio.listarLibrosXCadenaAutorR("%" + cadena + "%");
            if (listLibros.isEmpty()) {
                throw new ErrorServicio("No se encontraron libros con esos datos.");
            }
            return listLibros;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Libro> listarLibrosXCadenaEditorialS(String cadena) throws Exception {
        try {
            if (cadena.trim().isEmpty()) {
                throw new ErrorServicio("El nombre a buscar debe tener al menos un carácter no vacío.");
            }
            List<Libro> listLibros = libroRepositorio.listarLibrosXCadenaEditorialR("%" + cadena + "%");
            if (listLibros.isEmpty()) {
                throw new ErrorServicio("No se encontraron libros con esos datos.");
            }
            return listLibros;
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarDatos(String tituloForm, Integer anioForm, Integer ejemDispForm) throws Exception {
        if (tituloForm == null || tituloForm.trim().isEmpty()) {
            throw new ErrorServicio("El título debe tener al menos una letra.");
        }
        Date hoy = new Date();
        if (anioForm == null) {
            throw new ErrorServicio("Debes ingresar el año de publicación.");
        }
        if (anioForm > hoy.getYear() + 1900) {
            throw new ErrorServicio("El año de publicación no puede ser posterior al actual.");
        }
        if (ejemDispForm == null) {
            throw new ErrorServicio("Debes ingresar la cantidad de ejemplares disponibles.");
        }
        if (ejemDispForm < 0) {
            throw new ErrorServicio("La cantidad de ejemplares disponibles no puede ser un número negativo.");
        }
    }
    
    public Libro buscarXIsbn(Long isbn) throws Exception {
        Optional<Libro> libroOptional = libroRepositorio.findById(isbn);
        if (!libroOptional.isPresent()) {
            throw new ErrorServicio("El ISBN ingresado no corresponde a ningún libro.");
        }
        return libroOptional.get();
    }
    
    public List<Libro> listarTodosLibrosAlfabeticamenteS() {
        return libroRepositorio.listarTodosLibrosAlfabeticamenteR();
    }
    
    public List<Libro> listarLibrosDisponiblesAlfabeticamenteS() {
        return libroRepositorio.listarLibrosDisponiblesAlfabeticamenteR();
    }
    
    public List<Libro> listarLibrosDispXAutorS(Long idAutor) {
        return libroRepositorio.listarLibrosDispXAutorR(idAutor);
    }

    public List<Libro> listarLibrosNoDispXAutorS(Long idAutor) {
        return libroRepositorio.listarLibrosNoDispXAutorR(idAutor);
    }
    
    public List<Libro> listarLibrosXEditorialS(Long idEditorial) {
        return libroRepositorio.listarLibrosXEditorialR(idEditorial);
    }
    
    @Transactional
    public void eliminarLibroS(Long isbn) throws Exception {
        Libro libro = buscarXIsbn(isbn);
        if (prestamoRepositorio.libroAlgunaVezPrestadoR(isbn)) {
            throw new ErrorSeguridad("El libro no se puede eliminar ya que está asociado a uno o más préstamos.");
        }
        libroRepositorio.delete(libro);
    }
    
    @Transactional
    public void guardar(Libro libro) {
        libroRepositorio.save(libro);
    }
    
}
