package com.github.cuinipeng.entity;

import java.io.Serializable;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@ToString(exclude = {"id", "phone"})
@EqualsAndHashCode
@NoArgsConstructor
@AllArgsConstructor
public class User implements Serializable {

    private static final long serialVersionUID = -1762314849114826198L;
    @Getter(AccessLevel.PUBLIC) @Setter private Integer id;
    @Getter @Setter private String name;
    @Getter @Setter private String phone;

}
