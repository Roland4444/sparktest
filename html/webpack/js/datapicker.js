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