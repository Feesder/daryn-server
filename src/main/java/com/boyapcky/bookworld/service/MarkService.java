package com.boyapcky.bookworld.service;

import com.boyapcky.bookworld.entity.MarkEntity;
import com.boyapcky.bookworld.entity.Road;
import com.boyapcky.bookworld.entity.Status;
import com.boyapcky.bookworld.model.Mark;
import com.boyapcky.bookworld.model.MarkRequest;
import com.boyapcky.bookworld.repository.MarkRepository;
import io.socket.client.IO;
import io.socket.client.Socket;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.*;

@Service
@Slf4j
public class MarkService {
    private final MarkRepository markRepository;
    private Socket socket;

    @Autowired
    public MarkService(MarkRepository markRepository) {
        this.markRepository = markRepository;

        try {
            socket = IO.socket("http://localhost:8082");
            socket.connect();
        } catch (URISyntaxException e) {
            log.error(e.getMessage());
        }
    }

    public void create(MarkRequest markRequest) throws IOException {
        MarkEntity markEntity = new MarkEntity();
        markEntity.setAccuracy(markRequest.getAccuracy());
        markEntity.setLongitude(markRequest.getLongitude());
        markEntity.setLatitude(markRequest.getLatitude());
        markEntity.setTimeAdded(new Date(markRequest.getTime()));
        markEntity.setRoad(Road.INDEFINED);
        markEntity.setStatus(Status.ACTIVE);
        markEntity.setCreated(new Date());
        markEntity.setUpdated(new Date());
        markEntity = markRepository.save(markEntity);

        byte[] imageBytes = Base64.getDecoder().decode(markRequest.getImageFile());
        File imageFile = new File("src/main/resources/script/" + markEntity.getId() + ".jpg");
        FileOutputStream fileOutputStream = new FileOutputStream(imageFile);
        fileOutputStream.write(imageBytes);
        fileOutputStream.close();

        socket.emit("image", markEntity.getId());
    }

    public void update(Long id, String road) {
        MarkEntity markEntity = markRepository.getReferenceById(id);

        Road type = switch (road) {
            case "road_cracks" -> Road.ROAD_CRACKS;
            case "road_default" -> Road.ROAD_DEFAULT;
            case "road_pits" -> Road.ROAD_PITS;
            case "road_without_asphalt" -> Road.ROAD_WITHOUT_ASPHALT;
            default -> Road.INDEFINED;
        };

        markEntity.setRoad(type);
        markEntity.setUpdated(new Date());
        markRepository.save(markEntity);
    }

    public List<Mark> getAll() {
        List<MarkEntity> markEntities = markRepository.findAll();
        ArrayList<Mark> marks = new ArrayList<>();
        markEntities.forEach((value) -> marks.add(Mark.toModel(value)));
        return marks;
    }

    public List<Mark> getByTime(Date date) {
        List<MarkEntity> markEntities = markRepository.findByTimeAdded(date);
        ArrayList<Mark> marks = new ArrayList<>();
        markEntities.forEach((value) -> marks.add(Mark.toModel(value)));
        return marks;
    }
}
