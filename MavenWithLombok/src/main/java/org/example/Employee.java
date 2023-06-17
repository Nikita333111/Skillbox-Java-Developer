package org.example;

import lombok.*;

import java.beans.ConstructorProperties;
import java.util.Date;
@Data
@ToString(exclude = "workStart")
@AllArgsConstructor
public class Employee {
    private String name;
    private Integer salary;
    private Date workStart;
}
