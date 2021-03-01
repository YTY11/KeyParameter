
let allData = '';
$(".selectNum").click(function () {

    let floor = $(".selectFloor").val();
    let lineName = $(".selsetLine").val();

    if(floor == 0 || lineName == 0){
        layui.use('layer', function(){
            layer.msg('请选择楼层！！！', {
                offset: '20px',
                anim: 6,
                area:'300px',
                icon: 5,
                time: '2000',
            });
        });
    }
    else{
        $.ajax({
            url:'getKeyUpDate',
            dataType:'json',
            type:'get',
            data:{
                "floor":floor,
                "lineName":lineName,
            },
            success:function(data){
                // console.log(data);
                var html1 = "";
                var html2 = "";

                for(var i = 0; i < data.arrayLists.length; i++){

                    html1 += `<tr>
                                 <td>`+ data.arrayLists[i][0] +`</td>
                                 <td>`+ data.arrayLists[i][1] +`</td>
                                 <td>`+ data.arrayLists[i][2] +`</td>
                                 <td>`+ data.arrayLists[i][3] +`</td>
                                 <td>`+ data.arrayLists[i][4] +`</td>
                                 <td>`+ data.arrayLists[i][5] +`</td>
                                 <td>`+ data.arrayLists[i][6] +`</td>
                                 <td>`+ data.arrayLists[i][7] +`</td>
                                 <td>`+ data.arrayLists[i][8] +`</td>
                                 <td>`+ data.arrayLists[i][9] +"min" +`</td>
                                 <td>`+ data.arrayLists[i][10] +`</td>
                                 <td>`+ data.arrayLists[i][11] +`</td>
                                 <td class="state`+ (i+1) +`" onclick="upError('`+ (i+1) +`')">`+ data.arrayLists[i][12] +`</td>
                                 <td style="display: none" class="id`+ (i+1) +`">`+ data.arrayLists[i][13] +`</td>
                                 </tr>`

                }
                $(".errorMsg").html("");
                $(".errorMsg").html(html1);


                html2+=`<tr>
                        <td>All</td>
                        <td class="errorNum1" onclick="getKeyData(0)">`+ data.integers[0] +`</td>
                        <td class="errorNum2" onclick="getKeyData(2)">`+ data.integers[1] +`</td>
                        <td class="errorNum3" onclick="getKeyData(1)">`+ data.integers[2] +`</td>
                        <td class="errorNum4" onclick="getKeyData('null')"> `+ data.integers[3] +`</td>
                        <td class="errorNum5" onclick="getKeyData(6)">0</td>
                        <td class="errorNum6" onclick="getKeyData(6)">0</td>
                        <td class="errorNum7" onclick="getKeyData('null')">`+ data.integers[3] +`</td>
                    </tr>`


                $(".errorNum").html("");
                $(".errorNum").html(html2);
            }

        })
    }

    if( allData != ''){
        clearInterval(allData);
    }
    if(interval != ''){
        clearInterval(interval);
    }
    allData = setInterval(function () {
        let floor = $(".selectFloor").val();
        let lineName = $(".selsetLine").val();

        if(floor == 0 || lineName == 0){
            layui.use('layer', function(){
                layer.msg('请选择楼层！！！', {
                    offset: '20px',
                    anim: 6,
                    area:'300px',
                    icon: 5,
                    time: '2000',
                });
            });
        }
        else{
            $.ajax({
                url:'getKeyUpDate',
                dataType:'json',
                type:'get',
                data:{
                    "floor":floor,
                    "lineName":lineName,
                },
                success:function(data){
                    // console.log(data);
                    var html1 = "";
                    var html2 = "";

                    for(var i = 0; i < data.arrayLists.length; i++){

                        html1 += `<tr>
                                 <td>`+ data.arrayLists[i][0] +`</td>
                                 <td>`+ data.arrayLists[i][1] +`</td>
                                 <td>`+ data.arrayLists[i][2] +`</td>
                                 <td>`+ data.arrayLists[i][3] +`</td>
                                 <td>`+ data.arrayLists[i][4] +`</td>
                                 <td>`+ data.arrayLists[i][5] +`</td>
                                 <td>`+ data.arrayLists[i][6] +`</td>
                                 <td>`+ data.arrayLists[i][7] +`</td>
                                 <td>`+ data.arrayLists[i][8] +`</td>
                                 <td>`+ data.arrayLists[i][9] +"min"+`</td>
                                 <td>`+ data.arrayLists[i][10] +`</td>
                                 <td>`+ data.arrayLists[i][11] +`</td>
                                <td class="state`+ (i+1) +`" onclick="upError('`+ (i+1) +`')">`+ data.arrayLists[i][12] +`</td>
                                 <td style="display: none" class="id`+ (i+1) +`">`+ data.arrayLists[i][13] +`</td>
                            </tr>`

                    }
                    $(".errorMsg").html("");
                    $(".errorMsg").html(html1);


                    html2+=`<tr>
                        <td>All</td>
                        <td class="errorNum1" onclick="getKeyData(0)">`+ data.integers[0] +`</td>
                        <td class="errorNum2" onclick="getKeyData(2)">`+ data.integers[1] +`</td>
                        <td class="errorNum3" onclick="getKeyData(1)">`+ data.integers[2] +`</td>
                        <td class="errorNum4" onclick="getKeyData('null')"> `+ data.integers[3] +`</td>
                        <td class="errorNum5" onclick="getKeyData(6)">0</td>
                        <td class="errorNum6" onclick="getKeyData(6)">0</td>
                        <td class="errorNum7" onclick="getKeyData('null')">`+ data.integers[3] +`</td>
                    </tr>`


                    $(".errorNum").html("");
                    $(".errorNum").html(html2);
                }

            })
        }
    },15000);

})





let interval = '';
var cc = null;
function getKeyData(msg){
    cc = msg;
    var flag = msg;
    // console.log(flag);
    let floor = $(".selectFloor").val();
    let lineName = $(".selsetLine").val();

    if(floor != 0 && lineName != 0){
        $.ajax({
            url:'getKeyUpDate',
            dataType:'json',
            type:'get',
            data:{
                "floor":floor,
                "lineName":lineName,
                "flag":flag,
            },
            success:function(data){
                // console.log(data);
                var html1 = "";
                var html2 = "";

                for(var i = 0; i < data.arrayLists.length; i++){

                    html1 += `<tr>
                                 <td>`+ data.arrayLists[i][0] +`</td>
                                 <td>`+ data.arrayLists[i][1] +`</td>
                                 <td>`+ data.arrayLists[i][2] +`</td>
                                 <td>`+ data.arrayLists[i][3] +`</td>
                                 <td>`+ data.arrayLists[i][4] +`</td>
                                 <td>`+ data.arrayLists[i][5] +`</td>
                                 <td>`+ data.arrayLists[i][6] +`</td>
                                 <td>`+ data.arrayLists[i][7] +`</td>
                                 <td>`+ data.arrayLists[i][8] +`</td>
                                 <td>`+ data.arrayLists[i][9] +"min" +`</td>
                                 <td>`+ data.arrayLists[i][10] +`</td>
                                 <td>`+ data.arrayLists[i][11] +`</td>
                                 <td class="state`+ (i+1) +`" onclick="upError('`+ (i+1) +`')">`+ data.arrayLists[i][12] +`</td>
                                 <td style="display: none" class="id`+ (i+1) +`">`+ data.arrayLists[i][13] +`</td>
                            </tr>`

                }
                $(".errorMsg").html("");
                $(".errorMsg").html(html1);


                html2+=`<tr>
                        <td>All</td>
                        <td class="errorNum1" onclick="getKeyData(0)">`+ data.integers[0] +`</td>
                        <td class="errorNum2" onclick="getKeyData(2)">`+ data.integers[1] +`</td>
                        <td class="errorNum3" onclick="getKeyData(1)">`+ data.integers[2] +`</td>
                        <td class="errorNum4" onclick="getKeyData('null')"> `+ data.integers[3] +`</td>
                        <td class="errorNum5" onclick="getKeyData(6)">0</td>
                        <td class="errorNum6" onclick="getKeyData(6)">0</td>
                        <td class="errorNum7" onclick="getKeyData('null')">`+ data.integers[3] +`</td>
                    </tr>`


                $(".errorNum").html("");
                $(".errorNum").html(html2);
            }

        })
    }

    if( allData != ''){
        clearInterval(allData);
    }

    if(interval != ''){
        clearInterval(interval);
        clearInterval(allData);
    }
    interval = setInterval(function () {
        // console.log(flag);
        let floor = $(".selectFloor").val();
        let lineName = $(".selsetLine").val();

        if(floor != 0 && lineName != 0){
            $.ajax({
                url:'getKeyUpDate',
                dataType:'json',
                type:'get',
                data:{
                    "floor":floor,
                    "lineName":lineName,
                    "flag":flag,
                },
                success:function(data){
                    // console.log(data);
                    var html1 = "";
                    var html2 = "";

                    for(var i = 0; i < data.arrayLists.length; i++){

                        html1 += `<tr>
                                 <td>`+ data.arrayLists[i][0] +`</td>
                                 <td>`+ data.arrayLists[i][1] +`</td>
                                 <td>`+ data.arrayLists[i][2] +`</td>
                                 <td>`+ data.arrayLists[i][3] +`</td>
                                 <td>`+ data.arrayLists[i][4] +`</td>
                                 <td>`+ data.arrayLists[i][5] +`</td>
                                 <td>`+ data.arrayLists[i][6] +`</td>
                                 <td>`+ data.arrayLists[i][7] +`</td>
                                 <td>`+ data.arrayLists[i][8] +`</td>
                                 <td>`+ data.arrayLists[i][9] +"min" +`</td>
                                 <td>`+ data.arrayLists[i][10] +`</td>
                                 <td>`+ data.arrayLists[i][11] +`</td>
                                 <td class="state`+ (i+1) +`" onclick="upError('`+ (i+1) +`')">`+ data.arrayLists[i][12] +`</td>
                                 <td style="display: none" class="id`+ (i+1) +`">`+ data.arrayLists[i][13] +`</td>
                            </tr>`

                    }
                    $(".errorMsg").html("");
                    $(".errorMsg").html(html1);


                    html2+=`<tr>
                        <td>All</td>
                        <td class="errorNum1" onclick="getKeyData(0)">`+ data.integers[0] +`</td>
                        <td class="errorNum2" onclick="getKeyData(2)">`+ data.integers[1] +`</td>
                        <td class="errorNum3" onclick="getKeyData(1)">`+ data.integers[2] +`</td>
                        <td class="errorNum4" onclick="getKeyData('null')"> `+ data.integers[3] +`</td>
                        <td class="errorNum5" onclick="getKeyData(6)">0</td>
                        <td class="errorNum6" onclick="getKeyData(6)">0</td>
                        <td class="errorNum7" onclick="getKeyData('null')">`+ data.integers[3] +`</td>
                    </tr>`


                    $(".errorNum").html("");
                    $(".errorNum").html(html2);
                }

            })
        }
    },15000);


}








function upError(data) {
    let state = $(".state").text();
    // console.log(data);

    let id = $(".id" + data).text();

    // console.log(id);
    if (state != '已维修') {
        layui.use('layer', function () {
            layer.open({

                type: 1
                , skin: 'btnClass'
                , title: '提示'
                , area: ['40vw', '40vh']
                , shade: 0.3
                , resize: true
                , offset: 'auto'
                , content: `<div class="layerDiv1">

                                <div class="layui-form layui-form-item" >
                                
                                    <div class="layui-input-block1 upErrorClass">
                                        <span style="display: flex"><span style="align-self: center;font-size: 2vh;margin-left: 2vw">状态:</span></span>

                                        <select name="errorDispoes" lay-verify="required" class="selectStart">
                                            <option placeholder="请选择" value="0"></option>
                                            <option value="1">已完成</option>
                                            <option value="2">维修中</option>
                                        </select>
                                    </div>
                                </div>
                                <div style="margin-top: 2vh"> 
                                    <span style="font-size: 2vh;margin-left: 2vw">维修建议：</span><br/><br/>
                                    <textarea style="width: 30vw;margin-left: 2vw;padding: 1vw" class="limit-textarea textareaValue" ></textarea>
                                </div>
                            </div>
                            <script>
                                layui.use('form', function(){
                                   var form = layui.form 
                                   form.render('select')
                                });
                            </script>
                            `


                , btn: ['确认', '取消']
                , btnAlign: 'c'
                , yes: function () {
                    let start = $(".selectStart").val();
                    let content = $(".textareaValue").val();

                    if(start != 0){
                        $.ajax({
                            url:'updateErrorMsg',
                            type:"get",
                            dataType:'json',
                            data:{
                                "id":id,
                                "start":start,
                                "content":content,
                            },
                            success:function(data){
                                if(data == true){
                                    layui.use('layer', function(){
                                        layer.msg('修改成功！', {
                                            offset: '20px',
                                            anim: 6,
                                            area:'300px',
                                            icon: 1,
                                            time: '2000',
                                        });
                                    });

                                    function aa(){

                                        var flag = cc;
                                        // console.log(flag);
                                        let floor = $(".selectFloor").val();
                                        let lineName = $(".selsetLine").val();

                                        if(floor != 0 && lineName != 0){
                                            $.ajax({
                                                url:'getKeyUpDate',
                                                dataType:'json',
                                                type:'get',
                                                data:{
                                                    "floor":floor,
                                                    "lineName":lineName,
                                                    "flag":flag,
                                                },
                                                success:function(data){
                                                    // console.log(data);
                                                    var html1 = "";
                                                    var html2 = "";

                                                    for(var i = 0; i < data.arrayLists.length; i++){

                                                        html1 += `<tr>
                                 <td>`+ data.arrayLists[i][0] +`</td>
                                 <td>`+ data.arrayLists[i][1] +`</td>
                                 <td>`+ data.arrayLists[i][2] +`</td>
                                 <td>`+ data.arrayLists[i][3] +`</td>
                                 <td>`+ data.arrayLists[i][4] +`</td>
                                 <td>`+ data.arrayLists[i][5] +`</td>
                                 <td>`+ data.arrayLists[i][6] +`</td>
                                 <td>`+ data.arrayLists[i][7] +`</td>
                                 <td>`+ data.arrayLists[i][8] +`</td>
                                 <td>`+ data.arrayLists[i][9] +"min" +`</td>
                                 <td>`+ data.arrayLists[i][10] +`</td>
                                 <td>`+ data.arrayLists[i][11] +`</td>
                                 <td>`+ data.arrayLists[i][12] +`</td>
                            </tr>`

                                                    }
                                                    $(".errorMsg").html("");
                                                    $(".errorMsg").html(html1);


                                                    html2+=`<tr>
                        <td>All</td>
                        <td class="errorNum1" onclick="getKeyData(0)">`+ data.integers[0] +`</td>
                        <td class="errorNum2" onclick="getKeyData(2)">`+ data.integers[1] +`</td>
                        <td class="errorNum3" onclick="getKeyData(1)">`+ data.integers[2] +`</td>
                        <td class="errorNum4" onclick="getKeyData('null')"> `+ data.integers[3] +`</td>
                        <td class="errorNum5" onclick="getKeyData(6)">0</td>
                        <td class="errorNum6" onclick="getKeyData(6)">0</td>
                        <td class="errorNum7" onclick="getKeyData('null')">`+ data.integers[3] +`</td>
                    </tr>`


                                                    $(".errorNum").html("");
                                                    $(".errorNum").html(html2);
                                                }

                                            })
                                        }
                                        layer.closeAll();
                                    }
                                    aa();


                                }
                                else{
                                    layui.use('layer', function(){
                                        layer.msg('修改失败！', {
                                            offset: '20px',
                                            anim: 6,
                                            area:'300px',
                                            icon: 5,
                                            time: '2000',
                                        });
                                    });

                                }
                            },
                            error:function(){
                                layui.use('layer', function(){
                                    layer.msg('修改失败！', {
                                        offset: '20px',
                                        anim: 6,
                                        area:'300px',
                                        icon: 5,
                                        time: '2000',
                                    });
                                });
                            }
                        })

                    }
                    else{
                        layui.use('layer', function(){
                            layer.msg('请选择状态', {
                                offset: '20px',
                                anim: 6,
                                area:'300px',
                                icon: 5,
                                time: '2000',
                            });
                        });
                    }

                }
                , btn2: function () {
                    layer.closeAll();
                }
            })

        })
    }
}
