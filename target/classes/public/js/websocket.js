if (!window.WebSocket) {
	document.body.innerHTML = 'WebSocket в этом браузере не поддерживается.';
}
var socket = new WebSocket("ws://localhost:4567/echo");
// обработчик входящих сообщений
socket.onopen = function() {
  console.log("Соединение установлено.");
};
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