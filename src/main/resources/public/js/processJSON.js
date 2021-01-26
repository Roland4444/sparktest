const transform = require('./transformlongString');
    function proccessJSON(q){
        let maxitem = localStorage.getItem('maxitem');
        var obj = JSON.parse(q)
        for (var item in obj){
            var id  = (obj[item].id);
            createRow(obj[item])
        }
    }

    function createRow(a){
        var counter = String(a.id)
        if (document.getElementById(counter)==null)
        {
            var table = document.getElementById("mTable");
            var row = table.insertRow(1);
            var tbdy = document.getElementById('tbody');
            var tr = document.createElement('tr');
            tr.setAttribute("id", counter);
            for (var j = 0; j < 7; j++)
            {
                var td = document.createElement('td');
                td.setAttribute("align", "center");
                var div = document.createElement("div");
                var id = counter+'a'+j
                console.log(id)
                div.setAttribute("id", id);
                td.appendChild(div)
                tr.appendChild(td)
            }
            var td = document.createElement('td');
            var div = document.createElement("div");
            var id = 'approvetag'+counter
            div.setAttribute("id", id);
            td.setAttribute("align", "center");
            td.appendChild(div)
            tr.appendChild(td)
            tbdy.prepend(tr);
        }
        var id = 'approvetag'+counter
        var status___ = processRow(a)
        var json__a = {'number': a.id, 'status': status___};
        renderReact(json__a, id)

    }

  function flow(a222)
    {
        if (a222 == null)
            return ''
        if (a222 == "'null'")
            return '';
        if ((a222[0]=="'") && (a222[a222.length-1]=="'"))
        return a222.substring(1, a222.length-1)
        return a222;
    }
    function schoneJSON(input){
        if (input==null)
            return "";
        if (input.a ==0)
            return '';
        var result="";
        result+="<p>Дата: "+ input.Date +   "<br>";
        result+="<p>Время: "+ input.Time +   "<br>";
        result+="<p>Номер накладной: "+ input.Waybill_number +    "<br>";
        result+="<p>Металл: "+ input.Metall +    "<br>";
        result+="<p>Тара: "+ input.Tara +    "<br>";
        result+="<p>Нетто: "+ input.Netto +   "<br>";
        result+="<p>Брутто: "+ input.Brutto +   "<br>";
        result+="<p>Засор: "+ input.Clogging +   "<br>";
        result+="<p>Примесь: "+ input.Trash +   "<br>";
        result+="<p>Комментарий: "+ input.Comment +   "<br>";
        return result
    }

    function compareundschoneJSON(input, compare, rownumber)
    {
///alert('in compareundschoneJSON')
        if ((input==null) || (compare==null) ||  (input.a ==0) ){
            return schoneJSON(input);
        }
        var div = document.getElementById(String(rownumber)+'a'+5);
        div.innerHTML = "";

        var p1 = document.createElement("p");
        p1.innerHTML = 'Дата: '+input.Date
        if (input.Date != compare.Date)
            p1.setAttribute("style", 'background-color: yellow');
        div.appendChild(p1);

        var p2 = document.createElement("p");
        p2.innerHTML = 'Время: '+input.Time
        if (input.Time != compare.Time)
            p2.setAttribute("style", 'background-color: yellow');
        div.appendChild(p2);

        var p3 = document.createElement("p");
        p3.innerHTML = 'Номер накладной: '+input.Waybill_number
        if (input.Waybill_number != compare.Waybill_number)
            p3.setAttribute("style", 'background-color: yellow');
        div.appendChild(p3);

        var p4 = document.createElement("p");
        p4.innerHTML = 'Металл: '+input.Metall
        if (input.Metall != compare.Metall)
            p4.setAttribute("style", 'background-color: yellow');
        div.appendChild(p4);

        var p5 = document.createElement("p");
        p5.innerHTML = 'Тара: '+input.Tara
        if (input.Tara != compare.Tara)
            p5.setAttribute("style", 'background-color: yellow');
        div.appendChild(p5);

        var p6 = document.createElement("p");
        p6.innerHTML = 'Нетто: '+input.Netto
        if (input.Netto != compare.Netto)
            p6.setAttribute("style", 'background-color: yellow');
        div.appendChild(p6);

        var p7 = document.createElement("p");
        p7.innerHTML = 'Брутто: '+input.Brutto
        if (input.Brutto != compare.Brutto)
            p7.setAttribute("style", 'background-color: yellow');
        div.appendChild(p7);

        var p8 = document.createElement("p");
        p8.innerHTML = 'Засор: '+input.Clogging
        if (input.Clogging != compare.Clogging)
            p8.setAttribute("style", 'background-color: yellow');
        div.appendChild(p8);

        var p9 = document.createElement("p");
        p9.innerHTML = 'Примесь: '+input.Trash
        if (input.Trash != compare.Trash)
            p9.setAttribute("style", 'background-color: yellow');
        div.appendChild(p9);

        var p10 = document.createElement("p");
        p10.innerHTML = 'Комментарий: '+input.Comment
        if (input.Comment != compare.Comment)
            p10.setAttribute("style", 'background-color: yellow');
        div.appendChild(p10);
    }

    function transformlongString(input, cutter) {
        let i =0
        if (input == null)
            return ''
        let sb = ''
        while (i+cutter<input.length-1)
        {
            sb+=input.substring(i, i+cutter);
            sb+="<br>";
            i+=cutter;
        }
        sb+=input.substring(i, input.length);
        return sb
    };


    function fillRow(a)
    {
         ////   alert('in fillRow')

       let index____ = String(a.id)
     ///////  alert(index____)
       var id = document.getElementById(index____+'a'+0);
       var datetimerequest = document.getElementById(index____+'a'+1);
       var comment = document.getElementById(index____+'a'+2);
       var initialdata = document.getElementById(index____+'a'+3);
       var datetimeapprove = document.getElementById(index____+'a'+4);
       var updateddata = document.getElementById(index____+'a'+5);
       var datetimeupdate = document.getElementById(index____+'a'+6);
       id.innerHTML =''
       datetimerequest.innerHTML =''
       comment.innerHTML =''
       initialdata.innerHTML=''
       datetimeapprove.innerHTML=''
       updateddata.innerHTML = ''
       datetimeupdate.innerHTML =''
       id.innerHTML =flow( a.id)
       datetimerequest.innerHTML =flow( a.datetimerequest)
       comment.innerHTML =flow(transformlongString(a.comment,60))
       initialdata.innerHTML = schoneJSON((a.initialdata))
       datetimeapprove.innerHTML =flow( a.datetimeapprove)
       compareundschoneJSON((a.updateddata), (a.initialdata), a.id)
       datetimeupdate.innerHTML =flow( a.datetimeupdate)
    }

    function processRow(a22)
    {
     /////   alert('in process row')
     //////   alert('a22>>>>'+a22)
        fillRow(a22)
        if (a22.updateddata == null)
            return 'SUSPENDING'
        if (a22.updateddata.a==0)
            return 'DECLINED'
        if (JSON.stringify(a22.updateddata).length>15)
            return 'APPROVED'
        return 'SUSPENDING'

    };

