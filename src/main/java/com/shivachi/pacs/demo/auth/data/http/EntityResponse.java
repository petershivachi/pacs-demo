package com.shivachi.pacs.demo.auth.data.http;

import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EntityResponse {
    @Builder.Default
    private Integer resultCode = HttpStatus.OK.value();

    @Builder.Default
    private String returnMessage = HttpStatus.OK.getReasonPhrase();
}
