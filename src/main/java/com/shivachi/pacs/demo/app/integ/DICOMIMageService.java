package com.shivachi.pacs.demo.app.integ;

import com.shivachi.pacs.demo.app.data.DICOMTags;
import com.shivachi.pacs.demo.app.model.DICOMImage;
import com.shivachi.pacs.demo.app.repo.DICOMImageRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
public class DICOMIMageService {
    private final DICOMImageRepo dicomImageRepo;
    private final WebClient.Builder webClient;

    public Mono<DICOMTags> fetchDICOMDetails(String instanceId) {
        String url = "/instances/" + instanceId + "/tags";
        String baseUrl = "http://localhost:8042";
        return webClient.build().get()
                .uri(baseUrl + url)
                .retrieve()
                .bodyToMono(DICOMTags.class);
    }

    public Mono<DICOMImage> saveDICOMDetails(DICOMTags tags, String instanceId) {
        DICOMImage dicomImage = new DICOMImage();
        dicomImage.setPatientName(tags.getPatientName());
        dicomImage.setStudyId(tags.getStudyInstanceUID());
        dicomImage.setSeriesId(tags.getSeriesInstanceUID());
        dicomImage.setInstanceId(instanceId);

        return Mono.just(dicomImageRepo.save(dicomImage));
    }
}
