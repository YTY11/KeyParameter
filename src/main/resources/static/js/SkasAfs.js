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




var option2 = {
    legend: {

        left: 'right'
    },
    tooltip: {},
    dataset: {
        dimensions: ['product', '计划', '实际'],
        source: [
            {product: 'RGV', '计划': 43.3, '实际': 85.8 },
            {product: 'AGV', '计划': 83.1, '实际': 73.4,},
            {product: '暂存仓', '计划': 86.4, '实际': 65.2},
            {product: 'FAS', '计划': 72.4, '实际': 53.9}
        ]
    },
    xAxis: {type: 'category'},
    yAxis: {type: 'value',
        textStyle:{

        }
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
                            {offset: 1, color: '#CEEEF3 '}
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
