/**
 * The javascript class handles all incoming and outgoing websocket
 * messages from the front-end and back-end.
 */

/**
 * Opens a web socket connector to the server.
 * Listen for incoming messages in the form of JSON.
 * It then parses and adjust the messages received from the Twitter API.
 */
function callws() {
	var url = $("body").data("ws-url");	
	var ws = new WebSocket(url);
	index = 0;
	var data = [];
	ws.onmessage = function(event) {
		var _data = JSON.parse(event.data);
		if(_data.search && _data.search.statuses) {
			data = _data.search.statuses;
			//setTimeout(function(){
			var symbol = _data.search.search_metadata.query;
			if ($(document.getElementById(symbol)).length){
				createTweets(data , $(document.getElementById(symbol)).find("ul.reminders") , symbol);
			}else{
				updateTweets(symbol , data);
			}//},0);
		}
		if(data.time != null || data.time != undefined) {
			$('#time').append(message.time + "<br/>");
		}		
	}
	
	$("input.search-submit").click(function() {
		event.preventDefault();
		var _searchBox = $("#btnSearchKeyword");
		var _val = _searchBox.val();
		_searchBox.val('');
		ws.send(JSON.stringify({symbol: _val}));

	});
	
}
/**
 * Updates the tweets block on the front-end.
 */
function updateTweets(symbol , data){
	var _tweetwall = $("<ul>").addClass("reminders");
	var _searchkey = "<div class'search-info' style='background-color:#fff;color:#000;padding: 20px;'><p>Search Results for " +" " +symbol +"</p>" +
			"<span class='count'>"+ data.length +"</span><span class='totalCount'>0</span></div>";
	var tweets = $("<div>").addClass("tweet-con").prop("id", symbol).append(_searchkey).append(_tweetwall);
	 $("#searchResults").prepend(tweets);
	 createTweets(data , $(document.getElementById(symbol)).find("ul.reminders") , symbol);
}

var idTweetsArray = [];
/**
 * Create the tweets block on the front-end.
 */
function createTweets(resp , container , symbol){
	 var _tweets = resp;
	var _count =  container.parent().find('span.count');
	//_count.text(Number(_count.text()) + resp.length);
	$.each(_tweets , function(i , val){		
		 var info = val;
		 if($.inArray( info.id_str, idTweetsArray ) < 0) {
			 idTweetsArray.push(info.id_str);
			 //$('.count').text(index + 1);
			 var _item = "<input id=\"idTweet\" type=\"hidden\" value=\""+ info.user.id_str +"\" /><div class='_imgcon'><img class='w3-bar-item w3-circle' style='width:50px' src='"+ info.user.profile_image_url +"'/></div>" + 
			 "<div class='w3-rest'><span class='w3-large'><a class='_userprofile' href='/get_usertimeline/"+info.user.screen_name+"' target='_blank'>"+ info.user.name +"</a>" + 
			 "<span class=w3-small _screenName'>@"+info.user.screen_name+"</span></span><p class='_tweetDesc'>"+info.text+"</p></div>"
			 index = index+ 1;
			 _con = $("<li class='new-item'>").html(_item);
			 if(container.children().length > 9){
				 container.children().last().remove();
			 }
			 container.prepend(_con);
			 container.parent().find('span.totalCount').text(container.children().length);
			 
		 } else {
			 console.log("id tweet exist:" + info.user.id_str);
		 }
	});
	_count.text(idTweetsArray.length);
}