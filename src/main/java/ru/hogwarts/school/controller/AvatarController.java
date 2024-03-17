package ru.hogwarts.school.controller;

import jakarta.validation.constraints.Min;
import org.springframework.data.util.Pair;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.service.AvatarService;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping(AvatarController.BASE_URI)
@Validated
public class AvatarController {

    public static final String BASE_URI="/avatars";
    private final AvatarService avatarService;

    public AvatarController(AvatarService avatarService) {
        this.avatarService = avatarService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public void uploadAvatar(@RequestParam long studentId, @RequestPart MultipartFile avatar) throws IOException {
        avatarService.uploadAvatar(studentId, avatar);
    }

    @GetMapping("/from-db")
    public ResponseEntity<byte[]> getAvatarFromDb(@RequestParam long id) {
        return transform(avatarService.getAvatarFromDb(id));
    }

    @GetMapping("/from-file")
    public ResponseEntity<byte[]> getAvatarFromFs(@RequestParam long id){
        return transform(avatarService.getAvatarFromFs(id));
    }

    private ResponseEntity<byte[]> transform(Pair<byte[],String> pair) {
        byte[] data = pair.getFirst();
        return ResponseEntity.status(HttpStatus.OK)
                .contentLength(data.length)
                .contentType(MediaType.parseMediaType(pair.getSecond()))
                .body(data);
    }

    @GetMapping
    public List<AvatarDto> getAllAvatars(@RequestParam @Min(1) int page, @RequestParam @Min(1) int size){
        return avatarService.getAllAvatars(page,size);
    }
}
