package org.wsd.app.bootloader;

import com.github.javafaker.Faker;
import io.micrometer.observation.annotation.Observed;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.wsd.app.domain.PhotoEntity;
import org.wsd.app.repository.PhotoRepository;
import org.wsd.app.repository.UserRepository;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class BootLoader implements CommandLineRunner {
    private final PhotoRepository photoRepository;

    @Override
    @Transactional
    public void run(String... args) throws Exception {
        PhotoEntity photoEntity = new PhotoEntity();
        photoEntity.setId(1L);
        String name = new Faker().file().fileName();
        photoEntity.setName(name);
        PhotoEntity photo = photoRepository.save(photoEntity);
    }

}
