package org.example.Controller;

import org.example.Service.CitiesService;
import org.example.Service.DestinationService;
import org.example.entity.Destination;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.List;
import java.util.Map;

@RequestMapping("/api/destination")
@RestController
public class DestinationController {
    private static final String UPLOAD_DIR = "src/main/images/destinations/";
    @Autowired
    private final DestinationService destinationService;

    public DestinationController(DestinationService destinationService) {
        this.destinationService = destinationService;
    }



    @PostMapping("/save")
    public ResponseEntity<?> saveDestination(@RequestBody Destination destination) {
        try {

            destinationService.saveDestination(destination);
            return ResponseEntity.ok().body(Map.of("success", true));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error saving destination");
        }
    }

    @PutMapping("/deleteImageUrl/{destinationId}")
    public ResponseEntity<?> deleteDestinationImageUrlByDestinationId(@PathVariable int destinationId) {
        destinationService.deleteDestinationImageUrlByDestinationId(destinationId);
        return ResponseEntity.ok("Image URL deleted successfully for destinationId ");
    }
    @PostMapping("/uploadImage")
    public ResponseEntity<?> uploadImage(@RequestParam("image") MultipartFile image,
                                         @RequestParam("destinationId") int destinationId) {
        try {
            // Проверка, что файл был выбран
            if (image.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No file selected");
            }

            // Указываем абсолютный путь для сохранения файла
            // Пример: /path/to/your/project/src/main/resources/static/images/destinations/
            String directory = "src/main/resources/static/images/destinations/";

            // Генерация уникального имени для файла (чтобы не перезаписывать файлы с одинаковыми именами)
            String fileName = System.currentTimeMillis() + "_" + image.getOriginalFilename();

            // Полный путь для сохранения файла
            Path path = Path.of(directory + fileName);

            // Создаем директорию, если она не существует
            Files.createDirectories(path.getParent());

            // Сохраняем файл на сервере
            Files.copy(image.getInputStream(), path, StandardCopyOption.REPLACE_EXISTING);

            // Формируем URL для доступа к файлу
            String fileUrl = "/images/destinations/" + fileName;

            // Обновляем URL изображения в базе данных
            destinationService.updateImageUrl(destinationId, fileUrl);

            // Возвращаем успешный ответ с URL изображения
            return ResponseEntity.ok().body(Map.of("success", true, "filePath", fileUrl));
        } catch (IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading file");
        }
    }


    @GetMapping("/getAllDestination")
    public ResponseEntity<?> getAllDestinations(){
        return ResponseEntity.ok(destinationService.getAllDestinations());
    }
    @GetMapping("/getAllDestinationsByCityId/{cityId}")
    public ResponseEntity<?> getAllDestinationsByCityId(@PathVariable int cityId){
        return ResponseEntity.ok(destinationService.getAllDestinationByCityId(cityId));
    }
    @GetMapping("/findDestinationsByTypeByTripId/{tripId}/{typeName}")
    public ResponseEntity<?> findDestinationsByTypeByTripId(@PathVariable int tripId,@PathVariable String typeName){
        return ResponseEntity.ok(destinationService.findDestinationsByTypeByTripId(tripId,typeName));
    }
    @GetMapping("/findDestinationByDestinationType/{typeName}")
    public ResponseEntity<?> findDestinationByDestinationType(@PathVariable String typeName){
        return ResponseEntity.ok(destinationService.findDestinationByDestinationType(typeName));
    }
    @GetMapping("/findDestinationByCityAndDByDestinationType/{typeName}/{cityId}")
    public ResponseEntity<?> findDestinationByCityAndDByDestinationType(@PathVariable String typeName,@PathVariable int cityId){
        return ResponseEntity.ok(destinationService.findDestinationByCityAndDByDestinationType(typeName,cityId));
    }

    @GetMapping("/findDestinationByPrefix/{cityId}")
    public ResponseEntity<?> findDestinationByPrefix(@PathVariable int cityId,@RequestParam String query){
        return ResponseEntity.ok(destinationService.findDestinationByPrefix(cityId,query));
    }

}
