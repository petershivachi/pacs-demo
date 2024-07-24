package com.shivachi.pacs.demo.app.handler;

import com.shivachi.pacs.demo.app.data.DICOMTags;
import com.shivachi.pacs.demo.app.integ.DICOMIMageService;
import com.shivachi.pacs.demo.app.model.DICOMImage;
import io.swagger.annotations.Api;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v1/dicom-images")
@Api(tags = "DICOM Images")
public class DICOMImageHandler {
    private final DICOMIMageService dicomService;

    @RequestMapping(
            path = "/fetch/{instanceId}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<DICOMTags> fetchDICOMDetails(@PathVariable String instanceId) {
        return dicomService.fetchDICOMDetails(instanceId);
    }

    @RequestMapping(path = "/save/{instanceId}",
    method = RequestMethod.GET,
    produces = MediaType.APPLICATION_JSON_VALUE)
    public Mono<DICOMImage> saveDICOMDetails(@PathVariable String instanceId) {
        return dicomService.fetchDICOMDetails(instanceId)
                .flatMap(tags -> dicomService.saveDICOMDetails(tags, instanceId));
    }
}
