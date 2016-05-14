

// https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#w56ctx

Blockly.Blocks['turtle_common_materials'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["Wood", "wood"], ["Dirt", "dirt"], ["Grass", "grass"], ["Brick", "brick"], ["option", "OPTIONNAME"], ["option", "OPTIONNAME"], ["option", "OPTIONNAME"], ["option", "OPTIONNAME"], ["option", "OPTIONNAME"], ["option", "OPTIONNAME"]]), "MATERIAL");
    this.setOutput(true, "turtle_material");
    this.setColour(65);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

// https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#xn2coy


Blockly.Blocks['turtle_free_material'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldTextInput("wood"), "MATERIAL");
    this.setOutput(true, "turtle_material");
    this.setColour(65);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};