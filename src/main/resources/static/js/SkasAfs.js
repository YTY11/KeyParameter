//
// //圆的半分比
// let machineRate = getMachineRate();
//
// //設備關鍵指標
// let zhiBiaoData = getZhiBiaoData();
// //数据调整
// var  sysbarData= JSON.parse(zhiBiaoData);
// var sysbarxList=[];
// var sysbarTarList=[];
// var sysbarActionList=[];
// for (let i = 0; i < sysbarData.length; i++) {
//     sysbarxList.push(sysbarData[i].proudct);
//     sysbarTarList.push(sysbarData[i].storeIssueTarget);
//     sysbarActionList.push(sysbarData[i].storeIssueAction);
// }
//
// //機故率
// let jiGuData = getJiGuData();
// //数据调整
// var syslineData=JSON.parse(jiGuData);
// var syslinedateTime=[];
// var syslinemachineRate=[];
// for (let i = 0; i < syslineData.length; i++) {
//     syslinedateTime.push(syslineData[i].dateTime);
//     syslinemachineRate.push(syslineData[i].machineRate);
// }
//
// var chartDom = document.getElementById('doughnut');
// var myChart = echarts.init(chartDom);
// var option = {
//     title:{
//         show:true,
//         text:100+'%',
//         x:'center',
//         y:'center',
//         textStyle: {
//             fontSize: '20',
//             fontFamily:'topContentLBottomR',
//             letterSpacing: '0.71',
//             color:'#5FB900',
//             fontWeight: 'normal'
//         }
//     },
//     tooltip: {
//         trigger: 'item',
//         formatter: "{d}%",
//         show:false
//     },
//     legend: {
//         orient: 'vertical',
//         x: 'left',
//         show:false
//     },
//     series: [ {
//         center: ['50%', '50%'],
//         name:'',
//         type:'pie',
//         radius: ['70%', '85%'],
//         avoidLabelOverlap: true,
//         hoverAnimation:false,
//
//         label: {
//             normal: {
//                 show: false,
//                 position: 'center'
//             },
//             emphasis: {
//                 show: false
//             }
//         },
//         labelLine: {
//             normal: {
//                 show: false
//             }
//         },
//         data: [ {
//             value:machineRate,
//             name:'',
//             itemStyle: {
//                 normal: {
//                     color: "#5FB900",
//                 }
//             }
//         },{
//             value:100-machineRate,
//             name:'',
//             itemStyle: {
//                 normal: {
//                     color: "#708FBB7a",
//                 }
//             }
//         }]
//     }]
// };
//
// myChart.setOption(option,true);
// window.addEventListener("resize",function () {
//     myChart.resize();
// });
// myChart.clear();
// myChart.setOption(option,true);
//
//
// var chartDom2 = document.getElementById('keyData');
// var myChart2 = echarts.init(chartDom2);
//
// var option2 = {
//     grid:{
//         top:'20%',//距上边距
//         left:'2%',//距离左边距
//         right:'2%',//距离右边距
//         bottom:'5%',//距离下边距
//         containLabel:true
//     },
//     legend: {
//
//
//         right:'10',
//         itemWidth:35,
//
//         itemHeight:13,
//         itemGap:50,
//         textStyle:{
//             fontFamily: 'PingFangSC-Regular',
//             fontSize: 14,
//             color: 'rgba(0,0,0,0.85)',
//         },
//     },
//     tooltip: {},
//     // dataset: {
//     //     dimensions: ['product', '计划', '实际'],
//     //     source: [
//     //         {product: 'RGV', '计划': 370, '实际': 325 },
//     //         {product: 'AGV', '计划': 21, '实际': 20,},
//     //         {product: '暂存仓', '计划': 20, '实际': 18},
//     //         {product: 'FAS', '计划': 325, '实际': 258}
//     //     ]
//     // },
//     xAxis: {
//         type: 'category',
//         data:sysbarxList,
//         axisTick: {
//             show: false
//         },
//         axisLabel:{
//             textStylee:{
//                 fontFamily: 'PingFangSC-Regular',
//                 fontSize: 14,
//                 color: 'rgba(0,0,0,0.85)',
//                 letterSpacing: -0.04,
//             },
//         },
//
//     },
//     yAxis: {
//         axisLabel:{
//             textStyle:{
//                 fontFamily: 'PingFangSC-Regular',
//                 fontSize: 14,
//                 color: '#000000',
//                 letterSpacing: -1.48,
//                 lineHeight: 20,
//             },
//             interval:0,
//         },
//
//         type: 'value',
//         axisTick: {
//             show: false
//         },
//         axisLine: {
//             show: false
//         },
//
//
//
//     },
//
//     series: [
//         {
//             name:'计划',
//             type: 'bar',
//             data:sysbarTarList,
//             itemStyle: {
//
//                 normal:{
//                     color: new echarts.graphic.LinearGradient(
//                         0, 0, 0, 1,
//                         [
//                             {offset: 0, color: '#06B5FA'},
//                             {offset: 0.5, color: '#6DCEF4'},
//                             {offset: 1, color: '#CEEEF3'}
//                         ]
//                     ),
//                     label:{
//                         show:true,
//                         position: 'top',
//                         textStyle:{
//                             fontFamily: 'PingFangSC-Regular',
//                             fontSize: 12,
//                             letterSpacing: 0,
//                         }
//                     }
//                 }
//
//
//             },
//         },
//         {
//             name:'实际',
//             type: 'bar',
//             data:sysbarActionList,
//             itemStyle: {
//
//                 normal:{
//                     color: new echarts.graphic.LinearGradient(
//                         0, 0, 0, 1,
//                         [
//                             {offset: 0, color: '#03E22C'},
//                             {offset: 0.5, color: '#5CE876'},
//                             {offset: 1, color: '#A9F2AC'}
//                         ]
//                     ),
//                     label:{
//                         show:true,
//                         position: 'top',
//                         textStyle:{
//                             fontFamily: 'PingFangSC-Regular',
//                             fontSize: 12,
//                             letterSpacing: 0,
//                         }
//                     }
//                 }
//
//             },
//         },
//
//     ]
// };
// myChart2.setOption(option2,true);
// window.addEventListener("resize",function () {
//     myChart2.resize();
// });
// myChart2.clear();
// myChart2.setOption(option2,true);
//
//
//
//
//
//
// var chartDom3 = document.getElementById('jiGu');
// var myChart3 = echarts.init(chartDom3);
// var option3 = {
//     grid:{
//         top:'20%',//距上边距
//         left:'2%',//距离左边距
//         right:'2%',//距离右边距
//         bottom:'5%',//距离下边距
//         containLabel:true
//     },
//     xAxis: {
//         type: 'category',
//         data: syslinedateTime,
//
//         axisTick: {
//             show: false
//         },
//         axisLabel:{
//             textStyle:{
//                 fontFamily: 'PingFangSC-Regular',
//                 fontSize: 14,
//                 color: 'rgba(0,0,0,0.85)',
//                 letterSpacing: 0.08,
//             },
//         },
//
//     },
//     yAxis: [
//
//
//         {
//             type: 'value',
//
//             axisTick: {
//                 show: false
//             },
//             axisLine: {
//                 show: false
//             },
//             axisLabel: {
//                 show: true,
//                 interval: 'auto',
//                 formatter: '{value} %',
//                 textStyle:{
//                     fontFamily: 'PingFangSC-Regular',
//                     fontSize: 14,
//                     color: 'rgba(0,0,0,0.85)',
//                     letterSpacing: 0.08,
//                 },
//             },
//             show: true
//         }
//     ],
//     series: [{
//         data: syslinemachineRate,
//         smooth: true,
//
//         symbol: 'circle', //折线点设置为实心点
//         // symbolSize: 6, //折线点的大小
//         type: 'line',
//
//         itemStyle: {
//
//             normal: {
//                 label : {
//                     show: true,
//
//                     formatter: '{c}%',
//                     textStyle: {
//                         fontFamily: 'PingFangSC-Regular',
//                         fontSize: 12,
//                         color: 'rgba(0,0,0,0.85)',
//                         letterSpacing: 0.06,
//                     }
//                 },
//                 color:'#0AA200',
//                 lineStyle:{
//                     color:'#0AA200',
//                 },
//
//             },
//         },
//         textStyle:{
//             fontFamily: 'PingFangSC-Regular',
//             fontSize: 12,
//             color: 'rgba(0,0,0,0.85)',
//             letterSpacing: 0.06,
//         },
//     }]
// }
// myChart3.setOption(option3,true);
// window.addEventListener("resize",function () {
//     myChart3.resize();
// });
// myChart3.clear();
// myChart3.setOption(option3,true);
//
//
//
//
//
//
// setInterval(function(){
//     $.ajax({
//         url:'getSkasData',
//         dataType:'json',
//         type:'get',
//         success:function(data){
//
//             $(".startAllNum").text("");
//             $(".startAllNum").text(data.AGV_AFSState.machineSum);
//
//             $(".startJKNum").text("");
//             $(".startJKNum").text(data.AGV_AFSState.machineHealth);
//
//             $(".startYJNum").text("");
//             $(".startYJNum").text(data.AGV_AFSState.machineWarning);
//
//             $(".startWHNum").text("");
//             $(".startWHNum").text(data.AGV_AFSState.machineUnusual);
//
//             $(".startLXNum").text("");
//             $(".startLXNum").text(data.AGV_AFSState.machineLostConnection);
//
//             option = {
//                 title:{
//                     show:true,
//                     text:100+'%',
//                     x:'center',
//                     y:'center',
//                     textStyle: {
//                         fontSize: '20',
//                         fontFamily:'topContentLBottomR',
//                         letterSpacing: '0.71',
//                         color:'#5FB900',
//                         fontWeight: 'normal'
//                     }
//                 },
//                 tooltip: {
//                     trigger: 'item',
//                     formatter: "{d}%",
//                     show:false
//                 },
//                 legend: {
//                     orient: 'vertical',
//                     x: 'left',
//                     show:false
//                 },
//                 series: [ {
//                     center: ['50%', '50%'],
//                     name:'',
//                     type:'pie',
//                     radius: ['70%', '85%'],
//                     avoidLabelOverlap: true,
//                     hoverAnimation:false,
//
//                     label: {
//                         normal: {
//                             show: false,
//                             position: 'center'
//                         },
//                         emphasis: {
//                             show: false
//                         }
//                     },
//                     labelLine: {
//                         normal: {
//                             show: false
//                         }
//                     },
//                     data: [ {
//                         value:data.AGV_AFSState.machineRate,
//                         name:'',
//                         itemStyle: {
//                             normal: {
//                                 color: "#5FB900",
//                             }
//                         }
//                     },{
//                         value:100-data.AGV_AFSState.machineRate,
//                         name:'',
//                         itemStyle: {
//                             normal: {
//                                 color: "#708FBB7a",
//                             }
//                         }
//                     }]
//                 }]
//             };
//
//             myChart.setOption(option,true);
//             window.addEventListener("resize",function () {
//                 myChart.resize();
//             });
//             myChart.clear();
//             myChart.setOption(option,true);
//
//
//
//
//
//              sysbarData= JSON.parse(data.skas_afsDataJson);
//              sysbarxList=[];
//              sysbarTarList=[];
//              sysbarActionList=[];
//             for (let i = 0; i < sysbarData.length; i++) {
//                 sysbarxList.push(sysbarData[i].proudct);
//                 sysbarTarList.push(sysbarData[i].storeIssueTarget);
//                 sysbarActionList.push(sysbarData[i].storeIssueAction);
//             }
//
//             option2 = {
//                 grid:{
//                     top:'20%',//距上边距
//                     left:'2%',//距离左边距
//                     right:'2%',//距离右边距
//                     bottom:'5%',//距离下边距
//                     containLabel:true
//                 },
//                 legend: {
//
//
//                     right:'10',
//                     itemWidth:35,
//
//                     itemHeight:13,
//                     itemGap:50,
//                     textStyle:{
//                         fontFamily: 'PingFangSC-Regular',
//                         fontSize: 14,
//                         color: 'rgba(0,0,0,0.85)',
//                     },
//                 },
//                 tooltip: {},
//                 // dataset: {
//                 //     dimensions: ['product', '计划', '实际'],
//                 //     source: [
//                 //         {product: 'RGV', '计划': 370, '实际': 325 },
//                 //         {product: 'AGV', '计划': 21, '实际': 20,},
//                 //         {product: '暂存仓', '计划': 20, '实际': 18},
//                 //         {product: 'FAS', '计划': 325, '实际': 258}
//                 //     ]
//                 // },
//                 xAxis: {
//                     type: 'category',
//                     data:sysbarxList,
//                     axisTick: {
//                         show: false
//                     },
//                     axisLabel:{
//                         textStylee:{
//                             fontFamily: 'PingFangSC-Regular',
//                             fontSize: 14,
//                             color: 'rgba(0,0,0,0.85)',
//                             letterSpacing: -0.04,
//                         },
//                     },
//
//                 },
//                 yAxis: {
//                     axisLabel:{
//                         textStyle:{
//                             fontFamily: 'PingFangSC-Regular',
//                             fontSize: 14,
//                             color: '#000000',
//                             letterSpacing: -1.48,
//                             lineHeight: 20,
//                         },
//                         interval:0,
//                     },
//
//                     type: 'value',
//                     axisTick: {
//                         show: false
//                     },
//                     axisLine: {
//                         show: false
//                     },
//
//
//
//                 },
//
//                 series: [
//                     {
//                         name:'计划',
//                         type: 'bar',
//                         data:sysbarTarList,
//                         itemStyle: {
//
//                             normal:{
//                                 color: new echarts.graphic.LinearGradient(
//                                     0, 0, 0, 1,
//                                     [
//                                         {offset: 0, color: '#06B5FA'},
//                                         {offset: 0.5, color: '#6DCEF4'},
//                                         {offset: 1, color: '#CEEEF3'}
//                                     ]
//                                 ),
//                                 label:{
//                                     show:true,
//                                     position: 'top',
//                                     textStyle:{
//                                         fontFamily: 'PingFangSC-Regular',
//                                         fontSize: 12,
//                                         letterSpacing: 0,
//                                     }
//                                 }
//                             }
//
//
//                         },
//                     },
//                     {
//                         name:'实际',
//                         type: 'bar',
//                         data:sysbarActionList,
//                         itemStyle: {
//
//                             normal:{
//                                 color: new echarts.graphic.LinearGradient(
//                                     0, 0, 0, 1,
//                                     [
//                                         {offset: 0, color: '#03E22C'},
//                                         {offset: 0.5, color: '#5CE876'},
//                                         {offset: 1, color: '#A9F2AC'}
//                                     ]
//                                 ),
//                                 label:{
//                                     show:true,
//                                     position: 'top',
//                                     textStyle:{
//                                         fontFamily: 'PingFangSC-Regular',
//                                         fontSize: 12,
//                                         letterSpacing: 0,
//                                     }
//                                 }
//                             }
//
//                         },
//                     },
//
//                 ]
//             };
//             myChart2.setOption(option2,true);
//             window.addEventListener("resize",function () {
//                 myChart2.resize();
//             });
//             myChart2.clear();
//             myChart2.setOption(option2,true);
//
//
//
//
//
//
//             var syslineData=JSON.parse(data.afsLineDataJson);
//             var syslinedateTime=[];
//             var syslinemachineRate=[];
//             for (let i = 0; i < syslineData.length; i++) {
//                 syslinedateTime.push(syslineData[i].dateTime);
//                 syslinemachineRate.push(syslineData[i].machineRate);
//             }
//
//             option3 = {
//                 grid:{
//                     top:'20%',//距上边距
//                     left:'2%',//距离左边距
//                     right:'2%',//距离右边距
//                     bottom:'5%',//距离下边距
//                     containLabel:true
//                 },
//                 xAxis: {
//                     type: 'category',
//                     data: syslinedateTime,
//
//                     axisTick: {
//                         show: false
//                     },
//                     axisLabel:{
//                         textStyle:{
//                             fontFamily: 'PingFangSC-Regular',
//                             fontSize: 14,
//                             color: 'rgba(0,0,0,0.85)',
//                             letterSpacing: 0.08,
//                         },
//                     },
//
//                 },
//                 yAxis: [
//
//
//                     {
//                         type: 'value',
//
//                         axisTick: {
//                             show: false
//                         },
//                         axisLine: {
//                             show: false
//                         },
//                         axisLabel: {
//                             show: true,
//                             interval: 'auto',
//                             formatter: '{value} %',
//                             textStyle:{
//                                 fontFamily: 'PingFangSC-Regular',
//                                 fontSize: 14,
//                                 color: 'rgba(0,0,0,0.85)',
//                                 letterSpacing: 0.08,
//                             },
//                         },
//                         show: true
//                     }
//                 ],
//                 series: [{
//                     data: syslinemachineRate,
//                     smooth: true,
//
//                     symbol: 'circle', //折线点设置为实心点
//                     // symbolSize: 6, //折线点的大小
//                     type: 'line',
//
//                     itemStyle: {
//
//                         normal: {
//                             label : {
//                                 show: true,
//
//                                 formatter: '{c}%',
//                                 textStyle: {
//                                     fontFamily: 'PingFangSC-Regular',
//                                     fontSize: 12,
//                                     color: 'rgba(0,0,0,0.85)',
//                                     letterSpacing: 0.06,
//                                 }
//                             },
//                             color:'#0AA200',
//                             lineStyle:{
//                                 color:'#0AA200',
//                             },
//
//                         },
//                     },
//                     textStyle:{
//                         fontFamily: 'PingFangSC-Regular',
//                         fontSize: 12,
//                         color: 'rgba(0,0,0,0.85)',
//                         letterSpacing: 0.06,
//                     },
//                 }]
//             }
//             myChart3.setOption(option3,true);
//             window.addEventListener("resize",function () {
//                 myChart3.resize();
//             });
//             myChart3.clear();
//             myChart3.setOption(option3,true);
//
//
//
//
//
//
//
// var html = "";
//
//             for(var i = 0; i < data.ExceptionDescribe.length; i++){
//
//
//                 html +=`<li class="poster-item  zturn-item">
//                                             <div class="liName">
//                                                 <span>故障</span>
//                                             </div>
//                                             <div class="for_btn">
//                                                 <ul>
//                                                     <li>异常车号:`+ data.ExceptionDescribe[i].workstation+' '+ data.ExceptionDescribe[i].device +`</li>
//                                                     <li>异常类型:`+ data.ExceptionDescribe[i].error +`</li>
//                                                     <li>异常时间:`+ data.ExceptionDescribe[i].dateTime +`</li>
//                                                 </ul>
//                                             </div>
//                                         </li>`
//
//
//
//             }
//
//             $(".ulData").html("");
//             $(".ulData").html(html);
//
//
//
//
//
//         }
//     })
// },15000);
//
//
//
//
//
//
