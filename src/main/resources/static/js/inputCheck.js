function isEmptyInput() {
    var re=/(^(?!\s*$).+)/;
    var text=document.getElementById('textArea');
    if(!re.test(text.value)) {
        text.value='';
    }
}

