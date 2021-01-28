const REQUEST = 'REQUEST'
const SUCCESS = 'SUCCESS'
const FAILURE = 'FAILURE'
export default class  Approve extends React.Component {
constructor(props) {
    super(props);
    this.state = { state: -1};
}
showMessage= () =>{
    console.log( 'Всем привет!' );
}
approve= () =>
      {
      this.setState({ state: 1 })
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
        var params= this.props.info.number ;
        var request = "/approve?id="+params;
        xhr.open("GET", request, true);
        xhr.onreadystatechange=function()
        {
            if (xhr.readyState != 4) return
            clearTimeout(xhrTimeout)
            if (xhr.status == 200) {

                alert('ответ отправлен');
  /////////////              location.reload();
            }
            if (xhr.status == 500) {
                alert('ответ отправлен');
 ////////////               location.reload();
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
        this.setState({ state: 0 })

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
                var params= this.props.info.number ;
                var request = "/decline?id="+params;
                xhr.open("GET", request, true);
                xhr.onreadystatechange=function()
                {
                    if (xhr.readyState != 4) return
                    clearTimeout(xhrTimeout)
                    if (xhr.status == 200) {
                        alert('ответ отправлен');

                    }
                    if (xhr.status == 500) {
                        alert('ответ отправлен');
                     //////   location.reload();
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
        let minitem = Number(localStorage.getItem('minitem'));
        let maxitem = Number(localStorage.getItem('maxitem'));
        console.log('min>>'+minitem)
        console.log('max>>'+maxitem)
        let number = Number(this.props.info.number)
        if ((minitem == 0) && (maxitem == 0))
        {
            localStorage.setItem('minitem', this.props.info.number)
            localStorage.setItem('maxitem', this.props.info.number)
        }
        else
        {
            if (number > maxitem)
                localStorage.setItem('maxitem', number)
            if (number < minitem)
                localStorage.setItem('minitem', number)
        }

    console.log('min/max elem'+localStorage.getItem('minitem')+':'+localStorage.getItem('maxitem'))
    if (this.state.state==1) {
        return  (<div align='center'>
        <h5 class="approved">Разрешено</h5>
        </div>)
    }
    if (this.state.state==0) {
        return  (<div align='center'>
        <h5 class="declined">Запрещено</h5>
        </div>)
    }
    if (this.props.info.status=="SUSPENDING"){
        return  (<div align='center'>
        <button type="button"  class="btn btn-success" onClick={this.approve}>Разрешить </button><br/><br/>
        <button type="button"  class="btn btn-danger" onClick={this.suppress}>Запретить''</button>
        </div>)
    }
    if (this.props.info.status=="DECLINED"){
        return  (<div align='center'>
        <h5 class="declined">Запрещено</h5>
        </div>)
    }
    if (this.props.info.status=="APPROVED"){
         return  (<div align='center'>
         <h5 class="approved">Разрешено</h5>
         </div>)
    }
    return  (<div align='center'>
            <h3>Another way</h3>
            </div>)
    }
}