

//turtle_move
Blockly.JavaScript['turtle_move'] = function(block) {
  var value_direction = Blockly.JavaScript.valueToCode(block, 'DIRECTION', Blockly.JavaScript.ORDER_ATOMIC);
  var code = 'turtle.move('+value_direction+');\n';
  return code;
};

//turtle_turn
Blockly.JavaScript['turtle_turn'] = function(block) {
  var value_direction = Blockly.JavaScript.valueToCode(block, 'DIRECTION', Blockly.JavaScript.ORDER_ATOMIC);
  var code = 'turtle.rotate('+value_direction+');\n';
  return code;
};

//turtle_dig
Blockly.JavaScript['turtle_dig'] = function(block) {
  var value_direction = Blockly.JavaScript.valueToCode(block, 'DIRECTION', Blockly.JavaScript.ORDER_ATOMIC);
  var code = 'turtle.mine('+value_direction+');\n';
  return code;
};

//turtle_place
Blockly.JavaScript['turtle_place'] = function(block) {
  var value_direction = Blockly.JavaScript.valueToCode(block, 'DIRECTION', Blockly.JavaScript.ORDER_ATOMIC);
  var value_material = Blockly.JavaScript.valueToCode(block, 'MATERIAL', Blockly.JavaScript.ORDER_ATOMIC);
  var code = 'turtle.place('+value_direction+', '+value_material+');\n';
  return code;
};

//turtle_direction
Blockly.JavaScript['turtle_direction'] = function(block) {
  var dropdown_direction = block.getFieldValue('DIRECTION');
  // TODO: Assemble JavaScript into code variable.
  var code = '"'+dropdown_direction+'"';
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};
