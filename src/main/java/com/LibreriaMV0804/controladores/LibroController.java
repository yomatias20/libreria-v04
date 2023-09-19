
package com.LibreriaMV0804.controladores;

import com.LibreriaMV0804.entidades.Libro;
import com.LibreriaMV0804.enumeraciones.PermisoSession;
import com.LibreriaMV0804.errores.ErrorSeguridad;
import com.LibreriaMV0804.errores.ErrorServicio;
import com.LibreriaMV0804.servicios.AutorService;
import com.LibreriaMV0804.servicios.EditorialService;
import com.LibreriaMV0804.servicios.LibroService;
import com.LibreriaMV0804.servicios.PrestamoService;
import java.util.List;
import javax.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/libros")
public class LibroController {
    
    @Autowired
    private LibroService libroService;
    
    @Autowired
    private AutorService autorService;
    
    @Autowired
    private EditorialService editorialService;
    
    @Autowired 
    private PrestamoService prestamoService;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping
    public String portalLibros(HttpSession session) {
        return "libros/portal_libros.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/cargar1")
    public String cargarLibro1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            modelo.addAttribute("autores", autorService.listarAutoresAlfS());
            modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
            return "libros/carga_libro.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/cargar2")
    public String cargarLibro2C(Model modelo, String isbnStrForm, String tituloForm, Long idAutorForm, Long idEditorialForm,
            Integer anioForm, Integer ejemDispForm, String resumenForm, MultipartFile archivo, HttpSession session, 
            RedirectAttributes redir) throws Exception {
        try {
            Libro libro = libroService.cargarLibroS(isbnStrForm, tituloForm, idAutorForm, idEditorialForm, anioForm, ejemDispForm,
                    resumenForm, archivo);
            redir.addAttribute("isbnLink", libro.getIsbn());
            redir.addFlashAttribute("exito_carga", true);
            return "redirect:/libros/datos_libro";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("autores", autorService.listarAutoresAlfS());
            modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
            modelo.addAttribute("isbn_f", isbnStrForm);
            modelo.addAttribute("titulo_f", tituloForm);
            modelo.addAttribute("id_autor_f", idAutorForm);
            modelo.addAttribute("id_editorial_f", idEditorialForm);
            modelo.addAttribute("anio_f", anioForm);
            modelo.addAttribute("ejem_disp_f", ejemDispForm);
            modelo.addAttribute("resumen_f", resumenForm);
            return "libros/carga_libro.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/listar_todos")
    public String listarTodosLibrosAlfabeticamenteC(Model modelo, HttpSession session) {
        List<Libro> listLibros = libroService.listarTodosLibrosAlfabeticamenteS();
        modelo.addAttribute("libros", listLibros);
        modelo.addAttribute("tipo_busq", "orden alfabético");
        return "libros/lista_libros.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/busqueda_x_isbn1")
    public String buscarXIsbn1C(HttpSession session) {
        return "libros/busq_libros_isbn.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/busqueda_x_isbn2")
    public String buscarXIsbnStr2C(Model modelo, @RequestParam String isbnStrIng, HttpSession session) throws Exception {
        try {
            Libro libro = libroService.buscarLibroXIsbnS(isbnStrIng);
            modelo.addAttribute("libro", libro);
            modelo.addAttribute("ejem_tot", libro.getEjemDisp() + libro.getEjemPrest());
            modelo.addAttribute("prestado", prestamoService.libroAlgunaVezPrestadoS(libro.getIsbn()));
            return "libros/libro.html";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            return "libros/busq_libros_isbn.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/datos_libro")
    public String buscarXIsbnLinkC(Model modelo, @RequestParam Long isbnLink, HttpSession session) throws Exception {
        Libro libro = libroService.buscarXIsbn(isbnLink);
        modelo.addAttribute("libro", libro);
        modelo.addAttribute("ejem_tot", libro.getEjemDisp() + libro.getEjemPrest());
        modelo.addAttribute("prestado", prestamoService.libroAlgunaVezPrestadoS(isbnLink));
        return "libros/libro.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/busqueda_x_titulo1")
    public String buscarXCadenaTitulo1C(HttpSession session) {
        return "libros/busq_libros_titulo.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/busqueda_x_titulo2")
    public String buscarXCadenaTitulo2C(Model modelo, HttpSession session, String cadena) throws Exception {
        try {
            List<Libro> listLibros = libroService.listarLibrosXCadenaTituloS(cadena);
            modelo.addAttribute("libros", listLibros);
            modelo.addAttribute("tipo_busq", "título");
            return "libros/lista_libros.html";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            return "libros/busq_libros_titulo.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/busqueda_x_autor1")
    public String buscarXCadenaAutor1C(HttpSession session) {
        return "libros/busq_libros_autor.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/busqueda_x_autor2")
    public String buscarXCadenaAutor2C(Model modelo, HttpSession session, String cadena) throws Exception {
        try {
            List<Libro> listLibros = libroService.listarLibrosXCadenaAutorS(cadena);
            modelo.addAttribute("libros", listLibros);
            modelo.addAttribute("tipo_busq", "autor");
            return "libros/lista_libros.html";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            return "libros/busq_libros_autor.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/busqueda_x_editorial1")
    public String buscarXCadenaEditorial1C(HttpSession session) {
        return "libros/busq_libros_editorial.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/busqueda_x_editorial2")
    public String buscarXCadenaEditorial2C(Model modelo, HttpSession session, String cadena) throws Exception {
        try {
            List<Libro> listLibros = libroService.listarLibrosXCadenaEditorialS(cadena);
            modelo.addAttribute("libros", listLibros);
            modelo.addAttribute("tipo_busq", "editorial");
            return "libros/lista_libros.html";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            return "libros/busq_libros_editorial.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editar1")
    public String editarLibro1C(Model modelo, HttpSession session, @RequestParam Long isbnLink) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            modelo.addAttribute("libro", libroService.buscarXIsbn(isbnLink));
            modelo.addAttribute("autores", autorService.listarAutoresAlfS());
            modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
            return "libros/edicion_libro.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/editar2")
    public String editarLibro2C(Model modelo, @RequestParam Long isbnForm, String tituloForm, Long idAutorForm,
            Long idEditorialForm, Integer anioForm, Integer ejemDispForm, String resumenForm, MultipartFile archivo,
            HttpSession session, RedirectAttributes redir) throws Exception {
        Libro libro = libroService.buscarXIsbn(isbnForm);
        try {
            libroService.editarLibroS(isbnForm, tituloForm, idAutorForm, idEditorialForm, anioForm, ejemDispForm, 
                    resumenForm, archivo);
            redir.addAttribute("isbnLink", libro.getIsbn());
            redir.addFlashAttribute("exito_edicion", true);
            return "redirect:/libros/datos_libro";
        } catch (ErrorServicio e) {
            modelo.addAttribute("libro", libro);
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("autores", autorService.listarAutoresAlfS());
            modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
            return "libros/edicion_libro.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/eliminar")
    public String eliminarLibroC(Model modelo, HttpSession session, @RequestParam Long isbnForm, RedirectAttributes redir) 
            throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            libroService.eliminarLibroS(isbnForm);
            redir.addFlashAttribute("exito_borrado", true);
            return "redirect:/libros/listar_todos";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

}
