//document.addEventListener("DOMContentLoaded", function(event) {
//alert('It works!');
//});
var GLOBAL_SONG_MAP = {};
window.onload = function() {

var anchor = document.getElementById('clickDiv');
//if(anchor.addEventListener) // DOM method
  anchor.addEventListener('click', loadXMLDoc, false);
//else if(anchor.attachEvent) // this is for IE, because it doesn't support addEventListener
//   anchor.attachEvent('onclick', function(){ return loadXMLDoc.apply(anchor, [window.event]}); // this strange part for making the keyword 'this' indicate the clicked anchor

};

function bindClickActionOnSongs(){
var anchor = document.getElementsByName('songRow');
for(var i=0;i<anchor.length;i++){
   anchor[i].addEventListener('click', downloadSongToServer, false);
}
//if(anchor.addEventListener) // DOM method

}

function downloadSongToServer(){
var link=this.getAttribute('link');
var jsonSong = GLOBAL_SONG_MAP[link];
var xmlhttp = new XMLHttpRequest();
xmlhttp.onreadystatechange = function() {if (xmlhttp.status == 200) {
alert("Download result: "+xmlhttp.responseText)
}}
    xmlhttp.open("POST", "downloadService", true);
    xmlhttp.setRequestHeader("Content-Type", "application/json");
    xmlhttp.send(JSON.stringify(jsonSong));

}

function loadXMLDoc() {

 var songNameStr = document.getElementById('searchField').value;

    var xmlhttp = new XMLHttpRequest();
    xmlhttp.onreadystatechange = function() {
        if (xmlhttp.readyState == XMLHttpRequest.DONE ) {
           if (xmlhttp.status == 200) {

               var jsonObject=  JSON.parse(xmlhttp.responseText);
               var songListHtml="<ul>";
               GLOBAL_SONG_MAP={};

               for(var i=0;i<jsonObject.length;i++){
                 var song=jsonObject[i]
                 GLOBAL_SONG_MAP[song.link]=song;
                 songListHtml+="<li name='songRow'"+
                 " duration="+song.duration+
                 " size="+song.size+" "+
                 " link="+song.link+">"+
                 song.singer+"-"+song.songName+
                 "</li>";
               }
               songListHtml+="</ul>"
               document.getElementById("myDiv").innerHTML = songListHtml;

               bindClickActionOnSongs();
           }
           else if (xmlhttp.status == 400) {
              alert('There was an error 400');
           }
           else {
               alert('something else other than 200 was returned '+xmlhttp.status);
           }
        }
    };

    xmlhttp.open("GET", "searchService?songName="+songNameStr, true);
    xmlhttp.send();
}