
package com.LibreriaMV0802.controladores;

import com.LibreriaMV0802.entidades.Cliente;
import com.LibreriaMV0802.entidades.Prestamo;
import com.LibreriaMV0802.enumeraciones.PermisoSession;
import com.LibreriaMV0802.errores.ErrorGeneral;
import com.LibreriaMV0802.errores.ErrorSeguridad;
import com.LibreriaMV0802.errores.ErrorServicio;
import com.LibreriaMV0802.servicios.ClienteService;
import com.LibreriaMV0802.servicios.LibroService;
import com.LibreriaMV0802.servicios.PrestamoService;
import java.util.ArrayList;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

@Controller
@RequestMapping("/prestamos")
public class PrestamoController {

    @Autowired
    private PrestamoService prestamoService;

    @Autowired
    private ClienteService clienteService;

    @Autowired
    private LibroService libroService;
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public String portalPrestamos(HttpSession session) {
        return "prestamos/portal_prestamos.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/listado_x_cliente")
    public String prestamosXCliente(Model modelo, @RequestParam Integer idClienteLink, HttpSession session) throws Exception {
        try {
            Cliente clientePrestamos = clienteService.buscarXId(idClienteLink);
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR) 
                    && !clientePrestamos.getId().equals(clienteSession.getId())) {
                throw new ErrorSeguridad("No puedes acceder a los préstamos de otro usuario.");
            }
            modelo.addAttribute("cliente", clientePrestamos);
            modelo.addAttribute("prest_activos", prestamoService.listarPrestamosActivosXClienteS(idClienteLink));
            modelo.addAttribute("prest_cancelados", prestamoService.listarPrestamosCanceladosXClienteS(idClienteLink));
            return "prestamos/list_prest_x_cliente.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (ErrorGeneral e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_general.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listado_x_libro")
    public String prestamosXLibro(Model modelo, HttpSession session, @RequestParam Long isbnLink) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            List<Prestamo> listPrestCancelados = prestamoService.listarPrestamosCanceladosXLibroS(isbnLink);
            List<Cliente> listClientesPrestCancelados = new ArrayList<>();
            for (Prestamo prestamo : listPrestCancelados) {
                listClientesPrestCancelados.add(prestamo.getCliente());
            }
            modelo.addAttribute("libro", libroService.buscarXIsbn(isbnLink));
            modelo.addAttribute("prest_activos", prestamoService.listarPrestamosActivosXLibroS(isbnLink));
            modelo.addAttribute("prest_cancelados", listPrestCancelados);
            modelo.addAttribute("hay_bajas", clienteService.hayAlgunaBaja(listClientesPrestCancelados));
            return "prestamos/list_prest_x_libro.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";    
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/cargar1")
    public String cargarPrestamo1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            modelo.addAttribute("clientes", clienteService.listarClientesActivosAlfabeticamenteS());
            modelo.addAttribute("libros", libroService.listarLibrosDisponiblesAlfabeticamenteS());
            return "prestamos/carga_prestamo.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";    
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/cargar_l1")
    public String cargarPrestamoL1C(Model modelo, HttpSession session, @RequestParam Long isbnLink) throws Exception {
        try {
            if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                modelo.addAttribute("clientes", clienteService.listarClientesActivosAlfabeticamenteS());
            } else if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_USUARIO)) {
                throw new ErrorSeguridad("No se han definido los permisos para esta operación.");
            }
            modelo.addAttribute("libro_selecc", libroService.buscarXIsbn(isbnLink));
            modelo.addAttribute("libros", libroService.listarLibrosDisponiblesAlfabeticamenteS());
            return "prestamos/carga_prestamo.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";    
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/cargar_c1")
    public String cargarPrestamoC1C(Model modelo, HttpSession session, @RequestParam Integer idClienteLink) throws Exception {
        try {
            Cliente clientePrestamo = clienteService.buscarXId(idClienteLink);
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                modelo.addAttribute("clientes", clienteService.listarClientesActivosAlfabeticamenteS());
                modelo.addAttribute("cliente_selecc", clientePrestamo);
            } else if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_USUARIO)) {
                if (!clientePrestamo.getId().equals(clienteSession.getId())) {
                    throw new ErrorSeguridad("No puedes generar un préstamo para otro usuario.");
                }
            } else {
                throw new ErrorSeguridad("No se han definido los permisos para esta operación.");
            }
            modelo.addAttribute("libros", libroService.listarLibrosDisponiblesAlfabeticamenteS());
            return "prestamos/carga_prestamo.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (ErrorGeneral e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_general.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/cargar2")
    public String cargarPrestamo2C(Model modelo, HttpSession session, Long isbnForm, Integer idClienteForm, 
            String fechaPrestStrForm, RedirectAttributes redir) throws Exception {
        try {
            prestamoService.cargarPrestamoS(isbnForm, idClienteForm, fechaPrestStrForm);
            redir.addAttribute("idClienteLink", idClienteForm);
            redir.addFlashAttribute("exito_carga", true);
            return "redirect:/prestamos/listado_x_cliente";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("clientes", clienteService.listarClientesActivosAlfabeticamenteS());
            modelo.addAttribute("libros", libroService.listarLibrosDisponiblesAlfabeticamenteS());
            return "prestamos/carga_prestamo.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/devolver1")
    public String devolucion1C(Model modelo, HttpSession session, @RequestParam Integer idPrestLink) throws Exception {
        try {
            Prestamo prestamo = prestamoService.buscarXId(idPrestLink);
            Cliente clientePrestamo = prestamo.getCliente();
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_USUARIO)) {
                    if (!clientePrestamo.getId().equals(clienteSession.getId())) {
                        throw new ErrorSeguridad("No puedes devolver un préstamo de otro usuario.");
                    }
                } else {
                    throw new ErrorSeguridad("No se han definido los permisos para esta operación.");
                }
            }
            modelo.addAttribute("prestamo", prestamo);
            return "prestamos/devolucion.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (ErrorGeneral e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_general.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/devolver2")
    public String devolucion2C(Model modelo, HttpSession session, @RequestParam Integer idPrestamoForm, 
            String fechaDevolStrForm, RedirectAttributes redir) throws Exception {
        Prestamo prestamo = prestamoService.buscarXId(idPrestamoForm);
        Cliente cliente = prestamo.getCliente();
        try {
            prestamoService.devolucionS(prestamo, fechaDevolStrForm);
            redir.addAttribute("idClienteLink", cliente.getId());
            redir.addFlashAttribute("exito_devol", true);
            return "redirect:/prestamos/listado_x_cliente";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("prestamo", prestamo);
            return "prestamos/devolucion.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

}
