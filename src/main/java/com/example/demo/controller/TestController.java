package com.example.demo.controller;

import com.example.demo.model.InputOutputData;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author ChenOT
 * @date 2019-09-09
 * @see
 * @since
 */
@RestController
public class TestController {
    @RequestMapping(value = "/test")
    @ResponseBody
    public InputOutputData.Output test(@RequestBody InputOutputData.Input input){
        // 反序列化后
        System.out.println(input.getName());
        System.out.println(input.getAge());


        // 输出
        InputOutputData.Output.Builder outBuilder = InputOutputData.Output.newBuilder();
        outBuilder.setResult("老大回来了");

        return outBuilder.build();
    }
}
