package com.iamnbty.training.backend.model;

import lombok.Data;

@Data
public class MRegisterRequest {

    private String email;

    private String password;

    private String name;

}
