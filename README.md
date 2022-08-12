# ColorPicker

ColorPicker component for Vaadin 23.1+.

## Features

The component has design that fits Vaadin family of components, aka Lumo desing system. 

* Graphical color picker
* Text input accepts any css compatible color or preset color
* Dropdown can be populated by color presets
* Small, compact, etc. theme variants

## Unit and integration tests

Primary motivation of this add-on is to demonstrate how to implement unit and integation
tests for Vaadin add-on or custom component. This add-on features full test suite

* Unit testing the server side Java code
* TestBench based integration tests of the web-component part
* Visual screen shot tests with TestBench verifying the styles

## Development instructions

JavaScript modules can either be published as an NPM package or be kept as local 
files in your project. The local JavaScript modules should be put in 
`src/main/resources/META-INF/frontend` so that they are automatically found and 
used in the using application.

If the modules are published then the package should be noted in the component 
using the `@NpmPackage` annotation in addition to using `@JsModule` annotation.


Starting the test/demo server:
1. Run `mvn jetty:run`.
2. Open http://localhost:8080 in the browser.

Running unit and integration tests
1. Run `mvn verify -Pit`.

## Publishing to Vaadin Directory

You can create the zip package needed for [Vaadin Directory](https://vaadin.com/directory/) using
```
mvn versions:set -DnewVersion=1.0.0 # You cannot publish snapshot versions 
mvn install -Pdirectory
```

The package is created as `target/colorpicker-1.0.0.zip`

For more information or to upload the package, visit https://vaadin.com/directory/my-components?uploadNewComponent
