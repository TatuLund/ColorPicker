import { css, html, LitElement } from 'lit';
import { ThemableMixin } from '@vaadin/vaadin-themable-mixin/vaadin-themable-mixin.js';
import { customElement, property } from 'lit/decorators';
import '@vaadin/combo-box';
import { comboBoxRenderer, ComboBoxLitRenderer } from '@vaadin/combo-box/lit.js';
import { ComboBoxChangeEvent, ComboBoxCustomValueSetEvent, ComboBox } from '@vaadin/combo-box/vaadin-combo-box.js';
import '@vaadin/custom-field';

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
  color = '';
  @property()
  presets : Preset[] = [];
  @property()
  helperText = null;
  @property()
  errorMessage = null;
  @property({reflect: true})
  invalid = undefined;
  @property({reflect: true})
  compact = undefined;

  _comboBox : ComboBox | null = null;

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
    		background: var(--lumo-contrast-10pct);
		}
		#colorpicker:hover {
    		background: var(--lumo-contrast-30pct);
		}
		:host([theme~="compact"]) #combobox {
			display: none;
		}
        :host([invalid]) #colorpicker {
            background: var(--lumo-error-color-10pct);
        }
    `;
  }

  connectedCallback() {
    super.connectedCallback();
    const shadow = this.shadowRoot;
    this._comboBox = <ComboBox | null>shadow?.getElementById('combobox');
  }

  _colorToRGBA(color : string) : Uint8ClampedArray {
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

  _byteToHex(num : number) : string {
    // Turns a number (0-255) into a 2-character hex number (00-ff)
    return ('0'+num.toString(16)).slice(-2);
  }

  _colorToHex(color : string) : string {
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

  _handleChange(e: any) {
	// Color was picked from native input
	this.color = e.target.value;
	this._emitColorChanged();
  }

  _handlePreset(e: ComboBoxChangeEvent<Preset>) {
	// Color was selected using preset
	const preset = e.target.selectedItem;
	if (preset) {
		this.color = preset.color;
	}
	this._emitColorChanged();
	// Clear text input for better usability
	if (this._comboBox) {
	  this._comboBox.value='';
	}
  }

  _emitColorChanged() {
	const event = new CustomEvent('color-changed', {
		detail: this.color,
        composed: true,
        cancelable: true,
        bubbles: true		
	});
	this.dispatchEvent(event);	
  }

  _cssColorInput(e: ComboBoxCustomValueSetEvent) {
	// This function is called when custom value is input
	// Conversion to hex is needed as native input does not allow
	// other formats.
	const cssColor = e.detail;	
	this.color = this._colorToHex(cssColor);
    this._emitColorChanged();
  }

  render() {
    return html`
		<vaadin-custom-field 
          theme="${this.theme}"
		  part="field"
		  id="customfield" 
          class="container"
          label="${this.label}" 
          .helperText="${this.helperText}"
          .errorMessage="${this.errorMessage}"
          .invalid="${this.invalid}">

			<input
			  id="colorpicker"
	          name="colorpicker"
              type="color" 
              .value="${this.color}"
              @change=${this._handleChange}
            ><vaadin-combo-box
              theme="${this.theme}"
			  .invalid="${this.invalid}""
			  id='combobox'
			  allow-custom-value 
	          .items="${this.presets}""
              item-label-path="caption"
              @change=${this._handlePreset}
              @custom-value-set=${this._cssColorInput}
              ${comboBoxRenderer(this.renderer, [])}
            ></vaadin-combo-box>
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
