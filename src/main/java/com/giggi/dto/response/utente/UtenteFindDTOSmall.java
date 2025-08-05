package com.giggi.dto.response.utente;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UtenteFindDTOSmall {
    private Long id;
    private String firstName;
    private String lastName;
    private String email;
    private String username;
}
