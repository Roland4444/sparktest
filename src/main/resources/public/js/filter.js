function compareDate(input, dp1, dp2)
{
  var input__ = new Date(input)
  var dp1__ = new Date(dp1)
  var dp2__ = new Date(dp2)
  if ((input__<dp1__) || (input__>dp2__))
    return false;
  return true;
}

function filterRequest(value)
{
  let startItem = localStorage.getItem('minitem');
  let endItem = localStorage.getItem('maxitem');

  for (let i = endItem; i >= startItem; i--)
  {
    let divid = "approvetag" + String(i);

    if (document.getElementById(divid) == null)
         continue;
    let parent = document.getElementById(divid).firstChild;
    switchRequest(value, parent, i);
  }
}

function switchRequest(value,parent , i)
{
    switch (value) {
        case "N":
            document.getElementById(i).hidden=false;
            break;
        case "W":
             if (!parent.innerHTML.includes("button"))
                document.getElementById(i).hidden=true;
                else document.getElementById(i).hidden=false;
             break;
        default:
            if (!parent.innerHTML.includes(value))
                document.getElementById(i).hidden=true;
                else document.getElementById(i).hidden=false;

    }
}

function filterRows()
{
 // alert('button clicked!')
 // alert('in filter')
  var a = localStorage.getItem('minitem')
  var b = localStorage.getItem('maxitem')
  var begin__ = localStorage.getItem('begindate');
  var end__ =  localStorage.getItem('enddate');

  for (let i=b; i>=a; i--)
  {
      let divid = String(i)+'a'+1;
      if (document.getElementById(divid) == null)
        continue;
      let elem = document.getElementById(divid).innerHTML;
      var curDate = elem.substring(0,10);
      if (!compareDate(curDate, begin__, end__ ))
        document.getElementById(i).hidden=true;
      else document.getElementById(i).hidden=false;
  }

}
$(function() {
  $('input[name="daterange"]').daterangepicker({
    opens: 'left',
    "autoApply": true,
    "showDropdowns": true,
 "locale": {
        "format": "YYYY/MM/DD",
        "separator": " - ",
        "applyLabel": "Применить",
        "cancelLabel": "Отменить",
        "fromLabel": "От",
        "toLabel": "Дo",
        "customRangeLabel": "Произвольно",
        "weekLabel": "Н",
        "daysOfWeek": [
            "Вс",
            "Пн",
            "Вт",
            "Ср",
            "Чт",
            "Пт",
            "Сб"
        ],
        "monthNames": [
            "Январь",
            "Февраль",
            "Март",
            "Апрель",
            "Май",
            "Июнь",
            "Июль",
            "Август",
            "Сентябрь",
            "Октябрь",
            "Ноябрь",
            "Декабрь"
        ],
        "firstDay": 1
    },
    "startDate" : moment().startOf('day').add(-14, 'day'),
    "endDate" : moment()
  }, function(start, end, label) {
    console.log("A new date selection was made: " + start.format('YYYY-MM-DD') + ' to ' + end.format('YYYY-MM-DD'));
    localStorage.setItem('begindate', start.format('YYYY-MM-DD'))
    localStorage.setItem('enddate', end.format('YYYY-MM-DD'))
    filterRows()
  });
});

localStorage.setItem('begindate', moment().startOf('day').add(-14, 'day'))
localStorage.setItem('enddate', moment())
filterRows();