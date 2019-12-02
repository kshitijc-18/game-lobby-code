/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
window.onload = hideForm;
var socket = new WebSocket("ws://localhost:8080/GameLobby/data");
socket.onmessage = onMessage;
socket.onopen = function(e){
    console.log("[open] Connection established");
    init();
}

function onMessage(event) {
    var lobby = JSON.parse(event.data);
    if (lobby.action === "add") {
        printLobbyElement(lobby);
    }
    if (lobby.action === "remove") {
        document.getElementById(lobby.id).remove();
        //device.parentNode.removeChild(device);
    }
    if (lobby.action === "refresh") {
        var table = document.getElementById("myTable");
        //console.log(lobby);
        table.rows[lobby.id+1].cells[3].innerHTML = lobby.joinCount;
        
        //device.parentNode.removeChild(device);
    }
}

function addLobby(name, entryFee, size) {
    var LobbyAction = {
        action: "add",
        name: name,
        entryFee: entryFee,
        joinCount: Math.floor(Math.random()*100),
        size: size
    };
    socket.send(JSON.stringify(LobbyAction));
}

function removeLobby(element) {
    var id = element;
    var LobbyAction = {
        action: "remove",
        id: id
    };
    socket.send(JSON.stringify(LobbyAction));
}

function printLobbyElement(lobby) {
    var content = document.getElementById("content");
    var table = document.getElementById("myTable");
    var rowCount = table.getElementsByTagName("tr").length;
    var newRow = table.insertRow(rowCount);
    var cellId = newRow.insertCell(0);
    var cellName = newRow.insertCell(1);
    var cellEF = newRow.insertCell(2);
    var cellJC = newRow.insertCell(3);
    var cellSize = newRow.insertCell(4);
    cellId.innerHTML = lobby.id+1;
    cellName.innerHTML = lobby.name;
    cellEF.innerHTML = lobby.entryFee;
    cellJC.innerHTML = lobby.joinCount;
    cellSize.innerHTML = lobby.size;
}

function showForm() {
    document.getElementById("addDeviceForm").style.display = '';
}

function hideForm() {
    document.getElementById("addDeviceForm").style.display = "none";
}

function formSubmit() {
    var form = document.getElementById("addDeviceForm");
    var name = form.elements["name"].value;
    var entryFee = form.elements["entryFee"].value;
    var size = form.elements["size"].value;
    hideForm();
    document.getElementById("addDeviceForm").reset();
    addLobby(name, entryFee, size);
}

function init() {
    hideForm();
    var initArray = new Array();
    initArray.push({
        "name":"Rummy",
        "entryFee":"50",
        "size":"15"
    });
    initArray.push({
        "name":"TeenPatti",
        "entryFee":"100",
        "size":"8"
    });
    initArray.push({
        "name":"Howzat",
        "entryFee":"75",
        "size":"100"
    });
    initArray.push({
        "name":"Eat.io",
        "entryFee":"5",
        "size":"1000"
    });
    initArray.push({
        "name":"Poker",
        "entryFee":"150",
        "size":"8"
    });
    var table = document.getElementById("myTable");
    var rowCount = table.getElementsByTagName("tr").length;
    
    if(rowCount<5){
       initArray.forEach(function(item){
        addLobby(item.name, item.entryFee, item.size);
    });
    console.log("Initiated array");
}
    
}




