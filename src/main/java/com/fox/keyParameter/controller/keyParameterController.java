package com.fox.keyParameter.controller;

import com.fox.keyParameter.entity.AutoFloorKeyCheckup;
import com.fox.keyParameter.entity.AutofloorKeyCheckupHis;
import com.fox.keyParameter.entity.AutofloorTarget;
import com.fox.keyParameter.mapper.AutofloorKeyCheckupHisMapper;
import com.fox.keyParameter.service.AutoFloorKeyCheckupService;
import com.fox.keyParameter.service.AutofloorTargetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author Yang TanYing
 * @Description:
 * @create 2021-02-26 17:14
 */

@Controller
public class keyParameterController {
    @Autowired
    private AutofloorTargetService autofloorTargetService;
    @Autowired
    private AutoFloorKeyCheckupService autoFloorKeyCheckupService;

    @Autowired
    private AutofloorKeyCheckupHisMapper autofloorKeyCheckupHisMapper;


    //获取时间段
//    @RequestMapping("gettime")
//    @ResponseBody
    public ArrayList<String> getTime() throws ParseException {
        ArrayList<String> returnTime = new ArrayList<>();

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat ds = new SimpleDateFormat("yyyy-MM-dd ");
        //开始时间
        String startTime = null;
        //结束时间
        String endTime = null;


        String endT = autofloorTargetService.getSqlDate();
        Date nowDate = df.parse(endT.toString());
//        Date nowDate = new Date();
        Date now = df.parse(df.format(nowDate));
        //白班时间段
        Date datebegin = df.parse(ds.format(nowDate) + "08:30:00");
        Date dateend = df.parse(ds.format(nowDate) + "20:30:00");

        //晚上12点 分界线
        Date wan = new Date(nowDate.getTime() + 86400000);
        Date wanss = df.parse(ds.format(wan) + "00:00:00");
        Date nowWan = df.parse(ds.format(nowDate) + "00:00:00");
        //白班
        if (now.after(datebegin) && now.before(dateend)) {
            //白班开始时间
            startTime = ds.format(nowDate) + "08:30:00";
            //白班结束时间
            endTime = endT;
        }
        //夜班
        else {
            //在凌晨12点之前
            if (now.after(dateend) && now.before(wanss)) {
                //夜班开始时间
                startTime = ds.format(nowDate) + "20:30:00";
                //夜班结束时间
                Date endDate1 = new Date(nowDate.getTime() + 86400000);
                endTime = endT;
            }

            //在凌晨12点之后
            else if (now.after(nowWan) && now.before(datebegin)) {
                //夜班开始时间
                Date endDate1 = new Date(nowDate.getTime() - 86400000);
                startTime = ds.format(endDate1) + "20:30:00";
                //夜班结束时间
                endTime = endT;

            }
        }

        returnTime.add(startTime);
        returnTime.add(endTime);

        return returnTime;
    }


    @RequestMapping("getKeyHtml")
    public String getFloorAndLine(Map map){
        List<AutofloorTarget> autofloor_targets = autofloorTargetService.getAllFloorAndLine();

        HashSet<String> floorSet = new HashSet<>();
        ArrayList<String> lineList = new ArrayList<>();

        //存入楼层和线体
        for (AutofloorTarget target : autofloor_targets) {
            floorSet.add(target.gettFloor());
            lineList.add(target.getLineName());
        }

        map.put("floorSet", floorSet);
        map.put("lineList", lineList);

        return "keyParameter";
    }


    //获取数据
    @RequestMapping("getKeyUpDate")
    @ResponseBody
    public HashMap<String,Object> getKeyUpDate(String floor,String lineName,String flag) throws ParseException {
        int waitJob = 0;//待派工
        int inMaintenance = 0; //维修中
        int Maintenance = 0; //已维修
        int H = 0; //优先级都为‘H’


        DateFormat Timeformat = new SimpleDateFormat("HH:mm:ss");
        Date testDate = null;
        String errorEndTime = "";

        ArrayList<String> time = getTime();
        String startTime = time.get(0);
        String endTime = time.get(1);

//        System.out.println(floor+":"+lineName+":"+startTime+":"+endTime);

        Date date = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(endTime);

        //存储所有数据
        ArrayList<ArrayList<String>> arrayLists = new ArrayList<>();

        ArrayList<AutoFloorKeyCheckup> keyUpData = autoFloorKeyCheckupService.getKeyUpData(startTime, endTime, floor, lineName,flag);
        for (AutoFloorKeyCheckup keyUpDatum : keyUpData) {
            ArrayList<String> keyDataList = new ArrayList<>();

                keyDataList.add(keyUpDatum.getFloor());
                keyDataList.add(keyUpDatum.getLinename());
                keyDataList.add(keyUpDatum.getMachinetype());
                keyDataList.add("");

                String keyVal = keyUpDatum.getKeyVal();
                try {
                    Double valDouble = Double.parseDouble(keyVal);
                    BigDecimal valDoublebig=new BigDecimal(valDouble);
                    valDouble =valDoublebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                    keyVal = valDouble+"";
                } catch (Exception e) {
                    keyVal = "";
                }

                keyDataList.add(keyUpDatum.getKeyPmsCh() + ":" +keyVal+"(Spec:" + keyUpDatum.getSpec() + ")" );
                keyDataList.add("");
                keyDataList.add("H");


                //异常开始时间
                String testTime = keyUpDatum.getTesttime();
                if (testTime!=null&&!"".equals(testTime)){
                    try {
                        testDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(testTime);
                    } catch (ParseException e) {
                        testDate =null;
                    }
                    if (testDate!=null){
                        keyUpDatum.setTesttime(Timeformat.format(testDate));
                    }

                }
                keyDataList.add(keyUpDatum.getTesttime());

                //异常结束时间
                Date repairEndTime = keyUpDatum.getRepairEndTime();
                if (repairEndTime!=null){
                    errorEndTime = Timeformat.format(repairEndTime);
                    keyDataList.add(errorEndTime);
                }
                //异常时长
                if (testDate!=null){
                    long StartTime=testDate.getTime();
                    double Dispose = 0.0;
                    if(errorEndTime!="" && errorEndTime != null){
                        Date date1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(errorEndTime);
                         Dispose =(double)(date1.getTime()-StartTime)/1000;

                    }
                    else{
                         Dispose =(double)(date.getTime()-StartTime)/1000;
                    }
                    BigDecimal DisposeBig=new BigDecimal(Dispose/60); //异常时间取分钟数
                    int DisposeTime = DisposeBig.setScale(0,BigDecimal.ROUND_HALF_UP).intValue(); //四舍五入方法取值
                    if (DisposeTime<0){DisposeTime=0;}
                    keyDataList.add(DisposeTime+"");
                }
                keyDataList.add("EE");
                keyDataList.add(keyUpDatum.getRepairContent());
                switch (keyUpDatum.getFlag()){
                    case 0:
                        keyDataList.add("待派工");
                        waitJob++;
                        break;
                    case 1:
                        keyDataList.add("已完成");
                        Maintenance++;
                        break;
                    case 2:
                        keyDataList.add("维修中");
                        inMaintenance++;
                        break;
                    default:
                        break;
                }

            keyDataList.add(keyUpDatum.getId()+"");

            arrayLists.add(keyDataList);
        }

        H = waitJob+Maintenance+inMaintenance;

        ArrayList<Integer> integers = new ArrayList<>();
        integers.add(waitJob);
        integers.add(inMaintenance);
        integers.add(Maintenance);
        integers.add(H);

        HashMap<String, Object> map = new HashMap<>();



        map.put("arrayLists",arrayLists);
        map.put("integers",integers);



        return map;
    }


    //维修
    @RequestMapping("updateErrorMsg")
    @ResponseBody
    public boolean updateErrorMsg(String id,String start,String content){
//
        String sqlDate = autofloorTargetService.getSqlDate();

        boolean b = autoFloorKeyCheckupService.updateErrorMsg(start, sqlDate, content, id);

        AutoFloorKeyCheckup updataId = autoFloorKeyCheckupService.getUpdataId(id);

        AutofloorKeyCheckupHis autofloorKeyCheckupHis = new AutofloorKeyCheckupHis();



            autofloorKeyCheckupHis.setProduct(updataId.getProduct());
            autofloorKeyCheckupHis.setFloor(updataId.getFloor());
            autofloorKeyCheckupHis.setLinename(updataId.getLinename());
            autofloorKeyCheckupHis.setWorkstation(updataId.getWorkstation());
            autofloorKeyCheckupHis.setMachinetype(updataId.getMachinetype());
            autofloorKeyCheckupHis.setKeyPmsEn(updataId.getKeyPmsCh());
            autofloorKeyCheckupHis.setKeyPmsCh(updataId.getKeyPmsCh());
            autofloorKeyCheckupHis.setSpec(updataId.getSpec());
            autofloorKeyCheckupHis.setSpecMin(updataId.getSpecMin());
            autofloorKeyCheckupHis.setSpecMax(updataId.getSpecMax());
            autofloorKeyCheckupHis.setKeyVal(updataId.getKeyVal());
            autofloorKeyCheckupHis.setCheckResult(updataId.getCheckResult());
            autofloorKeyCheckupHis.setTesttime(updataId.getTesttime());
            autofloorKeyCheckupHis.setUpdateTime(updataId.getUpdateTime());
            autofloorKeyCheckupHis.setComponenttype(updataId.getComponenttype());
            autofloorKeyCheckupHis.setFlag(updataId.getFlag());
            autofloorKeyCheckupHis.setRepairContent(updataId.getRepairContent());
            autofloorKeyCheckupHis.setRepairEndTime(updataId.getRepairEndTime());



        int i = autofloorKeyCheckupHisMapper.insertKeyData(autofloorKeyCheckupHis);

//        System.out.println(i);


        return b;
    }

}
