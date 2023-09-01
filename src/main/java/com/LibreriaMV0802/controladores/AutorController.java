
package com.LibreriaMV0802.controladores;

import com.LibreriaMV0802.entidades.Autor;
import com.LibreriaMV0802.enumeraciones.PermisoSession;
import com.LibreriaMV0802.errores.ErrorSeguridad;
import com.LibreriaMV0802.servicios.AutorService;
import com.LibreriaMV0802.servicios.LibroService;
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
@RequestMapping("/autores")
public class AutorController {

    @Autowired
    private AutorService autorService;

    @Autowired
    private LibroService libroService;
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping
    public String portalAutores(Model modelo, HttpSession session) {
        modelo.addAttribute("autores", autorService.listarAutoresAlfS());
        return "autores/portal_autores.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/datos_autor")
    public String mostrarAutor(@RequestParam Long idLink, Model modelo, HttpSession session) throws Exception {
        modelo.addAttribute("autor", autorService.buscarXId(idLink));
        modelo.addAttribute("libros_disp", libroService.listarLibrosDispXAutorS(idLink));
        modelo.addAttribute("libros_no_disp", libroService.listarLibrosNoDispXAutorS(idLink));
        return "autores/autor.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/cargar1")
    public String cargarAutor1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            return "autores/carga_autor.html";
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
    public String cargarAutor2C(Model modelo, String nombreForm,
            String bioForm, MultipartFile archivo, HttpSession session, RedirectAttributes redir) throws Exception {
        try {
            Autor autor = autorService.cargarAutorS(nombreForm, bioForm, archivo);
            redir.addAttribute("idLink", autor.getId());
            redir.addFlashAttribute("exito_carga", true);
            return "redirect:/autores/datos_autor";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("nombre_f", nombreForm);
            modelo.addAttribute("bio_f", bioForm);
            return "autores/carga_autor.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/editar1")
    public String editarAutor1C(Model modelo, @RequestParam Long idLink, HttpSession session) {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            Autor autor = autorService.buscarXId(idLink);
            modelo.addAttribute("autor", autor);
            return "autores/edicion_autor.html";
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
    public String editarAutorC(Model modelo, @RequestParam Long idForm, @RequestParam String nombreForm,
            @RequestParam String bioForm, MultipartFile archivo, HttpSession session, RedirectAttributes redir) throws Exception {
        Autor autor = autorService.buscarXId(idForm);
        try {
            autorService.editarAutorS(idForm, nombreForm, bioForm, archivo);
            redir.addAttribute("idLink", autor.getId());
            redir.addFlashAttribute("exito_edicion", true);
            return "redirect:/autores/datos_autor";
        } catch (Exception e) {
            modelo.addAttribute("autor", autor);
            modelo.addAttribute("error", e.getMessage());
            return "autores/edicion_autor.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/eliminar")
    public String eliminarAutorC(Model modelo, @RequestParam Long idForm, HttpSession session, RedirectAttributes redir) {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            autorService.eliminarAutorS(idForm);
            redir.addFlashAttribute("exito_borrado", true);
            return "redirect:/autores";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

}
