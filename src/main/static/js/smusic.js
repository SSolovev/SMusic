//document.addEventListener("DOMContentLoaded", function(event) {
//alert('It works!');
//});
var GLOBAL_SONG_MAP = {};
var SONG_TILE_TEMPLATE;
var SONG_PLAYER_TEMPLATE;

window.onload = function () {

    var anchor = document.getElementById('searchBtn');
//if(anchor.addEventListener) // DOM method
    anchor.addEventListener('click', searchSong, false);
//else if(anchor.attachEvent) // this is for IE, because it doesn't support addEventListener
//   anchor.attachEvent('onclick', function(){ return loadXMLDoc.apply(anchor, [window.event]}); // this strange part for making the keyword 'this' indicate the clicked anchor


    var source = $("#song-template").html();
    SONG_TILE_TEMPLATE = Handlebars.compile(source);

    var sourcePlayer = $("#song-player-template").html();
    SONG_PLAYER_TEMPLATE = Handlebars.compile(sourcePlayer);

    $(document).keypress(function(e){
        if (e.which == 13){
            $("#searchBtn").click();
        }
    });

};

function sendRequest(method, url, onReadyFunction, body) {
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            onReadyFunction(xmlhttp);
        }
    };
    xmlhttp.open(method, url, true);
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    if (body) {
        xmlhttp.send(body);
    } else {
        xmlhttp.send();
    }

}

function bindClickActionOnSongs() {
    var anchor = document.getElementsByName('songDownloadBtn');
    for (var i = 0; i < anchor.length; i++) {
        anchor[i].addEventListener('click', downloadSongToServer, false);
    }

    var anchor2 = document.getElementsByName('songListenBtn');
    for (var i = 0; i < anchor2.length; i++) {
        anchor2[i].addEventListener('click', listenSong, false);
    }
//if(anchor.addEventListener) // DOM method

}

function listenSong() {
    var link = this.getAttribute('link');
    var playerDiv = $('div[link="' + link + '"][name="playerDiv"]');

    sendRequest("GET", "getSongUrl?songLink=" + link, function (xmlhttp) {
        var player = SONG_PLAYER_TEMPLATE({songUrl: xmlhttp.responseText});
        playerDiv.html(player);
    });
}

function downloadSongToServer() {
    var link = this.getAttribute('link');
    var jsonSong = GLOBAL_SONG_MAP[link];
    sendRequest("POST", "downloadService", function (xmlhttp) {
        alert("Download result: " + xmlhttp.responseText)
    }, JSON.stringify(jsonSong));

}

function searchSong() {
    var songNameStr = document.getElementById('searchField').value;
    sendRequest("GET", "searchService?songName=" + songNameStr, function (xmlhttp) {
        var jsonObject = JSON.parse(xmlhttp.responseText);
        var songListHtml = "";
        GLOBAL_SONG_MAP = {};

        for (var i = 0; i < jsonObject.length; i++) {
            var song = jsonObject[i];
            GLOBAL_SONG_MAP[song.link] = song;
            songListHtml += SONG_TILE_TEMPLATE(song);
        }
        document.getElementById("resultDiv").innerHTML = songListHtml;

        bindClickActionOnSongs();
    });

}