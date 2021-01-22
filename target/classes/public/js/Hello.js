class Hello extends React.Component {
approve= () =>
      {
        function getXmlHttp()
        {
            var xmlhttp;
            try {
                xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
            } catch (e)
            {
                try
                {
                    xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                } catch (E)
                {
                    xmlhttp = false;
                }
            }
            if (!xmlhttp && typeof XMLHttpRequest!='undefined')
            {
                xmlhttp = new XMLHttpRequest();
            }
            return xmlhttp;
        }

        var xhr = getXmlHttp()
        var params= this.props.number ;
        var request = "/approve?id="+params;
        xhr.open("GET", request, true);
        xhr.onreadystatechange=function()
        {
            if (xhr.readyState != 4) return
            clearTimeout(xhrTimeout)
            if (xhr.status == 200) {
                alert('ответ отправлен');
                location.reload();
            }
            if (xhr.status == 500) {
                alert('ответ отправлен');
                location.reload();
            }
        }

        xhr.send("a=5&b=4");
        var xhrTimeout = setTimeout( function(){ xhr.abort(); handleError("Timeout") }, 10000);

        function handleError(message)
        {
            alert("Ошибка: "+message)
        }

      }

suppress= () =>
      {
        function getXmlHttp()
                {
                    var xmlhttp;
                    try {
                        xmlhttp = new ActiveXObject("Msxml2.XMLHTTP");
                    } catch (e)
                    {
                        try
                        {
                            xmlhttp = new ActiveXObject("Microsoft.XMLHTTP");
                        } catch (E)
                        {
                            xmlhttp = false;
                        }
                    }
                    if (!xmlhttp && typeof XMLHttpRequest!='undefined')
                    {
                        xmlhttp = new XMLHttpRequest();
                    }
                    return xmlhttp;
                }

                var xhr = getXmlHttp()
                var params= this.props.number ;
                var request = "/decline?id="+params;
                xhr.open("GET", request, true);
                xhr.onreadystatechange=function()
                {
                    if (xhr.readyState != 4) return
                    clearTimeout(xhrTimeout)
                    if (xhr.status == 200) {
                        alert('ответ отправлен');
                        location.reload();
                    }
                    if (xhr.status == 500) {
                        alert('ответ отправлен');
                        location.reload();
                    }
                }

                xhr.send("a=5&b=4");
                var xhrTimeout = setTimeout( function(){ xhr.abort(); handleError("Timeout") }, 10000);

                function handleError(message)
                {
                    alert("Ошибка: "+message)
                }

      }
    render() {
    if (this.props.status=="SUSPENDING"){
        return  (<div align='center'>
        <button type="button"  class="btn btn-success" onClick={this.approve}>Разрешить </button><br/><br/>
        <button type="button"  class="btn btn-danger" onClick={this.suppress}>Запретить''</button>
        </div>)
    }
    if (this.props.status=="DECLINED"){
        return  (<div align='center'>
        <h5 class="declined">Запрещено</h5>
        </div>)
    }
    if (this.props.status=="APPROVED"){
         return  (<div align='center'>
         <h5 class="approved">Разрешено</h5>
         </div>)
    }
    return  (<div align='center'>
            <h3>Another way</h3>
            </div>)
    }
}