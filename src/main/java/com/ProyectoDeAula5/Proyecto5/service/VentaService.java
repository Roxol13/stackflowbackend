package com.ProyectoDeAula5.Proyecto5.service;

import com.ProyectoDeAula5.Proyecto5.model.Venta;
import com.ProyectoDeAula5.Proyecto5.model.Cliente;
import com.ProyectoDeAula5.Proyecto5.model.DetalleVenta;
import com.ProyectoDeAula5.Proyecto5.repository.VentaRepository;
import com.ProyectoDeAula5.Proyecto5.repository.ClienteRepository;
import com.ProyectoDeAula5.Proyecto5.repository.DetalleVentaRepository;

import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class VentaService {

    @Autowired
    private ClienteRepository clienteRepository;

    @Autowired
    private ClienteService clienteService; // ✅ Añadido para actualizar satisfacción

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private ProductoService productoService;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Transactional
    public Venta guardarVenta(Venta venta) {
        log.info("Generando venta " + venta.getDetallesVenta());

        // Guardar la venta primero (sin detalles aún)
        venta.setDetallesVenta(new ArrayList<>()); // Inicializa la lista vacía
        Venta ventaGuardada = ventaRepository.save(venta); // Se genera el ID aquí

        List<DetalleVenta> detalles = new ArrayList<>();

        if (venta.getDetallesVenta() != null) {
            for (DetalleVenta detalle : venta.getDetallesVenta()) {
                log.info("Guardando detalles de la venta " + detalle);

                // 🔄 Actualizar inventario
                productoService.actualizarInventario(detalle.getCod_pro(), detalle.getCantidad());

                // ⚠️ Establecer relación con la venta
                detalle.setVenta(ventaGuardada);

                // Guardar detalle con referencia válida
                DetalleVenta detalleGuardado = detalleVentaRepository.save(detalle);
                detalles.add(detalleGuardado);
            }
        }

        // Asignar los detalles guardados a la venta y actualizar
        ventaGuardada.setDetallesVenta(detalles);
        ventaGuardada = ventaRepository.save(ventaGuardada); // Update para incluir detalles

        // 🔄 Actualizar satisfacción del cliente
        Optional<Cliente> optionalCliente = clienteRepository.findByNombre(venta.getNomcliente());
        if (optionalCliente.isPresent() && !detalles.isEmpty()) {
            Cliente cliente = optionalCliente.get();
            Double satisfactionScore = detalles.get(0).getSatisfactionScore();
            if (satisfactionScore != null) {
                clienteService.actualizarSatisfaccion(cliente.getId(), satisfactionScore);
            }
        }
        
        return ventaGuardada;
    }

    public DetalleVenta guardarDetalleVenta(DetalleVenta detalleVenta) {
        return detalleVentaRepository.save(detalleVenta);
    }

    public List<Venta> obtenerVentas() {
        return ventaRepository.findAll();
    }
}
