package com.cpierres.p3backspring.services;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.cpierres.p3backspring.exception.UploadFailedException;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
public class FileUploadService {
    private final Cloudinary cloudinary;

    public FileUploadService(Cloudinary cloudinary) {
        this.cloudinary = cloudinary;
    }

    public String uploadFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) {
                return null;
            }
            Map<?, ?> result = cloudinary.uploader().upload(file.getBytes(), ObjectUtils.emptyMap());
            return result.get("secure_url").toString();
        } catch (IOException e) {
            throw new UploadFailedException("Erreur lors du téléchargement de l'image : " + e.getMessage());
        }
    }
}