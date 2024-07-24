package com.shivachi.pacs.demo.app.data;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class DICOMTags {
    private String PatientName;
    private String StudyInstanceUID;
    private String SeriesInstanceUID;
}
