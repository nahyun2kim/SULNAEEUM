package com.ssafy.sulnaeeum.model.user.service;

import com.ssafy.sulnaeeum.exception.CustomException;
import com.ssafy.sulnaeeum.exception.CustomExceptionList;
import com.ssafy.sulnaeeum.model.user.dto.UserPreferenceDto;
import com.ssafy.sulnaeeum.model.user.entity.User;
import com.ssafy.sulnaeeum.model.user.entity.UserPreference;
import com.ssafy.sulnaeeum.model.user.repo.UserPreferenceRepo;
import com.ssafy.sulnaeeum.model.user.repo.UserRepo;
import com.ssafy.sulnaeeum.util.FlaskUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class UserPreferenceService {
    private final UserPreferenceRepo userPreferenceRepo;
    private final UserRepo userRepository;
    private final FlaskUtil flaskUtil;

    /***
     * 회원 가입 시 회원 취향을 저장
     ***/
    @Transactional
    public void preference(String kakaoId, UserPreferenceDto userPreferenceDto) {

        User user = userRepository.findByKakaoId(kakaoId).orElseThrow(() -> new CustomException(CustomExceptionList.MEMBER_NOT_FOUND));

        UserPreference userPreference = userPreferenceDto.toEntity();
        userPreference.setUser(user);

        userPreferenceRepo.save(userPreference);
        user.updateFinish(true);
        user.updateInfo(userPreferenceDto.getSex(), userPreferenceDto.getAge());
    }

    /***
     * 회원 취향을 활용하여 Flask 에게 컨텐츠 기반 추천 API 요청
     ***/
    @Transactional
    public Map<String, Map<String, String>> recommendUserDrink(UserPreferenceDto userPreferenceDto) {

        int drinkLevel = 0;

        if(userPreferenceDto.getLevel() == 1){
            drinkLevel= 4;
        }else if (userPreferenceDto.getLevel() == 2){
            drinkLevel = 7;
        }else if (userPreferenceDto.getLevel() == 3){
            drinkLevel = 15;
        }else if (userPreferenceDto.getLevel() == 4){
            drinkLevel = 25;
        }else if (userPreferenceDto.getLevel() == 5){
            drinkLevel = 40;
        }

        List<Integer> input_data = new ArrayList<>();
        input_data.add(userPreferenceDto.getTasteSweet());
        input_data.add(userPreferenceDto.getTasteSour());
        input_data.add(userPreferenceDto.getTasteRefresh());
        input_data.add(userPreferenceDto.getTasteFlavor());
        input_data.add(userPreferenceDto.getTasteThroat());
        input_data.add(userPreferenceDto.getTasteBody());
        input_data.add(drinkLevel);

        String dish = userPreferenceDto.getDish();
        int[] dish_arr = null;

        if(dish.equals("전/무침")){
            dish_arr = new int[] {3, 0, 0, 0, 0, 0};
        }else if(dish.equals("육류")){
            dish_arr = new int[] {0, 3, 0, 0, 0, 0};
        }else if(dish.equals("해산물")){
            dish_arr = new int[] {0, 0, 3, 0, 0, 0};
        }else if(dish.equals("탕/전골")){
            dish_arr = new int[] {0, 0, 0, 3, 0, 0};
        }else if(dish.equals("양식")){
            dish_arr = new int[] {0, 0, 0, 0, 3, 0};
        }else{
            dish_arr = new int[] {0, 0, 0, 0, 0, 3};
        }

        for(int i = 0; i < 6; i++) input_data.add(dish_arr[i]);

        Map<String, List> params = new HashMap<>();
        params.put("input_data", input_data);

        System.out.println(params);

        String requestUrl = "http://j8a707.p.ssafy.io:5000/recommend/contents";
        JSONParser jsonParser = new JSONParser();
        JSONObject jsonObject = flaskUtil.requestFlask(requestUrl, params);

        System.out.println(jsonObject.keySet());
        System.out.println(jsonObject.get("0").toString());
        System.out.println(jsonObject.values());

        Map<String, Map<String, String>> recommend = new HashMap<>();

        for(int i = 0; i < 5; i++){
            String key = Integer.toString(i);
            String sVal = jsonObject.get(key).toString();

            JSONObject jsonValue = null;

            try {
                jsonValue = (JSONObject) jsonParser.parse(sVal);
            } catch (ParseException e) {
                e.printStackTrace();
            }

            Map<String, String> value = new HashMap<>();

            String imgUrl = jsonValue.get("drink_image").toString().replace("\\", "");
            System.out.println(imgUrl);

            value.put("drink_amount", jsonValue.get("drink_amount").toString());
            value.put("drink_name", jsonValue.get("drink_name").toString());
            value.put("drink_level", jsonValue.get("drink_level").toString());
            value.put("drink_id", jsonValue.get("drink_id").toString());
            value.put("drink_image", imgUrl);

            recommend.put(key, value);
        }

        return recommend;
    }
}
