
package com.LibreriaMV1704.controladores;

import com.LibreriaMV1704.entidades.Cliente;
import com.LibreriaMV1704.enumeraciones.Categoria;
import com.LibreriaMV1704.enumeraciones.PermisoSession;
import com.LibreriaMV1704.enumeraciones.Sexo;
import com.LibreriaMV1704.errores.ErrorGeneral;
import com.LibreriaMV1704.errores.ErrorSeguridad;
import com.LibreriaMV1704.errores.ErrorServicio;
import com.LibreriaMV1704.servicios.ClienteService;
import com.LibreriaMV1704.servicios.PrestamoService;
import java.util.List;
import jakarta.servlet.http.HttpSession;
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
@RequestMapping("/clientes")
public class ClienteController {

    @Autowired
    private ClienteService clienteService;
    
    @Autowired
    private PrestamoService prestamoService;

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping
    public String portalClientes(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            return "clientes/portal_clientes.html";
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
    public String cargarClienteAdm1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            modelo.addAttribute("permiso_modelo", PermisoSession.SESION_ADMINISTRADOR);
            return "clientes/carga_cliente.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    // No habilitado para usuarios registrados. Solo para administradores o usuarios sin registrar.
    @PostMapping("/cargar2")
    public String cargarCliente2C(Model modelo, HttpSession session, String nombreForm, String apellidosForm, String dniForm,
            String telefonoForm, String emailForm, String clave1Form, String clave2Form, Sexo sexoForm, 
            RedirectAttributes redir) throws Exception {
        try {
            Cliente cliente = clienteService.cargarClienteS(nombreForm, apellidosForm, dniForm, telefonoForm, emailForm, 
                    clave1Form, clave2Form, sexoForm);
            if (session.getAttribute("permiso_session") != null) {
                redir.addAttribute("idLink", cliente.getId());
                redir.addFlashAttribute("exito_carga", true);
                return "redirect:/clientes/datos_cliente";
            } else {
                redir.addFlashAttribute("alta_cliente", "Te has dado de alta satisfactoriamente.");
                return "redirect:/login";
            }
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("nombre_f", nombreForm);
            modelo.addAttribute("apellidos_f", apellidosForm);
            modelo.addAttribute("dni_f", dniForm);
            modelo.addAttribute("telefono_f", telefonoForm);
            modelo.addAttribute("email_f", emailForm);
            modelo.addAttribute("sexo_f", sexoForm.toString());
            if (session.getAttribute("permiso_session") != null) {
                modelo.addAttribute("permiso_modelo", PermisoSession.SESION_ADMINISTRADOR);
            } else {
                modelo.addAttribute("permiso_modelo", PermisoSession.SIN_SESION);
            }
            return "clientes/carga_cliente.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/editar1")
    public String editarCliente1C(Model modelo, @RequestParam Integer idLink, HttpSession session) throws Exception {
        try {
            Cliente clienteEditar = clienteService.buscarXId(idLink);
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR) 
                    && !clienteEditar.getId().equals(clienteSession.getId())) {
                throw new ErrorSeguridad("No puedes modificar datos de otro usuario.");
            }
            modelo.addAttribute("cliente", clienteEditar);
            return "clientes/edicion_cliente.html";
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
    @PostMapping("/editar2")
    public String editarCliente2C(Model modelo, @RequestParam Integer idForm, String nombreForm, String apellidosForm,
            String dniForm, String telefonoForm, String emailForm, Sexo sexoForm, HttpSession session, 
            RedirectAttributes redir) throws Exception {
        try {
            Cliente cliente = clienteService.editarClienteS(idForm, nombreForm, apellidosForm, dniForm, telefonoForm, emailForm, 
                    sexoForm);
            redir.addAttribute("idLink", cliente.getId());
            redir.addFlashAttribute("exito_edicion", true);
            return "redirect:/clientes/datos_cliente";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("cliente", clienteService.buscarXId(idForm));
            return "clientes/edicion_cliente.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/cambiar_password1")
    public String cambiarPassword1C(Model modelo, @RequestParam Integer idLink, HttpSession session) throws Exception {
        try {
            Cliente clienteCambiarPassword = clienteService.buscarXId(idLink);
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (!clienteCambiarPassword.getId().equals(clienteSession.getId())) {
                throw new ErrorSeguridad("No puedes modificar la contraseña de otro usuario.");
            }
            modelo.addAttribute("cliente", clienteCambiarPassword);
            return "clientes/cambio_password.html";
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
    @PostMapping("/cambiar_password2")
    public String cambiarPassword2C(Model modelo, @RequestParam Integer idForm, String claveActForm,
            String clave1Form, String clave2Form, HttpSession session, RedirectAttributes redir) throws Exception {
        try {
            Cliente cliente = clienteService.cambiarPasswordS(idForm, claveActForm, clave1Form, clave2Form);
            redir.addAttribute("idLink", cliente.getId());
            redir.addFlashAttribute("exito_password", true);
            return "redirect:/clientes/datos_cliente";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            modelo.addAttribute("cliente", clienteService.buscarXId(idForm));
            return "clientes/cambio_password.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/listar_todos")
    public String listarTodosClientesAlfabeticamenteC(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            List<Cliente> listClientes = clienteService.listarTodosClientesAlfabeticamenteS();
            modelo.addAttribute("clientes", listClientes);
            modelo.addAttribute("hay_bajas", clienteService.hayAlgunaBaja(listClientes));
            return "clientes/lista_clientes.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/busqueda_x_id1")
    public String buscarXIdStr1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            return "clientes/busq_clientes_id.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/busqueda_x_id2")
    public String buscarXIdStr2C(Model modelo, HttpSession session, @RequestParam String idStrIng, RedirectAttributes redir) 
            throws Exception {
        try {
            Cliente cliente = clienteService.buscarClienteXIdS(idStrIng);
            redir.addAttribute("idLink", cliente.getId());
            return "redirect:/clientes/datos_cliente";
        } catch (ErrorServicio e) {
            modelo.addAttribute("error", e.getMessage());
            return "clientes/busq_clientes_id.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/busqueda_x_nombre1")
    public String buscarXCadenaNombre1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            return "clientes/busq_clientes_nombre.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/busqueda_x_nombre2")
    public String buscarXCadenaNombre2C(Model modelo, HttpSession session, String cadena, RedirectAttributes redir) 
            throws Exception {
        try {
            List<Cliente> listClientes = clienteService.listarClientesXCadenaNombreS(cadena);
            modelo.addAttribute("clientes", listClientes);
            modelo.addAttribute("hay_bajas", clienteService.hayAlgunaBaja(listClientes));
            modelo.addAttribute("hay_administradores", clienteService.hayAlgunAdministrador(listClientes));
            return "clientes/busq_clientes_nombre.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "clientes/busq_clientes_nombre.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/busqueda_x_dni1")
    public String buscarXCadenaDni1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            return "clientes/busq_clientes_dni.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/busqueda_x_dni2")
    public String buscarXCadenaDni2C(Model modelo, HttpSession session, String cadena) throws Exception {
        try {
            List<Cliente> listClientes = clienteService.listarClientesXCadenaDniS(cadena);
            modelo.addAttribute("clientes", listClientes);
            modelo.addAttribute("hay_bajas", clienteService.hayAlgunaBaja(listClientes));
            modelo.addAttribute("hay_administradores", clienteService.hayAlgunAdministrador(listClientes));
            return "clientes/busq_clientes_dni.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "clientes/busq_clientes_dni.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/busqueda_x_telefono1")
    public String buscarXCadenaTelefono1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            return "clientes/busq_clientes_telefono.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/busqueda_x_telefono2")
    public String buscarXCadenaTelefono2C(Model modelo, HttpSession session, String cadena) throws Exception {
        try {
            List<Cliente> listClientes = clienteService.listarClientesXCadenaTelefonoS(cadena);
            modelo.addAttribute("clientes", listClientes);
            modelo.addAttribute("hay_bajas", clienteService.hayAlgunaBaja(listClientes));
            modelo.addAttribute("hay_administradores", clienteService.hayAlgunAdministrador(listClientes));
            return "clientes/busq_clientes_telefono.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "clientes/busq_clientes_telefono.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/busqueda_x_email1")
    public String buscarXCadenaEmail1C(Model modelo, HttpSession session) throws Exception {
        try {
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                throw new ErrorSeguridad("Se necesitan permisos de administrador para esta operación.");
            }
            return "clientes/busq_clientes_email.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/busqueda_x_email2")
    public String buscarXCadenaEmail2C(Model modelo, HttpSession session, String cadena) throws Exception {
        try {
            List<Cliente> listClientes = clienteService.listarClientesXCadenaEmailS(cadena);
            modelo.addAttribute("clientes", listClientes);
            modelo.addAttribute("hay_bajas", clienteService.hayAlgunaBaja(listClientes));
            modelo.addAttribute("hay_administradores", clienteService.hayAlgunAdministrador(listClientes));
            return "clientes/busq_clientes_email.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "clientes/busq_clientes_email.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/datos_cliente")
    public String buscarXIdLinkC(Model modelo, @RequestParam Integer idLink, HttpSession session) throws Exception {
        try {
            Cliente clienteDatos = clienteService.buscarXId(idLink);
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (!session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR) 
                    && !clienteDatos.getId().equals(clienteSession.getId())) {
                throw new ErrorSeguridad("No tienes acceso para ver datos de otro usuario.");
            }
            modelo.addAttribute("cliente", clienteDatos);
            modelo.addAttribute("con_prestamos", prestamoService.clienteConAlgunPrestamoActivoS(idLink));
            return "clientes/cliente.html";
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/otorgar_permisos")
    public String otorgarPermisosC(Model modelo, @RequestParam Integer idClienteForm, HttpSession session, 
            RedirectAttributes redir) throws Exception {
        try {
            Cliente cliente = clienteService.buscarXId(idClienteForm);
            clienteService.otorgarPermisosS(cliente);
            redir.addAttribute("idLink", cliente.getId());
            redir.addFlashAttribute("exito_permisos_si", true);
            return "redirect:/clientes/datos_cliente";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @PostMapping("/retirar_permisos")
    public String retirarPermisosC(Model modelo, @RequestParam Integer idClienteForm, HttpSession session, 
            RedirectAttributes redir) throws Exception {
        try {
            Cliente cliente = clienteService.buscarXId(idClienteForm);
            clienteService.retirarPermisosS(cliente);
            redir.addAttribute("idLink", cliente.getId());
            redir.addFlashAttribute("exito_permisos_no", true);
            return "redirect:/clientes/datos_cliente";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }
    
    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @PostMapping("/baja1")
    public String bajaClienteC(Model modelo, @RequestParam Integer idClienteForm, HttpSession session, RedirectAttributes redir) 
            throws Exception {
        try {
            Cliente cliente = clienteService.buscarXId(idClienteForm);
            if (cliente.getCategoria().equals(Categoria.ADMINISTRADOR)) {
                throw new ErrorSeguridad("El cliente " + cliente.getNombre() + " " + cliente.getApellidos() + 
                        " no se puede dar de baja porque es administrador.");
            }
            clienteService.bajaClienteS(cliente);
            if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                redir.addFlashAttribute("exito_borrado", true);
                return "redirect:/clientes/listar_todos";
            } else if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_USUARIO)) {
                redir.addAttribute("idLink", cliente.getId());
                return "redirect:/clientes/baja2";
            } else {
                throw new ErrorSeguridad("No se han establecido permisos para el usuario.");
            }
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/baja2")
    public String bajaCliente2C(Model modelo, @RequestParam Integer idLink, HttpSession session) throws Exception {
        try {
            Cliente cliente = clienteService.buscarXId(idLink);
            if (cliente.getCategoria().equals(Categoria.ADMINISTRADOR)) {
                throw new ErrorSeguridad("El cliente indicado no se puede dar de baja porque es administrador.");
            }
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (!cliente.getId().equals(clienteSession.getId())) {
                throw new ErrorSeguridad("No puedes dar de baja a otro usuario.");
            }
            modelo.addAttribute("situacion", "salida");
            return "index/usuario_de_baja.html";            
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
    @PostMapping("/reactivar")
    public String reactivarClienteC(Model modelo, @RequestParam Integer idClienteForm, HttpSession session, 
            RedirectAttributes redir) throws Exception {
        try {
            Cliente clienteReactivar = clienteService.buscarXId(idClienteForm);
            clienteService.altaClienteS(clienteReactivar);
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            if (clienteReactivar.getCategoria().equals(Categoria.ADMINISTRADOR) 
                    && clienteReactivar.getId().equals(clienteSession.getId())) {
                return "redirect:/inicio";
            }
            if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_ADMINISTRADOR)) {
                redir.addAttribute("idLink", clienteReactivar.getId());
                redir.addFlashAttribute("exito_reactiv", true);
                return "redirect:/clientes/datos_cliente";
            } else if (session.getAttribute("permiso_session").equals(PermisoSession.SESION_USUARIO)) {
                if (!clienteReactivar.getId().equals(clienteSession.getId())) {
                    throw new ErrorSeguridad("No puedes reactivar a otro usuario.");
                } 
                session.setAttribute("usuario_session", clienteReactivar);
                return "redirect:/inicio";
            } else {
                throw new ErrorSeguridad("No se han establecido permisos para el usuario.");
            }
        } catch (ErrorSeguridad e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_seguridad.html";
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

}
