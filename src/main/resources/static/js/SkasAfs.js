$(document).ready(function () {

    $(".a2").css("color","rgba(50,197,255,0.50)");
    $(".a2").css("border-bottom","");

    $(".a1").css("color","#44D7B6");
    $(".a1").css("border-bottom","1px solid #44D7B6");

})

$(".a1").click(function(){
    $(".content2").hide();
    $(".content1").show();
    $(".a2").css("color","rgba(50,197,255,0.50)");
    $(".a2").css("border-bottom","");

    $(".a1").css("color","#44D7B6");
    $(".a1").css("border-bottom","1px solid #44D7B6");
    $(".topHtml").css("height","calc( 100% - 80px)");
    $(".content").css("height","calc(100% - 88px - 6.512vw)");
})

$(".a2").click(function(){
    $(".content1").hide();
    $(".content2").show();
    $(".a1").css("color","rgba(50,197,255,0.50)");
    $(".a1").css("border-bottom","");

    $(".a2").css("color","#44D7B6");
    $(".a2").css("border-bottom","1px solid #44D7B6");

    $(".topHtml").css("height","calc( 2054px - 80px)");
    $(".content").css("height","calc( 2054px - 80px - 6.512vw)");
    // $(".content").css("heigth","calc( 2054px - 80px)")

})


var chartDom = document.getElementById('doughnut');
var myChart = echarts.init(chartDom);


var option = {
    title:{
        show:true,
        text:100+'%',
        x:'center',
        y:'center',
        textStyle: {
            fontSize: '20',
            fontFamily:'topContentLBottomR',
            letterSpacing: '0.71',
            color:'#5FB900',
            fontWeight: 'normal'
        }
    },
    tooltip: {
        trigger: 'item',
        formatter: "{d}%",
        show:false
    },
    legend: {
        orient: 'vertical',
        x: 'left',
        show:false
    },
    series: [ {
        center: ['50%', '50%'],
        name:'',
        type:'pie',
        radius: ['70%', '85%'],
        avoidLabelOverlap: true,
        hoverAnimation:false,

        label: {
            normal: {
                show: false,
                position: 'center'
            },
            emphasis: {
                show: false
            }
        },
        labelLine: {
            normal: {
                show: false
            }
        },
        data: [ {
            value:100,
            name:'',
            itemStyle: {
                normal: {
                    color: "#5FB900",
                }
            }
        },{
            value:0,
            name:'',
            itemStyle: {
                normal: {
                    color: "#708FBB7a",
                }
            }
        }]
    }]
};

myChart.setOption(option,true);
window.addEventListener("resize",function () {
    myChart.resize();
});
myChart.clear();
myChart.setOption(option,true);


var chartDom2 = document.getElementById('keyData');
var myChart2 = echarts.init(chartDom2);

var option2 = {
    grid:{
        top:'20%',//距上边距
        left:'2%',//距离左边距
        right:'2%',//距离右边距
        bottom:'5%',//距离下边距
        containLabel:true
    },
    legend: {


        right:'10',
        itemWidth:35,

        itemHeight:13,
        itemGap:50,
        textStyle:{
            fontFamily: 'PingFangSC-Regular',
            fontSize: 14,
            color: 'rgba(0,0,0,0.85)',
        },
    },
    tooltip: {},
    dataset: {
        dimensions: ['product', '计划', '实际'],
        source: [
            {product: 'RGV', '计划': 370, '实际': 325 },
            {product: 'AGV', '计划': 21, '实际': 20,},
            {product: '暂存仓', '计划': 20, '实际': 18},
            {product: 'FAS', '计划': 325, '实际': 258}
        ]
    },
    xAxis: {
        type: 'category',
        axisTick: {
            show: false
        },
        axisLabel:{
            textStylee:{
                fontFamily: 'PingFangSC-Regular',
                fontSize: 14,
                color: 'rgba(0,0,0,0.85)',
                letterSpacing: -0.04,
            },
        },

    },
    yAxis: {
        axisLabel:{
            textStyle:{
                fontFamily: 'PingFangSC-Regular',
                fontSize: 14,
                color: '#000000',
                letterSpacing: -1.48,
                lineHeight: 20,
            },
            interval:0,
        },

        type: 'value',
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },



    },

    series: [
        {type: 'bar',
            itemStyle: {

                normal:{
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#06B5FA'},
                            {offset: 0.5, color: '#6DCEF4'},
                            {offset: 1, color: '#CEEEF3'}
                        ]
                    ),
                    label:{
                        show:true,
                        position: 'top',
                        textStyle:{
                            fontFamily: 'PingFangSC-Regular',
                            fontSize: 12,
                            letterSpacing: 0,
                        }
                    }
                }


            },
        },
        {type: 'bar',
            itemStyle: {

                normal:{
                    color: new echarts.graphic.LinearGradient(
                        0, 0, 0, 1,
                        [
                            {offset: 0, color: '#03E22C'},
                            {offset: 0.5, color: '#5CE876'},
                            {offset: 1, color: '#A9F2AC'}
                        ]
                    ),
                    label:{
                        show:true,
                        position: 'top',
                        textStyle:{
                            fontFamily: 'PingFangSC-Regular',
                            fontSize: 12,
                            letterSpacing: 0,
                        }
                    }
                }

            },
        },

    ]
};
myChart2.setOption(option2,true);
window.addEventListener("resize",function () {
    myChart2.resize();
});
myChart2.clear();
myChart2.setOption(option2,true);






var chartDom3 = document.getElementById('jiGu');
var myChart3 = echarts.init(chartDom3);
var option3 = {
    grid:{
        top:'20%',//距上边距
        left:'2%',//距离左边距
        right:'2%',//距离右边距
        bottom:'5%',//距离下边距
        containLabel:true
    },
    xAxis: {
        type: 'category',
        data: ['8:30', '10:30', '12:30', '14:30', '16:30', '18:30', '20:30'],

        axisTick: {
            show: false
        },
        axisLabel:{
            textStyle:{
                fontFamily: 'PingFangSC-Regular',
                fontSize: 14,
                color: 'rgba(0,0,0,0.85)',
                letterSpacing: 0.08,
            },
        },

    },
    yAxis: [


        {
            type: 'value',

            axisTick: {
                show: false
            },
            axisLine: {
                show: false
            },
            axisLabel: {
                show: true,
                interval: 'auto',
                formatter: '{value} %',
                textStyle:{
                    fontFamily: 'PingFangSC-Regular',
                    fontSize: 14,
                    color: 'rgba(0,0,0,0.85)',
                    letterSpacing: 0.08,
                },
            },
            show: true
        }
    ],
    series: [{
        data: [2.06, 1.39, 0.71, 0.19, 0, 0, 0],
        smooth: true,

        symbol: 'circle', //折线点设置为实心点
        // symbolSize: 6, //折线点的大小
        type: 'line',

        itemStyle: {

            normal: {
                label : {
                    show: true,

                    formatter: '{c}%',
                    textStyle: {
                        fontFamily: 'PingFangSC-Regular',
                        fontSize: 12,
                        color: 'rgba(0,0,0,0.85)',
                        letterSpacing: 0.06,
                    }
                },
                color:'#0AA200',
                lineStyle:{
                    color:'#0AA200',
                },

            },
        },
        textStyle:{
            fontFamily: 'PingFangSC-Regular',
            fontSize: 12,
            color: 'rgba(0,0,0,0.85)',
            letterSpacing: 0.06,
        },
    }]
}
myChart3.setOption(option3,true);
window.addEventListener("resize",function () {
    myChart3.resize();
});
myChart3.clear();
myChart3.setOption(option3,true);












var chartDom21 = document.getElementById('faLiaoLiang');
var myChart21 = echarts.init(chartDom21);

var option21 = {
    grid:{
        top:'20%',//距上边距
        left:'2%',//距离左边距
        right:'2%',//距离右边距
        bottom:'5%',//距离下边距
        containLabel:true
    },
    xAxis: {
        type: 'category',
        data: ['投入', '产出']
    },
    yAxis: {
        type: 'value'
    },

    series: [{

        type: 'bar',
        itemStyle: {
            normal: {
                //通过数组下标选择颜色
                color: function(params) {
                    var colorList = [
                        '#53DDC1','#77A3F9'
                    ];
                    return colorList[params.dataIndex]
                },
            }
        },
        data: [345, 305]

    }]
};
myChart21.setOption(option21,true);
window.addEventListener("resize",function () {
    myChart21.resize();
});
myChart21.clear();
myChart21.setOption(option21,true);