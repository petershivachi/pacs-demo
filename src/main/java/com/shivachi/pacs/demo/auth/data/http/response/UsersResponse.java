package com.shivachi.pacs.demo.auth.data.http.response;

import com.shivachi.pacs.demo.auth.data.user.UserData;
import lombok.*;
import org.springframework.http.HttpStatus;

import java.util.List;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UsersResponse {
    @Builder.Default
    private Integer resultCode = HttpStatus.NOT_FOUND.value();

    @Builder.Default
    private String returnMessage = HttpStatus.NOT_FOUND.getReasonPhrase();

    @Builder.Default
    private List<UserData> data = null;
}
