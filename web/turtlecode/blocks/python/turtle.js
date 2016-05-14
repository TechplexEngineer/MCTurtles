

//turtle_move
Blockly.Python['turtle_move'] = function(block) {
  var value_direction = Blockly.Python.valueToCode(block, 'DIRECTION', Blockly.Python.ORDER_ATOMIC);
  var code = 'turtle.move('+value_direction+');\n';
  return code;
};

//turtle_turn
Blockly.Python['turtle_turn'] = function(block) {
  var value_direction = Blockly.Python.valueToCode(block, 'DIRECTION', Blockly.Python.ORDER_ATOMIC);
  var code = 'turtle.rotate('+value_direction+');\n';
  return code;
};

//turtle_dig
Blockly.Python['turtle_dig'] = function(block) {
  var value_direction = Blockly.Python.valueToCode(block, 'DIRECTION', Blockly.Python.ORDER_ATOMIC);
  var code = 'turtle.mine('+value_direction+');\n';
  return code;
};

//turtle_place
Blockly.Python['turtle_place'] = function(block) {
  var value_direction = Blockly.Python.valueToCode(block, 'DIRECTION', Blockly.Python.ORDER_ATOMIC);
  var value_material = Blockly.Python.valueToCode(block, 'MATERIAL', Blockly.Python.ORDER_ATOMIC);
  var code = 'turtle.place('+value_direction+', '+value_material+');\n';
  return code;
};

//turtle_direction
Blockly.Python['turtle_direction'] = function(block) {
  var dropdown_direction = block.getFieldValue('DIRECTION');
  // TODO: Assemble Python into code variable.
  var code = '"'+dropdown_direction+'"';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.Python.ORDER_ATOMIC];
};
