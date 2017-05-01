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

};

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

    var jsonSong = GLOBAL_SONG_MAP[link];
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {
            var player = SONG_PLAYER_TEMPLATE({songUrl: xmlhttp.responseText});
            playerDiv.html(player);
            // return false;
            // player.play();
            // this.getElementsByName("playerDiv")[0].getElementsByTagName("audio")
        }
    }
    xmlhttp.open("GET", "getSongUrl?songLink=" + link, true);
    xmlhttp.send();
    // return false;
}
function downloadSongToServer() {
    var link = this.getAttribute('link');
    var jsonSong = GLOBAL_SONG_MAP[link];
    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == 4 && xmlhttp.status == 200) {

            alert("Download result: " + xmlhttp.responseText)
        }
    }
    xmlhttp.open("POST", "downloadService", true);
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify(jsonSong));

}

function searchSong() {

    var songNameStr = document.getElementById('searchField').value;

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function () {
        if (xmlhttp.readyState == XMLHttpRequest.DONE) {
            if (xmlhttp.status == 200) {

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
            }
            else if (xmlhttp.status == 400) {
                alert('There was an error 400');
            }
            else {
                alert('something else other than 200 was returned ' + xmlhttp.status);
            }
        }
    };

    xmlhttp.open("GET", "searchService?songName=" + songNameStr, true);
    xmlhttp.send();
}