$(document).ready(function () {

    $(".a2").css("color","rgba(50,197,255,0.50)");
    $(".a2").css("border-bottom","");

    $(".a1").css("color","#44D7B6");
    $(".a1").css("border-bottom","1px solid #44D7B6");

})

$(".a1").click(function(){
    $(".a2").css("color","rgba(50,197,255,0.50)");
    $(".a2").css("border-bottom","");

    $(".a1").css("color","#44D7B6");
    $(".a1").css("border-bottom","1px solid #44D7B6");
})

$(".a2").click(function(){
    $(".a1").css("color","rgba(50,197,255,0.50)");
    $(".a1").css("border-bottom","");

    $(".a2").css("color","#44D7B6");
    $(".a2").css("border-bottom","1px solid #44D7B6");
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
        radius: ['65%', '85%'],
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
    legend: {

        // left: 'right',
        right:'50',
        itemWidth:50,

        itemHeight:20,
        itemGap:50,
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
        }},
    yAxis: {
        type: 'value',
        axisTick: {
            show: false
        },
        axisLine: {
            show: false
        },
        textStyle:{

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
    xAxis: {
        type: 'category',
        data: ['8:30', '10:30', '12:30', '14:30', '16:30', '18:30', '20:30'],

        axisTick: {
            show: false
        }
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
                formatter: '{value} %'
            },
            show: true
        }
    ],
    series: [{
        data: [2.06, 1.39, 0.71, 0.19, 0, 0, 0],
        smooth: true,
        symbol:'',
        color:'#0AA200',
        type: 'line',

        itemStyle: {
            normal: {
                label : {
                    show: true,

                    formatter: '{c}%'
                },
                lineStyle:{color:'#0AA200'}
            },
        },
    }]
}
myChart3.setOption(option3,true);
window.addEventListener("resize",function () {
    myChart3.resize();
});
myChart3.clear();
myChart3.setOption(option3,true);