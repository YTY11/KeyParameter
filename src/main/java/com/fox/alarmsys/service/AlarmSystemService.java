package com.fox.alarmsys.service;


import com.fox.alarmsys.entity.AlarmSystemTitle;
import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.alarmsys.mapper.Floor_Hermes_TargetMapper;
import com.fox.qualitysys.entity.AutoFloor_Key_CheCkUp;
import com.fox.qualitysys.mapper.AutoFloor_Key_CheCkUpMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author wu
 * @create 2020-06-18 18:26
 * @date 2020/10/14
 */
@Service
public class AlarmSystemService {

    @Autowired
    AutoFloor_Key_CheCkUpMapper autoFloor_key_cheCkUpMapper;

    @Autowired
    AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;
    @Autowired
    private Floor_Hermes_TargetMapper floor_hermes_targetMapper;
    public static final DateFormat Timeformat = new SimpleDateFormat("HH:mm:ss");

    private final  DateFormat df2=new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");

    Date testDate = null;

    //关键参数
    public List<AutoFloor_Key_CheCkUp>  AlarmSystemData(String FloorName, String StartDate, String EndDate, String PriorityOrFlg) {
        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> LowautoFloor_key_checkNGS =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> AccomplishFloor_key_check =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> LowAccomplishFloor_key_check =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> autoFloor_key_cheCkUps = autoFloor_key_cheCkUpMapper.selectNGMessage(FloorName, StartDate, EndDate);
        Date date = autoFloor_test_exceptionMapper.DBDate();//異常時長
        for (AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp : autoFloor_key_cheCkUps) {
            String keyVal = autoFloor_key_cheCkUp.getKeyVal();
            Boolean aBoolean = ValStrList(keyVal);
            if (aBoolean){
                Double valDouble = Double.parseDouble(keyVal);
                BigDecimal valDoublebig=new BigDecimal(valDouble);
                valDouble =valDoublebig.setScale(2,BigDecimal.ROUND_HALF_UP).doubleValue();
                autoFloor_key_cheCkUp.setKeyVal(valDouble+"");
            }
            //异常开始时间
            String testtime = autoFloor_key_cheCkUp.getTesttime();
            if (testtime!=null&&!"".equals(testtime)){
                try {
                    testDate = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").parse(testtime);
                } catch (ParseException e) {
                    testDate =null;
                }
                if (testDate!=null){
                    autoFloor_key_cheCkUp.setTesttime(Timeformat.format(testDate));
                }

            }
            //异常結束时间
            Date repairEndTime = autoFloor_key_cheCkUp.getRepairEndTime();
            if (repairEndTime!=null){autoFloor_key_cheCkUp.setStrupdateTime(Timeformat.format(repairEndTime));}
            //异常时长
            if (testDate!=null){
                long StartTime=testDate.getTime();
                double Dispose =(double)(date.getTime()-StartTime)/1000;
                BigDecimal DisposeBig=new BigDecimal(Dispose/60); //异常时间取分钟数
                int DisposeTime =DisposeBig.setScale(0,BigDecimal.ROUND_HALF_UP).intValue(); //四舍五入方法取值
                if (DisposeTime<0){DisposeTime=0;}
                autoFloor_key_cheCkUp.setDisposeTime(DisposeTime);
            }
            //关键参数等级 全为H
            String priority =  "H";
            Integer flag =  autoFloor_key_cheCkUp.getFlag();
            //summary 异步点击加载tab内容
            if ("".equals(PriorityOrFlg)){
                SortOrder(autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
            }else {
                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
            }
            }
            //按照 H/M/L 排序  维修完成的排在最后
            autoFloor_key_checkNGS.addAll(LowautoFloor_key_checkNGS);
            autoFloor_key_checkNGS.addAll(AccomplishFloor_key_check);
            autoFloor_key_checkNGS.addAll(LowAccomplishFloor_key_check);

            return autoFloor_key_checkNGS;
    }

    //Test
    public List<AutoFloor_Key_CheCkUp>  AlarmSystemTestTabData(String FloorName,String StartDate,String EndDate,String Type ,String Product,List<String> DisableException_TypeList,String MachineType,String PriorityOrFlg,String LineName){
        List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS =new ArrayList<>();
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions = new ArrayList<>();
        autoFloor_test_exceptions = getAutoFloor_test_exceptions(FloorName, StartDate, EndDate, Type, DisableException_TypeList, LineName);

            //PageRequest pageRequest=new PageRequest();
            //pageRequest.setPageSize(Integer.parseInt("pageSize"));
            //pageRequest.setPageNum(Integer.parseInt("pageNum"));
            //PageHelper.startPage(pageRequest.getPageNum(), pageRequest.getPageSize());
            //List<RiskUser> riskUserList=riskUserService.getUserByCompanyId(17);
            //PageResult pageResult= PageUtils.getPageResult(new PageInfo<>(riskUserList));



        List<AutoFloor_Key_CheCkUp> LowautoFloor_key_checkNGS =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> AccomplishFloor_key_check =new ArrayList<>();
        List<AutoFloor_Key_CheCkUp> LowAccomplishFloor_key_check =new ArrayList<>();
        Date date = autoFloor_test_exceptionMapper.DBDate();//異常時長
        for (int i = 0; i < autoFloor_test_exceptions.size(); i++) {
            AutoFloor_Test_Exception autoFloor_test_exception = autoFloor_test_exceptions.get(i);
            AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp=new AutoFloor_Key_CheCkUp();
            String exceptionType = autoFloor_test_exception.getExceptionType();
            String floor = autoFloor_test_exception.getFloor();
            String linename = autoFloor_test_exception.getLinename();
            String machine = autoFloor_test_exception.getMachine();
            String exceptionDescribe = autoFloor_test_exception.getExceptionDescribe();
            Date ExceptionTime = autoFloor_test_exception.getExceptionTime();
            String priority = autoFloor_test_exception.getPriority();
            Integer flag = autoFloor_test_exception.getFlag();
            BigDecimal id = autoFloor_test_exception.getId();
            Date updateTime = autoFloor_test_exception.getUpdateTime();
            String repairPropose = autoFloor_test_exception.getRepairPropose();
            String repairContent = autoFloor_test_exception.getRepairContent();
            String empName = autoFloor_test_exception.getEmpName();
            if ("K051FARF08".equals(LineName)){ linename=LineName; }
            String Responsibilities="TE";
            if ("Equipment".equals(Product)){ //Test线体显示需要截取字符串
                if (linename!=null&&linename.length()>=5){
                    linename=linename.substring(5);}//截取线体长度，不显示线体的楼层名称和Cellz
                //設備號 英文對照中文修改
                if (linename!=null&&!"".equals(linename)){
                    machine = EquipmentTranslation(exceptionType, linename, machine);}

            }else  {

            }

            Responsibilities = getResponsibilities(FloorName, Responsibilities);
            autoFloor_key_cheCkUp.setResponsibilities(Responsibilities);
            autoFloor_key_cheCkUp.setFlag(flag);
            autoFloor_key_cheCkUp.setFloor(floor);
            autoFloor_key_cheCkUp.setLinename(linename);
            autoFloor_key_cheCkUp.setMachinetype(machine);
            autoFloor_key_cheCkUp.setKeyPmsCh(exceptionDescribe);
            String testTime = Timeformat.format(ExceptionTime);
            String endTime="";
            if ("FacilityManager".equals(Product)){
                testTime = df2.format(ExceptionTime);
            }
            autoFloor_key_cheCkUp.setTesttime(testTime);
            autoFloor_key_cheCkUp.setProduct(priority);
            exceptionType = ExceptionTypeReplace(exceptionType);
            autoFloor_key_cheCkUp.setExceptionType(exceptionType);
            autoFloor_key_cheCkUp.setId(id);
            autoFloor_key_cheCkUp.setRepairPropose(repairPropose);
            autoFloor_key_cheCkUp.setRepairContent(repairContent);
            autoFloor_key_cheCkUp.setEmpName(empName);
            long Actiontime = ExceptionTime.getTime();//異常開始時間
            long EndTime = date.getTime();//異常時長
            if (flag==1){
                //flag為1時異常結束 異常時長時間為update時間
                if (updateTime!=null){
                    EndTime =updateTime.getTime();
                    autoFloor_key_cheCkUp.setStrupdateTime(Timeformat.format(updateTime));
                }
            }
            if ("SKASandAFS".equals(Product)){ //SKAS&AFS显示需要截取字符串
                int length = exceptionType.length();
                if (length>=3){
                    exceptionType=exceptionType.substring(0,3);
                }
            }
            double Dispose =(double)(EndTime-Actiontime)/1000;
            BigDecimal DisposeBig=new BigDecimal(Dispose/60); //异常时间取分钟数
            int DisposeTime =DisposeBig.setScale(0,BigDecimal.ROUND_HALF_UP).intValue(); //四舍五入方法取值
            if (DisposeTime<0){DisposeTime=0;}
            autoFloor_key_cheCkUp.setDisposeTime(DisposeTime);  //Flag  1=维修完成 0=发生异常  2=维修中
            Boolean NormTimeBoolean = RobotNormTime(exceptionType, flag, updateTime, Actiontime); //判断Robot 异常时间是否超出||5||秒;
            if ("".equals(PriorityOrFlg) && "".equals(MachineType)){
                //排序方式
                if("Robot".equals(exceptionType)){
                    if (NormTimeBoolean){
                        SortOrder(autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                    }
                }else {
                    SortOrder(autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
            }else {
                //summary 异步点击加载tab内容
                if ("Equipment".equals(Product)){//Test设备运行
                    switch (MachineType){
                        case "Robot":
                            if ("Robot".equals(exceptionType)||"Robot_SW".equals(exceptionType)){
                                //排序方式
                                if (NormTimeBoolean){
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                            }
                            break;
                        case "治具":
                            if ("治具".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "工站連接":
                            if ("工站連接".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "ALL":
                            //排序方式
                            if ("Robot".equals(exceptionType)){
                                //排序方式
                                if (NormTimeBoolean){
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                            }else {
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                    }
                }else if ("Equipment_SMT".equals(Product)){//SMT设备运行
                    switch (MachineType){
                        case "SMT投首":
                            if ("SMT投首".equals(exceptionType)){
                                //排序方式
                                if (NormTimeBoolean){
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                            }
                            break;
                        case "印刷機":
                            if ("PRINTER".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "SPI":
                            if ("SPI".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "貼片機":
                            if ("貼片機".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "爐前/后 AOI":
                            if ("爐前/后 AOI".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "回焊爐":
                            if ("REFLOW".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "自動化烤箱":
                            if ("自動化烤箱".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "鐳射Laber":
                            if ("鐳射Laber".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "鐳射B/C":
                            if ("鐳射B/C".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "點膠機anda":
                            if ("ANDA".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "自動化設備":
                            if ("自動化設備".equals(exceptionType)){
                                //排序方式
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            }
                            break;
                        case "ALL":
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                            break;
                    }

                }else if ("FacilityManager".equals(Product)){//廠務设备运行
                    if (MachineType != null) {
                        switch (MachineType){
                            case "能源":
                                if ("ENERGYMONITORING".equals(linename)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                            break;
                            case "環境":
                                if ("ENVIRONMENTAL".equals(linename)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                            break;
                            case "能耗":
                                if ("ENERGYCONSUMPTION".equals(linename)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                            break;
                            case "ALL":
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                break;
                        }
                    }
                }else if ("SKASandAFS".equals(Product)){//SKAS&AFS
                    if (MachineType != null) {
                        switch (MachineType){
                            case "RGV":
                                if ("RGV".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "AGV":
                                if ("AGV".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "暫存倉":
                                if ("暫存倉".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "AFS小車":
                                if ("AFS".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "ALL":
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                break;
                        }
                    }
                }else if ("Equipment_AVI".equals(Product)){//AVI
                    if (MachineType != null) {
                        switch (MachineType){
                            case "軟體":
                                if ("軟體".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "軌道":
                                if ("軌道".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "相機":
                                if ("相機".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "電腦":
                                if ("電腦".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "ALL":
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                break;
                        }
                    }
                }else if ("HotAirGuns".equals(Product)){//AVI
                    if (MachineType != null) {
                        switch (MachineType){
                            case "上溫度異常":
                                if ("上溫度異常".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "下溫度異常":
                                if ("下溫度異常".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "測量風速異常":
                                if ("測量風速異常".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "本機風速異常":
                                if ("本機風速異常".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "環境溫度異常":
                                if ("環境溫度異常".equals(exceptionType)){
                                    //排序方式
                                    SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                }
                                break;
                            case "ALL":
                                SortPriortyOrFlg(PriorityOrFlg, autoFloor_key_checkNGS, LowautoFloor_key_checkNGS, AccomplishFloor_key_check, LowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                                break;
                        }
                    }
                }
            }
        }
        //按照 H/M/L 排序  维修完成的排在最后
        autoFloor_key_checkNGS.addAll(LowautoFloor_key_checkNGS);
        autoFloor_key_checkNGS.addAll(AccomplishFloor_key_check);
        autoFloor_key_checkNGS.addAll(LowAccomplishFloor_key_check);
        return autoFloor_key_checkNGS;

    }

    public List<AutoFloor_Test_Exception> getAutoFloor_test_exceptions(String FloorName, String StartDate, String EndDate, String Type, List<String> DisableException_TypeList, String LineName) {
        List<AutoFloor_Test_Exception> autoFloor_test_exceptions;
        switch (Type){
            case "TEST":
                autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectTestExceptionList(FloorName,LineName, StartDate, EndDate,Type,DisableException_TypeList);
                autoFloor_test_exceptions.addAll(autoFloor_test_exceptionMapper.selectRobotTestExceptionList(FloorName,LineName, StartDate, EndDate,"TEST",DisableException_TypeList));
                if ("K051FARF08".equals(LineName)){
                    autoFloor_test_exceptions.addAll(autoFloor_test_exceptionMapper.selectK05CCD_Data(LineName, StartDate, EndDate));
                }
                break;
            case "SKAS":
                autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectSkasExceptionList(FloorName,LineName, StartDate, EndDate,Type,DisableException_TypeList);
                break;
            case "AVI":
                autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectSkasExceptionList(FloorName,LineName, StartDate, EndDate,Type,DisableException_TypeList);
                break;
            default:
                autoFloor_test_exceptions = autoFloor_test_exceptionMapper.selectNOTTestExceptionList(FloorName, StartDate, EndDate,Type,DisableException_TypeList);
        }
        return autoFloor_test_exceptions;
    }

    public String getResponsibilities(String FloorName, String responsibilities) {
        switch (FloorName){
            case "D061F":
                responsibilities ="TE";
                break;
            case "K051F":
                responsibilities ="AE";
                break;
        }
        return responsibilities;
    }


    public String ExceptionTypeReplace(String exceptionType) {
        //异常类型 英文替换
        switch (exceptionType){
            case "Fixtures ERROR":
                exceptionType="治具";
                break;
            case "Robot_SW":
                exceptionType="Robot";
                break;
        }
        return exceptionType;
    }


    public String EquipmentTranslation(String exceptionType, String linename, String machine) {
        if ("Robot".equals(exceptionType)||"Robot_SW".equals(exceptionType)){
            if (linename.length()>=5){
                switch (linename.substring(5)){
                    case "-P":
                        machine="Pre AP Cell";
                        break;
                    case "-A":
                        machine="Post AP Cell";
                        break;
                    case "-R":
                        machine="Router Cell";
                        break;
                    case "-B":
                        machine="RF Cell01";
                        break;
                    case "-C":
                        machine="RF Cell02";
                        break;
                    case "-D":
                        machine="RF Cell03";
                        break;
                    case "-E":
                        machine="RF Cell04";
                        break;
                    case "-F":
                        machine="RF Cell05";
                        break;
                    case "-G":
                        machine="RF Cell06";
                        break;
                    case "-H":
                        machine="RF Cell07";
                        break;
                    case "-J":
                        machine="RF Cell08";
                        break;
                    case "-PD":
                        machine="PD Cell";
                        break;
                }
            }
        }else if ("工站連接".equals(exceptionType)){
                switch (machine){
                    case "MODULE_ROUTE":
                        if ("ARF08".equals(linename)){
                            machine="多功能上下料機";
                        }else {
                            machine="懸臂模組";
                        }

                        break;
                    case "MODULE_NGBoard":
                        machine="不良板收集機";
                        break;
                    case "POST_AP_YATIAOJI":
                        machine="取壓條機";
                        break;
                    case "PRE_AP_YATIAOJI":
                        machine="Pre_ap取壓條機";
                        break;
                    case "PRE_AP_LOADER":
                        machine="Pre_ap推收板機";
                        break;
                    case "POST_AP_LOADER":
                        machine="Post_ap推收板機";
                        break;
                    case "MODULE_PD":
                        machine="PD模組";
                        break;
                    case "MODULE_CELL-H":
                        machine="RFCell07模組";
                        break;
                    case "MODULE_CELL-G":
                        machine="RFCell06模組";
                        break;
                    case "MODULE_CELL-F":
                        machine="RFCell05模組";
                        break;
                    case "MODULE_CELL-E":
                        machine="RFCell04模組";
                        break;
                    case "MODULE_CELL-D":
                        machine="RFCell03模組";
                        break;
                    case "MODULE_CELL-C":
                        machine="RFCell02模組";
                        break;
                    case "MODULE_CELL-B":
                        machine="RFCell01模組";
                        break;
                    case "CCD_Measure":
                        machine="CCD模組";
                        break;
                    case "CCD_ASSORT":
                        machine="CCD分板模組";
                        break;
                    case "CCD1":
                        machine="綫首CCD分板模組";
                        break;
                    case "CCD2":
                        machine="綫中CCD分板模組";
                        break;
                    case "CCD3":
                        machine="綫尾CCD分板模組";
                        break;
                }
        }
        return machine;
    }


    public Boolean RobotNormTime(String exceptionType, Integer flag, Date updateTime, long actiontime) {
        Boolean NormTime=false;
            if (flag==1){
                long time = updateTime.getTime()- actiontime;
                time/=1000;
                if (time>5){
                    NormTime=true;
                }
            }else {
                NormTime=true;
            }
        return NormTime;
    }


    public void SortPriortyOrFlg(String PriorityOrFlg, List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> lowautoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> accomplishFloor_key_check, List<AutoFloor_Key_CheCkUp> lowAccomplishFloor_key_check, AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp, String priority, Integer flag) {

        switch (PriorityOrFlg){
            case  "H":
                if ("H".equals(priority)){
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "M":
                if ("M".equals(priority)){
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "L":
                if ("L".equals(priority)){
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "0":
            case "1":
            case "2":
                int Flag= Integer.parseInt(PriorityOrFlg);
                if (Flag==flag){
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                }
                break;
            case "ALL":
                    //排序方式
                    SortOrder(autoFloor_key_checkNGS, lowautoFloor_key_checkNGS, accomplishFloor_key_check, lowAccomplishFloor_key_check, autoFloor_key_cheCkUp, priority, flag);
                    break;
        }
    }


    public void SortOrder(List<AutoFloor_Key_CheCkUp> autoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> lowautoFloor_key_checkNGS, List<AutoFloor_Key_CheCkUp> accomplishFloor_key_check, List<AutoFloor_Key_CheCkUp> lowAccomplishFloor_key_check, AutoFloor_Key_CheCkUp autoFloor_key_cheCkUp, String priority, Integer flag) {
        if ("L".equals(priority) && flag != 1) {   //异常等级为L 且没有维修完的List
            lowautoFloor_key_checkNGS.add(autoFloor_key_cheCkUp);
        } else if (flag == 1) {
            if ("L".equals(priority)) {
                lowAccomplishFloor_key_check.add(autoFloor_key_cheCkUp);
            } else {
                accomplishFloor_key_check.add(autoFloor_key_cheCkUp);
            }
        } else if (flag != 1) {
            autoFloor_key_checkNGS.add(autoFloor_key_cheCkUp);
        }
    }

   // Test
    public List<AlarmSystemTitle> AlarmSystemTitleData(List<AutoFloor_Test_Exception> autoFloor_test_exceptions, String Product){
        List<String> titleTypeList = titleTypeList(Product);//根據模塊種類顯示title內容
        List<AlarmSystemTitle> AlarmSystemTitleList=new ArrayList<>();
        int ALLWaitDispatchNum=0;//代派工數量
        int ALLUnderRepairNum=0;// 修理中數量
        int ALLRepairCompleteNum=0;// 修理完成數量
        int ALLMachineExALL=0;//機故ALL數量
        int ALLMachineExHNum=0;//機故H數量
        int ALLMachineExMNum=0;//機故M數量
        int ALLMachineExLNum=0;//機故L數量
        for (String title : titleTypeList) {
            AlarmSystemTitle alarmSystemTitle=new AlarmSystemTitle();
            //賦值機故類型
            alarmSystemTitle.setMachineExType(title);
            int WaitDispatchNum=0;//代派工數量
            int UnderRepairNum=0;// 修理中數量
            int RepairCompleteNum=0;// 修理完成數量
            int MachineExALL=0;//機故ALL數量
            int MachineExHNum=0;//機故H數量
            int MachineExMNum=0;//機故M數量
            int MachineExLNum=0;//機故L數量
            List<String> TitleException_TypeList = TestException_Type(Product,title);//Titil所對應的異常
            for (AutoFloor_Test_Exception autoFloor_test_exception : autoFloor_test_exceptions) {
                String linename = autoFloor_test_exception.getLinename();
                String exceptionType = autoFloor_test_exception.getExceptionType();
                if ("SKASandAFS".equals(Product)){ //SKAS&AFS显示需要截取字符串
                    int length = exceptionType.length();
                    if (length>=3){
                        exceptionType=exceptionType.substring(0,3);
                    }
                }
                for (String Exception : TitleException_TypeList) {//判讀異常相同
                    if (Exception!=null&&Exception.equals(linename)){
                        // FAC判断线体与异常明细相同
                        Integer flag = autoFloor_test_exception.getFlag();
                        String priority = autoFloor_test_exception.getPriority();
                        switch (flag){//維修狀態
                            case 0:
                                WaitDispatchNum++;
                                break;
                            case 1:
                                RepairCompleteNum++;
                                break;
                            case 2:
                                UnderRepairNum++;
                                break;
                        }

                        switch (priority){//異常等級
                            case "H":
                                MachineExHNum++;
                                break;
                            case "M":
                                MachineExMNum++;
                                break;
                            case "L":
                                MachineExLNum++;
                                break;
                        }


                    }else {
                        // TEST判讀異常与异常明细相同
                        if (exceptionType!=null&&exceptionType.equals(Exception)){
                            Date updateTime = autoFloor_test_exception.getUpdateTime();
                            long Actiontime= autoFloor_test_exception.getExceptionTime().getTime();
                            Integer flag = autoFloor_test_exception.getFlag();
                            String priority = autoFloor_test_exception.getPriority();
                            Boolean NormTimeBoolean = RobotNormTime(exceptionType, flag, updateTime, Actiontime); //判断Robot 异常时间是否超出||5||秒;
                            if ("Robot".equals(exceptionType)){ //判断Robot 的异常时间长度是否超出5秒 超出记录下来 没超出舍弃掉；
                                if (NormTimeBoolean){
                                    switch (flag){//維修狀態
                                        case 0:
                                            WaitDispatchNum++;
                                            break;
                                        case 1:
                                            RepairCompleteNum++;
                                            break;
                                        case 2:
                                            UnderRepairNum++;
                                            break;
                                    }

                                    switch (priority){//異常等級
                                        case "H":
                                            MachineExHNum++;
                                            break;
                                        case "M":
                                            MachineExMNum++;
                                            break;
                                        case "L":
                                            MachineExLNum++;
                                            break;
                                    }
                                }
                            }else {
                                switch (flag){//維修狀態
                                    case 0:
                                        WaitDispatchNum++;
                                        break;
                                    case 1:
                                        RepairCompleteNum++;
                                        break;
                                    case 2:
                                        UnderRepairNum++;
                                        break;
                                }

                                switch (priority){//異常等級
                                    case "H":
                                        MachineExHNum++;
                                        break;
                                    case "M":
                                        MachineExMNum++;
                                        break;
                                    case "L":
                                        MachineExLNum++;
                                        break;
                                }
                            }
                        }
                    }

                }
            }
            MachineExALL=MachineExHNum+MachineExMNum+MachineExLNum;
            //ALL的加總
            ALLWaitDispatchNum+=WaitDispatchNum;
            ALLUnderRepairNum+=UnderRepairNum;
            ALLRepairCompleteNum+=RepairCompleteNum;
            ALLMachineExALL+=MachineExALL;
            ALLMachineExHNum+=MachineExHNum;
            ALLMachineExMNum+=MachineExMNum;
            ALLMachineExLNum+=MachineExLNum;

            alarmSystemTitle.setWaitDispatchNum(WaitDispatchNum);
            alarmSystemTitle.setUnderRepairNum(UnderRepairNum);
            alarmSystemTitle.setRepairCompleteNum(RepairCompleteNum);
            alarmSystemTitle.setMachineExALL(MachineExALL);
            alarmSystemTitle.setMachineExHNum(MachineExHNum);
            alarmSystemTitle.setMachineExMNum(MachineExMNum);
            alarmSystemTitle.setMachineExLNum(MachineExLNum);
            AlarmSystemTitleList.add(alarmSystemTitle);
        }
        AlarmSystemTitle ALLalarmSystemTitle=new AlarmSystemTitle();
        //        ALL的數據
        ALLalarmSystemTitle.setMachineExType("ALL");
        ALLalarmSystemTitle.setWaitDispatchNum(ALLWaitDispatchNum);
        ALLalarmSystemTitle.setUnderRepairNum(ALLUnderRepairNum);
        ALLalarmSystemTitle.setRepairCompleteNum(ALLRepairCompleteNum);
        ALLalarmSystemTitle.setMachineExALL(ALLMachineExALL);
        ALLalarmSystemTitle.setMachineExHNum(ALLMachineExHNum);
        ALLalarmSystemTitle.setMachineExMNum(ALLMachineExMNum);
        ALLalarmSystemTitle.setMachineExLNum(ALLMachineExLNum);
        AlarmSystemTitleList.add(ALLalarmSystemTitle);
        return AlarmSystemTitleList;
    }

    // 關鍵參數
    public List<AlarmSystemTitle> AlarmSystemKeyTitleData(List<AutoFloor_Key_CheCkUp> autoFloor_test_exceptions,String Product){
        List<AlarmSystemTitle> AlarmSystemTitleList=new ArrayList<>();
            int WaitDispatchNum=0;//代派工數量
            int UnderRepairNum=0;// 修理中數量
            int RepairCompleteNum=0;// 修理完成數量
            int MachineExALL=0;//機故ALL數量
            AlarmSystemTitle ALLalarmSystemTitle=new AlarmSystemTitle();
            for (AutoFloor_Key_CheCkUp autoFloor_test_exception : autoFloor_test_exceptions) {
                    Integer flag = autoFloor_test_exception.getFlag();
                        switch (flag){//維修狀態
                            case 0:
                                WaitDispatchNum++;
                                break;
                            case 1:
                                RepairCompleteNum++;
                                break;
                            case 2:
                                UnderRepairNum++;
                                break;
                        }
            }
        MachineExALL=WaitDispatchNum+RepairCompleteNum+UnderRepairNum;
        //        ALL的數據
        ALLalarmSystemTitle.setMachineExType("ALL");
        ALLalarmSystemTitle.setWaitDispatchNum(WaitDispatchNum);
        ALLalarmSystemTitle.setUnderRepairNum(RepairCompleteNum);
        ALLalarmSystemTitle.setRepairCompleteNum(UnderRepairNum);
        ALLalarmSystemTitle.setMachineExALL(MachineExALL);
        AlarmSystemTitleList.add(ALLalarmSystemTitle);
        return AlarmSystemTitleList;
    }

    public Boolean ValStrList(String val) {
        Boolean ValStr = null;
        try {
            Double.parseDouble(val);
            ValStr = true;
        } catch (Exception e) {
            ValStr = false;
        }
        return ValStr;
    }

    //禁止出現的異常類型
    public List<String> DisableException_Type(String Product){
        List<String> DisableException_TypeList=new ArrayList<>();
        switch (Product){
            case "Equipment":
                DisableException_TypeList.add("SLANT");
                DisableException_TypeList.add("MACH_YIELD");
                DisableException_TypeList.add("WAIT");
                DisableException_TypeList.add("CELL_UPH_SUBSTANDARD");
                DisableException_TypeList.add("RETEST_TIMEOUT");
                break;
        }

        return DisableException_TypeList;
    }

    public Map<String, String> AlarmProjectMap(){
        Map<String,String> AlarmProjectMap=new HashMap<>();
        AlarmProjectMap.put("设备运行SKAS&AFS","SKASandAFS");
        AlarmProjectMap.put("设备运行SMT","Equipment_SMT");
        AlarmProjectMap.put("设备运行Test","Equipment");
        AlarmProjectMap.put("设备运行AVI","Equipment_AVI");
        AlarmProjectMap.put("廠務","FacilityManager");
        AlarmProjectMap.put("關鍵參數","KeyParameter");
        AlarmProjectMap.put("熱風槍","HotAirGuns");
        return AlarmProjectMap;
    }

    //summary类型种类
    public List<String> titleTypeList(String Product){
        List<String> titleTypeList=new ArrayList();
        switch (Product){
            case "KeyParameter":
                break;
            case "Equipment":
                titleTypeList.add("Robot");
                titleTypeList.add("治具");
                titleTypeList.add("工站連接");
                break;
            case "FacilityManager":
                titleTypeList.add("能源");
                titleTypeList.add("環境");
                titleTypeList.add("能耗");
                break;
            case "Equipment_SMT":
                titleTypeList.add("SMT投首");
                titleTypeList.add("印刷機");
                titleTypeList.add("SPI");
                titleTypeList.add("貼片機");
                titleTypeList.add("爐前/后 AOI");
                titleTypeList.add("回焊爐");
                titleTypeList.add("自動化烤箱");
                titleTypeList.add("鐳射Laber");
                titleTypeList.add("鐳射B/C");
                titleTypeList.add("點膠機anda");
                titleTypeList.add("自動化設備");
                break;
            case "SKASandAFS":
                titleTypeList.add("RGV");
                titleTypeList.add("AGV");
                titleTypeList.add("暫存倉");
                titleTypeList.add("AFS小車");
                break;
            case "Equipment_AVI":
                titleTypeList.add("軟體");
                titleTypeList.add("軌道");
                titleTypeList.add("相機");
                titleTypeList.add("電腦");
                break;
            case "HotAirGuns":
                titleTypeList.add("上溫度異常");
                titleTypeList.add("下溫度異常");
                titleTypeList.add("測量風速異常");
                titleTypeList.add("本機風速異常");
                titleTypeList.add("環境溫度異常");
                break;
        }
        return titleTypeList;
    }
    //summary类型种类对应异常
    public List<String> TestException_Type(String Product,String Type){
        List<String> Exception_TypeList =new ArrayList<>();
        if ("Equipment".equals(Product)){
            switch (Type){
                case "Robot":
                    Exception_TypeList.add("SLANT");
                    Exception_TypeList.add("SLANT_PAUSE");
                    Exception_TypeList.add("Robot");
                    Exception_TypeList.add("Robot_SW");
                    break;
                case "治具":
                    Exception_TypeList.add("TXRX ERROR");
                    Exception_TypeList.add("SW ERROR");
                    Exception_TypeList.add("TopItem ERROR");
                    Exception_TypeList.add("EQ_1WK ERROR");
                    Exception_TypeList.add("EQ_2WK ERROR");
                    Exception_TypeList.add("Pathloss ERROR");
                    Exception_TypeList.add("TOPX ERROR");
                    Exception_TypeList.add("Audit ERROR");
                    Exception_TypeList.add("治具");
                    Exception_TypeList.add("Fixtures ERROR");
                    break;
                case "工站連接":
                    Exception_TypeList.add("NOW_ERROR");
                    Exception_TypeList.add("CCD");
                    Exception_TypeList.add("工站連接");
                    Exception_TypeList.add("MODEL_SW");
                    break;
                case "ALL":
                    Exception_TypeList.add("SLANT");
                    Exception_TypeList.add("SLANT_PAUSE");
                    Exception_TypeList.add("Robot");
                    Exception_TypeList.add("Robot_SW");
                    Exception_TypeList.add("TXRX ERROR");
                    Exception_TypeList.add("SW ERROR");
                    Exception_TypeList.add("TopItem ERROR");
                    Exception_TypeList.add("EQ_1WK ERROR");
                    Exception_TypeList.add("EQ_2WK ERROR");
                    Exception_TypeList.add("Pathloss ERROR");
                    Exception_TypeList.add("TOPX ERROR");
                    Exception_TypeList.add("Audit ERROR");
                    Exception_TypeList.add("治具");
                    Exception_TypeList.add("Fixtures ERROR");
                    Exception_TypeList.add("NOW_ERROR");
                    Exception_TypeList.add("CCD");
                    Exception_TypeList.add("工站連接");
                    Exception_TypeList.add("MODEL_SW");
                    break;
            }
        }else if ("Equipment_SMT".equals(Product)){
            switch (Type){
                case "SMT投首":
                    Exception_TypeList.add("SMT投首");
                    break;
                case "印刷機":
                    Exception_TypeList.add("PRINTER");
                    break;
                case "SPI":
                    Exception_TypeList.add("SPI");
                    break;
                case "貼片機":
                    Exception_TypeList.add("貼片機");
                    break;
                case "爐前/后 AOI":
                    Exception_TypeList.add("爐前/后 AOI");
                    break;
                case "回焊爐":
                    Exception_TypeList.add("REFLOW");
                    break;
                case "自動化烤箱":
                    Exception_TypeList.add("自動化烤箱");
                    break;
                case "鐳射Laber":
                    Exception_TypeList.add("鐳射Laber");
                    break;
                case "鐳射B/C":
                    Exception_TypeList.add("鐳射B/C");
                    break;
                case "點膠機anda":
                    Exception_TypeList.add("點膠機anda");
                    Exception_TypeList.add("ANDA");
                    break;
                case "自動化設備":
                    Exception_TypeList.add("自動化設備");
                    break;
                case "ALL":
                    Exception_TypeList.add("SMT投首");
                    Exception_TypeList.add("PRINTER");
                    Exception_TypeList.add("SPI");
                    Exception_TypeList.add("貼片機");
                    Exception_TypeList.add("爐前/后 AOI");
                    Exception_TypeList.add("REFLOW");
                    Exception_TypeList.add("自動化烤箱");
                    Exception_TypeList.add("鐳射Laber");
                    Exception_TypeList.add("鐳射B/C");
                    Exception_TypeList.add("點膠機anda");
                    Exception_TypeList.add("ANDA");
                    Exception_TypeList.add("自動化設備");
                    break;
            }

        } else if ("FacilityManager".equals(Product)){
            switch (Type) {
                case "能源":
                    Exception_TypeList.add("ENERGYMONITORING");
                    break;
                case "環境":
                    Exception_TypeList.add("ENVIRONMENTAL");
                    break;
                case "能耗":
                    Exception_TypeList.add("ENERGYCONSUMPTION");
                    break;
                case "ALL":
                    Exception_TypeList.add("ENERGYMONITORING");
                    Exception_TypeList.add("ENVIRONMENTAL");
                    Exception_TypeList.add("ENERGYCONSUMPTION");
                    break;
            }
        } else if ("SKASandAFS".equals(Product)){
            switch (Type) {
                case "RGV":
                    Exception_TypeList.add("RGV");
                    break;
                case "AGV":
                    Exception_TypeList.add("AGV");
                    break;
                case "暫存倉":
                    Exception_TypeList.add("暫存倉");
                    break;
                case "AFS小車":
                    Exception_TypeList.add("AFS");
                    break;
                case "ALL":
                    Exception_TypeList.add("RGV");
                    Exception_TypeList.add("AGV");
                    Exception_TypeList.add("暫存倉");
                    Exception_TypeList.add("AFS");
                    break;
            }
        } else if ("Equipment_AVI".equals(Product)){
            switch (Type) {
                case "軟體":
                    Exception_TypeList.add("軟體");
                    break;
                case "軌道":
                    Exception_TypeList.add("軌道");
                    break;
                case "相機":
                    Exception_TypeList.add("相機");
                    break;
                case "電腦":
                    Exception_TypeList.add("電腦");
                    break;
                case "ALL":
                    Exception_TypeList.add("軟體");
                    Exception_TypeList.add("軌道");
                    Exception_TypeList.add("相機");
                    Exception_TypeList.add("電腦");
                    break;
            }
        } else if ("HotAirGuns".equals(Product)){
            switch (Type) {
                case "上溫度異常":
                    Exception_TypeList.add("上溫度異常");
                    break;
                case "下溫度異常":
                    Exception_TypeList.add("下溫度異常");
                    break;
                case "測量風速異常":
                    Exception_TypeList.add("測量風速異常");
                    break;
                case "本機風速異常":
                    Exception_TypeList.add("本機風速異常");
                    break;
                case "環境溫度異常":
                    Exception_TypeList.add("環境溫度異常");
                    break;
                case "ALL":
                    Exception_TypeList.add("上溫度異常");
                    Exception_TypeList.add("下溫度異常");
                    Exception_TypeList.add("測量風速異常");
                    Exception_TypeList.add("本機風速異常");
                    Exception_TypeList.add("環境溫度異常");
                    break;
            }
        }


        return Exception_TypeList;
    }

}




