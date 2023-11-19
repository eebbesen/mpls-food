package com.humegatech.mpls_food.controllers;

import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.services.UploadService;
import org.apache.tomcat.util.http.fileupload.IOUtils;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

@Controller
@RequestMapping("/uploads")
public class UploadController {
    private final UploadService uploadService;

    public UploadController(UploadService uploadService) {
        this.uploadService = uploadService;
    }

    @PostMapping("/{id}")
    @PreAuthorize("isAuthenticated()")
    public String upload(@PathVariable final Long id,
                         @RequestParam("file") final MultipartFile file,
                         final RedirectAttributes attributes) {
        if (null != file) {
            byte[] image;

            try {
                image = file.getBytes();
            } catch (IOException e) {
                throw new RuntimeException(e);
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
