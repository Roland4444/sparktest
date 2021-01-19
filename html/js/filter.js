/*function filterRequest()
{
  let startItem = localStorage.getItem('minitem');
  let endItem = localStorage.getItem('maxitem');

  for (let i = endItem; i >= startItem; i--)
  {
     let divid = "approvetag" + String(i);

        if (document.getElementById(divid) == null)
        continue;
    switchRequest(i);
  }
}

function switchRequest(i)
{
    let divid = "approvetag" + String(i);

    let parent = document.getElementById(divid).firstChild;
    switch (requestselect.value)
    {
        case "N":
            document.getElementById(i).hidden=false;
            break;
        case "W":
             if (!parent.innerHTML.includes("button"))
                document.getElementById(i).hidden=true;
                else document.getElementById(i).hidden=false;
             break;
        default:
            if (!parent.innerHTML.includes(requestselect.value))
                document.getElementById(i).hidden=true;
                else document.getElementById(i).hidden=true;

    }
}

function filterHidden(i)
{
      let divid = String(i)+"a"+1;
      //  alert(divid);
      let elem = document.getElementById(divid).innerHTML;
      var curDate = elem.substring(0,10);
  //    alert(i);
      if (!compareDate(curDate))
        document.getElementById(i).hidden=true;
      else document.getElementById(i).hidden=false;

      filterRequest(i);
}

function filterRows()
{
  var a = localStorage.getItem('minitem')

  var b = localStorage.getItem('maxitem')


  for (let i=b; i>=a; i--)
  {
  let divid = String(i)+"a"+1;
  if (document.getElementById(divid) == null)
          continue;
      filterHidden(i);
  }

}

function compareDate(input)
{
  var begin__ = localStorage.getItem('begindate');
  var end__ =  localStorage.getItem('enddate');
  var input__ = new Date(input)
  var dp1__ = new Date(begin__)
  var dp2__ = new Date(end__)
  if ((input__<dp1__) || (input__>dp2__))
    return false;
  return true;
}*/
/*
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
*/

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

	let startItem = localStorage.getItem('minitem');
	let endItem = localStorage.getItem('maxitem');
	let startDate = localStorage.getItem('begindate');
	let endDate =  localStorage.getItem('enddate');

	for (let i = endItem; i >= startItem; i--)
	{
		let divid_date = String(i)+'a'+1;
		let divid_approve = "approvetag" + String(i);
		let parent = document.getElementById(divid_approve).firstChild;

		if (document.getElementById(divid_date) == null)
			continue;

		let elem_date = document.getElementById(divid_date).innerHTML;
		let curDate = elem_date.substring(0,10);

		if (!compareDate(curDate, startDate, endDate))
			document.getElementById(i).hidden=true;
			else
			{
					document.getElementById(i).hidden=false;
					switchRequest(requestselect.value, parent, i);
			}
	}

}