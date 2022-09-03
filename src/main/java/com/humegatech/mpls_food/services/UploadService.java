package com.humegatech.mpls_food.services;

import com.humegatech.mpls_food.domains.Deal;
import com.humegatech.mpls_food.domains.Upload;
import com.humegatech.mpls_food.models.UploadDTO;
import com.humegatech.mpls_food.repositories.DealRepository;
import com.humegatech.mpls_food.repositories.UploadRepository;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

@Service
public class UploadService {
    private final UploadRepository uploadRepository;
    private final DealRepository dealRepository;

    public UploadService(UploadRepository uploadRepository, DealRepository dealRepository) {
        this.uploadRepository = uploadRepository;
        this.dealRepository = dealRepository;
    }

    public Long create(final UploadDTO uploadDTO) {
        final Upload upload = new Upload();
        mapToEntity(uploadDTO, upload);
        return uploadRepository.save(upload).getId();
    }

    private UploadDTO mapToDTO(final Upload upload, final UploadDTO uploadDTO) {
        uploadDTO.setImage(upload.getImage());
        uploadDTO.setVerified(upload.isVerified());
        uploadDTO.setDealId(upload.getDeal().getId());

        return uploadDTO;
    }

    private Upload mapToEntity(final UploadDTO uploadDTO, final Upload upload) {
        final Deal deal = uploadDTO.getDealId() == null ? null : dealRepository.findById(uploadDTO.getDealId())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "deal not found"));

        upload.setVerified(uploadDTO.isVerified());
        upload.setDeal(deal);
        upload.setImage(uploadDTO.getImage());

        return upload;
    }
}