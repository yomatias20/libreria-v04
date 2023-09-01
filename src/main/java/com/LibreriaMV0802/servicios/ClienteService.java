
package com.LibreriaMV0802.servicios;

import com.LibreriaMV0802.entidades.Cliente;
import com.LibreriaMV0802.enumeraciones.Categoria;
import com.LibreriaMV0802.enumeraciones.PermisoSession;
import com.LibreriaMV0802.enumeraciones.Sexo;
import com.LibreriaMV0802.errores.ErrorServicio;
import com.LibreriaMV0802.repositorios.ClienteRepositorio;
import com.LibreriaMV0802.utilidades.ValidacionPassword;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import javax.servlet.http.HttpSession;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

@Service
public class ClienteService implements UserDetailsService {

    @Autowired
    private ClienteRepositorio clienteRepositorio;
    
    @Autowired
    private ValidacionPassword validacionPassword;

    public Cliente buscarClienteXIdS(String idStrIng) throws Exception {
        Integer idIntIng;
        try {
            idIntIng = Integer.parseInt(idStrIng);
            if (idIntIng <= 0) {
                throw new ErrorServicio("El número de socio debe ser positivo.");
            }
            Optional<Cliente> clienteOptional = clienteRepositorio.findById(idIntIng);
            if (clienteOptional.isPresent()) {
                return clienteOptional.get();
            } else {
                throw new ErrorServicio("El número de socio ingresado no está en la lista.");
            }
        } catch (NumberFormatException e) {
            throw new ErrorServicio("Debes ingresar un número.");
        } catch (ErrorServicio e) {
            throw e;
        }
    }

    @Transactional
    public Cliente cargarClienteS(String nombreForm, String apellidosForm, String dniForm, String telefonoForm,
            String emailForm, String clave1Form, String clave2Form, Sexo sexoForm) throws Exception {
        try {
            validarDatos(nombreForm, apellidosForm, dniForm, telefonoForm, emailForm);
            validacionPassword.validarPassword(clave1Form, clave2Form);
            Cliente cliente = new Cliente();
            cliente.setNombre(nombreForm);
            cliente.setApellidos(apellidosForm);
            cliente.setDni(dniForm);
            cliente.setTelefono(telefonoForm);
            cliente.setEmail(emailForm);
            String encriptada = new BCryptPasswordEncoder().encode(clave1Form);
            cliente.setClave(encriptada);
            cliente.setSexo(sexoForm);
            if (listarTodosClientesAlfabeticamenteS().isEmpty()) {
                cliente.setCategoria(Categoria.ADMINISTRADOR);
            } else {
                cliente.setCategoria(Categoria.USUARIO);
            }
            cliente.setAlta(true);
            return clienteRepositorio.save(cliente);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public Cliente editarClienteS(Integer idForm, String nombreForm, String apellidosForm, String dniForm,
            String telefonoForm, String emailForm, Sexo sexoForm) throws Exception {
        try {
            validarDatos(nombreForm, apellidosForm, dniForm, telefonoForm, emailForm);
            Cliente cliente = buscarXId(idForm);
            cliente.setNombre(nombreForm);
            cliente.setApellidos(apellidosForm);
            cliente.setDni(dniForm);
            cliente.setTelefono(telefonoForm);
            cliente.setEmail(emailForm);
            cliente.setSexo(sexoForm);
            return clienteRepositorio.save(cliente);
        } catch (Exception e) {
            throw e;
        }
    }

    @Transactional
    public Cliente cambiarPasswordS(Integer idForm, String claveActForm, String clave1Form, String clave2Form) throws Exception {
        try {
            Cliente cliente = clienteRepositorio.findById(idForm).get();
            BCryptPasswordEncoder deco = new BCryptPasswordEncoder();
            if (!deco.matches(claveActForm, cliente.getClave())) {
                throw new ErrorServicio("La clave actual no es correcta.");
            }
            if (clave1Form.equals(claveActForm)) {
                throw new ErrorServicio("La nueva clave no puede ser igual a la anterior.");
            }
            validacionPassword.validarPassword(clave1Form, clave2Form);
            String encriptada = new BCryptPasswordEncoder().encode(clave1Form);
            cliente.setClave(encriptada);     
            return clienteRepositorio.save(cliente);
        } catch (Exception e) {
            throw e;
        }
    }

    public void validarDatos(String nombreForm, String apellidosForm, String dniForm, String telefonoForm,
            String emailForm) throws Exception {
        int contadorArrobas = 0;
        try {
            if (nombreForm.trim().isEmpty()) {
                throw new ErrorServicio("El nombre debe tener al menos un carácter no vacío.");
            }
            if (apellidosForm.trim().isEmpty()) {
                throw new ErrorServicio("El apellido debe tener al menos un carácter no vacío.");
            }
            if (dniForm.trim().isEmpty()) {
                throw new ErrorServicio("El DNI debe tener al menos un carácter no vacío.");
            }
            if (telefonoForm.trim().isEmpty()) {
                throw new ErrorServicio("El teléfono debe tener al menos un carácter no vacío.");
            }
            if (emailForm.trim().isEmpty()) {
                throw new ErrorServicio("El e-mail debe tener al menos un carácter no vacío.");
            }
            for (int i = 0; i < emailForm.length(); i++) {
                if (emailForm.substring(i, i + 1).equals("@")) {
                    contadorArrobas = contadorArrobas + 1;
                }
            }
            if (contadorArrobas == 0) {
                throw new ErrorServicio("El e-mail debe tener un símbolo arroba (@).");
            }
            if (contadorArrobas > 1) {
                throw new ErrorServicio("El e-mail no puede tener más de un símbolo arroba (@).");
            }
        } catch (Exception e) {
            throw e;
        }    
    }
    
//    public void validarPassword(String clave1Form, String clave2Form) throws Exception {
//        boolean claveConEspacios = false;
//        try {
//            if (clave1Form.length() < 6) {
//                throw new ErrorServicio("La clave debe tener al menos 6 caracteres.");
//            }
//            for (int i = 0; i < clave1Form.length(); i++) {
//                if (clave1Form.substring(i, i + 1).equals(" ")) {
//                    claveConEspacios = true;
//                }
//            }
//            if (claveConEspacios == true) {
//                throw new ErrorServicio("La clave no puede tener espacios vacíos.");
//            }
//            if (!clave1Form.equals(clave2Form)) {
//                throw new ErrorServicio("Las dos claves ingresadas deben ser iguales.");
//            }
//        } catch (Exception e) {
//            throw e;
//        }
//    }
    
    public List<Cliente> listarClientesXCadenaNombreS(String cadena) throws Exception {
        try {
            if (cadena.trim().isEmpty()) {
                throw new ErrorServicio("El nombre a buscar debe tener al menos un carácter no vacío.");
            }
            List<Cliente> listClientes = clienteRepositorio.listarClientesXCadenaNombreR("%" + cadena + "%");
            if (listClientes.isEmpty()) {
                throw new ErrorServicio("No se encontraron clientes con esos datos.");
            }
            return listClientes;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Cliente> listarClientesXCadenaDniS(String cadena) throws Exception {
        try {
            if (cadena.trim().isEmpty()) {
                throw new ErrorServicio("El DNI a buscar debe tener al menos un carácter no vacío.");
            }
            List<Cliente> listClientes = clienteRepositorio.listarClientesXCadenaDniR("%" + cadena + "%");
            if (listClientes.isEmpty()) {
                throw new ErrorServicio("No se encontraron clientes con esos datos.");
            }
            return listClientes;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Cliente> listarClientesXCadenaTelefonoS(String cadena) throws Exception {
        try {
            if (cadena.trim().isEmpty()) {
                throw new ErrorServicio("El teléfono a buscar debe tener al menos un carácter no vacío.");
            }
            List<Cliente> listClientes = clienteRepositorio.listarClientesXCadenaTelefonoR("%" + cadena + "%");
            if (listClientes.isEmpty()) {
                throw new ErrorServicio("No se encontraron clientes con esos datos.");
            }
            return listClientes;
        } catch (Exception e) {
            throw e;
        }
    }

    public List<Cliente> listarClientesXCadenaEmailS(String cadena) throws Exception {
        try {
            if (cadena.trim().isEmpty()) {
                throw new ErrorServicio("El e-mail a buscar debe tener al menos un carácter no vacío.");
            }
            List<Cliente> listClientes = clienteRepositorio.listarClientesXCadenaEmailR("%" + cadena + "%");
            if (listClientes.isEmpty()) {
                throw new ErrorServicio("No se encontraron clientes con esos datos.");
            }
            return listClientes;
        } catch (Exception e) {
            throw e;
        }
    }
    
    public boolean hayAlgunaBaja (List<Cliente> listClientes) {
        for (Cliente cliente : listClientes) {
            if (cliente.getAlta() == false) {
                return true;
            }
        }
        return false;
    }

    public boolean hayAlgunAdministrador (List<Cliente> listClientes) {
        for (Cliente cliente : listClientes) {
            if (cliente.getCategoria().equals(Categoria.ADMINISTRADOR)) {
                return true;
            }
        }
        return false;
    }
    
    public Cliente buscarXId(Integer id) throws Exception {
        Optional<Cliente> clienteOptional = clienteRepositorio.findById(id);
        if (!clienteOptional.isPresent()) {
            throw new ErrorServicio("El número ingresado no corresponde a ningún cliente.");
        }
        return clienteOptional.get();
    }
    
    public List<Cliente> listarTodosClientesAlfabeticamenteS() {
        return clienteRepositorio.listarTodosClientesAlfabeticamenteR();
    }
    
    public List<Cliente> listarClientesActivosAlfabeticamenteS() {
        return clienteRepositorio.listarClientesActivosAlfabeticamenteR();
    }
    
    public void altaClienteS(Cliente cliente) {
        cliente.setAlta(true);
        clienteRepositorio.save(cliente);
    }
    
    public void bajaClienteS(Cliente cliente) {
        cliente.setAlta(false);
        clienteRepositorio.save(cliente);
    }
    
    public void otorgarPermisosS(Cliente cliente) {
        cliente.setCategoria(Categoria.ADMINISTRADOR);
        clienteRepositorio.save(cliente);
    }
    
    public void retirarPermisosS(Cliente cliente) {
        cliente.setCategoria(Categoria.USUARIO);
        clienteRepositorio.save(cliente);
    }
    
    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Cliente cliente = clienteRepositorio.buscarClienteXEmailR(email);
        if (cliente != null) {
            List<GrantedAuthority> permisos = new ArrayList<>();
            GrantedAuthority permiso1 = new SimpleGrantedAuthority("ROLE_USUARIO");
            permisos.add(permiso1);
            if (cliente.getCategoria().equals(Categoria.ADMINISTRADOR)) {
                GrantedAuthority permiso2 = new SimpleGrantedAuthority("ROLE_ADMIN");
                permisos.add(permiso2);
            }
            ServletRequestAttributes attr = (ServletRequestAttributes) RequestContextHolder.currentRequestAttributes();
            HttpSession session = attr.getRequest().getSession(true);
            session.setMaxInactiveInterval(20 * 60);
            session.setAttribute("usuario_session", cliente);
            if (cliente.getCategoria().equals(Categoria.ADMINISTRADOR)) {
                session.setAttribute("permiso", PermisoSession.SESION_ADMINISTRADOR);
            } else {
                session.setAttribute("permiso", PermisoSession.SESION_USUARIO);
            }
            User user = new User(cliente.getEmail(), cliente.getClave(), permisos);
            return user;
        } else {
            return null;
        }
    }

}
