package ru.mkryuchkov.blogya.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import ru.mkryuchkov.blogya.service.FileService;

import java.util.Optional;

@Controller
@RequiredArgsConstructor
@RequestMapping("/image")
public class ImageController {

    private final FileService fileService;

    @GetMapping(
            value = "/{uuid}",
            produces = { MediaType.IMAGE_JPEG_VALUE, MediaType.IMAGE_PNG_VALUE }
    )
    public ResponseEntity<byte[]> findByUuid(@PathVariable(name = "uuid") String uuid) {
        Optional<byte[]> bytes = fileService.findById(uuid);
        return bytes.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }
}
