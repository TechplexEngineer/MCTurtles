

//turtle_common_materials
Blockly.JavaScript['turtle_common_materials'] = function(block) {
  var dropdown_material = block.getFieldValue('MATERIAL');
  // TODO: Assemble JavaScript into code variable.
  var code = dropdown_material;
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};

//turtle_free_material
Blockly.JavaScript['turtle_free_material'] = function(block) {
  var text_material = block.getFieldValue('MATERIAL');
  // TODO: Assemble JavaScript into code variable.
  var code = dropdown_material;
  // TODO: Change ORDER_NONE to the correct strength.
  return [code, Blockly.JavaScript.ORDER_ATOMIC];
};
