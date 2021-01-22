if (!window.WebSocket) {
	document.body.innerHTML = 'WebSocket в этом браузере не поддерживается.';
}
var socket = new WebSocket("ws://192.168.0.2:4567/echo");
// обработчик входящих сообщений
socket.onmessage = function(event) {
  var incomingMessage = event.data;
  ///addscript(incomingMessage);
  console.log('message'+incomingMessage)
  proccessJSON(incomingMessage)
  alert('получен новый запрос. проверьте таблицу')
  //location.reload();
};
// показать сообщение в div#subscribe
function addscript(scripted){
    var script = document.createElement('script');
    script.onload = function ()
    {
     ////   alert('dynamical loaded');
    };
    script.src = alert(scripted);;;
    document.head.appendChild(script);
}