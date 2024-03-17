package ru.hogwarts.school.service;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.util.Pair;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;
import ru.hogwarts.school.dto.AvatarDto;
import ru.hogwarts.school.entity.Avatar;
import ru.hogwarts.school.entity.Student;
import ru.hogwarts.school.exceptions.AvatarNotFoundException;
import ru.hogwarts.school.exceptions.AvatarProcessingException;
import ru.hogwarts.school.exceptions.StudentNotFoundException;
import ru.hogwarts.school.mapper.AvatarMapper;
import ru.hogwarts.school.repositories.AvatarRepository;
import ru.hogwarts.school.repositories.StudentRepository;
import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
public class AvatarService {

    private final Path pathToAvatarsDir;
    private final AvatarRepository avatarRepository;
    private final StudentRepository studentRepository;
    private final AvatarMapper avatarMapper;

    public AvatarService(AvatarRepository avatarRepository,
                         StudentRepository studentRepository,
                         AvatarMapper avatarMapper,
                         @Value("${application.path-to-avatars-dir}") String pathToAvatarsDir) {
        this.avatarRepository = avatarRepository;
        this.studentRepository = studentRepository;
        this.avatarMapper = avatarMapper;
        this.pathToAvatarsDir = Paths.get(pathToAvatarsDir);
    }

    @PostConstruct
    public void init() {
        try {
            if (!Files.exists(pathToAvatarsDir) || !Files.isDirectory(pathToAvatarsDir)) {
                Files.createDirectories(pathToAvatarsDir);
            }
        } catch (IOException e) {
            throw new RuntimeException();
        }
    }

    public void uploadAvatar(long studentId, MultipartFile avatarFile) {
        try {
            Student student = studentRepository.findById(studentId)
                    .orElseThrow(() -> new StudentNotFoundException());
            byte[] data = avatarFile.getBytes();
            String extension = StringUtils.getFilenameExtension(avatarFile.getOriginalFilename());
            String fileName = String.format("%s.%s", UUID.randomUUID(), extension);
            Path path = pathToAvatarsDir.resolve(fileName);
            Files.write(path, data);

            Avatar avatar = new Avatar();
            avatar.setStudent(student);
            avatar.setData(data);
            avatar.setFileSize(data.length);
            avatar.setMediaType(avatarFile.getContentType());
            avatar.setFilePath(path.toString());
            avatarRepository.save(avatar);
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    public Pair<byte[], String> getAvatarFromDb(long studentId) {
        Avatar avatar = avatarRepository.findByStudentId(studentId)
                .orElseThrow(() -> new AvatarNotFoundException());
        return Pair.of(avatar.getData(), avatar.getMediaType());
    }

    public Pair<byte[], String> getAvatarFromFs(long studentId) {
        try {
            Avatar avatar = avatarRepository.findByStudentId(studentId)
                    .orElseThrow(() -> new AvatarNotFoundException());
            byte[] data = Files.readAllBytes(Paths.get(avatar.getFilePath()));
            return Pair.of(avatar.getData(), avatar.getMediaType());
        } catch (IOException e) {
            throw new AvatarProcessingException();
        }
    }

    public List<AvatarDto> getAllAvatars(int page, int size) {
        return avatarRepository.findAll(PageRequest.of(page - 1, size)).get()
                .map(avatarMapper::toDto)
                .collect(Collectors.toList());
    }
}
