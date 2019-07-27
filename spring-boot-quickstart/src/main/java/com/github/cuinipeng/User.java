package com.github.cuinipeng;

import lombok.*;


// User(id=123, name=cuinipeng, phone=13*******57)
@ToString(exclude = {"id", "phone"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Getter(AccessLevel.PUBLIC) @Setter private Integer id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;

}
