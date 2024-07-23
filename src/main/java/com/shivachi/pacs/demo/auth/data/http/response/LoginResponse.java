package com.shivachi.pacs.demo.auth.data.http.response;

import com.shivachi.pacs.demo.auth.data.user.LoginData;
import lombok.*;
import org.springframework.http.HttpStatus;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class LoginResponse {
    @Builder.Default
    private Integer resultCode = HttpStatus.NOT_FOUND.value();

    @Builder.Default
    private String returnMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    @Builder.Default
    private LoginData user = null;
}
