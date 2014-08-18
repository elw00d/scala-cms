//zk.afterLoad('',function() {
	var editor = ace.edit("editor");
	editor.setTheme("ace/theme/github");
	editor.getSession().setMode("ace/mode/java");
	editor.setShowPrintMargin(false);
	editor.setDisplayIndentGuides(false);
	editor.setSelectionStyle('text'); // instead of default "line"
	// store configured instance
	$('#editor').data('editor', editor);

	//tryCompile();
//});