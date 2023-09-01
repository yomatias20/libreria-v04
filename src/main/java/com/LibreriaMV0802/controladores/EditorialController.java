
package com.LibreriaMV0802.controladores;

import com.LibreriaMV0802.entidades.Editorial;
import com.LibreriaMV0802.enumeraciones.PermisoSession;
import com.LibreriaMV0802.errores.ErrorSeguridad;
import com.LibreriaMV0802.errores.ErrorServicio;
import com.LibreriaMV0802.servicios.EditorialService;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/editoriales")
public class EditorialController {

    @Autowired
    private EditorialService editorialService;
    
    @Autowired
    private LibroService libroService;

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping
    public String portalEditoriales(Model modelo, HttpSession session) {
        modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
        return "editoriales/portal_editoriales.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/cargar1")
    public String portalEditorialesCarga(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
            modelo.addAttribute("estado", "carga");
            return "editoriales/portal_editoriales.html";
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
    public String registrarEditorialC(Model modelo, HttpSession session, @RequestParam String nombreEditorial, 
            RedirectAttributes redir) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            editorialService.registrarEditorialS(nombreEditorial);
            redir.addFlashAttribute("exito", "La editorial fue agregada satisfactoriamente:");
        } catch (ErrorServicio e) {
            redir.addFlashAttribute("estado", "carga");
            redir.addFlashAttribute("error", e.getMessage());
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
        return "redirect:/editoriales";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/edicion")
    public String portalEditorialesEdicion(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
            modelo.addAttribute("estado", "edición");
            return "editoriales/portal_editoriales.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/editar1")
    public String editarEditorial1C(Model modelo, HttpSession session, Long idForm, RedirectAttributes redir) throws Exception {
        try {
            if (idForm == null) {
                throw new ErrorServicio("Debes ingresar un número.");
            }
            Editorial editorial = editorialService.buscarXId(idForm);
            redir.addFlashAttribute("estado", "edición avanzada");
            redir.addFlashAttribute("editorial_seleccionada", editorial);            
        } catch (ErrorServicio e) {
            redir.addFlashAttribute("estado", "edición");
            redir.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";    
        }
        return "redirect:/editoriales";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/editar2")
    public String editarEditorial2C(Model modelo, HttpSession session, Long idForm, String nombreForm, RedirectAttributes redir) 
            throws Exception {
        try {
            editorialService.editarEditorialS(idForm, nombreForm);
            redir.addFlashAttribute("exito", "La editorial fue modificada satisfactoriamente:");
        } catch (ErrorServicio e) {
            redir.addFlashAttribute("estado", "edición avanzada");
            redir.addFlashAttribute("editorial_seleccionada", editorialService.buscarXId(idForm));
            redir.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";    
        }
        return "redirect:/editoriales";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/borrado")
    public String portalEditorialesBorrado(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            modelo.addAttribute("editoriales", editorialService.listarEditorialesAlfS());
            modelo.addAttribute("estado", "borrado");
            return "editoriales/portal_editoriales.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/eliminar1")
    public String eliminarEditorial1C(Model modelo, HttpSession session, Long idForm, RedirectAttributes redir) throws Exception {
        try {
            if (idForm == null) {
                throw new ErrorServicio("Debes ingresar un número.");
            }
            Editorial editorial = editorialService.buscarXId(idForm);
            if (!libroService.listarLibrosXEditorialS(idForm).isEmpty()) {
                throw new ErrorServicio("Esta editorial no se puede eliminar ya que hay libros suyos en la librería.");
            }
            redir.addFlashAttribute("estado", "borrado avanzado");
            redir.addFlashAttribute("editorial_seleccionada", editorial);
        } catch (ErrorServicio e) {
            redir.addFlashAttribute("estado", "borrado");
            redir.addFlashAttribute("error", e.getMessage());
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";    
        }
        return "redirect:/editoriales";
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/eliminar2")
    public String eliminarEditorial2C(Model modelo, HttpSession session, Long idForm, RedirectAttributes redir) throws Exception {
        try {
            editorialService.eliminarEditorialS(idForm);
            redir.addFlashAttribute("exito", "La editorial fue eliminada satisfactoriamente:");
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";    
        }
        return "redirect:/editoriales";
    }

}
