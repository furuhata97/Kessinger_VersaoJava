Button.prototype.toggle = function toggle() {
    var triggerChangeEvent = true;
    var addAriaPressed = true;
    var rootElement = $(this._element).closest(Selector.DATA_TOGGLE)[0];

    if (rootElement) {
        var input = $(this._element).find(Selector.INPUT)[0];

        if (input) {
            if (input.type === 'radio') {
                if (input.checked && $(this._element).hasClass(ClassName.ACTIVE)) {
                    triggerChangeEvent = false;
                } else {
                    var activeElement = $(rootElement).find(Selector.ACTIVE)[0];

                    if (activeElement) {
                        $(activeElement).removeClass(ClassName.ACTIVE);
                    }
                }
            }

            if (triggerChangeEvent) {
                if (input.hasAttribute('disabled') || rootElement.hasAttribute('disabled') || input.classList.contains('disabled') || rootElement.classList.contains('disabled')) {
                    return;
                }
                input.checked = !$(this._element).hasClass(ClassName.ACTIVE);
                $(input).trigger('change');
            }

            input.focus();
            addAriaPressed = false;
        }
    }

    if (addAriaPressed) {
        this._element.setAttribute('aria-pressed', !$(this._element).hasClass(ClassName.ACTIVE));
    }

    if (triggerChangeEvent) {
        $(this._element).toggleClass(ClassName.ACTIVE);
    }
};






