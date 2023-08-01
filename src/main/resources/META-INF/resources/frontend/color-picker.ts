import { css, html, LitElement } from 'lit';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import { customElement, property, query } from 'lit/decorators.js';
import { ifDefined } from "lit-html/directives/if-defined.js";
import '@vaadin/combo-box';
import { comboBoxRenderer, ComboBoxLitRenderer } from '@vaadin/combo-box/lit.js';
import { ComboBoxChangeEvent, ComboBoxCustomValueSetEvent, ComboBox } from '@vaadin/combo-box/vaadin-combo-box.js';
import '@vaadin/custom-field';
import { TooltipController } from '@vaadin/component-base/src/tooltip-controller.js';

// Type that corresponds to ColorPreset type in ColorPicker.java
interface Preset {
	color : string;
	caption : string;
}

@customElement('color-picker')
export class ColorPicker extends ThemableMixin(LitElement) {
  @property()
  label = '';
  @property()
  color : string | null = '';
  @property()
  presets : Preset[] = [];
  @property()
  helperText = null;
  @property()
  errorMessage = null;
  @property()
  nocssinput : boolean | undefined = undefined;

  @property({reflect: true})
  invalid : boolean | undefined = undefined;
  @property({reflect: true})
  compact : boolean | undefined = undefined;
  @property({reflect: true})
  readonly : boolean | undefined = undefined;
  @property({reflect: true})
  disabled : boolean | undefined = undefined;
  @property({reflect: true})
  required : boolean | undefined = undefined;
  @property()
  theme : string | null = null;

  @query("#combobox")
  _comboBox! : ComboBox;
  @query("#coloropicker")
  _colorPicker! : HTMLInputElement;

  _tooltipController : TooltipController | undefined;

  // This is needed just for ThemableMixin
  static get is() {
    return 'color-picker';
  }

  static get styles() {
	// Styles use lumo custom properties as parameters to fit
	// look and feel of the other Vaadin components.
    return css`
		.container {
			display: flex;
    		min-width: 100%;
    		max-width: 100%;
		}
		#colorpicker {
			color: var(--lumo-secondary-text-color);
			padding: 0 calc(0.375em + var(--lumo-border-radius) / 4 - 1px);
    		font-weight: 500;
    		line-height: 1;
			font-size: var(--lumo-font-size-m);
            vertical-align: bottom;
            margin-bottom: 4px;
            margin-right: 4px;
            height: var(--lumo-text-field-size);
			border-radius: var(--lumo-border-radius-m);
			border-width: 0px;
    		background: var(--lumo-contrast-20pct);
    		width: 50px;
    		flex-shrink: 0;
		}
		#colorpicker:focus-visible {
		    box-shadow: 0 0 0 2px var(--lumo-primary-color-50pct);
		    outline: unset;
		}
		#colorpicker(not([disabled])):hover {
    		background: var(--lumo-contrast-30pct);
		}
		@-moz-document url-prefix() { 
          #colorpicker {
            padding-top: 4px;
            padding-bottom: 4px;
          }
        }
		:host([theme~="compact"]) #combobox {
			display: none;
		}
        input#colorpicker[invalid] {
            background: var(--lumo-error-color-10pct);
        }
        :host([invalid][disabled]) #colorpicker {
            background: var(--lumo-error-color-10pct);
        }
        input#colorpicker[readonly][invalid] {
            background: var(--lumo-error-color-50pct);
        }
        :host([disabled]) #colorpicker {
            background: var(--lumo-contrast-10pct);
            pointer-events: none;
        }
        input#colorpicker[readonly] {
            background: transparent; 
            border: 1px dashed var(--lumo-contrast-30pct);
            pointer-events: none;
        }
        :host([disabled]) #colorpicker[readonly] {
            background: var(--lumo-contrast-10pct); 
        }
        :host([disabled]) #colorpicker[readonly][invalid] {
            background: var(--lumo-error-color-50pct);
        }
        #wrapper {
	        display: flex;
	        align-items: end;
        }
        #combobox {
	        flex-grow: 1;
        }
    `;
  }

  firstUpdated() {
	this._tooltipController = new TooltipController(this, 'tooltip');
    this.addController(this._tooltipController);
	this._tooltipController.setShouldShow((target) => !(target as ColorPicker)._comboBox.opened);
  }

  updated() {
	if (this.color) {
      this.removeAttribute('invalid');
    }
  }

  _isColor(strColor: string) : boolean {
    const s = new Option().style;
    s.color = strColor;
    return s.color !== '';
  }

  focus() {
	// Override focus to combobox when available
    if (!this.compact) {
       this._comboBox?.focus();
    } else {
       this._colorPicker?.focus();
    }
  }

  protected _colorToRGBA(color : string) : Uint8ClampedArray {
    // Returns the color as an array of [r, g, b, a] -- all range from 0 - 255
    // color must be a valid canvas fillStyle. This will cover most anything
    // you'd want to use.
    // Examples:
    // colorToRGBA('red')  # [255, 0, 0, 255]
    // colorToRGBA('#f00') # [255, 0, 0, 255]
    const cvs = document.createElement('canvas');
    cvs.height = 1;
    cvs.width = 1;
    const ctx = cvs.getContext('2d');
    if (ctx) {
       ctx.fillStyle = color;
       ctx.fillRect(0, 0, 1, 1);
       return ctx.getImageData(0, 0, 1, 1).data;
    } else {
	   var empty : Uint8ClampedArray;
       empty = new Uint8ClampedArray();
	   return empty;
    }
  }

  protected _byteToHex(num : number) : string {
    // Turns a number (0-255) into a 2-character hex number (00-ff)
    return ('0'+num.toString(16)).slice(-2);
  }

  protected _colorToHex(color : string) : string {
    // Convert any CSS color to a hex representation
    // Examples:
    // colorToHex('red')            # '#ff0000'
    // colorToHex('rgb(255, 0, 0)') # '#ff0000'
    var hex;
    const rgba = this._colorToRGBA(color);
    hex = [0,1,2].map(
        idx => { return this._byteToHex(rgba[idx]); }
        ).join('');
    return "#"+hex;
  }

  protected _handleChange(e: any) {
	// Color was picked from native input
	this.color = e.target.value;
	console.log("Color: "+this.color);
	this._emitColorChanged();
  }

  protected _handlePreset(e: ComboBoxChangeEvent<Preset>) {
	// Color was selected using preset
	const preset = e.target.selectedItem;
	if (preset) {
		this.color = preset.color;
	    this._emitColorChanged();
		// Clear text input for better usability
	}
	if (this._comboBox) {
		this._comboBox.value='';
	}
  }

  protected _emitColorChanged() {
	if (this.color) {
    	this.removeAttribute('invalid');
	}
	const event = new CustomEvent('color-changed', {
		detail: this.color,
        composed: true,
        cancelable: true,
        bubbles: true		
	});
	this.dispatchEvent(event);	
  }

  protected _cssColorInput(e: ComboBoxCustomValueSetEvent) {
	// This function is called when custom value is input
	// Conversion to hex is needed as native input does not allow
	// other formats.
	if (this.nocssinput) return;
	const cssColor = e.detail;
	if (this._isColor(cssColor)) {
		this.color = this._colorToHex(cssColor);
    	this._emitColorChanged();
	} else {
        this.setAttribute('invalid','');
		this.color = null;
    	this._emitColorChanged();
	}
  }

  protected _handleFocus(e : CustomEvent) {
	e.stopPropagation();
	const event = new CustomEvent('focus', {
        composed: true,
        cancelable: true,
        bubbles: true
	});
	this.dispatchEvent(event);		 
  }

  protected _handleBlur(e : CustomEvent) {
	e.stopPropagation();
	const event = new CustomEvent('blur', {
        composed: true,
        cancelable: true,
        bubbles: true
	});
	this.dispatchEvent(event);		 
  }

  _set_theme(theme : string) {
    this.theme = theme;
  }

  render() {
	// vaadin-custom-field is used as wrapper in order to have
	// the common implementation of label, error message, helper
	// text and required indicator.
    return html`
		<vaadin-custom-field 
		  part="field"
		  id="customfield" 
          class="container"
          .label="${this.label}" 
          .helperText="${this.helperText}"
          .errorMessage="${this.errorMessage}"
          ?readonly=${this.readonly}
          disabled=${ifDefined(this.disabled)}
          invalid=${ifDefined(this.invalid)}
          ?required=${this.required}
          theme="${ifDefined(this.theme)}">

          <div id="wrapper">
		  <input
            id="colorpicker"
            part="colorpicker"
            ?readonly=${this.readonly}
            disabled=${ifDefined(this.disabled)}
            invalid=${ifDefined(this.invalid)}
            theme="${ifDefined(this.theme)}"
            type="color" 
            .value="${this.color}"
            @change=${this._handleChange}
			@blur=${this._handleBlur}
			@focus=${this._handleFocus}
          >
          <vaadin-combo-box
            part="dropdown"
	        id="combobox"
            allow-custom-value
            ?readonly=${this.readonly}
            disabled=${ifDefined(this.disabled)}
            invalid=${ifDefined(this.invalid)}
            theme="${ifDefined(this.theme)}"
            .items="${this.presets}"
            item-label-path="caption"
            @change=${this._handlePreset}
            @custom-value-set=${this._cssColorInput}
            ${comboBoxRenderer(this.renderer, [])}
			@blur=${this._handleBlur}
			@focus=${this._handleFocus}
          ></vaadin-combo-box>
          </div>
		<slot name="tooltip"></slot>
    `;
  }

  private renderer: ComboBoxLitRenderer<Preset> = (preset) => {
    // Renders nicer looking color items with color badge and label in the dropdown
    return html`
      <div style="display: flex;">
        <div style="width: 20px; height: 20px; background: ${preset.color}; margin-right: 10px; border-radius: var(--lumo-border-radius-s);">          
        </div>
        <div>
          ${preset.caption}
        </div>
      </div>
    `;
  };
}
