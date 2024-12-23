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
import java.util.Base64;
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
    @PostMapping("/uploadBase64Image")
    public ResponseEntity<?> uploadBase64Image(@RequestBody Map<String, Object> payload) {
        try {
            // Извлекаем данные из JSON
            String base64Image = (String) payload.get("image");
            int destinationId = (Integer) payload.get("destinationId");

            if (base64Image == null || base64Image.isEmpty()) {
                return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("No image provided");
            }

            // Убираем префикс data:image/jpeg;base64, если он есть
            String base64ImageData = base64Image.replaceFirst("^data:image/[^;]+;base64,", "");

            // Декодируем Base64 в байты
            byte[] imageBytes = Base64.getDecoder().decode(base64ImageData);

            // Указываем абсолютный путь для сохранения файла
            String directory = "src/main/resources/static/images/destinations/";
            String fileName = System.currentTimeMillis() + ".jpg";
            Path path = Path.of(directory + fileName);

            // Создаем директорию, если она не существует
            Files.createDirectories(path.getParent());

            // Сохраняем файл
            Files.write(path, imageBytes);

            // Формируем URL для изображения
            String fileUrl = "http://localhost:8080/images/destinations/" + fileName;

            // Обновляем URL изображения в базе данных
            destinationService.updateImageUrl(destinationId, fileUrl);

            // Возвращаем успешный ответ с URL изображения
            return ResponseEntity.ok().body(Map.of("success", true, "filePath", fileUrl));
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error uploading image");
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
