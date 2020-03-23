package com.example.demo.controller;


import com.example.demo.dao.BridgeNodeRepository;
import com.example.demo.dao.RequestInformationRepository;
import com.example.demo.dao.UserRepository;
import com.example.demo.entity.BridgeNode;
import com.example.demo.entity.RequestInformation;
import com.example.demo.entity.User;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Controller;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/homepage")
public class HomePageController {

    @Resource
    private BridgeNodeRepository bridgeNodeRepository;
    @Resource
    private UserRepository userRepository;
    @Resource
    private RequestInformationRepository requestInformationRepository;

    public static int flags = 0;//记录用户长度  并发会有问题
    public static HashMap<String, HashSet<String>> candidateMaliciousUsers11 = new HashMap<>();
    public static HashSet<String> unavailableNodes11 = new HashSet<>();

    @GetMapping("/list")
    public String list() {
        //initNode();
        return "homepage";
    }

    //处置
    @GetMapping("/dealIp")
    @ResponseBody
    public String dealIp(String userName) {
        User user = userRepository.getByUserName(userName);
        requestInformationRepository.updateDeal(user.getId());
        List<RequestInformation> requestInformations = requestInformationRepository.findByUserId(user.getId());
        for (RequestInformation requestInformation : requestInformations) {
            Integer bridgeNodeId = requestInformation.getBridgeNodeId();
            BridgeNode one = bridgeNodeRepository.getOne(bridgeNodeId);
            one.setIsValid(0);
            bridgeNodeRepository.save(one);
        }
        return "";
    }

    //使用
    @GetMapping("/useIp")
    @ResponseBody
    public String useIp(String userName) {
        User user = userRepository.getByUserName(userName);
        user.setUsedNumber(user.getUsedNumber() + 1);
        user.setUpdateTime(new Date());
        userRepository.save(user);
        requestInformationRepository.updateIsValid(user.getId());
        return "";
    }

    //申请
    @GetMapping("/applyIp")
    @ResponseBody
    public String applyIp(String userName) {
        candidateMaliciousUsers11.clear();
        unavailableNodes11.clear();
        flags = 0;
        //判断用户是否存在
        User user = userRepository.getByUserName(userName);
        int apply_credibility = 0, node_status_credibility = 0, totle_credibility = 0;
        double p3 = 0;
        if (user == null) {
            //用户不存在 创建用户
            user = new User();
            user.setUserName(userName);
            user.setCreateTime(new Date());
        } else {
            double l = user.getTotleRequest() + 1;
            double a = user.getUsedNumber() / l;
            double b = 1 / l;
            double c = user.getTotleRequest() - user.getUsedNumber() / l;
            if (a > b && a > c) {
                apply_credibility = 1;
                user.setApplyCredibility1(user.getApplyCredibility1() + 1);
            }
            if (b >= a && b > c) {
                apply_credibility = 2;
                user.setApplyCredibility2(user.getApplyCredibility2() + 1);
            }
            if (c >= a && c >= b) {
                apply_credibility = 3;
                user.setApplyCredibility3(user.getApplyCredibility3() + 1);
            }
            Integer totleRequest = user.getTotleRequest();
            double p1 = user.getNodeStatusCredibility1() / totleRequest;
            double p2 = user.getNodeStatusCredibility2() / totleRequest;
            p3 = user.getNodeStatusCredibility3() / totleRequest;

            double m = 0.5 * a + 0.5 * p1;
            double n = 0.5 * b + 0.5 * p2;
            double k = 0.5 * c + 0.5 * p3;
            if (m > n && m > k) {
                totle_credibility = 1;
                user.setTotleCredibility1(user.getTotleCredibility1() + 1);
            }
            if (n >= m && n > k) {
                totle_credibility = 2;
                user.setTotleCredibility2(user.getTotleCredibility2() + 1);
            }
            if (k >= n && k >= m) {
                totle_credibility = 3;
                user.setTotleCredibility3(user.getTotleCredibility3() + 1);
            }
        }
        userRepository.save(user);
        Object[] objects = userRepository.queryZcUsers();
        node_status_credibility = getUsers(objects, String.valueOf(user.getId()));//调用算法 计算

        //查数据
        int x = userRepository.queryTotleCredibility1(user.getId(), apply_credibility, node_status_credibility, 1);
        int y = userRepository.queryTotleCredibility1(user.getId(), apply_credibility, node_status_credibility, 2);
        int z = userRepository.queryTotleCredibility1(user.getId(), apply_credibility, node_status_credibility, 3);
        int w = userRepository.queryTotle(user.getId(), apply_credibility, node_status_credibility);
        StringBuilder stringBuilder = new StringBuilder();
        if (w != 0) {
            double p4 = x / w;
            double p5 = y / w;
            double p6 = z / w;
            if (p3 > 0.8) {
                RequestInformation requestInformation = new RequestInformation();
                requestInformation.setApplyCredibility(apply_credibility);
                requestInformation.setNodeStatusCredibility(node_status_credibility);
                requestInformation.setTotleCredibility(totle_credibility);
                requestInformation.setUserId(user.getId());
                requestInformation.setBridgeNodeId(0);
                requestInformation.setIsUsed(0); //默认不可用
                requestInformation.setIsDeal(0);    //默认未处置
                requestInformation.setCreateTime(new Date());
                requestInformationRepository.save(requestInformation);
                stringBuilder.append("0");
            } else {
                List<BridgeNode> all, all_second;
                int a1, a2;
                int firstSave = 0;
                if (p4 >= p5 && p4 >= p6) {
                    all = bridgeNodeRepository.queryByIsValidMove(1, 0);
                    a1 = 3;
                    all_second = new ArrayList<>();
                    a2 = 0;
                } else if (p5 >= p4 && p5 >= p6) {
                    all = bridgeNodeRepository.queryByIsValidMove(1, 0);
                    a1 = 2;
                    all_second = bridgeNodeRepository.queryByIsValidMove(0, 0);
                    a2 = 1;
                } else {
                    all = bridgeNodeRepository.queryByIsValidMove(1, 0);
                    a1 = 1;
                    all_second = bridgeNodeRepository.queryByIsValid(0);
                    a2 = 2;
                }
                HashMap<Integer, Integer> hashMap = new HashMap<>();
                for (int i = 0; i < a1; i++) {
                    saveRequestInformationRepository(hashMap, all, firstSave, stringBuilder, user.getId()
                            , apply_credibility, node_status_credibility, totle_credibility);
                    firstSave++;
                }
                HashMap<Integer, Integer> hashMap11 = new HashMap<>();
                for (int i = 0; i < a2; i++) {
                    saveRequestInformationRepository(hashMap11, all_second, firstSave, stringBuilder, user.getId()
                            , apply_credibility, node_status_credibility, totle_credibility);
                    firstSave++;
                }
            }
        } else {
            List<BridgeNode> bridgeNodes = bridgeNodeRepository.queryByIsValidMove(1, 0);
            HashMap<Integer, Integer> hashMap = new HashMap<>();
            for (int i = 0; i < 3; i++) {
                saveRequestInformationRepository(hashMap, bridgeNodes, i, stringBuilder, user.getId()
                        , apply_credibility, node_status_credibility, totle_credibility);
            }
        }
        userRepository.updateNodeStatusAdd1(user.getId());
        return stringBuilder.toString();
    }

    private void saveRequestInformationRepository(HashMap<Integer, Integer> mm, List<BridgeNode> all, int i, StringBuilder stringBuilder,
                                                  int userId, int apply_credibility, int node_status_credibility, int totle_credibility) {
        if (all.size() == 0) {
            return;
        }
        Random random = new Random();
        int random_i = 0;
        for (int qq = 0; qq < 5; qq++) {
            random_i = random.nextInt(all.size());
            if (!mm.containsKey(random_i)) {
                mm.put(random_i, 1);
                break;
            }
        }
        BridgeNode bridgeNode = all.get(random_i);
        bridgeNode.setIsDistribute(1);
        bridgeNodeRepository.save(bridgeNode);
        //记录每个ip 和用户的关系
        RequestInformation requestInformation = new RequestInformation();
        if (i == 0) {
            requestInformation.setApplyCredibility(apply_credibility);
            requestInformation.setNodeStatusCredibility(node_status_credibility);
            requestInformation.setTotleCredibility(totle_credibility);
        }
        requestInformation.setUserId(userId);
        requestInformation.setBridgeNodeId(bridgeNode.getId());
        requestInformation.setIsUsed(0); //默认不可用
        requestInformation.setIsDeal(0);    //默认未处置
        requestInformation.setCreateTime(new Date());
        requestInformationRepository.save(requestInformation);
        stringBuilder.append(bridgeNode.getIp());
        stringBuilder.append("<br/>");
    }

    private int getUsers(Object[] objects, String newUserId) {
        int returnInt = 0;
        List<String> listList = new ArrayList<>();
        if (objects != null && objects.length > 0) {
            int three = 0;
            boolean isValid = false;
            StringBuilder forStr = new StringBuilder();
            HashMap<String, Object> dbMap = new HashMap<>();
            for (int i = 0; i < objects.length; i++) {
                three++;
                Object[] object = (Object[]) objects[i];
                String userId = String.valueOf(object[1]);
                dbMap.put(userId, 1);
                String nodeId = String.valueOf(object[2]);
                BridgeNode one = bridgeNodeRepository.getOne(Integer.parseInt(nodeId));
                forStr.append(one.getIp() + ",");
                if (one.getIsValid() != 0) {
                    isValid = true;
                }
                if (three == 3) {
                    three = 0;
                    if (!isValid) {
                        String s = forStr.toString();
                        String[] split = s.split(",");
                        HashSet<String> hashSet = new HashSet<>();
                        for (String str : split) {
                            if (!StringUtils.isEmpty(str)) {
                                unavailableNodes11.add(str);
                                hashSet.add(str);
                            }
                        }
                        candidateMaliciousUsers11.put(userId, hashSet);
                    }
                    forStr.delete(0, forStr.length() - 1);
                }
            }
            List<String> list = new ArrayList<>();
            getUsers(unavailableNodes11, candidateMaliciousUsers11, list, "");
            HashMap<String, Object> listMap = new HashMap<>();
            for (int i = 0; i < list.size(); i++) {
                String[] split = list.get(i).split(",");
                for (int j = 0; j < split.length; j++) {
                    String s = split[j];
                    if (!StringUtils.isEmpty(s)) {
                        listMap.put(s, 1);
                    }
                }
            }
            listList = listMap.keySet().stream().collect(Collectors.toList());
            if (listList != null && listList.size() > 0) {
                userRepository.updateNodeStatusCredibilityThree(listList);
                if (listList.contains(newUserId)) {
                    returnInt = 3;
                }
            }
        }
        List<String> dbList = new ArrayList<>();
        Object[] objectsaaa = userRepository.queryZcUserssss();
        int three = 0;
        boolean isValid = false;
        for (int i = 0; i < objectsaaa.length; i++) {
            three++;
            Object[] object = (Object[]) objectsaaa[i];
            String userId = String.valueOf(object[1]);
            String nodeId = String.valueOf(object[2]);
            BridgeNode one = bridgeNodeRepository.getOne(Integer.parseInt(nodeId));
            if (one.getIsValid() != 0) {
                isValid = true;
            }
            if (three == 3) {
                three = 0;
                if (!isValid) {
                    dbList.add(userId);
                }
            }
        }
        dbList.removeAll(listList);//取差集
        if (dbList.size() > 0) {
            if (dbList.contains(newUserId)) {
                returnInt = 2;
            }
            userRepository.updateNodeStatusCredibilityTwo(dbList);
        }
        dbList.addAll(listList);
        if (dbList.size() > 0) {
            if (!dbList.contains(newUserId)) {
                returnInt = 1;
            }
            userRepository.updateNodeStatusCredibilityOne(dbList);
        }
        if (dbList.size() == 0 && listList.size() == 0) {
            userRepository.updateNodeStatusCredibilityOneAll();
            returnInt = 1;
        }
        return returnInt;
    }


    public void initNode() {
        List<String> list = new ArrayList<>();
        try {
            ClassPathResource classPathResource = new ClassPathResource("static/file/bridge-test.txt");
            File file = classPathResource.getFile();
            FileReader fr = new FileReader(file);
            BufferedReader br = new BufferedReader(fr);
            String line = "";
            while ((line = br.readLine()) != null) {
                list.add(line);
            }
            br.close();
            fr.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
        for (int i = 0; i < list.size(); i++) {
            BridgeNode bridgeNode = new BridgeNode();
            bridgeNode.setIp(list.get(i));
            bridgeNode.setIsDistribute(0);
            bridgeNode.setIsValid(1);
            bridgeNode.setCreateTime(new Date());
            bridgeNodeRepository.save(bridgeNode);
        }
    }

    private static void getUsers(HashSet<String> unavailableNodes1, HashMap<String, HashSet<String>> candidateMaliciousUsers, List<String> list, String userIdStrs) {
        HashSet<String> stringHashSetCheck = new HashSet<>();
        HashSet<String> hashSet = new HashSet<>(unavailableNodes1);
        String[] splitCheck = userIdStrs.split(",");
        for (String s : splitCheck) {
            HashSet<String> stringss = candidateMaliciousUsers11.get(s);
            if (stringss != null) {
                stringHashSetCheck.addAll(stringss);
            }
        }
        int i = unavailableNodes11.size() - stringHashSetCheck.size();
        if (i <= unavailableNodes1.size()) {            //优化 过滤没必要的数据
            int length = userIdStrs.length() > 1 ? userIdStrs.split(",").length : 0;
            int unavailableNodeSize = unavailableNodes1.size() % 3 == 0 ? unavailableNodes1.size() / 3 : unavailableNodes1.size() / 3 + 1;
            if (flags == 0 || length + unavailableNodeSize < flags) {//优化 少于最优的 不再遍历

                HashMap<String, HashSet<String>> candidateMaliciousUsers1 = new HashMap<>(candidateMaliciousUsers);
                HashMap<String, Integer> data_number = new HashMap<>();//每个数据 出现次数
                HashMap<Integer, ArrayList<String>> number_data = new HashMap<>();//出现次数 对应的数据
                HashMap<String, ArrayList<String>> dataUser = new HashMap<>();//每个数据 对应的用户
                SortedSet<Integer> sortedSet = new TreeSet<>(); //记录出现最少次数的数据
                Set<String> strings = candidateMaliciousUsers1.keySet();
                for (String userId : strings) {
                    HashSet<String> strings1 = candidateMaliciousUsers1.get(userId);
                    for (String datas : strings1) {
                        if (unavailableNodes1.contains(datas)) {
                            ArrayList<String> userLists = dataUser.get(datas);
                            if (userLists == null) {
                                userLists = new ArrayList<>();
                                userLists.add(userId);
                                dataUser.put(datas, userLists);
                            } else {
                                userLists.add(userId);
                            }
                            if (data_number.containsKey(datas)) {
                                Integer integer = data_number.get(datas);
                                data_number.put(datas, integer + 1);
                            } else {
                                data_number.put(datas, 1);
                            }
                        }
                    }
                }
                Set<String> strings2 = data_number.keySet();
                for (String data : strings2) {
                    sortedSet.add(data_number.get(data));
                    ArrayList<String> strings1 = number_data.get(data_number.get(data));
                    if (strings1 == null) {
                        ArrayList<String> arrayList = new ArrayList<>();
                        arrayList.add(data);
                        number_data.put(data_number.get(data), arrayList);
                    } else {
                        strings1.add(data);
                    }
                }
                if (sortedSet.size() > 0) {
                    Integer first = sortedSet.first();
                    ArrayList<String> strings3 = number_data.get(first);
                    String str = userIdStrs;
                    int length2 = str.split(",").length;
                    if (length2 < flags || flags == 0) {    //优化 少于最优的 不再遍历
                        for (String minNumberData : strings3) {
                            ArrayList<String> users = dataUser.get(minNumberData);
                            for (String userId : users) {
                                hashSet.addAll(unavailableNodes1);
                                HashSet<String> strings1 = candidateMaliciousUsers1.get(userId);
                                if (strings1 != null) {
                                    if (hashSet.size() == 0) {
                                        hashSet.addAll(unavailableNodes1);
                                    }
                                    for (String data : strings1) {
                                        hashSet.remove(data);//移除存在的数据
                                    }
                                    userIdStrs = str + userId + ",";
                                    candidateMaliciousUsers1.remove(userId);
                                    if (hashSet.size() == 0) {
                                        candidateMaliciousUsers1.put(userId, strings1);
                                        int length1 = userIdStrs.split(",").length;
                                        if (length1 <= flags || flags == 0) { //优化 少于最优的 不再存入集合 防止内存溢出
                                            HashSet<String> stringHashSet = new HashSet<>();
                                            String[] split = userIdStrs.split(",");
                                            for (String s : split) {
                                                HashSet<String> stringss = candidateMaliciousUsers11.get(s);
                                                stringHashSet.addAll(stringss);
                                            }
                                            if (stringHashSet.size() == unavailableNodes11.size()) {
                                                flags = length1;
                                                list.add(userIdStrs);
                                            }
                                        }
                                        continue;
                                    }
                                    //递归 剩下的数据 取最小出现次数
                                    getUsers(hashSet, candidateMaliciousUsers1, list, userIdStrs);
                                }

                            }
                        }
                    }
                }
            }
        }
    }
}
