<websocketform>
<form name="publish">
    <input type="text" name="message"/>
    <input type="submit" value="Отправить"/>
</form>
<script>
  if (!window.WebSocket) {
  	document.body.innerHTML = 'WebSocket в этом браузере не поддерживается.';
  }

  // создать подключение
  var socket = new WebSocket("ws://localhost:4567/echo");
  alert('start socket with babel');
  // отправить сообщение из формы publish
  this.forms.publish.onsubmit = function() {
    var outgoingMessage = this.message.value;
    socket.send(outgoingMessage);
    return false;
  };

  // обработчик входящих сообщений
  socket.onmessage = function(event) {
    var incomingMessage = event.data;
    showMessage(incomingMessage);
    addscript(incomingMessage);

  };

  // показать сообщение в div#subscribe
  function showMessage(message) {
    var messageElem = document.createElement('div');
    messageElem.appendChild(document.createTextNode(message));
    document.getElementById('subscribe').appendChild(messageElem);
  }

  function addscript(scripted){
  var script = document.createElement('script');
  script.onload = function () {
     alert('dynamical loaded');

  };
  script.src = alert(scripted);;;

  document.head.appendChild(script);
  }

</script>
</websocketform>