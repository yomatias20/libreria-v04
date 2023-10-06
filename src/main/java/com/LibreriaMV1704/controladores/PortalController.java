
package com.LibreriaMV1704.controladores;

import com.LibreriaMV1704.entidades.Cliente;
import com.LibreriaMV1704.enumeraciones.PermisoSession;
import com.LibreriaMV1704.utilidades.CargaBDOriginal;
import java.io.IOException;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/")
public class PortalController {

    @Autowired
    private CargaBDOriginal cargaBDOriginal;
    
    @GetMapping
    public String index() throws IOException {
        cargaBDOriginal.cargarFoto(5L, "src/main/resources/static/Imágenes/Autores/Michael Connelly.jpg");
        return "index/index.html";
    }

    @GetMapping("/registro")
    public String registro(Model modelo) {
        // Se invoca la página de carga de cliente nuevo sin sesión iniciada.
        modelo.addAttribute("permiso_modelo", PermisoSession.SIN_SESION);
        return "clientes/carga_cliente.html";
    }

    @GetMapping("/login")
    public String login(Model modelo, @RequestParam(required = false) String error,
            @RequestParam(required = false) String logout) {
        if (error != null) {
            modelo.addAttribute("error", "El nombre de usuario o la clave son incorrectos.");
        }
        if (logout != null) {
            modelo.addAttribute("logout", "Te has desconectado correctamente.");
        }
        return "index/login.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/inicio")
    public String administradorOUsuario(Model modelo, HttpSession session) throws Exception {
        try {
            Cliente clienteSession = (Cliente) session.getAttribute("usuario_session");
            switch (clienteSession.getCategoria().name()) {
                case "USUARIO":
                    session.setAttribute("permiso_session", PermisoSession.SESION_USUARIO);
                    break;
                case "ADMINISTRADOR":
                    return "index/adm_o_usuario.html";
            }
            if (clienteSession.getAlta() == false) {
                modelo.addAttribute("situacion", "entrada");
                return "index/usuario_de_baja.html";
            } else {
                return "index/usuario.html";
            }
        } catch (Exception e) {
            modelo.addAttribute("error", e.getMessage());
            return "error/error_desconocido.html";
        }
    }

    @PreAuthorize("hasAnyRole('ROLE_ADMIN')")
    @GetMapping("/administrador")
    public String administrador(HttpSession session) {
        session.setAttribute("permiso_session", PermisoSession.SESION_ADMINISTRADOR);
        return "index/administrador.html";
    }

    @PreAuthorize("hasAnyRole('ROLE_USUARIO')")
    @GetMapping("/usuario")
    public String usuario(HttpSession session) {
        session.setAttribute("permiso_session", PermisoSession.SESION_USUARIO);
        return "index/usuario.html";
    }

}
