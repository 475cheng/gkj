package com.example.demo.controller;

import com.example.demo.dao.Obfs4gmailRepository;
import com.example.demo.entity.BridgeNode;
import com.example.demo.entity.Obfs4gmail;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import javax.annotation.Resource;
import java.util.List;
import java.util.Random;

/**
 * Created by Administrator on 2020/4/8.
 */

@Controller
@RequestMapping("/obfs")
public class ObfsController {
    @Resource
    private Obfs4gmailRepository obfs4gmailRepository;

    @GetMapping("/list")
    @ResponseBody
    public String list() {
        List<String> bridgeNodes = obfs4gmailRepository.queryAllDisUserName();
        Random random = new Random();
        int i = random.nextInt(bridgeNodes.size() - 10);
        List<String> list = bridgeNodes.subList(i, i + 10);
        Integer integer = obfs4gmailRepository.queryCountDisHost(list);
        System.out.println("去重host: " + integer);
        return String.valueOf(integer);
    }
}
