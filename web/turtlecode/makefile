
.PHONY: help
help:
	@echo "\nUSAGE:\n"
	@echo "make all		Do tasks for core, blocks, and generators"
	@echo "make clean		remove the $(OUTDIR) and its contents"
	@echo "make core		Compress the blockly core & EasyJ extensions"
	@echo "make blocks		Compress the EasyJ blocks"
	@echo "make generators	Compress the generators"
	@echo "make chelp		use less to view closure help"

.PHONY: dist
dist:
	cp ../blockly/*_compressed.js resources
	cp -r ../blockly/media resources
	cp -r ../blockly/msg resources
	cp ../blockly/demos/prettify.css resources
	cp ../blockly/demos/prettify.js resources