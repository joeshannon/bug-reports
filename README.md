Small test PDE product to investigate differences in validation for products in
the IDE vs Tycho bulid.

# Dependency overview

* There are two plugins `example.plugin1` and `example.plugin2`
* `example.plugin1` requires `example.plugin2`
* The feature `example.feature1` includes `example.plugin1` only
* There are two products:
    * a plugin based product including `example.plugin1` (and minimum plugins to launch the product) but *not* `example.plugin2`
    * a feature based product including `example.feature1` (and three RCP required features)

# Instructions

* Import the projects into Eclipse (i.e. Eclipse for RCP and RAP developers 2020-12)
* Run each of the two products and observe the validation errors
* Run a tycho build with `mvn clean verify` to build the products which completes successfully

# Observations

* In the IDE the products fail to launch as a required plugin is not present (via explicit inclusion or from contents of included feature)
* In Tycho the products build successfully:
    * The Tycho products do contain the undeclared plugin (`example.plugin2`) even though it is not expected to be available according to PDE's resolution
    * The Tycho p2 for the products (`target/repository`) do *not* contain `example.plugin2`

