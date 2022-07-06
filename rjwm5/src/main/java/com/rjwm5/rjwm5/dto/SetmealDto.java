package com.rjwm5.rjwm5.dto;


import com.rjwm5.rjwm5.entity.Setmeal;
import com.rjwm5.rjwm5.entity.SetmealDish;
import lombok.Data;
import java.util.List;

@Data
//既包含了setmeal表中的数据又包含了setmeal_dish表中的数据
public class SetmealDto extends Setmeal {

//    SetmealDto:一个setmeal包含多个setmealDish
    private List<SetmealDish> setmealDishes;

    private String categoryName;
}
