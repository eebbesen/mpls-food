package com.humegatech.mpls_food.controllers;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.actuate.web.exchanges.HttpExchange.Principal;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.services.UploadService;

import jakarta.servlet.http.HttpServletResponse;

@Controller
@RequestMapping("/uploads")
public class UploadController {
    private final UploadService uploadService;
    private final Logger logger = LoggerFactory.getLogger(UploadController.class);

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String upload(@PathVariable final Long id,
                         @RequestParam("file") final MultipartFile file,
                         final RedirectAttributes attributes, final Principal principal) {
        if (null != file) {
            byte[] image;

            try {
                image = file.getBytes();
            } catch (IOException e) {
                logger.error(String.format("Error uploading file for user %s", principal.getName()));
                throw new FileUploadException("Error uploading file", e);
            }

            final UploadDTO uploadDTO = UploadDTO.builder()
                    .dealId(id)
                    .image(image)
                    .build();
            uploadService.create(uploadDTO);

            return "redirect:/deals";
        }

        return "deal/edit";
    }

    @GetMapping("/{id}")
    public void renderImage(@PathVariable final Long id, HttpServletResponse response) throws IOException {
        final UploadDTO dto = uploadService.get(id);

        byte[] image = dto.getImage();
        InputStream bais = new ByteArrayInputStream(image);
        response.setContentType("image/jpeg");
        IOUtils.copy(bais, response.getOutputStream());
    }
}

class FileUploadException extends RuntimeException {
    FileUploadException(final String message, final Throwable cause) {
        super(message, cause);
    }
}
