//聊天室主人
var username;
// 消息接收者
var toName;

var isPublic;

var map=new Map();

var ws
/*发送消息功能*/
function sendMsg(){
    var msg=document.getElementById("content_text").innerHTML;
    if (isPublic){
        //发送公共消息
        var sendJson = {"publicChat": true,
                        "to":'',
                        "msg":msg};
        ws.send(JSON.stringify(sendJson));
    }else {
        //发送私人消息
        var sendJson={"publicChat": false,
                      "to":toName,
                      "msg":msg};
        ws.send(JSON.stringify(sendJson));
    }
    var myUsername=$("#myUsername").val();
    var appendMsg=getMsgStr(myUsername,msg);
    if (isPublic){
        getPublicMsgContainer().push(appendMsg);
    }else {
        getP2PMsgContaine(toName).push(appendMsg);
    }
    $("#msgContainer").append(appendMsg);
    document.getElementById("content_text").innerHTML="";
    scrollToEnd();
}


//登录后显示用户名和状态
$(function () {
    //创建websocket对象
    ws= new WebSocket("ws://yikolemon.cn/chatSocket");
     //ws= new WebSocket("ws://localhost:8765/chatSocket");

    //建立连接后触发
    ws.onopen = function () {
        //进行打开操作
        isPublic=true;
        //alert("ok");
        console.log("ok");
    };

    //接收到服务端的推送后触发
    ws.onmessage = function (evt) {
        //获取数据
        var dataStr = evt.data;
        //alert(dataStr);
        console.log(dataStr);
        //jsonData 格式举例（需要判断是否是系统消息）：{“systemMsgFlag”: false, "fromName": "YYJ", "message": “你在哪里呀？”}
        //publicChat,from,msg,
        var jsonData = JSON.parse(dataStr);
        var systemMsg=jsonData.systemMsg;
        var onlineNum=jsonData.onlineNum;
        var publicChat=jsonData.publicChat;
        var from=jsonData.from;
        var msg=jsonData.msg;
        //判断是否是系统消息
        if (systemMsg) {
            $("#onlineNum").text(onlineNum);
        }else {
            if (publicChat){
                var publicMsg=getPublicMsgContainer();
                var appendMsg=getMsgStr(from,msg);
                publicMsg.push(appendMsg);
                if (isPublic){
                    //正在这个页面,直接添加
                    $("#msgContainer").append(appendMsg);
                }else {
                    //什么都不做,下次切换的时候再去显示
                }
            }else {
                //p2p聊天
                if (from==toName){
                    //就是当前的页面,发送del的请求给服务器,表示redis的未读可以删除了
                    $.ajax({url:"/chatDel/"+toName});
                    //再加入
                    var appendMsg=getMsgStr(toName,msg);
                    var p2pMsg=getP2PMsgContaine(toName);
                    p2pMsg.push(appendMsg);
                    console.log("放入后数量"+p2pMsg.length);
                    $("#msgContainer").append(appendMsg);
                }else {
                    //不是的,我就通知他去pull就行
                    var node=document.getElementById(from);
                    $(node).children(".uncheck_tag").removeClass("hidden");
                }
            }
        }
        scrollToEnd();
    };

    ws.onclose=function () {
        //页面进行离线的提示
        $("#offLinNotice").show();
    }

});


//点击好友列表后，执行的动作
function chatWithPerson(obj){
    isPublic=false;
    toName=$(obj).data("username");
    $('#chatTarget').text(toName);
    $.ajax({url:"/chat/"+toName,success:function(result){
        $("#msgContainer").empty();
        var p2pMsg=getP2PMsgContaine(toName);
        console.log("拉取前P2P数量"+p2pMsg.length);
        for(var i=0;i<result.length;i++){
            var appendMsg=getMsgStr(toName,result[i]);
            p2pMsg.push(appendMsg);
        }
        console.log("拉取后P2P数量"+p2pMsg.length);
        for(var i=0;i<p2pMsg.length;i++){
            $("#msgContainer").append(p2pMsg[i]);
        }
        scrollToEnd();
        //去除消息标志
        $(obj).children(".uncheck_tag").addClass("hidden");
    }});
}

function chatWithPublic() {
    isPublic=true;
    $('#chatTarget').text("公共聊天室");
    var publicMsg = getPublicMsgContainer();
    $("#msgContainer").empty();
    for (var i=0;i<publicMsg.length;i++){
        $("#msgContainer").append(publicMsg[i]);
    }
    scrollToEnd();
}

function scrollToEnd() {
    var element = document.getElementById("scrollContainer");
    element.scrollTop=element.scrollHeight;//滚动条自动滚动到最下方
}

function getPublicMsgContainer() {
    var publicMsg=map.get("public");
    if (publicMsg==null){
        map.set("public",new Array());
        publicMsg=map.get("public");
    }//如果存在,直接push
    return publicMsg;
}

function getP2PMsgContaine(toName) {
    var P2PMsg=map.get(toName);
    if (P2PMsg==null){
        map.set(toName,new Array());
        P2PMsg=map.get(toName);
    }//如果存在,直接push
    return P2PMsg;
}

function getMsgStr(username,content) {
    return "<div class=\"ui comment chat-msg\">\n" +
        "                                <div class=\"avatar\">\n" +
        "                                    <img src=\"/images/user_avatar.jpg\">\n" +
        "                                </div>\n" +
        "                                <div class=\"content\">\n" +
        "                                    <div class=\"author\">"+username+"</div>\n" +
        "\n" +
        "                                    <div class=\"text chat_text\">\n" +
        "                                        "+content+"\n" +
        "                                    </div>\n" +
        "                                </div>\n" +
        "                            </div>";
}

function getNoticeStr() {
    return "<div class=\"ui  basic label miku-label-right uncheck_tag\">消息</div>"
}

function pullCloudHistory() {
    /*"/chatHistory/{isPublic}/{from}"*/
    if (isPublic){
        //公共聊天记录拉取
        $.ajax({url:"/chatHistory/public",success:function(result){
                var res=JSON.parse(result);
                map.set("public",null);
                var publicMsg = getPublicMsgContainer();
                $("#msgContainer").empty();
                for(var i=0;i<res.length;i++){
                    var appendMsg=getMsgStr(res[i].from,res[i].msg);
                    publicMsg.push(appendMsg);
                    $("#msgContainer").append(appendMsg);
                }
                scrollToEnd();
            }
        })
    }else {
        //私人聊天记录拉取
        $.ajax({url:"/chatHistory/p2p/"+toName,success:function(result){
                var res=JSON.parse(result);
                map.set(toName,null);
                var p2pMsg = getP2PMsgContaine(toName);
                $("#msgContainer").empty();
                console.log(res);
                for(var i=0;i<res.length;i++){
                    var appendMsg=getMsgStr(res[i].from,res[i].msg);
                    p2pMsg.push(appendMsg);
                    $("#msgContainer").append(appendMsg);
                }
                scrollToEnd();
            }
        })
    }
}