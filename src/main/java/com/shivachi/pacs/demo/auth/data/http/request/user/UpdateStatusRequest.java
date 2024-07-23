package com.shivachi.pacs.demo.auth.data.http.request.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@ToString
@Data
@AllArgsConstructor
@NoArgsConstructor
public class UpdateStatusRequest {
    private String email;
}
