function _(out) {
    //out.push('<span', this.domAttrs_(), '>', this.getValue(), '</span>');

    out.push('<div id="editor" style="width: 300px; height:200px;" ', this.domAttrs_(), '>', this.getValue(), '</div>');

    this.bind_ = function (desktop, skipper, after) {
        this.$supers('bind_', arguments);

        var editor = ace.edit("editor");
        editor.setTheme("ace/theme/github");
        editor.getSession().setMode("ace/mode/ftl");
        editor.setShowPrintMargin(false);
        editor.setDisplayIndentGuides(false);
        editor.setSelectionStyle('text'); // instead of default "line"
        // store configured instance
        $('#editor').data('editor', editor);

        this._editor = editor;

        var self = this;
        editor.on("blur", function() {
            self.setValue(editor.getValue());
            self.fire('onChange', {text: editor.getValue()}, {toServer: true})
        });
    };

    // doesn't work
    //this.listen({onClick: function() {
    //}});
}