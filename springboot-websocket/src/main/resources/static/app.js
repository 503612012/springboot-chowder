var stompClient = null;

function setConnected(connected) {
    $("#connect").prop("disabled", connected);
    $("#disconnect").prop("disabled", !connected);
    if (connected) {
        $("#conversation").show();
    } else {
        $("#conversation").hide();
    }
    $("#greetings").html("");
}

function connect() {
    let socket = new SockJS('http://localhost:8080/poc');
    stompClient = Stomp.over(socket);
    stompClient.connect({"id": "5217"}, function(frame) { // 客户端ID
        debugger
        setConnected(true);
        console.log('Connected: ' + frame);
        stompClient.subscribe('/scene/' + "5217", function(greeting) { // 表明客户端地址
            showGreeting(greeting.body);
        }, {"id": "Host_" + "5217"});
    });
}

function disconnect() {
    if (stompClient !== null) {
        stompClient.disconnect();
    }
    setConnected(false);
    console.log("Disconnected");
}

function sendName() {
    debugger
    stompClient.send("/app/scene", {}, JSON.stringify({
        'content': $("#name").val(),
        'id': "5217"
    }));
}

function showGreeting(message) {
    $("#greetings").append("<tr><td>" + message + "</td></tr>");
}

$(function() {
    $("form").on('submit', function(e) {
        e.preventDefault();
    });
    $("#connect").click(function() {
        connect();
    });
    $("#disconnect").click(function() {
        disconnect();
    });
    $("#send").click(function() {
        sendName();
    });
});