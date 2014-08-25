ru.ace.AceEditorComponent = zk.$extends(zk.Widget, {
    _value : '', // default value

    getValue : function() {
        return this._value;
    },

    setValue : function(value) {
        if (this._value != value) {
            this._value = value;
            //if (this.desktop)
            //    this._editor.getSession().setValue(zUtl.encodeXML(value));
        }
    }

});