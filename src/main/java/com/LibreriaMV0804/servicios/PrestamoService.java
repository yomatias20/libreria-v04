
package com.LibreriaMV0802.servicios;

import com.LibreriaMV0802.entidades.Libro;
import com.LibreriaMV0802.entidades.Prestamo;
import com.LibreriaMV0802.errores.ErrorServicio;
import com.LibreriaMV0802.repositorios.PrestamoRepositorio;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import javax.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PrestamoService {
    
    @Autowired
    private PrestamoRepositorio prestamoRepositorio;
    
    @Autowired
    private LibroService libroService;
    
    @Autowired
    private ClienteService clienteService;
    
    @Transactional
    public void cargarPrestamoS(Long isbnForm, Integer idClienteForm, String fechaPrestStrForm) throws Exception {
        Date hoy = new Date();
        Date fechaPrestDateForm;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        try {
            if (fechaPrestStrForm == null) {
                fechaPrestDateForm = hoy;
            } else {
                fechaPrestDateForm = formato.parse(fechaPrestStrForm);
            }
            if (fechaPrestDateForm.after(hoy)) {
                throw new ErrorServicio("La fecha del préstamo no puede ser posterior a la actual.");
            }
            Prestamo prestamo = new Prestamo();
            Libro libro = libroService.buscarXIsbn(isbnForm);
            prestamo.setLibro(libro);
            prestamo.setCliente(clienteService.buscarXId(idClienteForm));
            prestamo.setFechaPrest(fechaPrestDateForm);
            prestamoRepositorio.save(prestamo);
            libro.ejemplarPrestado();
            libroService.guardar(libro);
        } catch (ParseException e) {
            throw new ErrorServicio("Debes ingresar una fecha válida.");
        } catch (ErrorServicio e) {
            throw e;
        }
    }
    
    public void devolucionS(Prestamo prestamo, String fechaDevolStrForm) throws Exception {
        Date hoy = new Date();
        Date fechaDevolDateForm;
        SimpleDateFormat formato = new SimpleDateFormat("yyyy-MM-dd");
        Libro libro = prestamo.getLibro();
        try {
            if (fechaDevolStrForm == null) {
                fechaDevolDateForm = hoy;
            } else {
                fechaDevolDateForm = formato.parse(fechaDevolStrForm);
            }
            if (fechaDevolDateForm.after(hoy)) {
                throw new ErrorServicio("La fecha de devolución no puede ser posterior a la actual.");
            }
            if (fechaDevolDateForm.before(prestamo.getFechaPrest())) {
                throw new ErrorServicio("La fecha de devolución no puede ser anterior a la del préstamo.");
            }
            prestamo.setFechaDevol(fechaDevolDateForm);
            prestamoRepositorio.save(prestamo);
            libro.ejemplarDevuelto();
            libroService.guardar(libro);
        } catch (ParseException e) {
            throw new ErrorServicio("Debes ingresar una fecha válida.");
        } catch (ErrorServicio e) {
            throw e;
        }
    }
    
    public Prestamo buscarXId(Integer id) throws Exception {
        Optional<Prestamo> prestamoOptional = prestamoRepositorio.findById(id);
        if (!prestamoOptional.isPresent()) {
            throw new ErrorServicio("El número ingresado no corresponde a ningún préstamo.");
        }
        return prestamoOptional.get();
    }
    
    public boolean clienteConAlgunPrestamoActivoS(Integer idCliente) {
        return prestamoRepositorio.clienteConAlgunPrestamoActivoR(idCliente);
    }
    
    public boolean libroAlgunaVezPrestadoS(Long isbn) {
        return prestamoRepositorio.libroAlgunaVezPrestadoR(isbn);
    }
    
    public List<Prestamo> listarPrestamosActivosXClienteS( Integer idCliente) {
        return prestamoRepositorio.listarPrestamosActivosXClienteR(idCliente);
    }
    
    public List<Prestamo> listarPrestamosCanceladosXClienteS(Integer idCliente) {
        return prestamoRepositorio.listarPrestamosCanceladosXClienteR(idCliente);
    }
    
    public List<Prestamo> listarPrestamosActivosXLibroS(Long isbn) {
        return prestamoRepositorio.listarPrestamosActivosXLibroR(isbn);
    }
    
    public List<Prestamo> listarPrestamosCanceladosXLibroS(Long isbn) {
        return prestamoRepositorio.listarPrestamosCanceladosXLibroR(isbn);
    }
    
}
