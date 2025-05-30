package com.ieva.ieva.controller;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Optional;

import org.hibernate.Hibernate;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ieva.ieva.exceptions.ResourceNotFoundException;
import com.ieva.ieva.model.entity.Egresado;
import com.ieva.ieva.model.entity.Hablanos;
import com.ieva.ieva.service.IevaServiceIface;
import com.ieva.ieva.utils.paginator.PageRender;

import jakarta.validation.Valid;

@Controller
@RequestMapping("/ieva")
@SessionAttributes("egresado")
public class EgresadoController {

    @Value("${uploads.img}")
    private String uploadsExtDir;

    private final IevaServiceIface ievaService;

    public EgresadoController(IevaServiceIface ievaService) {
        this.ievaService = ievaService;
    }

     @GetMapping("/egresadolistar")
    public String listarEgresados(
            @RequestParam(defaultValue = "0") int pag,
            @RequestParam(required = false) Integer anioGraduacion,
            @RequestParam(required = false) String grupoPertenecio,
            Model model) {
        
        Pageable pagina = PageRequest.of(pag, 5);
        Page<Egresado> egresados;
        
        if (anioGraduacion != null || grupoPertenecio != null) {
            // Usar filtro por año y grupo
            egresados = ievaService.filtrarPorAnioYGrupo(anioGraduacion, grupoPertenecio, pagina);
            model.addAttribute("titulo", "Egresados filtrados");
        } else {
            // Listado normal
            egresados = ievaService.buscarEgresadosTodos(pagina);
            model.addAttribute("titulo", "Listado de Egresados");
        }
        
        if (egresados != null && !egresados.isEmpty()) {
            PageRender<Egresado> pageRender = new PageRender<>("/ieva/egresadolistar", egresados);
            model.addAttribute("pageRender", pageRender);
        }
        model.addAttribute("gruposDisponibles", Arrays.asList("11A", "11B", "11C", "11D"));
        model.addAttribute("egresados", egresados);
        return "egresado/listado_egresados";
    }
    
    @GetMapping("/egresadolistar/filtrar")
    public String filtrarEgresados(
            @RequestParam Integer anioGraduacion,
            @RequestParam String grupoPertenecio,
            @RequestParam(defaultValue = "0") int page,
            Model model) {
        
        Pageable pageable = PageRequest.of(page, 5);
        Page<Egresado> egresados = ievaService.filtrarPorAnioYGrupo(anioGraduacion, grupoPertenecio, pageable);
        
        PageRender<Egresado> pageRender = new PageRender<>(
            "/ieva/egresadolistar/filtrar?anioGraduacion=" + anioGraduacion + 
            "&grupoPertenecio=" + grupoPertenecio, egresados);
        
        model.addAttribute("titulo", "Egresados filtrados por año " + anioGraduacion + " y grupo " + grupoPertenecio);
        model.addAttribute("egresados", egresados);
        model.addAttribute("pageRender", pageRender);
         
        return "egresado/listado_egresados";
    }


    @GetMapping("/egresadonuevo")
    public String nuevoEgresado(Model model) {
        model.addAttribute("titulo", "Nuevo Egresado");
        model.addAttribute("accion", "Crear");
        model.addAttribute("egresado", new Egresado());
        return "egresado/formulario_egresado";
    }

    @PostMapping("/egresadoguardar")
    public String guardarEgresado(@Valid @ModelAttribute Egresado egresado,
            BindingResult result,
            Model model,
            SessionStatus status,
            RedirectAttributes flash,
            @RequestParam("file") MultipartFile imagen) {

        boolean esNuevo = (egresado.getId() == null);

        if (result.hasErrors()) {
            model.addAttribute("titulo", esNuevo ? "Nuevo egresado" : "Modificar egresado");
            model.addAttribute("accion", esNuevo ? "Crear" : "Modificar");
            model.addAttribute("info", "Complemente o corrija la información de los campos del formulario");
            return "egresado/formulario_egresado";
        }

        if (!imagen.isEmpty()) {
            String nombreImagen = guardarArchivo(imagen);
            if (nombreImagen != null) {
                egresado.setImagen(nombreImagen);
                flash.addFlashAttribute("info", "Imagen cargada correctamente: " + nombreImagen);
            }
        } else {
            egresado.setImagen("");
        }

        ievaService.guardarEgresado(egresado);
        status.setComplete();
        flash.addFlashAttribute("success", "El egresado fue " + (esNuevo ? "creado" : "modificado") + " con éxito");
        return "redirect:/ieva/egresadolistar";
    }

    private String guardarArchivo(MultipartFile archivo) {
        String nombreArchivo = archivo.getOriginalFilename();
        if (nombreArchivo == null) return null;

        try {
            if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
                Path rutaExistente = Paths.get(uploadsExtDir).resolve(nombreArchivo).toAbsolutePath();
                Files.deleteIfExists(rutaExistente);
            }

            Path rutaNueva = Paths.get(uploadsExtDir).resolve(nombreArchivo).toAbsolutePath();
            Files.copy(archivo.getInputStream(), rutaNueva);
            return nombreArchivo;

        } catch (IOException e) {
            System.err.println("Error al guardar archivo: " + e.getMessage());
            return null;
        }
    }

    @GetMapping("/hablanos/eliminar/{id}")
public String eliminarComentario(@PathVariable Long id, 
                               @RequestParam Long egresadoId,
                               RedirectAttributes flash) {
    try {
        ievaService.eliminarHablanos(id);
        flash.addFlashAttribute("success", "Mensaje eliminado correctamente");
    } catch (Exception e) {
        flash.addFlashAttribute("error", "Error al eliminar mensaje: " + e.getMessage());
    }
    return "redirect:/ieva/egresadoconsultar/" + egresadoId;
}

    @GetMapping("/egresadover/{id}")
    public String verEgresado(@PathVariable Long id, Model model) {
        Egresado egresado = ievaService.buscarEgresadoPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Egresado no encontrado"));
        
        Hibernate.initialize(egresado.getDocumentos());
        Hibernate.initialize(egresado.getHablanos());
        
        model.addAttribute("egresado", egresado);
        model.addAttribute("titulo", "Información del Egresado");
        return "egresado/consulta_egresado";
    }

    @PostMapping("/{id}/hablanos")
public String agregarComentario(@PathVariable Long id,
                              @RequestParam String descripcion,
                              RedirectAttributes flash) {
    try {
        Egresado egresado = ievaService.buscarEgresadoPorId(id)
            .orElseThrow(() -> new ResourceNotFoundException("Egresado no encontrado"));
        
        Hablanos comentario = new Hablanos();
        comentario.setDescripcion(descripcion);
        comentario.setEgresado(egresado);
        
        ievaService.guardarHablanos(comentario);
        flash.addFlashAttribute("success", "Comentario agregado correctamente");
        
    } catch (Exception e) {
        flash.addFlashAttribute("error", "Error al agregar comentario: " + e.getMessage());
    }
    
    return "redirect:/ieva/egresadoconsultar/" + id;
}

    @GetMapping("/egresadoconsultar/{id}")
    public String consultarEgresado(@PathVariable Long id, RedirectAttributes flash, Model model) {
        Optional<Egresado> egresadoOpt = ievaService.buscarEgresadoPorId(id);
        if (!egresadoOpt.isPresent()) {
            flash.addFlashAttribute("error", "El egresado no existe en la base de datos");
            return "redirect:/ieva/egresadolistar";
        }
        
        Egresado egresado = egresadoOpt.get();
        model.addAttribute("titulo", "Consultar Egresado");
        model.addAttribute("accion", "Modificar");
        model.addAttribute("egresado", egresado);
        return "egresado/consulta_egresado";
    }

    @GetMapping("/egresadolistar/buscar")
    public String buscarEgresadosDesdeVista(@RequestParam String filtro, 
                                          @RequestParam(defaultValue = "0") int page, 
                                          Model model) {
        Pageable pageable = PageRequest.of(page, 10);
        Page<Egresado> egresados = ievaService.buscarEgresadosConFiltro(filtro, pageable);

        PageRender<Egresado> pageRender = new PageRender<>("/ieva/egresadolistar/buscar?filtro=" + filtro, egresados);

        model.addAttribute("titulo", "Resultado de la búsqueda: " + filtro);
        model.addAttribute("egresados", egresados.getContent());
        model.addAttribute("pageRender", pageRender);

        return "egresado/listado_egresados"; 
    }

    @GetMapping("/egresadoeliminar/{id}")
    public String eliminarEgresado(@PathVariable Long id, RedirectAttributes flash) {
        Optional<Egresado> egresadoOpt = ievaService.buscarEgresadoPorId(id);
        if (egresadoOpt.isPresent()) {
            ievaService.eliminarEgresadoPorId(id);
            eliminarArchivo(egresadoOpt.get().getImagen());
            flash.addFlashAttribute("success", "Egresado eliminado con éxito");
        } else {
            flash.addFlashAttribute("error", "El egresado no existe en la base de datos");
        }
        return "redirect:/ieva/egresadolistar";
    }

    private void eliminarArchivo(String nombreArchivo) {
        if (nombreArchivo != null && !nombreArchivo.isEmpty()) {
            try {
                Path ruta = Paths.get(uploadsExtDir).resolve(nombreArchivo).toAbsolutePath();
                Files.deleteIfExists(ruta);
            } catch (IOException e) {
                System.err.println("No se pudo eliminar el archivo: " + nombreArchivo);
            }
        }
    }

    @GetMapping("/egresadomodificar/{id}")
    public String modificarEgresado(@PathVariable Long id, RedirectAttributes flash, Model model) {
        Optional<Egresado> egresadoOpt = ievaService.buscarEgresadoPorId(id);
        if (!egresadoOpt.isPresent()) {
            flash.addFlashAttribute("error", "El egresado no existe en la base de datos");
            return "redirect:/ieva/egresadolistar";
        }
        
        model.addAttribute("titulo", "Modificar Egresado");
        model.addAttribute("accion", "Modificar");
        model.addAttribute("egresado", egresadoOpt.get());
        return "egresado/formulario_egresado";
    }
}