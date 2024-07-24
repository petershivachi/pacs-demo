package com.shivachi.pacs.demo.app.repo;

import com.shivachi.pacs.demo.app.model.DICOMImage;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DICOMImageRepo extends JpaRepository<DICOMImage, Long> {
}
