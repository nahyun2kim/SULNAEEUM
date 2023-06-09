package com.ssafy.sulnaeeum.model.drink.entity;

import com.ssafy.sulnaeeum.model.drink.dto.LikeDrinkDto;
import com.ssafy.sulnaeeum.model.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.SuperBuilder;

import javax.persistence.*;

@Entity
@Data
@SuperBuilder
@NoArgsConstructor
@AllArgsConstructor
@Table(name="like_drink")
public class LikeDrink {

    @Id
    @GeneratedValue
    @Column(nullable = false)
    private Long likeDrinkId; // auto_increment PK

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user; // 찜한 회원

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "drink_id")
    private Drink drink; // 찜한 술

    // Entity -> DTO 변환
    public LikeDrinkDto toDto() {
        return LikeDrinkDto.builder()
                .likeDrinkId(this.likeDrinkId)
                .userDto(this.user.toDto())
                .drinkDto(this.drink.toDto())
                .build();
    }
}
