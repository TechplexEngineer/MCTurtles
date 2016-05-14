

//turtle_common_materials
Blockly.Python['turtle_common_materials'] = function(block) {
  var dropdown_material = block.getFieldValue('MATERIAL');
  // TODO: Assemble Python into code variable.
  var code = dropdown_material;
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.Python.ORDER_ATOMIC];
};

//turtle_free_material
Blockly.Python['turtle_free_material'] = function(block) {
  var text_material = block.getFieldValue('MATERIAL');
  // TODO: Assemble Python into code variable.
  var code = dropdown_material;
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.Python.ORDER_ATOMIC];
};
