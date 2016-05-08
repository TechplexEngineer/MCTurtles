

var pause = false;

function pause() {
	pause = true;
}

var initFunc = function(iterp, scope) {

	var wrapper = function(turtlename) {
		turtlename = turtlename ? turtlename.toString() : '';
		var tm = com.tpl.turtles.TurtleMgr.getInstance();
		return tm.getByName(turtlename);
	};
	iterp.setProperty(scope, 'getTurtle', iterp.createNativeFunction(wrapper));

	var wrapper = function(direction) {
		direction = direction ? direction.toString() : '';
		var tm = com.tpl.turtles.TurtleMgr.getInstance();
		return tm.getByName('Turtle0').move(direction);
	};
	iterp.setProperty(scope, 'move', iterp.createNativeFunction(wrapper));

	var wrapper = function(txt) {
		txt = txt ? txt.toString() : '';
		java.lang.System.out.println("-----"+txt);
	};
	iterp.setProperty(scope, 'print', iterp.createNativeFunction(wrapper));

	// Add an API function for highlighting blocks.
      var wrapper = function() {
        return interpreter.createPrimitive(pause());
      };
      interpreter.setProperty(scope, 'pause',
          interpreter.createNativeFunction(wrapper));
};

function stepOneLine(interp) {

	var ret = interp.step();
	if (pause) {
		pause = false;
		return ret;
	} else {
		if (ret) {
			stepOneLine(interp);
		}
	}
}