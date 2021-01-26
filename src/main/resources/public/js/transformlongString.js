let transformlongString = function (input, cutter) {
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
	

module.exports.transformlongString=transformlongString 
