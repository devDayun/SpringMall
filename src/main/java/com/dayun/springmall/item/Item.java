package com.dayun.springmall.item;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.Range;

@Entity // 붙이면
@Getter @Setter
public class Item { // 해당 class 이름으로 테이블 생성

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // 알아서 1씩 증가시켜줌
    private Long id;    // 데이터마다 unique한 번호 부여
    @NotBlank
    private String itemName;
    @NotNull
    @Range(min = 1000, max = 10000000)
    private Integer itemPrice;

}
