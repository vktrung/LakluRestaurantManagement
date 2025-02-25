package com.laklu.pos.dataObjects.response;

import lombok.Data;

@Data
public class UserInfo {
    private Integer id;
    private String username;
    private String email;
    // Thêm các trường khác nếu cần
}
