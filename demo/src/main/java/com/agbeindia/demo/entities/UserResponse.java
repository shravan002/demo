package com.agbeindia.demo.entities;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class UserResponse {
    /**
     * Purpose of this class is to a return the response to the user
     */
    private String id;
    private String firstName;
    private String lastName;
    private String userName;

}
