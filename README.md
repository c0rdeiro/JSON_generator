# JSON_generator

## How to Use the Data Model
The JSON data model its compose by 
 
 - JSONObject
 - JSONArray
 - JSONString
 - JSONNumber
 - JSONBoolean
 - JSONNull 

All of these derive from JSONValue. The JSONObject model uses JSONKey to represent the keys.
This model is then used to instantiate data classes, collections, maps, primitive types, strings and enums.


### Serialize Data

1. Create a ```PrintJSONVisitor```
 ```kotlin
 val v = PrintJSONVisitor()
 ```
 2. Instanciate the data

 a. Manually
 ```kotlin
 val obj1 = JSONObject(hashMapOf(JSONKey("second") to arr1, JSONKey("tier2") to JSONString("Kappa"), JSONKey("thirdin") to JSONNull()))
obj1.accept(v)
 ```
 b. Using supported reflection with ```JSONGenerator```, calling the method ```instantiate``` passing the data as an argument.
 ```kotlin
val p1 = Point(2.0, 2.0, null, Form.Circle)

val jg = JSONGenerator()
val ee = jg.instantiate(p1)
ee.accept(v)
 ```
 
 ```JSONGenerator``` also supports some annotations, these are:
 - @Exclude - skip unwated properties (In the example below the name will not be instantiated
 - @ChangeName(val newName: String) - change the name of a property (In the example below the Form will be instantiated with the name "geometry" instead of "form"

 Example:
 ```kotlin
     data class Point(val x: Double, val y: Double,
                     @Exclude val name: String?,
                     @ChangeName("geometry") val form: Form
                        )
 ```
 
 3. Print the data
 
 To print the JSON it is only needed to access the ```output``` variable from visitor.
 ```kotlin
 println(v.output)
 ```
 
 ## How to Work with the UI
 UI supports plugins for Icon Setups and Actions. These are easily implemented using the interfaces and adding the corresponding implementation to the ```di.properties``` file.

 
 ### Icons
 This library only supports one icon setup at a time.
 ```kotlin
 interface IconSetup {

    fun getText(value : JSONValue): String
    fun getPath(value: JSONValue): String
    fun getIconWidth(value: JSONValue): Int
    fun getIconHeight(value: JSONValue): Int
    fun toExclude(value: JSONValue): Boolean = false
}
 ```
 - ```getText``` method allows naming according to the JSONValue 
 - ```getPath``` method allows to pass the path to the icon image according to the JSONValue 
 - ```getIconWidth```and ```getIconHeight``` gives the user the freedom to choose the icon size to each JSONValue
 - ```toExclude```method allows the user to exclude some JSONValues - not mandatory, default as false, meaning that it does not exclude anything
 
 An example of an implementation is [here](https://github.com/c0rdeiro/JSON_generator/blob/main/src/main/kotlin/ui/IconSetups/DefaultIconSetup.kt)
 
 ### Actions
 ```kotlin
 
interface Action {
    val name: String
    fun execute(treeItem: TreeItem)
}
```
- ```name``` is the name of the button that will be created for the action.
- ```execute``` is the method that executes the action when the respective button is clicked.


There are three examples implemented:
- [ValidateNumbers](https://github.com/c0rdeiro/JSON_generator/blob/main/src/main/kotlin/ui/Actions/ValidateNumbers.kt) - checks if all JSONNumbers are positive.
- [EditTreeItem](https://github.com/c0rdeiro/JSON_generator/blob/main/src/main/kotlin/ui/Actions/EditTreeItem.kt) - edits the name of the selected item.
- [SaveToFile](https://github.com/c0rdeiro/JSON_generator/blob/main/src/main/kotlin/ui/Actions/SaveToFile.kt) - saves the selected item to a JSON file.

### Properties file
 If no plugins are supplied in the ```di.properties``` file, the library still works as expected in the simplest form.

```
Visualizer.icons=ui.IconSetups.DefaultIconSetup
Visualizer.actions=ui.Actions.EditTreeItem,ui.Actions.SaveToFile,ui.Actions.ValidateNumbers
```
