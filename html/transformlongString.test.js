const imp = require('./transformlongString');
let sb = imp.transformlongString('realylongcommentary', 5);
test('transform test', () => {
expect(sb).toBe('realy<br>longc<br>ommen<br>tary');
});