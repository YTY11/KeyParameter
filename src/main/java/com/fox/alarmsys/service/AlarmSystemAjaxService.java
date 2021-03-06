package com.fox.alarmsys.service;


import com.fox.alarmsys.entity.AutoFloor_Test_Exception;
import com.fox.alarmsys.mapper.AutoFloor_Test_ExceptionMapper;
import com.fox.avisys.entity.AutoFloor_AVI_Employee;
import com.fox.avisys.entity.AutoFloor_AVI_Maintain_Record;
import com.fox.avisys.mapper.AutoFloor_AVI_EmployeeMapper;
import com.fox.avisys.mapper.AutoFloor_AVI_Maintain_RecordMapper;
import org.apache.ibatis.annotations.Param;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author
 * @create 2020-11-19 17:47
 */
@Service
public class AlarmSystemAjaxService {
    @Autowired
    private AutoFloor_AVI_EmployeeMapper autoFloor_avi_employeeMapper;

    @Autowired
    private AutoFloor_AVI_Maintain_RecordMapper autoFloor_avi_maintain_recordMapper;

    @Autowired
    private AutoFloor_Test_ExceptionMapper autoFloor_test_exceptionMapper;

    public List<String> getAVIEmployeeList(){
        List<String>  employeeNameList=new ArrayList<>();
        List<AutoFloor_AVI_Employee> autoFloor_avi_employees = autoFloor_avi_employeeMapper.selectALLEmployee();
        autoFloor_avi_employees.forEach( employeesList-> employeeNameList.add(employeesList.getEmployeeNumber()));

        return employeeNameList;
    }

    public void getMaintenanceCepntent(@Param("Machinetype") String Machinetype, List<String> maintenanceCpntentList) {
        switch (Machinetype){
            //TEST
            case "Robot":
                maintenanceCpntentList.add("手動扶正主板");
                maintenanceCpntentList.add("調試點位");
                maintenanceCpntentList.add("手動清除主板信息");
                maintenanceCpntentList.add("檢查手爪感應器信息及狀態");
                maintenanceCpntentList.add("重啟上位機");
                maintenanceCpntentList.add("檢查上料料橋");
                maintenanceCpntentList.add("維修iBot本體");
                break;
            case "治具":
                maintenanceCpntentList.add("清潔天線頭/探針");
                maintenanceCpntentList.add("重啟Wipas/儀器/Mini/RF板");
                maintenanceCpntentList.add("更換耗材");
                maintenanceCpntentList.add("重新量值");
                maintenanceCpntentList.add("更換測試儀器");
                maintenanceCpntentList.add("維修治具");
                maintenanceCpntentList.add("更換治具");
                break;
            case "工站連接":
                maintenanceCpntentList.add("觸摸/調試感應器");
                maintenanceCpntentList.add("PLC初始化");
                maintenanceCpntentList.add("模組卡板/卡框,手動處理");
                maintenanceCpntentList.add("扶正偏位主板");
                maintenanceCpntentList.add("更換耗材");
                maintenanceCpntentList.add("氣壓異常");
                maintenanceCpntentList.add("機構件異常");
                break;
            //FAC
            case "氮氣純度超標":case "氮氣壓力超標":case "氮氣流量超標":
                maintenanceCpntentList.add("檢查氮氣系統");
                break;
            case "空壓流量超標":case "空壓壓力超標":case "空壓露點溫度超標":
                maintenanceCpntentList.add("檢查空壓系統");
                break;
            case "電壓 KV":case "電流":case "功率因數超標":
                maintenanceCpntentList.add("檢查供電系統");
                break;
            case "HUMIDITY":case "TEMP":
                maintenanceCpntentList.add("調整空調運行參數");
                break;
            case "DUST":
                maintenanceCpntentList.add("調整ACU/FFU運行參數");
                break;
            case "LIGHT":
                maintenanceCpntentList.add("調整光照環境參數");
                break;
            case "NOISE":
                maintenanceCpntentList.add("控制噪聲源");
                break;
            case "TVOC":case "CO2":
                maintenanceCpntentList.add("室內通風");
                break;
            case "壓縮空氣":case "氮氣用量":case "動力用量":case "水用量":
                maintenanceCpntentList.add("控制單片生產消耗量");
                break;
            //AVI
            case "軟體":
            case "相機":
            case "軌道":
            case "電腦":
                maintenanceCpntentList.add("檢查本機COM口使用數據，將其維護在相機拍照軟體數據庫表格中");
                maintenanceCpntentList.add("打開AVI測試軟體");
                maintenanceCpntentList.add("檢查AVI報錯信息，針對進行排除。或者重啟AVI軟體");
                maintenanceCpntentList.add("首先查看白熒光原圖，排除是否丟圖，是否圖像異常，是否真实不良");
                maintenanceCpntentList.add("排查相機、路由器或者網絡連接是否異常并處理");
                maintenanceCpntentList.add("排查是否連板堆板并處理");
                break;
            //熱風槍
            case "上溫度異常":
                maintenanceCpntentList.add("調整熱風槍參數");
                break;
            case "下溫度異常":
                maintenanceCpntentList.add("調整熱風槍參數");
                break;
            case "測量風速異常":
                maintenanceCpntentList.add("調整熱風槍風速");
                break;
            case "本機風速異常":
                maintenanceCpntentList.add("調整熱風槍本機風速");
                break;
            case "環境溫度異常":
                maintenanceCpntentList.add("調整熱風槍環境溫度");
                break;

        }
    }

    public void getAlarmAVIMachineFaultMessage(String machineType,List<String> machineFaultMessageList){
        switch (machineType){
            case "軟體":
                machineFaultMessageList.add("白熒光AVI判讀程式介面中斷連接");
                machineFaultMessageList.add("AVI判讀程式介面卡死");
                machineFaultMessageList.add("AVI判讀程式介面異常窗口彈出");
                machineFaultMessageList.add("AVI判讀程式介面不更新");
                machineFaultMessageList.add("GK Server拍照程式崩潰");
                machineFaultMessageList.add("GK Server拍照程式卡死");
                machineFaultMessageList.add("GK Server拍照程式異常窗口跳出");
                machineFaultMessageList.add("感應器觸發，GK Server拍照程式不拍照");
                break;
            case "軌道":
                machineFaultMessageList.add("1m軌道皮帶線脫落");
                machineFaultMessageList.add("包裝機機故熒光連板導致判讀結果6pcs fail報警");
                machineFaultMessageList.add("包裝機進板慢熒光連板導致判讀結果6pcs fail報警");
                machineFaultMessageList.add("軌道卡板導致判讀結果6pcs fail報警");
                machineFaultMessageList.add("60cm軌道皮帶線脫落");
                machineFaultMessageList.add("1m軌道卡板");
                machineFaultMessageList.add("60cm軌道卡板");
                machineFaultMessageList.add("1m軌道位置偏移");
                machineFaultMessageList.add("60cm軌道位置偏移");
                machineFaultMessageList.add("包裝機機故導致熒光連板");
                machineFaultMessageList.add("包裝機進板慢導致熒光連板");
                machineFaultMessageList.add("軌道卡板導致熒光連板");
                machineFaultMessageList.add("包裝機機故，翻板機進2panel板報警");
                machineFaultMessageList.add("翻板機出板卡板報警");
                machineFaultMessageList.add("翻板機進板卡板報警");
                machineFaultMessageList.add("感應器位置變動");
                machineFaultMessageList.add("正常過板情況下，感應器不觸發");
                machineFaultMessageList.add("正常過板情況下，感應器連續多次觸發");

                break;
            case "相機":
                 machineFaultMessageList.add("白光1號相機，失焦");
                 machineFaultMessageList.add("白光1號相機，參數錯誤");
                 machineFaultMessageList.add("白光1號相機，丟圖");
                 machineFaultMessageList.add("白光1號相機，傳圖慢");
                 machineFaultMessageList.add("白光1號相機，不拍照");
                 machineFaultMessageList.add("白光1號相機，燈光異常");
                 machineFaultMessageList.add("白光1號相機，位置變動");
                 machineFaultMessageList.add("白光2號相機，失焦");
                 machineFaultMessageList.add("白光2號相機，參數錯誤");
                 machineFaultMessageList.add("白光2號相機，丟圖");
                 machineFaultMessageList.add("白光2號相機，傳圖慢");
                 machineFaultMessageList.add("白光2號相機，不拍照");
                 machineFaultMessageList.add("白光2號相機，燈光異常");
                 machineFaultMessageList.add("白光2號相機，位置變動");
                 machineFaultMessageList.add("白光3號相機，失焦");
                 machineFaultMessageList.add("白光3號相機，參數錯誤");
                 machineFaultMessageList.add("白光3號相機，丟圖");
                 machineFaultMessageList.add("白光3號相機，傳圖慢");
                 machineFaultMessageList.add("白光3號相機，不拍照");
                 machineFaultMessageList.add("白光3號相機，燈光異常");
                 machineFaultMessageList.add("白光3號相機，位置變動");
                 machineFaultMessageList.add("白光4號相機，失焦");
                 machineFaultMessageList.add("白光4號相機，參數錯誤");
                 machineFaultMessageList.add("白光4號相機，丟圖");
                 machineFaultMessageList.add("白光4號相機，傳圖慢");
                 machineFaultMessageList.add("白光4號相機，不拍照");
                 machineFaultMessageList.add("白光4號相機，燈光異常");
                 machineFaultMessageList.add("白光4號相機，位置變動");
                 machineFaultMessageList.add("熒光1號相機，失焦");
                 machineFaultMessageList.add("熒光1號相機，參數錯誤");
                 machineFaultMessageList.add("熒光1號相機，丟圖");
                 machineFaultMessageList.add("熒光1號相機，傳圖慢");
                 machineFaultMessageList.add("熒光1號相機，不拍照");
                 machineFaultMessageList.add("熒光1號相機，燈光異常");
                 machineFaultMessageList.add("熒光2號相機，位置變動");
                break;
            case "電腦":
                machineFaultMessageList.add("白光電腦卡死");
                machineFaultMessageList.add("白光電腦無法開機");
                machineFaultMessageList.add("白光電腦清理內存");
                machineFaultMessageList.add("熒光電腦卡死");
                machineFaultMessageList.add("熒光電腦無法開機");
                machineFaultMessageList.add("熒光電腦清理內存");
                machineFaultMessageList.add("路由器指示燈狀態錯誤");
                machineFaultMessageList.add("路由器信號不滿格");
                break;
        }

    }

    public void insertAVIMaintainRecord(String id,String machineFaultName,String machineFaultType,String machineFaultMessage,String maintainval,String employeeName){
        AutoFloor_Test_Exception data = autoFloor_test_exceptionMapper.selectByIdAutoFloorTestException(id);

        Date actionDate = autoFloor_avi_maintain_recordMapper.selectAVIDBDate();
        long actionTime = data.getExceptionTime().getTime();
        long endTime = actionDate.getTime();
        double Dispose =(double)(endTime-actionTime)/1000;
        BigDecimal DisposeBig=new BigDecimal(Dispose/60); //异常时间取分钟数
        int DisposeTime =DisposeBig.setScale(0,BigDecimal.ROUND_HALF_UP).intValue(); //四舍五入方法取值
        if (DisposeTime<0){DisposeTime=0;}
        autoFloor_avi_maintain_recordMapper.insert( new AutoFloor_AVI_Maintain_Record(
         data.getFloor(),data.getLinename(),machineFaultName,machineFaultType,machineFaultMessage,
         data.getRepairPropose(),data.getPriority(),data.getExceptionTime(),actionDate,DisposeTime+"",data.getFlag().toString(),"AVI",maintainval,employeeName
        ));

    }

}



