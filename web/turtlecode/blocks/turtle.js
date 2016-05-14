

// https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#m5ntpu

Blockly.Blocks['turtle_move'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("move");
    this.appendValueInput("DIRECTION")
        .setCheck("turtle_direction");
    this.setInputsInline(true);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(20);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

//https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#8oe8ee

Blockly.Blocks['turtle_turn'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("turn");
    this.appendValueInput("DIRECTION")
        .setCheck("turtle_direction");
    this.setInputsInline(true);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(20);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

// https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#ix8k93

Blockly.Blocks['turtle_dig'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("dig");
    this.appendValueInput("DIRECTION")
        .setCheck("turtle_direction");
    this.setInputsInline(true);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(20);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};


// https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#34972j
Blockly.Blocks['turtle_place'] = {
  init: function() {
    this.appendDummyInput()
        .appendField("place");
    this.appendValueInput("DIRECTION")
        .setCheck("turtle_direction");
    this.appendValueInput("MATERIAL")
        .setCheck("turtle_material")
        .appendField("type");
    this.setInputsInline(true);
    this.setPreviousStatement(true, null);
    this.setNextStatement(true, null);
    this.setColour(20);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};

// https://blockly-demo.appspot.com/static/demos/blockfactory/index.html#7q3qv2

Blockly.Blocks['turtle_direction'] = {
  init: function() {
    this.appendDummyInput()
        .appendField(new Blockly.FieldDropdown([["North", "NORTH"], ["South", "SOUTH"], ["East", "EAST"], ["West", "WEST"], ["Right", "RIGHT"], ["Up", "UP"], ["Down", "DOWN"], ["Forward", "FORWARD"], ["Back", "BACK"]]), "DIRECTION");
    this.setOutput(true, "turtle_direction");
    this.setColour(20);
    this.setTooltip('');
    this.setHelpUrl('http://www.example.com/');
  }
};