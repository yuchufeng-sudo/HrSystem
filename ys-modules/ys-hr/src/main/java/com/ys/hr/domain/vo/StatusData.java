package com.ys.hr.domain.vo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class StatusData {

    private String name;

    private Long value;

    private String percentage;

    private String color;

}
