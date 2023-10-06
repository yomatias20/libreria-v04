
package com.LibreriaMV1704.utilidades;

import com.LibreriaMV1704.errores.ErrorServicio;
import org.springframework.stereotype.Service;

@Service
public class ValidacionPassword {

    public void validarPassword(String clave1Form, String clave2Form) throws Exception {
        boolean claveConEspacios = false;
        try {
            if (clave1Form.length() < 6) {
                throw new ErrorServicio("La clave debe tener al menos 6 caracteres.");
            }
            for (int i = 0; i < clave1Form.length(); i++) {
                if (clave1Form.substring(i, i + 1).equals(" ")) {
                    claveConEspacios = true;
                }
            }
            if (claveConEspacios == true) {
                throw new ErrorServicio("La clave no puede tener espacios vacíos.");
            }
            if (!clave1Form.equals(clave2Form)) {
                throw new ErrorServicio("Las dos claves ingresadas deben ser iguales.");
            }
        } catch (Exception e) {
            throw e;
        }
    }    
    
}
