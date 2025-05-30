package com.ieva.ieva.controller;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.ieva.ieva.exceptions.ResourceNotFoundException;
import com.ieva.ieva.model.entity.Documento;
import com.ieva.ieva.model.entity.Egresado;
import com.ieva.ieva.service.IevaServiceIface;

import jakarta.validation.Valid;

@Controller
@SessionAttributes("documento")
@RequestMapping("/documento")
public class DocumentoController {

    @Value("${uploads.doc}")
    private String uploadsExtDir;

    private final IevaServiceIface ievaService;

    public DocumentoController(IevaServiceIface ievaService) {
        this.ievaService = ievaService;
    }

    @GetMapping("/documentonuevo/{id}")
public String documentoNuevo(@PathVariable Long id, Model model, RedirectAttributes flash) {
    // Usando Option (mejor práctica)
    Egresado egresado = ievaService.buscarEgresadoPorId(id)
        .orElseThrow(() -> {
            flash.addFlashAttribute("error", "El egresado no existe en la base de datos");
            return new ResourceNotFoundException("Egresado no encontrado con ID: " + id);
        });

    Documento documento = new Documento();
    documento.setEgresado(egresado);

    model.addAttribute("titulo", "Nuevo documento");
    model.addAttribute("btn_accion", "Guardar documento");
    model.addAttribute("documento", documento);
    return "documentos/formulario_documentos";
}

    @PostMapping("/guardardocumento")
    public String guardarDocumento(@Valid @ModelAttribute Documento documento, BindingResult errors,
            SessionStatus status, RedirectAttributes flash, Model model) {

        if (errors.hasErrors()) {
            model.addAttribute("titulo", "Nuevo documento");
            model.addAttribute("btn_accion", "Guardar documento");
            model.addAttribute("warning", "Corrija o complemente la información del formulario");
            return "documentos/formulario_documentos";
        }

        ievaService.guardarDocumento(documento);
        status.setComplete();

        flash.addFlashAttribute("success", "El documento se guardó correctamente");
        return "redirect:/ieva/egresadoconsultar/" + documento.getEgresado().getId();
    }

    @GetMapping("/documentoconsultar/{id}")
    public String documentoConsultar(@PathVariable Long id, RedirectAttributes flash, Model model) {
        Documento documento = ievaService.buscarDocumentoPorId(id);

        if (documento == null) {
            flash.addFlashAttribute("error", "El documento no existe en la base de datos");
            return "redirect:/ieva/egresadolistar";
        }

        model.addAttribute("documento", documento);
        model.addAttribute("titulo", "Documento número " + documento.getId() + " - " + documento.getDescripcion());

        return "documentos/documento_consulta";
    }

    @GetMapping("/documentoeliminar/{id}")
    public String documentoEliminar(@PathVariable Long id, RedirectAttributes flash) {
        Documento documento = ievaService.buscarDocumentoPorId(id);

        if (documento == null) {
            flash.addFlashAttribute("error", "El documento no existe en la base de datos");
            return "redirect:/ieva/egresadolistar";
        }

        ievaService.eliminarDocumentoPorId(id);
        flash.addFlashAttribute("success", "El documento " + id + " fue eliminado");
        return "redirect:/ieva/egresadoconsultar/" + documento.getEgresado().getId();
    }

    @PostMapping("/documentoGuardar")
    public String documentoGuardar(
            @ModelAttribute Documento documento,
            BindingResult result,
            Model model,
            SessionStatus status,
            RedirectAttributes flash,
            @RequestParam("archivo") MultipartFile archivo) {

        boolean esNuevo = (documento.getId() == null);

        if (result.hasErrors()) {
            model.addAttribute("titulo", esNuevo ? "Nuevo documento" : "Modificar documento");
            model.addAttribute("accion", esNuevo ? "Crear" : "Modificar");
            model.addAttribute("info", "Complemente o corrija la información de los campos del formulario");
            return "redirect:/documento/documentonuevo/" + documento.getEgresado().getId();
        }
        try {
            getDatosDocumento(archivo, documento);

            ievaService.guardarDocumento(documento);
            status.setComplete();
            flash.addFlashAttribute("success",
                    "El documento fue " + (esNuevo ? "creado" : "modificado") + " con éxito");
        } catch (Exception e) {
            flash.addFlashAttribute("error", e.getMessage());
        }

        return "redirect:/ieva/egresadoconsultar/" + documento.getEgresado().getId();
    }

    public void getDatosDocumento(MultipartFile archivo, Documento documento) {
        if (archivo == null || archivo.isEmpty()) {
            throw new IllegalArgumentException("El documento no puede estar vacío o nulo.");
        }

        try {
            String nombreArchivo = documento.getEgresado().getId() + "_" + archivo.getOriginalFilename();
            Path rutaNueva = Paths.get(uploadsExtDir).resolve(nombreArchivo).toAbsolutePath();
            if (rutaNueva.toFile().exists()) {
                throw new IllegalArgumentException("El documento " + nombreArchivo + ", ya existe para el egresado.");
            }
            Files.copy(archivo.getInputStream(), rutaNueva);

            BasicFileAttributes attr = Files.readAttributes(rutaNueva, BasicFileAttributes.class);

            documento.setFechaCreacion(new java.util.Date(attr.creationTime().toMillis()));
            documento.setFechaModificacion(new java.util.Date(attr.lastModifiedTime().toMillis()));
            documento.setTamanio(archivo.getSize());
            documento.setNombreDocumento(nombreArchivo);
            documento.setFechaCarga(java.time.LocalDateTime.now());
        } catch (Exception e) {
            throw new RuntimeException("Error al procesar el archivo: " + e.getMessage(), e);
        }
    }
}